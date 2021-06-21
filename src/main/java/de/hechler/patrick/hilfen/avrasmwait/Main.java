package de.hechler.patrick.hilfen.avrasmwait;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hechler.patrick.hilfen.autoarggui.objects.AutoGUIFenster;
import de.hechler.patrick.hilfen.avrasmwait.interfaces.CodeGenerator;
import de.hechler.patrick.hilfen.avrasmwait.objects.SimpleCodeGeneratorImpl;

public class Main {
	
	private static CodeGenerator cg;
	private static BigInteger[]  tickCnts;
	private static String[]      labels;
	private static BigInteger    lastTickCnt;
	private static String        lastLabel;
	
	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				System.err.println("[LOG] no args start gui");
				// new GUIFenster(new ArgContainer(), args0 -> main(args0)).load("ENTER ARGS");
				// new AutoGUIFenster(Main::main, new ArgContainer().load()).load("ENTER ARGS");;
				new AutoGUIFenster("ENTER ARGS", Main::main, new ArgContainer()).load("completed the Code generation", "FINISH");
				return;
			}
			setup(args);
			for (int i = 0; i < tickCnts.length; i ++ ) {
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
	
	private static final BigInteger JEAR = BigInteger.valueOf(356L);
	private static final BigInteger DAY  = BigInteger.valueOf(24L);
	private static final BigInteger HOUR = BigInteger.valueOf(60L);
	private static final BigInteger MIN  = BigInteger.valueOf(60L);
	private static final BigInteger SEC  = BigInteger.valueOf(1000L);
	private static final BigInteger MS   = BigInteger.valueOf(16000L);
	
	private static void setup(String[] args) throws FileNotFoundException {
		String target = null;
		String loop = null;
		BigInteger max = BigInteger.ZERO;
		List <String> labels = new ArrayList <>();
		List <BigInteger> tickCnts = new ArrayList <>();
		int regs = -1;
		int nops = -1;
		boolean nosafe = false;
		boolean force = false;
		int first = -1;
		for (int i = 0; i < args.length; i ++ ) {
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
				case "j":
				case "jear":
					val = val.multiply(JEAR);
				case "d":
				case "day":
					val = val.multiply(DAY);
				case "h":
				case "hour":
					val = val.multiply(HOUR);
				case "m":
				case "min":
					val = val.multiply(MIN);
				case "s":
				case "sec":
					val = val.multiply(SEC);
				case "ms":
					val = val.multiply(MS);
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
				i ++ ;
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
				i ++ ;
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
				i ++ ;
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
				i ++ ;
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
			case "-f":
			case "-first":
				if (first != -1) {
					exit("double set of the nop count", i, args);
				}
				i ++ ;
				if (i >= args.length) {
					exit("not enough args for option <-nops>", i, args);
				}
				first = Integer.parseInt(args[i]);
				if (first < 0) {
					exit("illegal first register: " + first, i, args);
				}
				break;
			default:
				exit("unknown arg: '" + args[i] + "'", i, args);
			}
		}
		if (loop == null) {
			loop = "wastTimeLoop";
		}
		PrintStream out;
		if (target == null) {
			out = System.out;
		} else {
			if ( !force && Files.exists(Paths.get(target))) {
				exit("target file exit already (use <-force> to force overwrite)", -1, args);
			}
			out = new PrintStream(target);
		}
		if (first == -1) {
			if (regs == -1) {
				cg = SimpleCodeGeneratorImpl.create(out, !nosafe, loop, max);
			} else {
				cg = SimpleCodeGeneratorImpl.create(out, regs, !nosafe, loop);
			}
		} else {
			if (regs == -1) {
				cg = SimpleCodeGeneratorImpl.create(out, first, !nosafe, loop, max);
			} else {
				cg = SimpleCodeGeneratorImpl.create(out, first, regs, !nosafe, loop);
			}
		}
		Main.labels = labels.toArray(new String[labels.size()]);
		Main.tickCnts = tickCnts.toArray(new BigInteger[tickCnts.size()]);
	}
	
	private static void help(PrintStream out) {
		// case "-help":
		// case "-?":
		out.println("<-help> or <-?>");
		out.println("          to print this message on the default out stream");
		// help(System.out);
		// break;
		// case "-i":
		// case "-init": {
		out.println("(<-init> or <-i>) [INIT_LABEL_NAME] (<jear>/<j> or <day>/<d> or <hour>/<h> or <min>/<m> or <sec>/<s> or <ms> or <t>/<tick>) [TIME]");
		out.println("          to generate an init-label with the name INIT_LABEL_NAME");
		out.println("          it will init the regs for the given TIME converted to ticks");
		out.println("          TIME is a decimal number");
		out.println("               <jear>/<j>: a <jear> contains 365 <day>s");
		out.println("               <day>/<d>: a <day> contains 24 <hour>s");
		out.println("               <hour>/<h>: a <hour> contains 60 <min>utes");
		out.println("               <min>/<m>: a <min> contains 60 <sec>onds");
		out.println("               <sec>/<s>: a <sec> contains 1.000 <ms> milli-seconds");
		out.println("               <ms>: a <ms> contains 16.000 <tick>s");
		out.println("               <tick>: a <tick> is the smallest time-unit");
		out.println("          after all regs have been initialised the program wil JMP to the loop-label");
		// if (i + 3 >= args.length) {
		// exit("not enugh args for option <-init>", i, args);
		// }
		// labels.add(args[i + 1]);
		// BigInteger val = new BigInteger(args[i + 3], 10);
		// switch (args[i + 2].toLowerCase()) {
		// case "j":
		// case "jear":
		// val = val.multiply(JEAR);
		// case "d":
		// case "day":
		// val = val.multiply(DAY);
		// case "h":
		// case "hour":
		// val = val.multiply(HOUR);
		// case "m":
		// case "min":
		// val = val.multiply(MIN);
		// case "s":
		// case "sec":
		// val = val.multiply(SEC);
		// case "ms":
		// val = val.multiply(MS);
		// case "t":
		// case "tick":
		// break;
		// default:
		// exit("unknown sub-option: '" + args[i] + "' of option <-init> (<-init> [LABEL_NAME] <j>/<d>/<h>/<m>/<s>/<ms>/<t> [TIME])", i, args);
		// }
		// tickCnts.add(val);
		// if (val.compareTo(max) > 0) {
		// max = val;
		// }
		// i += 3;
		// break;
		// }
		// case "-l":
		// case "-loop":
		out.println("<-loop> or <-l> [LOOP_LABEL_NAME]");
		out.println("          to set the name of the loop-label to the given LOOP_LABEL_NAME");
		// i ++ ;
		// if (i >= args.length) {
		// exit("not enough args for option <-loop>", i, args);
		// }
		// loop = args[i];
		// break;
		// case "-t":
		// case "-target":
		out.println("<-target> or <-t> [TARGET_FILE]");
		out.println("          to set the target file to recive the generated code");
		out.println("          if no target file is set the generated code will be printed on the default out stream");
		out.println("<-force>");
		out.println("          to force overwrite if the target file exits already");
		out.println("          if no target file exits this will be ignored");
		// if (target != null) {
		// exit("double set of the target", i, args);
		// }
		// i ++ ;
		// if (i >= args.length) {
		// exit("not enough args for option <-target>", i, args);
		// }
		// target = args[i];
		// break;
		// case "-r":
		// case "-regs":
		out.println("<-regs> or <-r> [REGISTER_COUNT]");
		out.println("          to set the number of registers the loop should use");
		out.println("          if no register count is set the smallest usable register count will be used");
		// if (regs != -1) {
		// exit("double set of the reg count", i, args);
		// }
		// i ++ ;
		// if (i >= args.length) {
		// exit("not enough args for option <-regs>", i, args);
		// }
		// regs = Integer.parseInt(args[i]);
		// break;
		// case "-n":
		// case "-nops":
		out.println("<-nops> or <-n> [NOP_COUNT]");
		out.println("          to set the number of nops used in each iteration of the loop");
		out.println("          if no nop count is set the no nops will be used");
		// if (nops != -1) {
		// exit("double set of the nop count", i, args);
		// }
		// i ++ ;
		// if (i >= args.length) {
		// exit("not enough args for option <-nops>", i, args);
		// }
		// nops = Integer.parseInt(args[i]);
		// break;
		// case "-nosafe":
		out.println("<-nosafe>");
		out.println("          to supress te sasve and load of the used registers");
		out.println("          if set all used registers will have the value 255/0xFF after a loop-call");
		// if (unsafe) {
		// exit("double set of the nop count", i, args);
		// }
		// unsafe = true;
		// break;
		// case "-f":
		// case "-first":
		// if (first != -1) {
		// exit("double set of the nop count", i, args);
		// }
		// i ++ ;
		// if (i >= args.length) {
		// exit("not enough args for option <-nops>", i, args);
		// }
		// first = Integer.parseInt(args[i]);
		// if (first < 0) {
		// exit("illegal first register: " + first, i, args);
		// }
		// break;
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
