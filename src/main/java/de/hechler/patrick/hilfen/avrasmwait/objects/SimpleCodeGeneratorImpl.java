package de.hechler.patrick.hilfen.avrasmwait.objects;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Set;

import de.hechler.patrick.hilfen.avrasmwait.interfaces.CodeGenerator;


@SuppressWarnings("javadoc")
public class SimpleCodeGeneratorImpl implements CodeGenerator {
	
	public static final BigInteger MAX_REGS  = BigInteger.valueOf(MAX_REG_CNT);
	public static final BigInteger GOOD_REGS = BigInteger.valueOf(GOOD_REGISTER_CNT);
	public static final BigInteger TWO       = BigInteger.valueOf(2);
	
	private final PrintStream out;
	private final int         firstReg;
	private final int         regCnt;
	private final boolean     saveRegs;
	private final boolean     exactValues;
	private final String      loopLabel;
	private final int         nops;
	private final BigInteger  maxTicks;
	private final BigInteger  setupTicks;
	private final BigInteger  loopTicks;
	private final BigInteger  eachLoop;
	
	// AVRe+
	// ATmega328P
	private SimpleCodeGeneratorImpl(PrintStream out, int firstReg, int regCnt, boolean saveRegs, boolean exactValues, String loopLabel, BigInteger nops) {
		this.out         = out;
		this.firstReg    = firstReg;
		this.regCnt      = regCnt;
		this.saveRegs    = saveRegs;
		this.exactValues = exactValues;
		this.loopLabel   = loopLabel;
		this.nops        = nops.intValue();
		this.eachLoop    = BigInteger.valueOf(/* SUBI first + SBCI other */regCnt + /* BRCC */2L).add(nops);
		BigInteger st = Loop.maxIterations(regCnt);
		st             = st.subtract(BigInteger.ONE);
		this.loopTicks = st.multiply(this.eachLoop);
		st             = BigInteger.valueOf( /* init values */ regCnt + /* JMP */ 3L + /* CALL+RET */ 8L);
		if (firstReg + regCnt > GOOD_REGISTER_CNT) {
			st = st.add(BigInteger.valueOf((long) firstReg + regCnt - GOOD_REGISTER_CNT));
		}
		if (saveRegs) {
			st = st.add(BigInteger.valueOf(regCnt << 1));
		}
		st              = st.add(nops);
		this.setupTicks = st;
		this.maxTicks   = this.loopTicks.add(this.setupTicks);
	}
	
	public static SimpleCodeGeneratorImpl create(PrintStream out, int firstReg, int regCnt, boolean saveRegs, boolean exactValues, String loopLabel, int nops) {
		if (out == null) {
			throw new NullPointerException("null output stream on the SimpeCodeGenerator creation");
		} else if (regCnt < 1) {
			throw new IllegalArgumentException("regCnt to small: regCnt=" + regCnt + " MIN_REG_CNT=1");
		} else if (regCnt + firstReg > MAX_REG_CNT) {
			throw new IllegalArgumentException("not enugh registers! regCnt=" + regCnt + " firstReg=" + firstReg + " MAX_REG_CNT=" + MAX_REG_CNT);
		} else if (firstReg < 0) {
			throw new IllegalArgumentException("regs can not be negative: firstReg=" + firstReg);
		}
		return new SimpleCodeGeneratorImpl(out, firstReg, regCnt, saveRegs, exactValues, loopLabel, BigInteger.valueOf(nops));
	}
	
	public static final int minRegCnt(BigInteger ticks, boolean saveRegs, int nops) {
		final BigInteger origTicks = ticks;
		BigInteger       regs      = BigInteger.ONE;
		if (saveRegs) {
			ticks = ticks.subtract(TWO);
		}
		while (true) {
			BigInteger actualMaxTicks = regs.add(TWO).add(BigInteger.valueOf(nops)).multiply(Loop.maxIterations(regs.intValue()));
			if (actualMaxTicks.compareTo(ticks) > 0) {
				break;
			}
			regs = regs.add(BigInteger.ONE);
			if (saveRegs) {
				ticks = ticks.subtract(TWO);
			}
			if (regs.compareTo(MAX_REGS) > 0) {
				throw new IllegalArgumentException("too much ticks: need=HEX[" + origTicks.toString(16) + "], MAX_REGS=HEX[" + MAX_REGS.toString(16) + "]");
			} else if (regs.compareTo(GOOD_REGS) > 0) {
				ticks = ticks.subtract(BigInteger.ONE);
			}
		}
		return regs.intValue();
	}
	
	@Override
	public int myUsedRegs() {
		return this.regCnt;
	}
	
	@Override
	public int myFirstReg() {
		return this.firstReg;
	}
	
	@Override
	public boolean savesRegs() {
		return this.saveRegs;
	}
	
	@Override
	public String loopLabel() {
		return this.loopLabel;
	}
	
