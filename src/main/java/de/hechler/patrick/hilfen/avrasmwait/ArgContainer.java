package de.hechler.patrick.hilfen.avrasmwait;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.hechler.patrick.hilfen.autoarggui.enums.GUIArt;
import de.hechler.patrick.hilfen.autoarggui.interfaces.ArgList;
import de.hechler.patrick.hilfen.autoarggui.interfaces.Arguments;
import de.hechler.patrick.hilfen.autoarggui.interfaces.Line;
import de.hechler.patrick.hilfen.autoarggui.objects.AbstractLine;
import de.hechler.patrick.hilfen.avrasmwait.interfaces.CodeGenerator;

@SuppressWarnings("javadoc")
public class ArgContainer implements Arguments, Serializable {
	
	private static final long serialVersionUID = -945832172930488088L;
	
	private String              target    = null;
	private boolean             force     = false;
	private String              loop      = null;
	private Init[]              init      = null;
	private Integer             regs      = null;
	private Integer             nops      = null;
	private boolean             nosafe    = false;
	private boolean             exact     = false;
	private String              firstReg  = null;
	private Map<String, String> modifyReg = null;
	
	private static final int LINE_TARGET     = 0;
	private static final int LINE_FORCE      = 1;
	private static final int LINE_LOOP       = 2;
	private static final int LINE_INIT       = 3;
	private static final int LINE_EXACT      = 4;
	private static final int LINE_NOPS       = 5;
	private static final int LINE_REGS       = 6;
	private static final int LINE_NOSAFE     = 7;
	private static final int LINE_REG_MODIFY = 8;
	private static final int LINE_COUNT      = 9;
	
	private transient Line[] lines;
	
	public ArgContainer() {
		this.lines                  = new Line[LINE_COUNT];
		this.lines[LINE_TARGET]     = new TargetLine();
		this.lines[LINE_FORCE]      = new ForceLine();
		this.lines[LINE_LOOP]       = new LoopLine();
		this.lines[LINE_INIT]       = new InitLine();
		this.lines[LINE_EXACT]      = new BoolLine("exact wait:", "return a few ticks to early (at most `reg-cnt + 1´)", "do NOP padding for the last few ticks",
				LINE_EXACT);
		this.lines[LINE_NOSAFE]     = new BoolLine("register saving:", "no restore of registers (if only registers below r16 are used r31 contains garbage)",
				"save and load used registers", LINE_NOSAFE);
		this.lines[LINE_REGS]       = new RegsLine();
		this.lines[LINE_NOPS]       = new NopsLine();
		this.lines[LINE_REG_MODIFY] = new RegModify();
	}
	
	@Override
	public Line[] getAllLines() {
		return this.lines;
	}
	
	@Override
	public Line getLine(int indx) {
		return this.lines[indx];
	}
	
	@Override
	public int size() {
		return LINE_COUNT;
	}
	
	@Override
	public ArgList toArgList() {
		ArgList args = new ArgList();
		for (Line line : this.lines) {
			line.addArgs(args);
		}
		return args;
	}
	
	private static class Init implements Serializable {
		
		private static final long serialVersionUID = -2716620008993021012L;
		
		private String     name  = null;
		private BigInteger time  = null;
		private String     combo = "ms";
		
		private String[] toStringArray() {
			return new String[] { "-init", this.name, this.combo, this.time.toString(10) };
		}
		
	}
	
	public class TargetLine extends AbstractLine {
		
