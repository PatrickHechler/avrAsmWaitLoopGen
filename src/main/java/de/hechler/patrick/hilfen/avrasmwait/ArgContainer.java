package de.hechler.patrick.hilfen.avrasmwait;

import java.math.BigInteger;

import de.hechler.patrick.hilfen.gui.alt.annotations.others.StringArray;
import de.hechler.patrick.hilfen.gui.alt.annotations.values.ComboValue;
import de.hechler.patrick.hilfen.gui.alt.annotations.values.NumberValue;
import de.hechler.patrick.hilfen.gui.alt.annotations.values.TextValue;

public class ArgContainer {
	
	// <-target> or <-t> [TARGET_FILE]
	// to set the target file to recive the generated code
	// if no target file is set the generated code will be printed on the default out stream
	// @Arg("-target")
	// @GUINormalLine(firstArt = GUIArt.deleteButton, firstText = "reset", secondArt = GUIArt.fileChoose, secondText = "select", thirdArt = GUIArt.choosenFileModifiable, thirdText = "target file",
	// order = 0)
	private String targetFile = null;
	// <-force>
	// to force overwrite if the target file exits already
	// if no target file exits this will be ignored
	// @Arg("-force")
	// @GUINormalLine(firstArt = GUIArt.unmodifiableText, firstText = "force overwrite:", secondArt = GUIArt.comboBoxFalseTrue, secondText = {"exit if target exist", "force overwrite if target exist"
	// },
	// order = 1)
	private boolean force = false;
	// <-loop> or <-l> [LOOP_LABEL_NAME]
	// to set the name of the loop-label to the given LOOP_LABEL_NAME
	// @Arg("-loop")
	// @GUINormalLine(firstArt = GUIArt.deleteButton, firstText = "reset the loop-label", secondArt = GUIArt.modifiableText, secondText = "your loop label", order = 2)
	private String loop = null;
	// (<-init> or <-i>) [INIT_LABEL_NAME] (<jear>/<j> or <day>/<d> or <hour>/<h> or <min>/<m> or <sec>/<s> or <ms> or <t>/<tick>) [TIME]
	// @Arg("-init")
	// @GUIOwnWindow(deleteAllText = "delete all", addNewText = "add new init label", firstArt = GUIArt.deleteButton, firstText = "delete", secondArt = GUIArt.modifiableText, secondText =
	// "init-label-name",
	// thirdArt = GUIArt.comboBoxTrueFalse, thirdText = {"jear (365 d)", "day (24 h)", "hour (60 m)", "min (60 s)", "sec (1.000 ms)", "ms (16.000 t)", "tick (1 t)" }, forthArt = GUIArt.number,
	// forthText = "time count", myFirstArt = GUIArt.unmodifiableText, myFirstText = "your init-labels", mySecondArt = GUIArt.nothing, mySecondText = "manage init labels", title = "your inits", order
	// = 3)
	private Init[] inits = null;
	// <-regs> or <-r> [REGISTER_COUNT]
	// to set the number of registers the loop should use
	// if no register count is set the smallest usable register count will be used
	// @Arg("-regs")
	// @GUINormalLine(firstArt = GUIArt.deleteButton, firstText = "reset", secondArt = GUIArt.number, secondText = "register count", order = 4)
	private Integer regs = null;
	// <-nops> or <-n> [NOP_COUNT]
	// to set the number of nops used in each iteration of the loop
	// if no nop count is set the no nops will be used
	// @Arg("-nops")
	// @GUINormalLine(firstArt = GUIArt.deleteButton, firstText = "reset", secondArt = GUIArt.number, secondText = "NOP count", order = 5)
	private Integer nops = null;
	// <-nosafe>
	// to supress te sasve and load of the used registers
	// if set all used registers will have the value 255/0xFF after a loop-call
	// @Arg("-nosafe")
	// @GUINormalLine(firstArt = GUIArt.unmodifiableText, firstText = "safe used registers:", secondArt = GUIArt.comboBoxFalseTrue, secondText = {"safe and load them", "dont save them" }, order = 6)
	private boolean nosafe = false;
	
	private static class Init {
		
		// @TextValue
		private String name = null;
		// @NumberValue
		private BigInteger ticks = null;
		// @ComboValue
		private int combo = -1;
		
		// @StringArray
		private String[] toStringArray() {
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
