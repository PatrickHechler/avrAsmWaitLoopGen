package de.hechler.patrick.hilfen.avrasmwait;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		boolean unsafe = false;
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
			case "-u":
			case "-unsafe":
				if (unsafe) {
					exit("double set of the nop count", i, args);
				}
				unsafe = true;
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
			out = new PrintStream(target);
		}
		if (first == -1) {
			if (regs == -1) {
				cg = SimpleCodeGeneratorImpl.create(out, !unsafe, loop, max);
			} else {
				cg = SimpleCodeGeneratorImpl.create(out, regs, !unsafe, loop);
			}
		} else {
			if (regs == -1) {
				cg = SimpleCodeGeneratorImpl.create(out, first, !unsafe, loop, max);
			} else {
				cg = SimpleCodeGeneratorImpl.create(out, first, regs, !unsafe, loop);
			}
		}
		Main.labels = labels.toArray(new String[labels.size()]);
		Main.tickCnts = tickCnts.toArray(new BigInteger[tickCnts.size()]);
	}
	
	private static void help(PrintStream out) {
		out.println("this is not done jet!");
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
