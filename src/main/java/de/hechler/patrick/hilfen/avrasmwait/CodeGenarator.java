package de.hechler.patrick.hilfen.avrasmwait;

import java.io.PrintStream;
import java.math.BigInteger;

public class CodeGenarator {
	
	/**
	 * the count of available registers of the ATMEGA-328p
	 */
	public static final int MAX_REG_CNT = 32;
	public static final int MIN_REG_CNT = 1;
	
	
	
	/**
	 * there are at first the register 16-31, because they have more functions (ldi)
	 * 
	 * @see #goodRegisterCnt
	 */
	private static final String[] register = new String[] {"R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31",
		"R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", };
	
	/**
	 * the number of good registers
	 * 
	 * @see #register
	 */
	private static final int goodRegisterCnt = 16;
	
	private String  labelPrefix;
	private String  labelSuffix;
	private String  loopLabel;
	private boolean saveRegs;
	private int     maxNops;
	
	
	
	public CodeGenarator(String labelPrefix, String labelSuffix, boolean saveRegs, int maxNops) {
		this(labelPrefix, labelSuffix, (labelPrefix == null ? "" : labelPrefix) + "wait" + (labelSuffix == null ? "" : labelSuffix), saveRegs, maxNops);
	}
	
	public CodeGenarator(String labelPrefix, String labelSuffix, String loopLabel, boolean saveRegs, int maxNops) {
		this.labelPrefix = labelPrefix == null ? "" : labelPrefix;
		this.labelSuffix = labelSuffix == null ? "" : labelSuffix;
		this.loopLabel = loopLabel;
		this.saveRegs = saveRegs;
		this.maxNops = maxNops;
	}
	
	public void setSaveRegs(boolean saveRegs) {
		this.saveRegs = saveRegs;
	}
	
	public boolean isSaveRegs() {
		return saveRegs;
	}
	
	public int getMaxNops() {
		return maxNops;
	}
	
	public void setMaxNops(int maxNops) {
		this.maxNops = maxNops;
	}
	
	private static BigInteger maxTicks(int regCnt, boolean saveRegs, BigInteger nops) {
		BigInteger loopLen = generateLoopLen(regCnt, nops);
		Loop loop = new Loop(regCnt, Loop.maxIterations(regCnt));
		BigInteger setup = generateSetupTicks(regCnt, saveRegs);
		return loop.calculateTicks(setup, loopLen);
	}
	
	private static BigInteger generateLoopLen(int regCnt, BigInteger nops) {
		if (nops.signum() < 0) {
			throw new AssertionError("can't have a negative number of nops: DEC[" + nops.toString(10) + "]");
		}
		BigInteger loopLen;
		if (regCnt > goodRegisterCnt) {
			loopLen = genarateBI( (2 * (regCnt - goodRegisterCnt)) + goodRegisterCnt + 2);// 2*badRegs + goodRegs + branch
		} else {
			loopLen = genarateBI(regCnt + 2);// decrement + branch
		}
		loopLen = loopLen.add(nops);
		return loopLen;
	}
	
	private static BigInteger generateSetupTicks(int regCnt, boolean saveRegs) {
		BigInteger setup;
		if (saveRegs) {
			setup = genarateBI(regCnt * 4);// push and pop need both 2 ticks
		} else {
			setup = BigInteger.ZERO;
		}
		if (regCnt > goodRegisterCnt) {
			setup = setup.add(genarateBI( (2 * (regCnt - goodRegisterCnt)) + goodRegisterCnt));
		} else {
			setup = setup.add(genarateBI(regCnt));
		}
		return setup;
	}
	
	public void genatate(PrintStream out, BigInteger ticks) {
		int regs = findRegCount(ticks);
		Loop loop = new Loop(regs);
		ticks = ticks.subtract(generateSetupTicks(regs, saveRegs));
		BigInteger nops = BigInteger.ZERO;
		BigInteger[] zw;
		while (true) {
			BigInteger singleLoopLen = generateLoopLen(regs, nops);
			zw = ticks.divideAndRemainder(singleLoopLen);
			int cmp = zw[0].compareTo(Loop.maxIterations(regs));
			if (cmp < 0) {
				loop.setIterations(zw[0]);
				break;
			} else if (cmp == 0) {
				if (zw[1].signum() <= 0) {
					loop.setIterations(zw[0]);
					break;
				}
			}
			nops.add(BigInteger.ONE);
		}
		if (saveRegs) {
			for (int i = 0; i < regs; i ++ ) {
				out.println("\tPUSH " + register[i]);
			}
		}
		byte[] bytes = loop.getIterations().toByteArray();
		if (regs > goodRegisterCnt) {// at first the badRegs, because they need a goodReg to overwrite
			for (int i = goodRegisterCnt + 1; i < regs; i ++ ) {
				out.println("\tLDI " + register[0] + ", 0x" + Integer.toHexString( ((int) bytes[i]) & 0xFF));
				out.println("\tMOV " + register[i] + ", " + register[0]);
			}
		}
		for (int i = 0; i < regs && i < goodRegisterCnt; i ++ ) {
			out.println("\tLDI " + register[i] + ", 0x" + Integer.toHexString( ((int) bytes[i]) & 0xFF));
		}
		out.println(loopLabel + ":");
		out.println("\tDEC " + register[0]);
		for (int i = 1; i < regs && i < goodRegisterCnt; i ++ ) {
			out.println("\tSBCI " + register[i] + ", 0");
		}
		for (int i = goodRegisterCnt; i < regs; i ++ ) {
			String noCarryLabel = labelPrefix + "noCarryBy" + i + labelSuffix;
			out.println("\tBRCC " + noCarryLabel);
			out.println("\tDEC " + register[i]);
			out.println(noCarryLabel + ":");
		}
		out.println("\tBRCC " + loopLabel);
		if (saveRegs) {
			for (int i = 0; i < regs; i ++ ) {
				out.println("\tPOP " + register[i]);
			}
		}
		out.println("\tRET");
	}
	
	private int findRegCount(BigInteger ticks) {
		BigInteger nops = genarateBI(maxNops);
		BigInteger maxTicks = null;
		for (int regs = MIN_REG_CNT; regs < MAX_REG_CNT; regs ++ ) {
			maxTicks = maxTicks(regs, saveRegs, nops);
			if (maxTicks.compareTo(ticks) >= 0) {
				return regs;
			}
		}
		throw new AssertionError("too many ticks! need: HEX[" + ticks.toString(16) + "] real-max: HEX[" + maxTicks.toString(16) + "]");
	}
	
	public static BigInteger genarateBI(int val) {
		return new BigInteger(new byte[] {(byte) (val >> 24), (byte) (val >> 16), (byte) (val >> 8), (byte) val });
	}
	
}
