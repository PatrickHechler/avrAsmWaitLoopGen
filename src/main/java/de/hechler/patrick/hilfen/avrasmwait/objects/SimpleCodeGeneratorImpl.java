package de.hechler.patrick.hilfen.avrasmwait.objects;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Set;

import de.hechler.patrick.hilfen.avrasmwait.interfaces.CodeGenerator;


public class SimpleCodeGeneratorImpl implements CodeGenerator {
	
	public static final BigInteger MAX_REGS = BigInteger.valueOf(GOOD_REGISTER_CNT);
	public static final BigInteger TWO      = BigInteger.valueOf(2);
	
	private final PrintStream out;
	private final int         firstReg;
	private final int         regCnt;
	private final boolean     saveRegs;
	private final String      loopLabel;
	private final boolean     crashOnWrongname;
	private final BigInteger  maxTicks;
	private final BigInteger  setupTicks;
	private final BigInteger  loopTicks;
	private final BigInteger  eachLoop;
	
	
	
	private SimpleCodeGeneratorImpl(PrintStream out, int firstReg, int regCnt, boolean saveRegs, String loopLabel, boolean crashOnWrongname) {
		this.out = out;
		this.firstReg = firstReg;
		this.regCnt = regCnt;
		this.saveRegs = saveRegs;
		this.loopLabel = loopLabel;
		this.crashOnWrongname = crashOnWrongname;
		this.eachLoop = BigInteger.valueOf(/* SUBI first + SBCI other */regCnt + /* BRCC */2);
		BigInteger zw = Loop.maxIterations(regCnt);
		zw = zw.subtract(BigInteger.ONE);
		this.loopTicks = zw.multiply(eachLoop);
		zw = BigInteger.valueOf( /* last loop + 1 */ (regCnt << 1) - /* last loop branch with only 1 t */ 1 + /* JMP */ 3 + /* CALL+RET */ 8);
		if (saveRegs) {
			zw = zw.add(BigInteger.valueOf(regCnt << 1));
		}
		this.setupTicks = zw;
		this.maxTicks = loopTicks.add(setupTicks);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int firstReg, int regCnt, boolean saveRegs, String loopLabel, boolean crashOnWrongname) {
		if (out == null) {
			throw new NullPointerException("null output stream on the SimpeCodeGenerator creation");
		} else if (regCnt < MIN_REG_CNT) {
			throw new IllegalArgumentException("regCnt to small: regCnt=" + regCnt + " MIN_REG_CNT=" + MIN_REG_CNT);
		} else if (regCnt + firstReg < GOOD_REGISTER_CNT) {
			throw new IllegalArgumentException("I refuse to use not good regs! regCnt=" + regCnt + " firstReg=" + firstReg + " GOOD_REG_CNT=" + GOOD_REGISTER_CNT);
		} else if (firstReg < 0) {
			throw new IllegalArgumentException("regs can not be negative: firstReg=" + firstReg);
		}
		return new SimpleCodeGeneratorImpl(out, firstReg, regCnt, saveRegs, loopLabel, crashOnWrongname);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int firstReg, int regCnt, boolean saveRegs, String loopLabel) {
		return create(out, firstReg, regCnt, saveRegs, loopLabel, true);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int regCnt, boolean saveRegs, String loopLabel, boolean crashOnWrongname) {
		return create(out, 0, regCnt, saveRegs, loopLabel, crashOnWrongname);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int firstReg, int regCnt, String loopLabel, boolean crashOnWrongname) {
		return create(out, firstReg, regCnt, true, loopLabel, crashOnWrongname);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int firstReg, int regCnt, String loopLabel) {
		return create(out, firstReg, regCnt, true, loopLabel, true);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int regCnt, boolean saveRegs, String loopLabel) {
		return create(out, 0, regCnt, saveRegs, loopLabel, true);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int regCnt, String loopLabel, boolean crashOnWrongname) {
		return create(out, 0, regCnt, true, loopLabel, crashOnWrongname);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int regCnt, String loopLabel) {
		return create(out, 0, regCnt, true, loopLabel, true);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int firstReg, boolean saveRegs, String loopLabel, boolean crashOnWrongname, BigInteger maxTicks) {
		int regCnt = minRegCnt(maxTicks, saveRegs);
		return create(out, firstReg, regCnt, saveRegs, loopLabel, crashOnWrongname);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int firstReg, boolean saveRegs, String loopLabel, BigInteger maxTicks) {
		int regCnt = minRegCnt(maxTicks, saveRegs);
		return create(out, firstReg, regCnt, saveRegs, loopLabel, true);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, boolean saveRegs, String loopLabel, boolean crashOnWrongname, BigInteger maxTicks) {
		int regCnt = minRegCnt(maxTicks, saveRegs);
		return create(out, 0, regCnt, saveRegs, loopLabel, crashOnWrongname);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int firstReg, String loopLabel, boolean crashOnWrongname, BigInteger maxTicks) {
		int regCnt = minRegCnt(maxTicks, true);
		return create(out, firstReg, regCnt, true, loopLabel, crashOnWrongname);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int firstReg, String loopLabel, BigInteger maxTicks) {
		int regCnt = minRegCnt(maxTicks, true);
		return create(out, firstReg, regCnt, true, loopLabel, true);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, boolean saveRegs, String loopLabel, BigInteger maxTicks) {
		int regCnt = minRegCnt(maxTicks, saveRegs);
		return create(out, 0, regCnt, saveRegs, loopLabel, true);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, String loopLabel, boolean crashOnWrongname, BigInteger maxTicks) {
		int regCnt = minRegCnt(maxTicks, true);
		return create(out, 0, regCnt, true, loopLabel, crashOnWrongname);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, String loopLabel, BigInteger maxTicks) {
		int regCnt = minRegCnt(maxTicks, true);
		return create(out, 0, regCnt, true, loopLabel, true);
	}
	