	@Override
	public String generateInitAndLoop(BigInteger ticks, Set<String> alreadyUsed) {
		String label = "wait_0x" + ticks.toString(16) + "t_" + this.loopLabel;
		while (!alreadyUsed.add(label) || label.equals(this.loopLabel)) {
			label = label.concat("_");
		}
		generateInitAndLoop(ticks, label);
		return label;
	}
	
	@Override
	public void generateInitAndLoop(BigInteger ticks, String initLabelName) {
		ticks = ticks.add(BigInteger.valueOf(3L));
		if (ticks.compareTo(this.maxTicks) > 0) {
			CodeGenerator.super.generateInitAndLoop(ticks, initLabelName);
		} else {
			generateInit(ticks, initLabelName, true);
			generateLoop();
		}
	}
	
	@Override
	public String generateInit(BigInteger ticks, Set<String> alreadyUsed) {
		String label = "wait_0x" + ticks.toString(16) + "t_" + this.loopLabel;
		while (!alreadyUsed.add(label) || label.equals(this.loopLabel)) {
			label = label.concat("_");
		}
		generateInit(ticks, label);
		return label;
	}
	
	@Override
	public void generateInit(BigInteger ticks, String initLabelName) {
		generateInit(ticks, initLabelName, false);
	}
	
	public void generateInit(BigInteger ticks, String initLabelName, boolean suppressJump) {
		if (this.loopLabel.equals(initLabelName)) {
			throw new IllegalArgumentException("label-name already used as the loop-label: '" + initLabelName + "' loop-label: '" + this.loopLabel + "'");
		}
		if (ticks.compareTo(this.maxTicks) > 0) {
			throw new IllegalArgumentException("too large value: maxTicks= HEX[" + this.maxTicks.toString(16) + "] ticks=HEX[" + ticks.toString(16) + "]");
		}
		ticks = ticks.subtract(this.setupTicks);
		if (this.exactValues) {
			BigInteger[] val = ticks.divideAndRemainder(this.eachLoop);
			ticks = val[0];
			long remainder = val[1].longValueExact();
			for (; remainder > 0; remainder--) {
				this.out.println("\tNOP");
			}
		} else {
			ticks = ticks.divide(this.eachLoop);
		}
		byte[] bytes = ticks.toByteArray();
		if (bytes.length < this.regCnt) {
			byte[] zw = new byte[this.regCnt];
			System.arraycopy(bytes, 0, zw, this.regCnt - bytes.length, bytes.length);
			bytes = zw;
		}
		this.out.println(initLabelName + ":");
		for (int i = this.regCnt; --i >= 0;) { // do reverse order in case non-good registers are used
			int    val     = 0xFF & bytes[this.regCnt - i - 1];
			String str     = Integer.toHexString(val);
			String regName = CodeGenerator.register(i + this.firstReg);
			if (this.saveRegs) {
				this.out.println("\tPUSH " + regName);
			}
			if (i >= GOOD_REGISTER_CNT) {
				String tmpRegName = CodeGenerator.register(GOOD_REGISTER_CNT - 1);
				if (this.saveRegs && this.firstReg >= GOOD_REGISTER_CNT && i == this.regCnt - 1) {
					this.out.println("\tPUSH " + tmpRegName);
					// there is no need for a flag only for this special case
					// this can only occur if the first register is explicitly set, so the user is (partly) responsible for that
				}
				this.out.println("\tLDI " + tmpRegName + ", 0x" + str);
				this.out.println("\tMOV " + regName + ", " + tmpRegName);
			} else if (str.length() > 1) {
				this.out.println("\tLDI " + regName + ", 0x" + str);
			} else {
				this.out.println("\tLDI " + regName + ", 0x0" + str);
			}
		}
		if (!suppressJump) {
			this.out.println("\tJMP " + this.loopLabel);
		}
	}
	
	@Override
	public void generateLoop() {
		this.out.println(this.loopLabel + ":");
		for (int i = 0; i < this.nops; i++) {
			this.out.println("\tNOP");
		}
		this.out.println("\tSUBI " + CodeGenerator.register(this.firstReg) + ", 1; DEC does not set the Carry");
		for (int i = 1; i < this.regCnt; i++) {
			this.out.println("\tSBCI " + CodeGenerator.register(i + this.firstReg) + ", 0");
		}
		this.out.println("\tBRCC " + this.loopLabel + "; loop until an overflow occurs");
		if (this.saveRegs) {
			if (this.firstReg >= GOOD_REGISTER_CNT) {
				this.out.println("\tPOP " + CodeGenerator.register(GOOD_REGISTER_CNT - 1));
			}
			for (int i = 0; i < this.regCnt; i++) { // the registers are pushed in reverse order, so they are popped in reversed reversed order
				this.out.println("\tPOP " + CodeGenerator.register(this.firstReg + i));
			}
		}
		this.out.println("\tRET");
	}
	
}
