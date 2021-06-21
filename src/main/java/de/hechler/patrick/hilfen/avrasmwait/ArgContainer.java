package de.hechler.patrick.hilfen.avrasmwait;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hechler.patrick.hilfen.autoarggui.enums.GUIArt;
import de.hechler.patrick.hilfen.autoarggui.interfaces.Arguments;
import de.hechler.patrick.hilfen.autoarggui.interfaces.Line;
import de.hechler.patrick.hilfen.autoarggui.objects.AbstractLine;

public class ArgContainer implements Arguments {
	
	// <-target> or <-t> [TARGET_FILE]
	// to set the target file to recive the generated code
	// if no target file is set the generated code will be printed on the default out stream
	// @Arg("-target")
	// @GUINormalLine(firstArt = GUIArt.deleteButton, firstText = "reset", secondArt = GUIArt.fileChoose, secondText = "select", thirdArt = GUIArt.choosenFileModifiable, thirdText = "target file",
	// order = 0)
	private String target = null;
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
	private Init[] init = null;
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
	
	private static final int TARGET     = 0;
	private static final int FORCE      = 1;
	private static final int LOOP       = 2;
	private static final int INIT       = 3;
	private static final int REGS       = 4;
	private static final int NOPS       = 5;
	private static final int NOSAFE     = 6;
	private static final int LINE_COUNT = 7;
	
	private transient Line[] lines;
	
	private static final List <ArgContainer> ALL = new ArrayList <>();
	
	public ArgContainer() {
		lines = new Line[LINE_COUNT];
		lines[TARGET] = new TargetLine();
		lines[FORCE] = new ForceLine();
		lines[LOOP] = new LoopLine();
		lines[INIT] = new InitLine();
		lines[REGS] = new RegsLine();
		lines[NOPS] = new NopsLine();
		lines[NOSAFE] = new NosafeLine();
		ALL.add(this);
		System.out.println(ALL);
	}
	
	@Override
	public Line[] getAllLines() {
		return lines;
	}
	
	@Override
	public Line getLine(int indx) {
		return lines[indx];
	}
	
	@Override
	public int size() {
		return LINE_COUNT;
	}
	
	@Override
	public String[] toArgs() {
		List <String> args = new ArrayList <>();
		for (Line line : lines) {
			String[] zw = line.toArgs();
			List <String> zwl = Arrays.asList(zw);
			args.addAll(zwl);
		}
		return args.toArray(new String[args.size()]);
	}
	
	private static class Init {
		