	private static final int minRegCnt(BigInteger ticks, boolean saveRegs) {
		final BigInteger origTicks = ticks;
		BigInteger regs = BigInteger.valueOf(MIN_REG_CNT);
		if (saveRegs) {
			ticks = ticks.subtract(regs.shiftLeft(1));
		}
		while (true) {
			BigInteger actualMaxTicks = regs.multiply(Loop.maxIterations(regs.intValue()));
			if (actualMaxTicks.compareTo(ticks) > 0) {
				break;
			}
			regs = regs.add(BigInteger.ONE);
			if (saveRegs) {
				ticks = ticks.subtract(TWO);
			}
			if (regs.compareTo(MAX_REGS) > 0) {
				throw new IllegalArgumentException("too much ticks: need=HEX[" + origTicks.toString(16) + "], MAX_REGS=HEX[" + MAX_REGS.toString(16) + "]");
			}
		}
		return regs.intValue();
	}
	
	
	
	@Override
	public int myUsedRegs() {
		return regCnt;
	}
	
	@Override
	public int myFirstReg() {
		return firstReg;
	}
	
	@Override
	public boolean savesRegs() {
		return saveRegs;
	}
	
	@Override
	public String loopLabel() {
		return loopLabel;
	}
	
	@Override
	public String generateInit(BigInteger ticks, Set <String> alreadyUsed) {
		String label = loopLabel + "_HEX_" + ticks.toString(16);
		while ( !alreadyUsed.add(label)) {
			if (crashOnWrongname) {
				throw new AssertionError("label-name already used: '" + label + "' used: '" + alreadyUsed + "'");
			}
			label = label + "_";
		}
		generateInit(ticks, label);
		return label;
	}
	
	@Override
	public void generateInit(BigInteger ticks, String initLabelName) {
		if (ticks.compareTo(maxTicks) > 0) {
			throw new IllegalArgumentException("too large value: maxTicks= HEX[" + maxTicks.toString(16) + "] ticks=HEX[" + ticks.toString(16) + "]");
		}
		ticks = ticks.subtract(setupTicks);
		byte[] bytes = ticks.divide(eachLoop).toByteArray();
		if (bytes.length < regCnt) {
			byte[] zw = new byte[regCnt];
			System.arraycopy(bytes, 0, zw, 0, bytes.length);
			bytes = zw;
		}
		out.println(initLabelName + ":");
		for (int i = 0; i < regCnt; i ++ ) {
			int val = (int) bytes[i];
			val = val & 0xFF;
			String str = Integer.toHexString(val);
			if (str.length() > 1) {
				out.println("\tLDI " + CodeGenerator.register(i + firstReg) + ", 0x" + str);
			} else {
				out.println("\tLDI " + CodeGenerator.register(i + firstReg) + ", 0x0" + str);
			}
		}
		out.println("\tJMP " + loopLabel);
	}
	
	@Override
	public void generateLoop() {
		out.println("\tSUBI " + CodeGenerator.register(firstReg) + ", 1; DEC does not set the Carry");
		for (int i = 1; i < regCnt; i ++ ) {
			out.println("\tSBCI " + CodeGenerator.register(i + firstReg) + ", 0");
		}
		out.println("\tBRCC " + loopLabel);
		out.println("\tRET");
	}
	
}
