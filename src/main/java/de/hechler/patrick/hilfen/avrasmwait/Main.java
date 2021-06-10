package de.hechler.patrick.hilfen.avrasmwait;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	
	private static CodeGenarator         cg;
	private static PrintStream           out;
	private static BigInteger            ticks;
	private static ByteArrayOutputStream baos;
	
	public static void main(String[] args) throws IOException {
		args = setup(args);
		if (baos != null) {
			cg.generate(new PrintStream(baos), ticks);
			Scanner sc = new Scanner(new String(baos.toByteArray()));
			de.hechler.patrick.fileparser.Main.parse(args, sc, out);
		} else {
			cg.generate(out, ticks);
		}
	}
	
	private static String[] setup(String[] args) throws FileNotFoundException {
		BigInteger secMul = CodeGenarator.genarateBI(1600000);
		BigInteger minMul = CodeGenarator.genarateBI(60);
		BigInteger hourMul = CodeGenarator.genarateBI(60);
		BigInteger dayMul = CodeGenarator.genarateBI(24);
		BigInteger jearMul = CodeGenarator.genarateBI(365);
		BigInteger time = null;
		int art = -1;
		int parser = args.length;
		String target = null;
		String loop = null;
		String prefix = null;
		String suffix = null;
		int maxNops = -1;
		boolean saveRegs = true;
		final int artTick = 0, artSec = 1, artMin = 2, artHour = 3, artDay = 4, artJear = 5;
		boolean onlyHelp = false;
		boolean force = false;
		for (int i = 0; i < args.length; i ++ ) {
			switch (args[i].toLowerCase()) {
			case "-?":
			case "-help":
				if (i == 0) {
					onlyHelp = true;
				}
				help(System.out);
				break;
			case "--help":
				if (i == 0) {
					onlyHelp = true;
				}
				help(System.err);
				break;
			case "-t":
			case "-tick":
				onlyHelp = false;
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-tick>", i, args);
				}
				if (time != null) {
					exit("time already set: DEC[" + time.toString(10) + "] art: "
						+ (art == artTick ? "tick" : (art == artSec ? "sec" : (art == artMin ? "min" : (art == artHour ? "hour" : (art == artDay ? "day" : (art == artJear ? "jear" : "invalid")))))),
						i, args);
				}
				art = artTick;
				time = new BigInteger(args[i]);
				break;
			case "-s":
			case "-sec":
				onlyHelp = false;
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-sec>", i, args);
				}
				if (time != null) {
					exit("time already set: DEC[" + time.toString(10) + "] art: "
						+ (art == artTick ? "tick" : (art == artSec ? "sec" : (art == artMin ? "min" : (art == artHour ? "hour" : (art == artDay ? "day" : (art == artJear ? "jear" : "invalid")))))),
						i, args);
				}
				art = artSec;
				time = new BigInteger(args[i]);
				break;
			case "-m":
			case "-min":
				onlyHelp = false;
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-min>", i, args);
				}
				if (time != null) {
					exit("time already set: DEC[" + time.toString(10) + "] art: "
						+ (art == artTick ? "tick" : (art == artSec ? "sec" : (art == artMin ? "min" : (art == artHour ? "hour" : (art == artDay ? "day" : (art == artJear ? "jear" : "invalid")))))),
						i, args);
				}
				art = artMin;
				time = new BigInteger(args[i]);
				break;
			case "-h":
			case "-hour":
				onlyHelp = false;
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-hour>", i, args);
				}
				if (time != null) {
					exit("time already set: DEC[" + time.toString(10) + "] art: "
						+ (art == artTick ? "tick" : (art == artSec ? "sec" : (art == artMin ? "min" : (art == artHour ? "hour" : (art == artDay ? "day" : (art == artJear ? "jear" : "invalid")))))),
						i, args);
				}
				art = artHour;
				time = new BigInteger(args[i]);
				break;
			case "-d":
			case "-day":
				onlyHelp = false;
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-day>", i, args);
				}
				if (time != null) {
					exit("time already set: DEC[" + time.toString(10) + "] art: "
						+ (art == artTick ? "tick" : (art == artSec ? "sec" : (art == artMin ? "min" : (art == artHour ? "hour" : (art == artDay ? "day" : (art == artJear ? "jear" : "invalid")))))),
						i, args);
				}
				art = artDay;
				time = new BigInteger(args[i]);
				break;
			case "-j":
			case "-jear":
				onlyHelp = false;
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-jear>", i, args);
				}
				if (time != null) {
					exit("time already set: DEC[" + time.toString(10) + "] art: "
						+ (art == artTick ? "tick" : (art == artSec ? "sec" : (art == artMin ? "min" : (art == artHour ? "hour" : (art == artDay ? "day" : (art == artJear ? "jear" : "invalid")))))),
						i, args);
				}
				art = artJear;
				time = new BigInteger(args[i]);
				break;
			case "-set": {
				onlyHelp = false;
				i ++ ;
				String zw = args[i];
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-set>", i, args);
				}
				BigInteger bi = new BigInteger(args[i]);
				switch (zw.toLowerCase()) {
				case "s":
				case "sec":
					secMul = bi;
					break;
				case "m":
				case "min":
					minMul = bi;
					break;
				case "h":
				case "hour":
					hourMul = bi;
					break;
				case "d":
				case "day":
					dayMul = bi;
					break;
				case "j":
				case "jear":
					jearMul = bi;
					break;
				default:
					exit("unknown option: '" + args[i] + "'", i, args);
				}
				break;
			}
			case "-parser":
				onlyHelp = false;
				parser = i + 1;
				break;
			case "-target":
			case "-dest":
				onlyHelp = false;
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-tick>", i, args);
				}
				target = args[i];
				break;
			case "--force":
				if (force) {
					exit("double option <--force>", i, args);
				}
				force = true;
				break;
			case "-loop":
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-loop>", i, args);
				}
				if (loop != null) {
					exit("double option <-loop>", i, args);
				}
				loop = args[i];
				break;
			case "-prefix":
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-prefix>", i, args);
				}
				if (prefix != null) {
					exit("double option <-prefix>", i, args);
				}
				prefix = args[i];
				break;
			case "-suffix":
				i ++ ;
				if (i >= args.length) {
					exit("too les arguments after option <-suffix>", i, args);
				}
				if (suffix != null) {
					exit("double option <-suffix>", i, args);
				}
				suffix = args[i];
				break;
			default:
				exit("unknown option: '" + args[i] + "'", i, args);
			}
			if (parser < args.length) {
				break;
			}
		}
		if (onlyHelp) {
			Runtime r = Runtime.getRuntime();
			r.runFinalization();
			r.exit(0);
		}
		if (time == null) {
			exit("no time set in the args!", -1, args);
		}
		switch (art) {
		case artJear:
			time = time.multiply(jearMul);
		case artDay:
			time = time.multiply(dayMul);
		case artHour:
			time = time.multiply(hourMul);
		case artMin:
			time = time.multiply(minMul);
		case artSec:
			time = time.multiply(secMul);
		case artTick:
			ticks = time;
			break;
		default:
			exit("illegal art: " + art, -1, args);
		}
		maxNops = maxNops == -1 ? 1 : maxNops;
		if (loop == null) {
			cg = new CodeGenarator(prefix, suffix, saveRegs, maxNops);
		}else {
			cg = new CodeGenarator(prefix, suffix, loop, saveRegs, maxNops);
		}
		if (target != null) {
			if ( !force && Files.exists(Paths.get(target))) {
				exit("target file exsits already! use <--force> to overwrite. target: '" + target + "'", -1, args);
			}
			out = new PrintStream(target);
		} else {
			out = System.out;
		}
		String[] ret;
		if (parser < args.length) {
			baos = new ByteArrayOutputStream();
			ret = new String[args.length - parser];
			System.arraycopy(args, parser, ret, 0, ret.length);
		} else {
			baos = null;
			ret = null;
		}
		return ret;
	}
	
	private static void help(PrintStream out) {
		out.println("<-?> or <-help>");
		out.println("          to print this msg on the default out");
		out.println("<--help>");
		out.println("          to print this msg on the default err");
		out.println("<-tick> or <-t> [NUMBER]");
		out.println("          to define the time in ticks");
		out.println("<-sec> or <-s> [NUMBER]");
		out.println("          to define the time in seconds");
		out.println("<-min> or <-m> [NUMBER]");
		out.println("          to define the time in minutes");
		out.println("<-hour> or <-h> [NUMBER]");
		out.println("          to define the time in hours");
		out.println("<-day> or <-d> [NUMBER]");
		out.println("          to define the time in days");
		out.println("<-jear> or <-j> [NUMBER]");
		out.println("          to define the time in jears");
		out.println("<-set> (<sec>/<s>, <min>/<m>, <hour>/<h>, <day>/<d> or <jear>/<j>) [NUMBER]");
		out.println("          to define the multiplicatot from (seconds to ticks), (minutes to seconds), (hours to minutes), (days to hours) or (jears to days)");
		out.println("          un of the named will be woth NUMBER of the next step");
		out.println("          ticks <- seconds <- minutes <- hours <- days <- jears");
		out.println("<-parser> ...");
		out.println("          to let after the code genaration the parser parse with the following args");
		out.println("          if there is a src or target file defined in the parser args they will be ignored");
		out.println("<-target> or <-dest> [FILE]");
		out.println("          to define the target of the code generation");
		out.println("          if no target is defined it will output the generated code in the defaut out stream");
		out.println("<--force>");
		out.println("          to force the overwrite of the already exsisting target file");
		out.println("          if no target file exsist or there was no defined this arg will be ignored");
		out.println("<-loop>");
		out.println("          to define the setup label of the wait code");
		out.println("          if there is no init-label defined there it will not be printed");
		out.println("<-prefix>");
		out.println("          to define the prefix of the labels");
		out.println("          the init-label and the start-loop-iteration-label will ignore this");
		out.println("<-prefix>");
		out.println("          to define the suffix of the labels");
		out.println("          the init-label will ignore this");
	}
	
	private static void exit(String msg, int i, String[] args) {
		System.err.println(msg);
		if (i != -1) {
			System.err.println("index=" + i);
		}
		System.err.println("args=" + Arrays.deepToString(args));
		help(System.err);
		System.exit(1);
	}
	
}
