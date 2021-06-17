package de.hechler.patrick.hilfen.avrasmwait;

import java.math.BigInteger;

import de.hechler.patrick.fileparser.Arg;
import de.hechler.patrick.hilfen.gui.annotations.lines.GUINormalLine;
import de.hechler.patrick.hilfen.gui.annotations.lines.GUIOwnWindow;
import de.hechler.patrick.hilfen.gui.annotations.values.ComboValue;
import de.hechler.patrick.hilfen.gui.annotations.values.NumberValue;
import de.hechler.patrick.hilfen.gui.annotations.values.StringArray;
import de.hechler.patrick.hilfen.gui.annotations.values.TextValue;
import de.hechler.patrick.hilfen.gui.enums.GUIArt;

public class ArgContainer {
	
	// <-target> or <-t> [TARGET_FILE]
	// to set the target file to recive the generated code
	// if no target file is set the generated code will be printed on the default out stream
	@Arg("-target")
	// @GUIFileChose(value = "no target file", reset = "reset target file", buttonText = "choose target file")
	@GUINormalLine(firstArt = GUIArt.deleteButton, firstText = "reset", secondArt = GUIArt.fileChoose, secondText = "select", thirdArt = GUIArt.choosenFileModifiable, thirdText = "target file",
		order = 0)
	private String targetFile;
	// <-force>
	// to force overwrite if the target file exits already
	// if no target file exits this will be ignored
	@Arg("-force")
	@GUINormalLine(firstArt = GUIArt.unmodifiableText, firstText = "force overwrite:", secondArt = GUIArt.comboBoxFalseTrue, secondText = {"exit if target exist", "force overwrite if target exist" },
		order = 1)
	private boolean force;
	// <-loop> or <-l> [LOOP_LABEL_NAME]
	// to set the name of the loop-label to the given LOOP_LABEL_NAME
	@Arg("-loop")
	// @GUITextWithReset(reset = "reset you loop label", value = "your loop label")
	@GUINormalLine(firstArt = GUIArt.deleteButton, firstText = "reset the loop-label", secondArt = GUIArt.modifiableText, secondText = "your loop label", order = 2)
	private String loop;
	// (<-init> or <-i>) [INIT_LABEL_NAME] (<jear>/<j> or <day>/<d> or <hour>/<h> or <min>/<m> or <sec>/<s> or <ms> or <t>/<tick>) [TIME]
	@Arg("-init")
	// @GUIOwnWindow(title = "your init labels", manage = "manage your init-labels", reset = "reset all init-labels", deleteAllText = "delete all inits", firstText = "delete",
	// secondText = "label-name", thirdText = "time", forthText = {"jear", "day", "hour", "min", "sec", "ms", "tick" }, firstArt = GUIArt.deleteButton, secondArt = GUIArt.modifiableText,
	// thirdArt = GUIArt.number,
	// forthArt = GUIArt.comboBox)
	@GUIOwnWindow(deleteAllText = "delete all", firstArt = GUIArt.deleteButton, firstText = "delete"/* TODO second, third and forth */, myFirstArt = GUIArt.unmodifiableText,
		myFirstText = "your init-labels", mySecondArt = GUIArt.nothing, mySecondText = "manage", title = "your inits", order = 3)
	private Init[] inits;
	// <-regs> or <-r> [REGISTER_COUNT]
	// to set the number of registers the loop should use
	// if no register count is set the smallest usable register count will be used
	@Arg("-regs")
	// @GUIIntegerChoseWithReset(resetText = "reset register number", value = "register count")
	@GUINormalLine(firstArt = GUIArt.deleteButton, firstText = "reset", secondArt = GUIArt.number, secondText = "register count", order = 4)
	private Integer regs;
	// <-nops> or <-n> [NOP_COUNT]
	// to set the number of nops used in each iteration of the loop
	// if no nop count is set the no nops will be used
	@Arg("-nops")
	// @GUIIntegerChoseWithReset(resetText = "reset NOP number", value = "NOP count")
	@GUINormalLine(firstArt = GUIArt.deleteButton, firstText = "reset", secondArt = GUIArt.number, secondText = "NOP count", order = 5)
	private Integer nops;
	// <-nosafe>
	// to supress te sasve and load of the used registers
	// if set all used registers will have the value 255/0xFF after a loop-call
	@Arg("-nosafe")
	// @GUIBolleanDropBox(falseText = "save and load used register", trueText = "don't save/load used register", value = false)
	@GUINormalLine(firstArt = GUIArt.unmodifiableText, firstText = "safe used registers:", secondArt = GUIArt.comboBoxTrueFalse, secondText = {"safe and load them", "dont save them" }, order = 6)
	private boolean nosafe;
	
	private static class Init {
		
		@TextValue
		private String     name;
		@NumberValue
		private BigInteger ticks;
		@ComboValue
		private int        combo;
		
		@StringArray
		public String[] toStringArray() {
			String c;
			switch (combo) {
			case 0:
				c = "jear";
				break;
			case 1:
				c = "day";
				break;
			case 2:
				c = "hour";
				break;
			case 3:
				c = "min";
				break;
			case 4:
				c = "sec";
				break;
			case 5:
				c = "ms";
				break;
			case 6:
				c = "tick";
				break;
			default:
				throw new AssertionError("illegal combo index!");
			}
			return new String[] {null, name, c, ticks.toString(10) };
		}
		
	}
	
}
