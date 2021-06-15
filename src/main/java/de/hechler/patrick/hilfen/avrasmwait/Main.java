package de.hechler.patrick.hilfen.avrasmwait;

import java.io.PrintStream;
import java.util.Arrays;

public class Main {
	
	public static void main(String[] args) {
		setup(args);
	}
	
	private static void setup(String[] args) {
		for (int i = 0; i < args.length; i ++ ) {
			switch (args[i]) {
			case "-help":
				help(System.out);
				break;
			default:
				exit("unknown arg: '" + args[i] + "'", i, args);
			}
		}
	}
	
	private static void help(PrintStream out) {
	}
	
	private static void exit(String msg, int index, String[] args) {
		System.err.println(msg);
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