		public TargetLine() {
			super(new String[][] { { "clear target" }, { "choose target", "Target file" }, { "no file" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] { GUIArt.deleteButton, GUIArt.fileChoose, GUIArt.choosenFileModifiable };
		}
		
		@Override
		public Object getValue(@SuppressWarnings("unused") int index) {
			return ArgContainer.this.target;
		}
		
		@Override
		public Class<?> getType(@SuppressWarnings("unused") int index) {
			return String.class;
		}
		
		@Override
		public void setValue(@SuppressWarnings("unused") int index, Object val) {
			ArgContainer.this.target = (String) val;
		}
		
		@Override
		public void addArgs(ArgList list) {
			if (ArgContainer.this.target != null) {
				list.add("-target", ArgContainer.this.target);
			}
		}
		
	}
	
	public class RegsLine extends AbstractLine {
		
		public RegsLine() {
			super(new String[][] { { "del register count" }, { "register count" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] { GUIArt.deleteButton, GUIArt.number };
		}
		
		@Override
		public Object getValue(@SuppressWarnings("unused") int index) {
			return ArgContainer.this.regs;
		}
		
		@Override
		public Class<?> getType(@SuppressWarnings("unused") int index) {
			return Integer.class;
		}
		
		@Override
		public void setValue(@SuppressWarnings("unused") int index, Object val) {
			ArgContainer.this.regs = (Integer) val;
		}
		
		@Override
		public void addArgs(ArgList list) {
			if (ArgContainer.this.regs != null) {
				list.add("-regs", ArgContainer.this.regs.toString());
			}
		}
		
	}
	
	public class NopsLine extends AbstractLine {
		
		public NopsLine() {
			super(new String[][] { { "del NOP count" }, { "NOP count" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] { GUIArt.deleteButton, GUIArt.number };
		}
		
		@Override
		public Object getValue(@SuppressWarnings("unused") int index) {
			return ArgContainer.this.nops;
		}
		
		@Override
		public Class<?> getType(@SuppressWarnings("unused") int index) {
			return Integer.class;
		}
		
		@Override
		public void setValue(@SuppressWarnings("unused") int index, Object val) {
			ArgContainer.this.nops = (Integer) val;
		}
		
		@Override
		public void addArgs(ArgList list) {
			if (ArgContainer.this.nops != null) {
				list.add("-nops", ArgContainer.this.nops.toString());
			}
		}
		
	}
	
	public class BoolLine extends AbstractLine {
		
		private final int line;
		
		public BoolLine(String label, String yes, String no, int line) {
			super(new String[][] { { label }, { no, yes } });
			this.line = line;
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] { GUIArt.unmodifiableText, GUIArt.comboBox };
		}
		
		@Override
		public Object getValue(@SuppressWarnings("unused") int index) {
			switch (this.line) {
			case LINE_EXACT:
				return Boolean.valueOf(ArgContainer.this.exact);
			case LINE_NOSAFE:
				return Boolean.valueOf(ArgContainer.this.nosafe);
			default:
				throw new AssertionError("illegal line: " + this.line);
			}
		}
		
		@Override
		public Class<?> getType(@SuppressWarnings("unused") int index) {
			return Boolean.TYPE;
		}
		
		@Override
		public void setValue(@SuppressWarnings("unused") int index, Object val) {
			switch (this.line) {
			case LINE_EXACT:
				ArgContainer.this.exact = ((Boolean) val).booleanValue();
				break;
			case LINE_NOSAFE:
				ArgContainer.this.nosafe = ((Boolean) val).booleanValue();
				break;
			default:
				throw new AssertionError("illegal line: " + this.line);
			}
		}
		
		@Override
		public void addArgs(ArgList list) {
			switch (this.line) {
			case LINE_EXACT:
				if (ArgContainer.this.nosafe) {
					list.add("-exact");
				}
				break;
			case LINE_NOSAFE:
				if (ArgContainer.this.nosafe) {
					list.add("-nosafe");
				}
				break;
			default:
				throw new AssertionError("illegal line: " + this.line);
			}
		}
		
	}
	
	public class LoopLine extends AbstractLine {
		
		public LoopLine() {
			super(new String[][] { { "del loop label" }, { "loop label" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] { GUIArt.deleteButton, GUIArt.modifiableText };
		}
		
		@Override
		public Object getValue(@SuppressWarnings("unused") int index) {
			return ArgContainer.this.loop;
		}
		
		@Override
		public Class<?> getType(@SuppressWarnings("unused") int index) {
			return String.class;
		}
		
		@Override
		public void setValue(@SuppressWarnings("unused") int index, Object val) {
			ArgContainer.this.loop = (String) val;
		}
		
		@Override
		public void addArgs(ArgList list) {
			if (ArgContainer.this.loop != null) {
				list.add("-loop", ArgContainer.this.loop);
			}
		}
		
	}
	
	public class InitLine extends AbstractLine {
		
		public InitLine() {
			super(new String[][] { { "reset init lines" }, { "manage inits", "init lines", "add new init", "rem all inits" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] { GUIArt.deleteButton, GUIArt.ownWindow };
		}
		
		@Override
		public Object getValue(@SuppressWarnings("unused") int index) {
			return null;
		}
		
		@Override
		public Class<?> getType(@SuppressWarnings("unused") int index) {
			return null;
		}
		
		@Override
		public void setValue(@SuppressWarnings("unused") int index, Object val) {
			if (val != null) {
				throw new UnsupportedOperationException("setValue(int,Object) " + getClass().getName());
			}
			ArgContainer.this.init = null;
		}
		
		@Override
		public void addLine(int index) {
			if (index != 1) {
				throw new IllegalArgumentException("index=" + index + " only 1 is supported! (addLine)");
			}
			ArgContainer.this.init                                    = Arrays.copyOf(ArgContainer.this.init, ArgContainer.this.init.length + 1);
			ArgContainer.this.init[ArgContainer.this.init.length - 1] = new Init();
		}
		
		@Override
		public void initSubLines() {
			if (ArgContainer.this.init == null) {
				ArgContainer.this.init = new Init[0];
			}
			for (int i = 0; i < ArgContainer.this.init.length; i++) {
				if (ArgContainer.this.init[i] == null) {
					ArgContainer.this.init[i] = new Init();
				}
			}
		}
		
		@Override
		public void removeAllLines(int index) {
			if (index != 1) {
				throw new IllegalArgumentException("index=" + index + " only 1 is supported! (addLine)");
			}
			ArgContainer.this.init = null;
		}
		
		@Override
		public Line[] subLines(int index) {
			if (index != 1) {
				throw new IllegalArgumentException("index=" + index + " only 1 is supported! (addLine)");
			}
			Line[] subLines = new Line[ArgContainer.this.init.length];
			for (int i = 0; i < subLines.length; i++) {
				final int constI = i;
				subLines[i] = new AbstractLine(
						new String[][] { { "del init label" }, { "init label name" }, { "year", "day", "hour", "min", "sec", "ms", "tick" }, { "time count" } }) {
					
					@Override
					public GUIArt[] arten() {
						return new GUIArt[] { GUIArt.deleteButton, GUIArt.modifiableText, GUIArt.comboBox, GUIArt.number };
					}
					
					@Override
					public boolean hasValue(int index) {
						switch (index) {
						case 1:
							return ArgContainer.this.init[constI].name != null;
						case 2:
							return ArgContainer.this.init[constI].combo != null;
						case 3:
							return ArgContainer.this.init[constI].time != null;
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (addLine)");
						}
					}
					
					@Override
					public void setValue(int index, Object val) {
						switch (index) {
						case 1:
							ArgContainer.this.init[constI].name = (String) val;
							break;
						case 2:
							ArgContainer.this.init[constI].combo = (String) val;
							break;
						case 3:
							ArgContainer.this.init[constI].time = (BigInteger) val;
							break;
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (addLine)");
						}
					}
					
					@Override
					public Object getValue(int index) {
						switch (index) {
						case 1:
							return ArgContainer.this.init[constI].name;
						case 2:
							return ArgContainer.this.init[constI].combo;
						case 3:
							return ArgContainer.this.init[constI].time;
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (getValue)");
						}
					}
					
					@Override
					public Class<?> getType(int index) {
						switch (index) {
						case 1:
							return String.class;
						case 2:
							return String.class;
						case 3:
							return BigInteger.class;
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (addLine)");
						}
					}
					
				};
			}
			return subLines;
		}
		
		@Override
		public void addArgs(ArgList list) {
			for (Init initVal : ArgContainer.this.init) {
				String[] sa = initVal.toStringArray();
				list.add(sa);
			}
		}
		
	}
	
	public class ForceLine extends AbstractLine {
		
		public ForceLine() {
			super(new String[][] { { "if target exist:" }, { "exit programm", "overwrite it" } });
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] { GUIArt.unmodifiableText, GUIArt.comboBox };
		}
		
		@Override
		public Object getValue(@SuppressWarnings("unused") int index) {
			return Boolean.valueOf(ArgContainer.this.force);
		}
		
		@Override
		public Class<?> getType(@SuppressWarnings("unused") int index) {
			return Boolean.TYPE;
		}
		
		@Override
		public void setValue(@SuppressWarnings("unused") int index, Object val) {
			ArgContainer.this.force = ((Boolean) val).booleanValue();
		}
		
		@Override
		public void addArgs(ArgList list) {
			if (ArgContainer.this.force) {
				list.add("-force");
			}
		}
		
	}
	
	
	private static String[] regModifyComboBoxVals() {
		String[] res = new String[CodeGenerator.MAX_REG_CNT];
		for (int i = 0; i < res.length; i++) {
			res[i] = CodeGenerator.register(i);
		}
		return res;
	}
	
	public class RegModify extends AbstractLine {
		
		public RegModify() {
			super(new String[][] { //
					{ "first register:" }, //
					regModifyComboBoxVals(), //
					{ "reset register modifications" }, //
					{ "manage register modifications", "init lines", "modify another register", "rem all modifications" } //
			});
		}
		
		@Override
		public GUIArt[] arten() {
			return new GUIArt[] { GUIArt.unmodifiableText, GUIArt.comboBox, GUIArt.deleteButton, GUIArt.ownWindow };
		}
		
		@Override
		public Object getValue(int index) {
			if (index == 1) {
				return ArgContainer.this.firstReg;
			}
			return null;
		}
		
		@Override
		public Class<?> getType(int index) {
			if (index == 1) {
				return String.class;
			}
			return null;
		}
		
		@Override
		public void setValue(int index, Object val) {
			switch (index) {
			case 1:
				String sval = (String) val;
				if (sval != null) {
					if (ArgContainer.this.modifyReg == null) {
						if (!sval.startsWith("R") || !sval.matches("R(0|[12][0-9]?|3[01]?|[4-9])")) {
							return;
						}
					} else if (!ArgContainer.this.modifyReg.containsValue(sval)
							&& (!sval.startsWith("R") || !sval.matches("R(0|[12][0-9]?|3[01]?|[4-9])") || ArgContainer.this.modifyReg.containsKey(sval))) {
								return;
							}
				}
				ArgContainer.this.firstReg = sval;
				break;
			case 2:
				if (val != null) {
					throw new IllegalArgumentException("only delete allowed here");
				}
				ArgContainer.this.modifyReg = null;
				break;
			default:
				throw new IllegalArgumentException("illegal index: " + index);
			}
		}
		
		@Override
		public void addLine(int index) {
			if (index != 3) {
				throw new IllegalArgumentException("index=" + index + " only 3 is supported! (addLine)");
			}
			if (ArgContainer.this.modifyReg == null) {
				ArgContainer.this.modifyReg = new LinkedHashMap<>();
			}
			if (ArgContainer.this.modifyReg.size() >= CodeGenerator.MAX_REG_CNT) {
				return;
			}
			for (int r = 0;; r++) {
				String name = CodeGenerator.register(r);
				if (ArgContainer.this.modifyReg.putIfAbsent(name, name) == null) {
					break;
				}
			}
		}
		
		@Override
		public void removeAllLines(int index) {
			if (index != 3) {
				throw new IllegalArgumentException("index=" + index + " only 3 is supported! (addLine)");
			}
			ArgContainer.this.modifyReg = null;
		}
		
		@Override
		public void initSubLines() {
			if (ArgContainer.this.modifyReg == null) {
				addLine(3);
			}
		}
		
		@Override
		public Line[] subLines(int index) {
			if (index != 3) {
				throw new IllegalArgumentException("index=" + index + " only 3 is supported! (addLine)");
			}
			initSubLines();
			Line[]                          subLines = new Line[ArgContainer.this.modifyReg.size()];
			Iterator<Entry<String, String>> iter     = ArgContainer.this.modifyReg.entrySet().iterator();
			for (int i = 0; i < subLines.length; i++) {
				Entry<String, String> e    = iter.next();
				final String          key0 = e.getKey();
				subLines[i] = new AbstractLine(
						new String[][] { { "del reg rename" }, { key0 }, { "year", "day", "hour", "min", "sec", "ms", "tick" }, { "time count" } }) {
					
					String key = key0;
					
					@Override
					public GUIArt[] arten() {
						return new GUIArt[] { GUIArt.deleteButton, GUIArt.comboBox, GUIArt.modifiableText };
					}
					
					@Override
					public void setValue(int index, Object val) {
						switch (index) {
						case 0:
							if (val != null) {
								throw new IllegalArgumentException("only delete allowed here");
							}
							ArgContainer.this.modifyReg.remove(this.key);
							break;
						case 1:
							String sval = (String) val;
							if (ArgContainer.this.modifyReg.containsKey(sval)) {
								break;
							}
							String value = ArgContainer.this.modifyReg.remove(this.key);
							ArgContainer.this.modifyReg.put(sval, value);
							break;
						case 2:
							ArgContainer.this.modifyReg.put(this.key, (String) val);
							break;
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (addLine)");
						}
					}
					
					@Override
					public Object getValue(int index) {
						switch (index) {
						case 1:
							return this.key;
						case 2:
							return ArgContainer.this.modifyReg.get(this.key);
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (getValue)");
						}
					}
					
					@Override
					public Class<?> getType(int index) {
						switch (index) {
						case 1:
							return String.class;
						case 2:
							return String.class;
						default:
							throw new IllegalArgumentException("index=" + index + " only 1, 2 and 3 are supported! (addLine)");
						}
					}
					
				};
			}
			return subLines;
		}
		
		@Override
		public void addArgs(ArgList list) {
			if (ArgContainer.this.firstReg != null) {
				list.add("-first");
				list.add(ArgContainer.this.firstReg);
			}
			if (ArgContainer.this.modifyReg != null) {
				list.add("-set-names");
				list.add("");
				for (Entry<String, String> renameReg : ArgContainer.this.modifyReg.entrySet()) {
					list.add(renameReg.getKey());
					list.add(renameReg.getValue());
				}
				list.add("");
			}
		}
		
	}
	
}
