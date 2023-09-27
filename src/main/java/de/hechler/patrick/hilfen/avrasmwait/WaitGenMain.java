package de.hechler.patrick.hilfen.avrasmwait;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hechler.patrick.hilfen.autoarggui.objects.AutoArgGUIFrame;
import de.hechler.patrick.hilfen.avrasmwait.interfaces.CodeGenerator;
import de.hechler.patrick.hilfen.avrasmwait.objects.SimpleCodeGeneratorImpl;

@SuppressWarnings("javadoc")
public class WaitGenMain {
	
	public static String[] REGISTER_NAMES = new String[CodeGenerator.MAX_REG_CNT];
	
	private static CodeGenerator cg;
	private static BigInteger[]  tickCnts;
	private static String[]      labels;
	private static BigInteger    lastTickCnt;
	private static String        lastLabel;
	
	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				System.err.println("[WaitGenMain]: no args start gui");
				AutoArgGUIFrame ag = new AutoArgGUIFrame("ENTER ARGS", WaitGenMain::main, new ArgContainer());
				ag.load("completed the Code generation", "FINISH");
				return;
			}
			setup(args);
			for (int i = 0; i < tickCnts.length; i++) {
				cg.generateInit(tickCnts[i], labels[i]);
			}
			if (lastTickCnt != null) {
				cg.generateInitAndLoop(lastTickCnt, lastLabel);
			} else {
				cg.generateLoop();
			}
		} catch (Exception e) {
			e.printStackTrace();
			exit(e.getMessage(), -1, args);
		}
	}
	
	private static void setup(String[] args) throws FileNotFoundException {
		Arrays.fill(REGISTER_NAMES, null);
		String           target    = null;
		String           loop      = null;
		BigInteger       max       = BigInteger.ZERO;
		List<String>     labels    = new ArrayList<>();
		List<BigInteger> tickCnts  = new ArrayList<>();
		int              regs      = -1;
		int              nops      = -1;
		boolean          nosafe    = false;
		boolean          exactVals = false;
		boolean          force     = false;
		String           first     = null;
		for (int i = 0; i < args.length; i++) {
			switch (args[i].toLowerCase()) {
			case "-help":
			case "-?":
				help(System.out);
				break;
			case "-i":
			case "-init": {
				if (i + 3 >= args.length) {
					exit("not enugh args for option <-init>", i, args);
				}
				labels.add(args[i + 1]);
				BigInteger val = new BigInteger(args[i + 3], 10);
				System.out.println();
				switch (args[i + 2].toLowerCase()) {
				case "y":
				case "year":
					val = val.multiply(BigInteger.valueOf(365L));
					//$FALL-THROUGH$
				case "d":
				case "day":
					val = val.multiply(BigInteger.valueOf(24L));
					//$FALL-THROUGH$
				case "h":
				case "hour":
					val = val.multiply(BigInteger.valueOf(60L));
					//$FALL-THROUGH$
				case "m":
				case "min":
					val = val.multiply(BigInteger.valueOf(60L));
					//$FALL-THROUGH$
				case "s":
				case "sec":
					val = val.multiply(BigInteger.valueOf(1000L));
					//$FALL-THROUGH$
				case "ms":
					val = val.multiply(BigInteger.valueOf(16000L));
					//$FALL-THROUGH$
				case "t":
				case "tick":
					break;
				default:
					exit("unknown sub-option: '" + args[i] + "' of option <-init> (<-init> [LABEL_NAME] <j>/<d>/<h>/<m>/<s>/<ms>/<t> [TIME])", i, args);
				}
				tickCnts.add(val);
				if (val.compareTo(max) > 0) {
					max = val;
				}
				i += 3;
				break;
			}
			case "-l":
			case "-loop":
				i++;
				if (i >= args.length) {
					exit("not enough args for option <-loop>", i, args);
				}
				loop = args[i];
				break;
			case "-t":
			case "-target":
				if (target != null) {
					exit("double set of the target", i, args);
				}
				i++;
				if (i >= args.length) {
					exit("not enough args for option <-target>", i, args);
				}
				target = args[i];
				break;
			case "-force":
				if (force) {
					exit("double set of the force overwrite", i, args);
				}
				force = true;
				break;
			case "-r":
			case "-regs":
				if (regs != -1) {
					exit("double set of the reg count", i, args);
				}
				i++;
				if (i >= args.length) {
					exit("not enough args for option <-regs>", i, args);
				}
				regs = Integer.parseInt(args[i]);
				break;
			case "-n":
			case "-nops":
				if (nops != -1) {
					exit("double set of the nop count", i, args);
				}
				i++;
				if (i >= args.length) {
					exit("not enough args for option <-nops>", i, args);
				}
				nops = Integer.parseInt(args[i]);
				break;
			case "-nosafe":
				if (nosafe) {
					exit("double set of nosafe", i, args);
				}
				nosafe = true;
				break;
			case "-exact":
				if (exactVals) {
					exit("double set of exact", i, args);
				}
				exactVals = true;
				break;
			case "-first":
				if (first != null) {
					exit("double set of first", i, args);
				}
				i++;
				if (i >= args.length) {
					exit("not enough args for option <-first>", i, args);
				}
				first = args[i];
				break;
			case "-set-names":
				if (first != null) {
					exit("double set of first", i, args);
				}
				if (++i >= args.length) {
					exit("not enough args for option <-set-names>", i - 1, args);
				}
				String magic = args[i];
				String[] tmpRegs = new String[CodeGenerator.MAX_REG_CNT];
				while (true) {
					if (++i >= args.length) {
						exit("not enough args for option <-set-names>", i - 1, args);
					}
					if (magic.equals(args[i])) {
						break;
					}
					int reg = findRegister(args[i], tmpRegs, "", -1, args);
					if (++i >= args.length) {
						exit("not enough args for option <-set-names>", i - 1, args);
					}
					tmpRegs[reg] = args[i];
				}
				for (int ii = 0; ii < tmpRegs.length; ii++) {
					if (tmpRegs[ii] != null) {
						REGISTER_NAMES[ii] = tmpRegs[ii];
					}
				}
				break;
			default:
				exit("unknown arg: '" + args[i] + "'", i, args);
			}
		}
		if (nops == -1) {
			nops = 0;
		}
		int first0 = first != null ? findRegister(first, null, " first", -1, args) : 0;
		if (regs == -1) {
			regs = SimpleCodeGeneratorImpl.minRegCnt(max, !nosafe, nops);
		}
		if (loop == null) {
			loop = "waste_time_loop_f" + first0 + "_r" + regs;
		}
		PrintStream out;
		if (target == null) {
			out = System.out;
		} else {
			if (!force && Files.exists(Paths.get(target))) {
				exit("target file exit already (use <-force> to force overwrite)", -1, args);
			}
			out = new PrintStream(target);
		}
		cg                   = SimpleCodeGeneratorImpl.create(out, first0, regs, !nosafe, exactVals, loop, nops);
		WaitGenMain.labels   = labels.toArray(new String[labels.size()]);
		WaitGenMain.tickCnts = tickCnts.toArray(new BigInteger[tickCnts.size()]);
	}
	
	private static int findRegister(String name, String[] ignore, String type, int argI, String[] args) {
		int reg = 0;
		while (true) {
			if ((ignore == null || ignore[reg] == null) && name.equals(CodeGenerator.register(reg))) {
				break;
			}
			if (++reg >= CodeGenerator.MAX_REG_CNT) {
				if (ignore != null) {
					while (--reg >= 0) {
						if (ignore[reg] != null) {
							REGISTER_NAMES[reg] = "<ignored (" + REGISTER_NAMES[reg] + ")>";
						}
					}
				}
				exit("unknown" + type + " register: '" + name + "' (registers: " + Arrays.toString(REGISTER_NAMES) + ")", argI, args);
			}
		}
		return reg;
	}
	
	private static void help(PrintStream out) {
		out.println("<-help> or <-?>");
		out.println("          to print this message on the default out stream");
		out.println("(<-init> or <-i>) [INIT_LABEL_NAME] (<year>/<y> or <day>/<d> or <hour>/<h> or <min>/<m> or <sec>/<s> or <ms> or <t>/<tick>) [TIME]");
		out.println("          to generate an init-label with the name INIT_LABEL_NAME");
		out.println("          it will init the regs for the given TIME converted to ticks");
		out.println("          TIME is a decimal number");
		out.println("               <year>/<j>: a <year> contains 365 <day>s");
		out.println("               <day>/<d>: a <day> contains 24 <hour>s");
		out.println("               <hour>/<h>: a <hour> contains 60 <min>utes");
		out.println("               <min>/<m>: a <min> contains 60 <sec>onds");
		out.println("               <sec>/<s>: a <sec> contains 1000 <ms> (milli-seconds)");
		out.println("               <ms>: a <ms> contains 16000 <tick>s");
		out.println("               <tick>: a <tick> is the smallest possible time-unit");
		out.println("          after all regs have been initialised the program wil JMP to the loop-label");
		out.println("<-loop> or <-l> [LOOP_LABEL_NAME]");
		out.println("          to set the name of the loop-label to the given LOOP_LABEL_NAME");
		out.println("<-target> or <-t> [TARGET_FILE]");
		out.println("          to set the target file to recive the generated code");
		out.println("          if no target file is set the generated code will be printed on the default out stream");
		out.println("<-force>");
		out.println("          to force overwrite if the target file exits already");
		out.println("          if no target file exits this will be ignored");
		out.println("<-exact>");
		out.println("          wait the exact time");
		out.println("          when this option is set the remaining time will be filled with NOPs");
		out.println("<-regs> or <-r> [REGISTER_COUNT]");
		out.println("          to set the number of registers the loop should use");
		out.println("          if no register count is set the smallest usable register count will be used");
		out.println("<-nops> or <-n> [NOP_COUNT]");
		out.println("          to set the number of nops used in each iteration of the loop");
		out.println("          if no nop count is set the no nops will be used");
		out.println("<-nosafe>");
		out.println("          to supress te sasve and load of the used registers");
		out.println("          if set all used registers will have the value 255/0xFF after a loop-call");
		out.println("<-first> [REGISTER_NAME]");
		out.println("          set the first register to be used");
		out.println("          if the names are/will be modified, the new(est) name must be used");
		out.println("          if multiple registers have the same name the one with the lowest intern number will be used");
		out.println("<-set-names> [MAGIC_END] ([REGISTER_NAME_OLD] [REGISTER_NAME_NEW])* [MAGIC_END]");
		out.println("          set the name of the given registers");
		out.println("          note that the intern representation of registers is (by default):");
		out.println("            r16, r17, ..., r31, r0, r1, ..., r15");
		out.println("          note that the first " + CodeGenerator.GOOD_REGISTER_CNT + " intern register (r16 .. r31)");
		out.println("          support the LDI instruction, while the others do not.");
		out.println("            the generator does not check the register names when using LDI:");
		out.println("            when setting the names of the registers, only registers in the same group should be swapped");
	}
	
	private static void exit(String msg, int index, String[] args) {
		System.err.println(msg);
		new Throwable().printStackTrace();
		if (index != -1) {
			System.out.println("index=" + index);
		}
		System.err.println(Arrays.deepToString(args));
		help(System.err);
		Runtime r = Runtime.getRuntime();
		r.runFinalization();
		r.exit(1);
	}
	
}
