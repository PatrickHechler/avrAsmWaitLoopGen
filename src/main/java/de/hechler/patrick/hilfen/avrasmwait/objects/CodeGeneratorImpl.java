package de.hechler.patrick.hilfen.avrasmwait.objects;

import java.math.BigInteger;
import java.util.Set;

import de.hechler.patrick.hilfen.avrasmwait.interfaces.CodeGenerator;


public class CodeGeneratorImpl implements CodeGenerator {
	
	private final int     firstReg;
	private final int     regCnt;
	private final boolean saveRegs;
	private final String  loopLabel;
	private final boolean crashOnWrongname;
	
	
	
	public CodeGeneratorImpl(int firstReg, int regCnt, boolean saveRegs, String loopLabel) {
		this(firstReg, regCnt, saveRegs, loopLabel, true);
	}
	
	public CodeGeneratorImpl(int firstReg, int regCnt, boolean saveRegs, String loopLabel, boolean crashOnWrongname) {
		this.firstReg = firstReg;
		this.regCnt = regCnt;
		this.saveRegs = saveRegs;
		this.loopLabel = loopLabel;
		this.crashOnWrongname = crashOnWrongname;
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
		while (!alreadyUsed.add(label)) {
			if (crashOnWrongname) {
				throw new AssertionError("");
			}
			label = label + "_";
		}
		generateInit(ticks, label);
		return label;
	}
	
	@Override
	public void generateInit(BigInteger ticks, String initLabelName) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void generateLoop() {
		// TODO Auto-generated method stub
		
	}
	
}
