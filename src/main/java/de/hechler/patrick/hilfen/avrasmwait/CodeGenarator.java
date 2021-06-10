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
	 * there are at first the register 16-31, because they have more functions
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
	
	private String     labelPrefix;
	private String     labelSuffix;
	private String     initLabel;
	private boolean    saveRegs;
	private BigInteger maxNops;
	
	
	
	public CodeGenarator(String labelPrefix, String labelSuffix, boolean saveRegs, int maxNops) {
		this(labelPrefix, labelSuffix, null, saveRegs, maxNops);
	}
	
	public CodeGenarator(String labelPrefix, String labelSuffix, String initLabel, boolean saveRegs, int maxNops) {
		this.labelPrefix = labelPrefix == null ? "" : labelPrefix;
		this.labelSuffix = labelSuffix == null ? "" : labelSuffix;
		this.initLabel = initLabel;
		this.saveRegs = saveRegs;
		this.maxNops = genarateBI(maxNops);
	}
	
	public void setSaveRegs(boolean saveRegs) {
		this.saveRegs = saveRegs;
	}
	
	public boolean isSaveRegs() {
		return saveRegs;
	}
	
	public int getMaxNops() {
		return maxNops.intValue();
	}
	
	public void setMaxNops(int maxNops) {
		this.maxNops = genarateBI(maxNops);
	}
	
	private BigInteger maxTicks(int regCnt, boolean saveRegs, BigInteger nops) {
		BigInteger loopLen = generateLoopLen(regCnt, nops);
		Loop loop = new Loop(regCnt, Loop.maxIterations(regCnt));
		BigInteger setup = generateSetupTicksWithoutLastLoop(regCnt, saveRegs);
		setup = setup.add(loopLen.subtract(BigInteger.ONE));
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
	
	private BigInteger generateSetupTicksWithoutLastLoop(int regCnt, boolean saveRegs) {
		BigInteger setup;
		setup = genarateBI(regCnt);
		if (regCnt > goodRegisterCnt) {
			setup = setup.add(genarateBI(regCnt - goodRegisterCnt));
		}
		if (saveRegs) {
			setup = setup.add(genarateBI(regCnt * 4));// push and pop need both 2 ticks
		}
		if (this.initLabel != null) {
			setup = setup.add(genarateBI(8));// call + ret
		}
		return setup;
	}
	
	public void generate(PrintStream out, final BigInteger wanted) {
		final int regs = findRegCount(wanted);
		BigInteger setup = generateSetupTicksWithoutLastLoop(regs, this.saveRegs);
		BigInteger remaining = wanted.subtract(setup);
		BigInteger nops = BigInteger.ZERO;
		BigInteger maxIterations = Loop.maxIterations(regs).subtract(BigInteger.ONE);// last loop in setup
		BigInteger singleLoop = generateLoopLen(regs, BigInteger.ZERO);
		BigInteger allLoopTicks = maxIterations.multiply(singleLoop);
		BigInteger zw = allLoopTicks.subtract(BigInteger.ONE);
		setup = setup.add(zw);
		remaining = remaining.subtract(zw);
		while (nops.compareTo(maxNops) <= 0) {
			if (allLoopTicks.compareTo(remaining) >= 0) {
				break;
			}
			nops = nops.add(BigInteger.ONE);
			singleLoop = singleLoop.add(BigInteger.ONE);
			allLoopTicks.add(maxIterations);
			setup = setup.add(BigInteger.ONE);
			remaining = remaining.subtract(BigInteger.ONE);
		}
		BigInteger loopCnt = wanted.divide(remaining);
		BigInteger zw0 = singleLoop.multiply(loopCnt);
		allLoopTicks = zw0;
		zw = remaining.subtract(zw0).abs();
		zw0 = zw0.add(singleLoop);
		zw0 = remaining.subtract(zw0).abs();
		if (zw0.compareTo(zw) < 0) {
			loopCnt = loopCnt.add(BigInteger.ONE);
			allLoopTicks = allLoopTicks.add(singleLoop);
		}
		printCode(out, regs, setup, singleLoop, allLoopTicks, loopCnt, wanted);
	}
	
	private void printCode(PrintStream out, final int regs, BigInteger setup, BigInteger singleLoop, BigInteger allLoopTicks, BigInteger loopCnt, BigInteger wanted) {
		if (initLabel != null) {
			out.println(initLabel + ":;wait: HEX[" + allLoopTicks.add(setup).toString(16) + "] ticks");
		}
		if (saveRegs) {
			for (int i = 0; i < regs; i ++ ) {
				out.println("\tPUSH " + register[i] + "; 4t");
			}
		}
		loopCnt = loopCnt.add(BigInteger.ONE);// last loop in setup
		byte[] bytes = loopCnt.toByteArray();
		out.println("\tLDI " + register[0] + ", " + (bytes.length > 0 ? Integer.toHexString((int) (0xFF & bytes[0])) : "0x00") + ";setup: HEX[" + setup.toString(16) + "]ticks");
		for (int i = 1; i < bytes.length; i ++ ) {
			out.println("\tLDI " + register[i] + ", " + Integer.toHexString((int) (0xFF & bytes[i])));
		}
		for (int i = bytes.length; i < regs; i ++ ) {
			out.println("\tLDI " + register[i] + ", 0x00");
		}
		String loopLabel = (labelPrefix == null ? "" : labelPrefix) + (initLabel == null ? initLabel : "wait") + (labelSuffix == null ? "_loop" : labelSuffix);
		out.println(loopLabel + ":;loob-count: HEX[" + loopCnt.toString(16) + "]");
		out.println("\tDEC " + register[0] + "; 1t single-loop: HEX[" + singleLoop.toString(16) + "]");
		if (regs > 1) {
			out.println("\tSBCI " + register[1] + "; 1t single-loop: HEX[" + allLoopTicks.toString(16) + "]");
		}
		for (int i = 2; i < regs && i < goodRegisterCnt; i ++ ) {
			out.println("\tSBCI " + register[i] + "; 1t");
		}
		for (int i = goodRegisterCnt; i < regs; i ++ ) {
			String name = loopLabel + "_no_carry_" + i;
			out.println("\tBRCC " + loopLabel + "; 1/2t");
			out.println("\tDEC " + register[i] + "; 1t");
			out.println(name + ":;together always 2t");
		}
		if (saveRegs) {
			for (int i = 0; i < regs; i ++ ) {
				out.println("\tPOP " + register[i] + "; 4t");
			}
		}
		out.println("\tRET; 4t wanted: HEX[" + wanted.toString(16) + "]");
	}
	
	private int findRegCount(BigInteger ticks) {
		BigInteger nops = maxNops;
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