		// @TextValue
		private String name = null;
		// @NumberValue
		private BigInteger time = null;
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
			return new String[] {"-init", name, c, time.toString(10) };
		}
		
	}
	
	public class TargetLine extends AbstractLine {
		
		public TargetLine() {
			super(new String[][] {{"del target" }, {"choose target", "Target file" }, {"no file" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] {GUIArt.deleteButton, GUIArt.fileChoose, GUIArt.choosenFileModifiable };
		}
		
		@Override
		public Object getValue(int index) {
			return target;
		}
		
		@Override
		public Class <?> getType(int index) {
			return String.class;
		}
		
		@Override
		public void setValue(int index, Object val) {
			target = (String) val;
		}
		
		@Override
		public String[] toArgs() {
			if (target != null) {
				return new String[] {"-target", target };
			} else {
				return new String[0];
			}
		}
		
	}
	
	public class RegsLine extends AbstractLine {
		
		public RegsLine() {
			super(new String[][] {{"del register count" }, {"register count" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] {GUIArt.deleteButton, GUIArt.number };
		}
		
		@Override
		public Object getValue(int index) {
			return regs;
		}
		
		@Override
		public Class <?> getType(int index) {
			return Integer.class;
		}
		
		@Override
		public void setValue(int index, Object val) {
			regs = (Integer) val;
		}
		
		@Override
		public String[] toArgs() {
			if (regs != null) {
				return new String[] {"-regs", regs.toString() };
			} else {
				return new String[0];
			}
		}
		
	}
	
	public class NopsLine extends AbstractLine {
		
		public NopsLine() {
			super(new String[][] {{"del NOP count" }, {"NOP count" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] {GUIArt.deleteButton, GUIArt.number };
		}
		
		@Override
		public Object getValue(int index) {
			return nops;
		}
		
		@Override
		public Class <?> getType(int index) {
			return Integer.class;
		}
		
		@Override
		public void setValue(int index, Object val) {
			nops = (Integer) val;
		}
		
		@Override
		public String[] toArgs() {
			if (nops != null) {
				return new String[] {"-nops", nops.toString() };
			} else {
				return new String[0];
			}
		}
		
	}
	
	public class NosafeLine extends AbstractLine {
		
		public NosafeLine() {
			super(new String[][] {{"register safing:" }, {"safe and load used registers", "no safe and no load of used registers" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] {GUIArt.unmodifiableText, GUIArt.comboBoxFalseTrue};
		}
		
		@Override
		public Object getValue(int index) {
			return nosafe;
		}
		
		@Override
		public Class <?> getType(int index) {
			return Boolean.TYPE;
		}
		
		@Override
		public void setValue(int index, Object val) {
			nosafe = (boolean) (Boolean) val;
		}
		
		@Override
		public String[] toArgs() {
			if (nosafe) {
				return new String[] {"-nosafe" };
			} else {
				return new String[0];
			}
		}
		
	}
	
	public class LoopLine extends AbstractLine {
		
		public LoopLine() {
			super(new String[][] {{"del loop label" }, {"loop label" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] {GUIArt.deleteButton, GUIArt.modifiableText };
		}
		
		@Override
		public Object getValue(int index) {
			return loop;
		}
		
		@Override
		public Class <?> getType(int index) {
			return String.class;
		}
		
		@Override
		public void setValue(int index, Object val) {
			loop = (String) val;
		}
		
		@Override
		public String[] toArgs() {
			if (loop != null) {
				return new String[] {"-loop", loop };
			} else {
				return new String[0];
			}
		}
		
	}
	
	public class InitLine extends AbstractLine {
		
		public InitLine() {
			super(new String[][] {{"del init lines" }, {"manage inits", "init lines", "add new init", "rem all inits" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] {GUIArt.deleteButton, GUIArt.ownWindow };
		}
		
		@Override
		public Object getValue(int index) {
			return null;
		}
		
		@Override
		public Class <?> getType(int index) {
			return null;
		}
		
		@Override
		public void setValue(int index, Object val) {
			throw new UnsupportedOperationException("setValue(int,Object) " + getClass().getName());
		}
		
		@Override
		public void addLine(int index) {
			if (index != 1) {
				throw new IllegalArgumentException("index=" + index + " only 1 is supported! (addLine)");
			}
			init = Arrays.copyOf(init, init.length + 1);
			init[init.length - 1] = new Init();
		}
		
		@Override
		public void initSubLines() {
			if (init == null) {
				init = new Init[0];
			}
			for (int i = 0; i < init.length; i ++ ) {
				if (init[i] == null) {
					init[i] = new Init();
				}
			}
		}
		
		@Override
		public void removeAllLines(int index) {
			if (index != 1) {
				throw new IllegalArgumentException("index=" + index + " only 1 is supported! (addLine)");
			}
			init = new Init[0];
		}
		
		@Override
		public Line[] subLines(int index) {
			if (index != 1) {
				throw new IllegalArgumentException("index=" + index + " only 1 is supported! (addLine)");
			}
			Line[] lines = new Line[init.length];
			for (int i = 0; i < lines.length; i ++ ) {
				final int constI = i;
				lines[i] = new AbstractLine(new String[][] {{"del init label" }, {"init label name" }, {"jear", "day", "hour", "min", "sec", "ms", "tick" }, {"time count" } }) {
					
					@Override
					public GUIArt[] arten() {
						return new GUIArt[] {GUIArt.deleteButton, GUIArt.modifiableText, GUIArt.comboBoxFalseTrue, GUIArt.number };
					}
					
					@Override
					public boolean hasValue(int index) {
						switch (index) {
						case 1:
							return init[constI].name != null;
						case 2:
							return init[constI].combo != -1;
						case 3:
							return init[constI].time != null;
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (addLine)");
						}
					}
					
					@Override
					public void setValue(int index, Object val) {
						switch (index) {
						case 1:
							init[constI].name = (String) val;
							break;
						case 2:
							init[constI].combo = (int) (Integer) val;
							break;
						case 3:
							init[constI].time = (BigInteger) val;
							break;
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (addLine)");
						}
					}
					
					@Override
					public Object getValue(int index) {
//						System.err.println(init);
//						System.err.println(Arrays.deepToString(init));
//						System.err.println(init.length);
//						System.err.println(init[constI]);
						switch (index) {
						case 1:
							return init[constI].name;
						case 2:
							return init[constI].combo;
						case 3:
							return init[constI].time;
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (addLine)");
						}
					}
					
					@Override
					public Class <?> getType(int index) {
						switch (index) {
						case 1:
							return String.class;
						case 2:
							return Integer.TYPE;
						case 3:
							return BigInteger.class;
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (addLine)");
						}
					}
					
				};
			}
			return lines;
		}
		
		@Override
		public String[] toArgs() {
			List <String> args = new ArrayList <>();
			for (Init init : init) {
				String[] sa = init.toStringArray();
				List <String> sal = Arrays.asList(sa);
				args.addAll(sal);
			}
			return args.toArray(new String[args.size()]);
		}
		
	}
	
	public class ForceLine extends AbstractLine {
		
		public ForceLine() {
			super(new String[][] {{"if target exist:" }, {"exit programm", "overwrite it" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] {GUIArt.unmodifiableText, GUIArt.comboBoxFalseTrue };
		}
		
		@Override
		public Object getValue(int index) {
			return force;
		}
		
		@Override
		public Class <?> getType(int index) {
			return Boolean.TYPE;
		}
		
		@Override
		public void setValue(int index, Object val) {
			force = (boolean) (Boolean) val;
		}
		
		@Override
		public String[] toArgs() {
			if (force) {
				return new String[] {"-force" };
			} else {
				return new String[0];
			}
		}
		
	}
	
}
