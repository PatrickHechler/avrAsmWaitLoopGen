package de.hechler.patrick.hilfen.avrasmwait;

import java.math.BigInteger;

public class Loop {
	
	private static final BigInteger[] MAX_ITERATIONS = new BigInteger[] {
		new BigInteger(1, new byte[] { -1 }),
		new BigInteger(1, new byte[] { -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
		new BigInteger(1, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }),
	};
	
	private int        registers;
	private BigInteger iterations;
	
	public Loop() {
		this.iterations = BigInteger.ZERO;
	}
	
	public Loop(int registers) {
		this.registers = registers;
		this.iterations = BigInteger.ZERO;
	}
	
	public Loop(int registers, BigInteger iterations) {
		this.registers = registers;
		this.iterations = iterations;
	}
	
	
	public int getRegisters() {
		return registers;
	}
	
	public void setRegisters(int registers) throws IllegalArgumentException {
		validate(this.iterations, registers);
		this.registers = registers;
	}
	
	public BigInteger getIterations() {
		return iterations;
	}
	
	public void setIterations(BigInteger iterations) throws IndexOutOfBoundsException {
		validate(iterations, this.registers);
		this.iterations = iterations;
	}
	
	public BigInteger calculateTicks(BigInteger setup, BigInteger loopLen) {
		BigInteger cal = loopLen;
		cal.multiply(iterations);
		cal.add(setup);
		return cal;
	}
	
	private void validate(BigInteger iterations, int registers) throws IllegalArgumentException, IndexOutOfBoundsException {
		if (registers > CodeGenarator.MAX_REG_CNT) {
			throw new IllegalArgumentException("too many registers: " + registers + " max=" + CodeGenarator.MAX_REG_CNT);
		} else if (registers < CodeGenarator.MIN_REG_CNT) {
			throw new IllegalArgumentException("too less registers: " + registers + " min=" + CodeGenarator.MIN_REG_CNT);
		}
		BigInteger max = maxIterations(registers);
		if (iterations.compareTo(max) > 0) {
			throw new IndexOutOfBoundsException("iters=" + iterations.toString(16) + " max=" + max.toString(16));
		}
	}
	
	public static BigInteger maxIterations(int registers) {
		return MAX_ITERATIONS[registers - 1];
	}
	
}

