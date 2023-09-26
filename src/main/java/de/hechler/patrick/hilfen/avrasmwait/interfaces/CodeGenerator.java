package de.hechler.patrick.hilfen.avrasmwait.interfaces;

import static de.hechler.patrick.hilfen.avrasmwait.Main.REGISTER_NAMES;

import java.math.BigInteger;
import java.util.Set;

public interface CodeGenerator {
	
	/**
	 * the count of available registers of the ATMEGA-328p
	 */
	int MAX_REG_CNT       = 32;
	/**
	 * the number of good registers (they support more instructions (for example {@code LDI}))
	 */
	int GOOD_REGISTER_CNT = 16;
	
	/**
	 * this method converts intern {@code int} registers to the extern {@link String} registers<br>
	 * all registers below {@link #GOOD_REGISTER_CNT} are good registers and all above not (ignoring the fact that it is actually reversed)
	 * 
	 * @param reg
	 *            the intern register number
	 * 			
	 * @return the extern {@link String} register
	 */
	public static String register(int reg) {
		if (reg < 0) {
			throw new IndexOutOfBoundsException("negative reg: " + reg);
		}
		if (reg >= MAX_REG_CNT) {
			throw new IndexOutOfBoundsException("too large reg: " + reg);
		}
		String name = REGISTER_NAMES[reg];
		if (name != null) {
			return name;
		}
		if (reg < GOOD_REGISTER_CNT) {
			name = "R" + (reg + (MAX_REG_CNT - GOOD_REGISTER_CNT));
		} else {
			name = "R" + (reg - GOOD_REGISTER_CNT);
		}
		REGISTER_NAMES[reg] = name;
		return name;
	}
	
	/**
	 * returns the number registers used by this {@link CodeGenerator}
	 * 
	 * @return the number registers used by this {@link CodeGenerator}
	 */
	int myUsedRegs();
	
	/**
	 * returns the intern number of the first register used by this {@link CodeGenerator}
	 * 
	 * @return the intern number of the first register used by this {@link CodeGenerator}
	 */
	int myFirstReg();
	
	/**
	 * returns <code>true</code> if this {@link CodeGenerator} saves the registers and sets them back after the loop ended and <code>false</code> if not
	 * 
	 * @return <code>true</code> if this {@link CodeGenerator} saves the registers and sets them back after the loop ended and <code>false</code> if not
	 */
	boolean savesRegs();
	
	/**
	 * returns the label of the loop to be called if the init is not needed
	 * 
	 * @return the label of the loop to be called if the init is not needed
	 */
	String loopLabel();
	
	/**
	 * generates an init label and returns the name of the generated label<br>
	 * init-label-name will not be in the previous {@code alreadyUsed} {@link Set}, but it will be added to it
	 * 
	 * @param ticks
	 *                    the ticks of the loop to waste
	 * @param alreadyUsed
	 *                    the used label-names
	 * 					
	 * @return the name of the generated init-label
	 */
	String generateInit(BigInteger ticks, Set<String> alreadyUsed);
	
	void generateInit(BigInteger ticks, String initLabelName);
	
	/**
	 * generates an init-label with an loop following and returns the name of the generated init-label<br>
	 * init-label-name will not be in the previous {@code alreadyUsed} {@link Set}, but it will be added to it<br>
	 * the {@link #loopLabel() loop-label} will be added to the {@code alreadyUsed} {@link Set} if it is not already in it and if it is already there it will ignore
	 * this fact
	 * 
	 * @param ticks
	 *                    the ticks of the loop to waste
	 * @param alreadyUsed
	 *                    the used label-names
	 * 					
	 * @return the name of the generated init-label
	 * 					
	 * @see #generateInit(BigInteger, Set)
	 * @see #generateLoop()
	 */
	default String generateInitAndLoop(BigInteger ticks, Set<String> alreadyUsed) {
		String label = generateInit(ticks, alreadyUsed);
		generateLoop();
		return label;
	}
	
	default void generateInitAndLoop(BigInteger ticks, String initLabelName) {
		generateInit(ticks, initLabelName);
		generateLoop();
	}
	
	/**
	 * this will generate the loop with the {@link #loopLabel() loop-label} at the start without any init to set the number of iteration<br>
	 * the behavior of this method an a second call is not defined
	 */
	void generateLoop();
	
}
