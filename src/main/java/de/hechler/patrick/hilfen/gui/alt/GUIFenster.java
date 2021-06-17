package de.hechler.patrick.hilfen.gui.alt;

import java.awt.Component;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;

import de.hechler.patrick.fileparser.serial.Deserializer;
import de.hechler.patrick.fileparser.serial.Serializer;
import de.hechler.patrick.hilfen.gui.interfaces.MainMethod;

@Deprecated
@SuppressWarnings("all")
public class GUIFenster {
	
	static {
		System.err.println("[WARN] this class does not work");
	}
	
	private static final Serializer   SERIALIZER   = new Serializer(false, true, false, true, true);
	private static final Deserializer DESERIALIZER = new Deserializer(Collections.emptyMap());
	
	
	private final Object                   obj;
	private final MainMethod               main;
	private final int                      high;
	private final int                      whidh;
	private final int                      empty;
	private final int                      x1;
	private final int                      x2;
	private final int                      x3;
	private final int                      x4;
	private final JFrame                   window         = new JFrame();
	private final JButton                  finishButton   = new JButton("finish");
	private final JButton                  saveArgsButton = new JButton("save");
	private final JButton                  loadArgsButton = new JButton("load");
	private final Field[]                  argFields;
	private final List <Component>         rebuildableComps;
	private final Map <Integer, ?> frames;
	
	
	
	public GUIFenster(Object obj, MainMethod main) {
		this(obj, main, 20, 500, 10);
	}
	
	public GUIFenster(Object obj, MainMethod main, int high, int whidh, int empty) {
		throw new UnsupportedOperationException("GUIFenster");
//		this.obj = obj;
//		this.main = main;
//		this.high = high;
//		this.whidh = whidh;
//		this.empty = empty;
//		this.x1 = this.whidh - (this.empty * 2);
//		this.x2 = (this.whidh - (this.empty * 3)) / 2;
//		this.x3 = (this.whidh - (this.empty * 4)) / 3;
//		this.x4 = (this.whidh - (this.empty * 5)) / 4;
//		this.argFields = getArgFields();
//		this.rebuildableComps = new ArrayList <>(this.argFields.length /* minimum len */);
//		this.frames = new HashMap <>();
	}
	
	public GUIFenster load(String title) {
		throw new UnsupportedOperationException("GUIFenster");
//		window.setTitle(title);
//		int x = empty, y = empty;
//		final JFileChooser argsFC = new JFileChooser();
//		{
//			FileFilter argsFCFilter = new FileFilter() {
//				
//				@Override
//				public String getDescription() {
//					return "[.args] files";
//				}
//				
//				@Override
//				public boolean accept(File f) {
//					if (f.isHidden()) return false;
//					if (f.isDirectory()) return true;
//					else if ( !f.getName().toLowerCase().endsWith(".args")) return false;
//					else return true;
//				}
//				
//			};
//			argsFC.addChoosableFileFilter(argsFCFilter);
//			FileFilter argsFCFilter2 = new FileFilter() {
//				
//				@Override
//				public String getDescription() {
//					return "[.args] files and hidden";
//				}
//				
//				@Override
//				public boolean accept(File f) {
//					if (f.isDirectory()) return true;
//					else if ( !f.getName().toLowerCase().endsWith(".args")) return false;
//					else return true;
//				}
//				
//			};
//			argsFC.addChoosableFileFilter(argsFCFilter2);
//			FileFilter[] allFilters = argsFC.getChoosableFileFilters();
//			argsFC.setFileFilter(argsFCFilter);
//			for (FileFilter check : allFilters) {
//				if ( !argsFCFilter.equals(check) && !argsFCFilter2.equals(check)) {
//					argsFC.removeChoosableFileFilter(check);
//				}
//			}
//		}
//		finishButton.addActionListener(e -> {
//			String[] args = generateArgs();
//			main.main(args);
//			JOptionPane.showMessageDialog(window, "finish with the code generation from", "FINISH", JOptionPane.INFORMATION_MESSAGE);
//			window.setVisible(false);
//			Runtime r = Runtime.getRuntime();
//			args = null;
//			r.runFinalization();
//			r.exit(0);
//		});
//		finishButton.setBounds(x, y, x3, high);
//		window.add(finishButton);
//		x += empty + x3;
//		saveArgsButton.addActionListener(e -> {
//			int returnVal = argsFC.showSaveDialog(window);
//			if (returnVal == JFileChooser.APPROVE_OPTION) {
//				File file = argsFC.getSelectedFile();
//				if (file.isDirectory()) {
//					JOptionPane.showMessageDialog(window, "i can not save to a folder! ('" + file.getPath() + "')", "NO FOLDERS!", JOptionPane.ERROR_MESSAGE);
//					return;
//				}
//				if (Files.exists(file.toPath())) {
//					int chose = JOptionPane.showConfirmDialog(window, "this file exists already ('" + file.getPath() + "'), should I overwrite the file?");
//					if (chose != JOptionPane.OK_OPTION) {
//						return;
//					}
//				}
//				if ( !file.getName().endsWith(".args")) {
//					file = new File(file.getPath() + ".args");
//				}
//				try {
//					SERIALIZER.writeObject(new FileOutputStream(file), obj);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//		});
//		saveArgsButton.setBounds(x, y, x3, high);
//		window.add(saveArgsButton);
//		x += empty + x3;
//		loadArgsButton.addActionListener(e -> {
//			int returnVal = argsFC.showSaveDialog(window);
//			if (returnVal == JFileChooser.APPROVE_OPTION) {
//				File file = argsFC.getSelectedFile();
//				if (file.isDirectory()) {
//					JOptionPane.showMessageDialog(window, "i can not save to a folder! ('" + file.getPath() + "')", "NO FOLDERS!", JOptionPane.ERROR_MESSAGE);
//					return;
//				}
//				if ( !file.getName().endsWith(".args")) {
//					file = new File(file.getPath() + ".args");
//				}
//				if ( !Files.exists(file.toPath())) {
//					JOptionPane.showMessageDialog(window, "this file does not exists ('" + file.getPath() + "')", "FILE NOT FOUND", JOptionPane.ERROR_MESSAGE);
//					return;
//				}
//				try {
//					DESERIALIZER.overwriteObject(new FileInputStream(file), obj);
//					rebuild();
//				} catch (IOException | AssertionError | NoClassDefFoundError | InternalError err) {
//					err.printStackTrace();
//					JOptionPane.showMessageDialog(window, err.getMessage(), err.getClass().getName(), JOptionPane.ERROR_MESSAGE);
//				}
//			}
//		});
//		rebuild();
//		loadArgsButton.setBounds(x, y, x3, high);
//		window.add(loadArgsButton);
//		window.setLayout(null);
//		window.setVisible(true);
//		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		return this;
	}
	
//	private void rebuild() {
//		for (int i = 0; i < rebuildableComps.size(); i ++ ) {
//			window.remove(rebuildableComps.get(i));
//		}
//		rebuildableComps.clear();
//		int x = empty, y = empty + high + empty;
//		for (int index = 0; index < argFields.length; index ++ ) {
//			final int i = index;
//			GUINormalLine normal = argFields[i].getAnnotation(GUINormalLine.class);
//			GUIOwnWindow ownWindow = argFields[i].getAnnotation(GUIOwnWindow.class);
//			int xx;
//			GUIArt[] arten;
//			String[][] text;
//			boolean nothingAllowed = false;
//			if (normal != null) {
//				if (normal.secondArt() != GUIArt.nothing) {
//					if (normal.thirdArt() != GUIArt.nothing) {
//						if (normal.forthArt() != GUIArt.nothing) {
//							xx = x4;
//							arten = new GUIArt[4];
//						} else {
//							xx = x3;
//							arten = new GUIArt[3];
//						}
//					} else {
//						xx = x2;
//						arten = new GUIArt[2];
//					}
//				} else {
//					xx = x1;
//					arten = new GUIArt[1];
//				}
//				text = new String[arten.length][];
//				arten[0] = normal.firstArt();
//				text[0] = normal.firstText();
//				if (arten.length > 1) {
//					arten[1] = normal.secondArt();
//					text[1] = normal.secondText();
//					if (arten.length > 2) {
//						arten[2] = normal.thirdArt();
//						text[2] = normal.thirdText();
//						if (arten.length > 3) {
//							arten[3] = normal.forthArt();
//							text[3] = normal.forthText();
//						}
//					}
//				}
//			} else {
//				nothingAllowed = true;
//				if (ownWindow.myFirstArt() != GUIArt.nothing) {
//					if (ownWindow.mySecondArt() != GUIArt.nothing) {
//						if (ownWindow.myThirdArt() != GUIArt.nothing) {
//							if (ownWindow.myFirstArt() != GUIArt.nothing) {
//								throw new AssertionError("no manage button (nothing)");
//							} else {
//								xx = x4;
//								arten = new GUIArt[4];
//							}
//						} else {
//							if (ownWindow.myForthArt() != GUIArt.nothing) {
//								xx = x4;
//								arten = new GUIArt[4];
//							} else {
//								xx = x3;
//								arten = new GUIArt[3];
//							}
//						}
//					} else {
//						if (ownWindow.myThirdArt() != GUIArt.nothing) {
//							if (ownWindow.myForthArt() != GUIArt.nothing) {
//								xx = x4;
//								arten = new GUIArt[4];
//							} else {
//								xx = x3;
//								arten = new GUIArt[3];
//							}
//						} else {
//							xx = x2;
//							arten = new GUIArt[2];
//						}
//					}
//				} else {
//					if (ownWindow.mySecondArt() != GUIArt.nothing) {
//						if (ownWindow.myThirdArt() != GUIArt.nothing) {
//							if (ownWindow.myForthArt() != GUIArt.nothing) {
//								xx = x4;
//								arten = new GUIArt[4];
//							} else {
//								xx = x3;
//								arten = new GUIArt[3];
//							}
//						} else {
//							xx = x2;
//							arten = new GUIArt[2];
//						}
//					} else {
//						xx = x1;
//						arten = new GUIArt[1];
//					}
//				}
//				text = new String[arten.length][];
//				arten[0] = ownWindow.myFirstArt();
//				text[0] = ownWindow.myFirstText();
//				if (arten.length > 1) {
//					arten[1] = ownWindow.mySecondArt();
//					text[1] = ownWindow.mySecondText();
//					if (arten.length > 2) {
//						arten[2] = ownWindow.myThirdArt();
//						text[2] = ownWindow.myThirdText();
//						if (arten.length > 3) {
//							arten[3] = ownWindow.myForthArt();
//							text[3] = ownWindow.myForthText();
//						}
//					}
//				}
//			}
//			x = empty;
//			List <JButton> fileChooser = new ArrayList <>();
//			List <Component> needFileChooser = new ArrayList <>();
//			for (int _ii = 0; _ii < arten.length; _ii ++ ) {
//				final int ii = _ii;
//				final Component comp;
//				switch (arten[ii]) {
//				case comboBoxTrueFalse:
//				case comboBoxFalseTrue:
//					comp = new JComboBox <String>(text[ii]);
//					((JComboBox <?>) comp).addFocusListener(new FocusAdapter() {
//						
//						@Override
//						public void focusLost(FocusEvent e) {
//							Field field = argFields[i];
//							setValue(field);
//						}
//						
//						private void setValue(Field field) throws InternalError {
//							Class <?> type = field.getType();
//							if (type == String.class) {
//								setField(i, ((JComboBox <?>) comp).getSelectedItem());
//							} else if (type == Boolean.TYPE) {
//								int si = ((JComboBox <?>) comp).getSelectedIndex();
//								if (GUIArt.comboBoxTrueFalse == arten[ii]) {
//									setField(i, si == 0);
//								} else {
//									setField(i, si == 1);
//								}
//							} else if (type == Integer.TYPE) {
//								setField(i, ((JComboBox <?>) comp).getSelectedIndex());
//							} else if (type == Boolean.class) {
//								int si = ((JComboBox <?>) comp).getSelectedIndex();
//								if (GUIArt.comboBoxTrueFalse == arten[ii]) {
//									setField(i, si == 0 ? Boolean.TRUE : si == 1 ? Boolean.FALSE : null);
//								} else {
//									setField(i, si == 1 ? Boolean.TRUE : si == 0 ? Boolean.FALSE : null);
//								}
//							} else {
//								Field[] fs = type.getDeclaredFields();
//								for (Field f : fs) {
//									ComboValue cv = f.getAnnotation(ComboValue.class);
//									if (cv == null) {
//										continue;
//									}
//									setValue(f);
//									break;
//								}
//							}
//						}
//						
//					}); {
//					Object val = getValue(argFields[i]);
//					if (val != null) {
//						if (val instanceof Number) {
//							int iindex = ((Number) val).intValue();
//							((JComboBox <?>) comp).setSelectedIndex(iindex);
//						} else if (val instanceof Boolean) {
//							if (arten[ii] == GUIArt.comboBoxTrueFalse) {
//								if ((boolean) (Boolean) val) {
//									((JComboBox <?>) comp).setSelectedIndex(0);
//								} else {
//									((JComboBox <?>) comp).setSelectedIndex(1);
//								}
//							} else {
//								if ((boolean) (Boolean) val) {
//									((JComboBox <?>) comp).setSelectedIndex(1);
//								} else {
//									((JComboBox <?>) comp).setSelectedIndex(0);
//								}
//							}
//						} else {
//							String name = val.toString();
//							((JComboBox <?>) comp).setSelectedItem(name);
//						}
//					}
//				}
//					break;
//				case number:
//					comp = new JTextField(text[ii][0]);
//					((JTextField) comp).addFocusListener(new FocusListener() {
//						
//						String oldText;
//						
//						@Override
//						public void focusGained(FocusEvent e) {
//							oldText = ((JTextField) comp).getText();
//						}
//						
//						@Override
//						public void focusLost(FocusEvent e) {
//							String newText = ((JTextField) comp).getText();
//							if (Objects.equals(newText, oldText)) {
//								return;
//							}
//							Class <?> type = argFields[i].getType();
//							Object num;
//							String str = newText;
//							if ( !str.replaceFirst("([0-9]+[.,][0-9]*)|([0-9]*[.,][0-9]+)|([0-9]+)", "").isEmpty()) {
//								((JTextField) comp).setText(oldText);
//								return;
//							}
//							str = str.replaceFirst("^([^,]*)[,]([^,]$)", "$1\\.$2");
//							try {
//								if (type == Long.TYPE || type == Long.class) {
//									num = Long.parseLong(str);
//								} else if (type == Integer.TYPE || type == Integer.class) {
//									num = Integer.parseInt(str);
//								} else if (type == Short.TYPE || type == Short.class) {
//									num = Short.parseShort(str);
//								} else if (type == Byte.TYPE || type == Byte.class) {
//									num = Byte.parseByte(str);
//								} else if (type == Double.TYPE || type == Double.class) {
//									num = Double.parseDouble(str);
//								} else if (type == Float.TYPE || type == Float.class) {
//									num = Float.parseFloat(str);
//								} else if (type == BigInteger.class) {
//									num = new BigInteger(str);
//								} else if (type == BigDecimal.class) {
//									num = new BigDecimal(str);
//								} else if (type == String.class) {
//									num = str;
//								} else {
//									throw new AssertionError("unsupported number-type: " + type + " of field: " + argFields[i]);
//								}
//								setField(i, num);
//								((JTextField) comp).setText(num.toString());
//							} catch (Exception ex) {
//								ex.printStackTrace();
//								JOptionPane.showMessageDialog(window, ex.getMessage(), ex.getClass().getName(), JOptionPane.ERROR_MESSAGE);
//								((JTextField) comp).setText(oldText);
//								return;
//							}
//						}
//						
//					}); {
//					Object val = getValue(argFields[i]);
//					if (val != null) {
//						((JTextField) comp).setText(val.toString());
//					}
//				}
//					break;
//				case choosenFileModifiable:
//				case modifiableText: {
//					comp = new JTextField(text[ii][0]);
//					((JTextField) comp).addFocusListener(new FocusListener() {
//						
//						String oldText;
//						
//						@Override
//						public void focusGained(FocusEvent e) {
//							oldText = ((JTextField) comp).getText();
//						}
//						
//						@Override
//						public void focusLost(FocusEvent e) {
//							String newText = ((JTextField) comp).getText();
//							if (Objects.equals(newText, oldText)) {
//								return;
//							}
//							setField(i, newText);
//						}
//						
//					});
//					Object val = getValue(argFields[i]);
//					if (null != val) {
//						((JTextField) comp).setText(val.toString());
//					}
//					if (GUIArt.choosenFileModifiable == arten[ii]) {
//						needFileChooser.add(comp);
//					}
//					break;
//				}
//				case choosenFileunmodifiable:
//				case unmodifiableText:
//					comp = new JTextPane();
//					((JTextPane) comp).setText(text[ii][0]);
//					((JTextPane) comp).setEditable(false);
//					if (GUIArt.choosenFileModifiable == arten[ii]) {
//						needFileChooser.add(comp);
//					}
//					break;
//				case deleteButton:
//					comp = new JButton(text[ii][0]);
//					((JButton) comp).addActionListener(ae -> {
//						setField(i, null);
//						rebuild();
//					});
//					break;
//				case fileChoose:
//					comp = new JButton(text[ii][0]);
//					fileChooser.add((JButton) comp);
//					break;
//				case nothing: {
//					if ( !nothingAllowed) {
//						throw new AssertionError("'nothing' can not be here now! arten: " + Arrays.deepToString(arten) + " field: " + argFields[i]);
//					}
//					nothingAllowed = false;
//					comp = new JButton(text[ii][0]);
//					window.add(comp);
//					final Field ff = argFields[i];
//					if ( !ff.getType().isArray()) {
//						throw new AssertionError("type missmatch: asserted field to be an array, but it is: " + ff.getType().getName() + " field: " + ff);
//					}
//					final SubWindow sw;
//					if ( !frames.containsKey(i)) {
//						sw = new SubWindow(ownWindow.title(), ownWindow.deleteAllText(), ownWindow.addNewText(), argFields[i]);
//						frames.put(i, sw);
//					} else {
//						sw = frames.get(i);
//					}
//					sw.setup();
//					((JButton) comp).addActionListener(ae -> sw.rebuildSubWindow());
//					break;
//				}
//				default:
//					throw new AssertionError("illegal line!");
//				}
//				comp.setBounds(x, y, xx, high);
//				window.add(comp);
//				rebuildableComps.add(comp);
//				x += xx + empty;
//			}
//			if (fileChooser.size() < needFileChooser.size()) {
//				throw new AssertionError("need too much file chooser");
//			}
//			for (int ii = 0; ii < needFileChooser.size(); ii ++ ) {
//				final JFileChooser fc = new JFileChooser();
//				final Component a = needFileChooser.get(ii);
//				final JButton b = fileChooser.get(ii);
//				b.addActionListener(ae -> {
//					int returnVal = fc.showSaveDialog(window);
//					if (returnVal == JFileChooser.APPROVE_OPTION) {
//						File file = fc.getSelectedFile();
//						if (file.isDirectory()) {
//							JOptionPane.showMessageDialog(window, "folders are not allowed ('" + file.getPath() + "')", "NO FOLDERS!", JOptionPane.ERROR_MESSAGE);
//							return;
//						}
//						FocusListener[] fls;
//						if (a instanceof JTextField) {
//							((JTextField) a).setText(null);
//							fls = ((JTextField) a).getFocusListeners();
//						} else {
//							((JTextPane) a).setText(null);
//							fls = ((JTextPane) a).getFocusListeners();
//						}
//						FocusEvent fe;
//						boolean foc = a.hasFocus();
//						if ( !foc) {
//							fe = new FocusEvent(a, -1);
//							for (FocusListener fl : fls) {
//								fl.focusGained(fe);
//							}
//						}
//						if (a instanceof JTextField) {
//							((JTextField) a).setText(file.getPath());
//						} else {
//							((JTextPane) a).setText(file.getPath());
//						}
//						fe = new FocusEvent(a, -1, foc);
//						for (FocusListener fl : fls) {
//							fl.focusLost(fe);
//						}
//						if (foc) {
//							fe = new FocusEvent(a, -1);
//							for (FocusListener fl : fls) {
//								fl.focusGained(fe);
//							}
//						}
//					}
//				});
//			}
//			y += high + empty;
//		}
//		window.setBounds(0, 0, whidh + 10, y + empty + 30);
//		window.setLocationRelativeTo(null);
//		window.repaint();
//	}
//	
//	private void setField(int i, Object val) throws InternalError {
//		Field f = argFields[i];
//		try {
//			f.set(obj, val);
//		} catch (IllegalArgumentException er) {
//			throw new InternalError(er);
//		} catch (IllegalAccessException err) {
//			boolean a = f.isAccessible();
//			if (a) {// if it was already accessible i do not need to try it again
//				throw new InternalError(err);
//			}
//			f.setAccessible(true);
//			try {
//				f.set(obj, val);
//				f.setAccessible(false);
//			} catch (IllegalArgumentException | IllegalAccessException er) {
//				er.printStackTrace();
//				throw new InternalError(err);
//			}
//		}
//	}
//	
//	private void setField(Field f, Object val) throws InternalError {
//		try {
//			f.set(obj, val);
//		} catch (IllegalArgumentException er) {
//			throw new InternalError(er);
//		} catch (IllegalAccessException err) {
//			boolean a = f.isAccessible();
//			if (a) {// if it was already accessible i do not need to try it again
//				throw new InternalError(err);
//			}
//			f.setAccessible(true);
//			try {
//				f.set(obj, val);
//				f.setAccessible(false);
//			} catch (IllegalArgumentException | IllegalAccessException er) {
//				er.printStackTrace();
//				throw new InternalError(err);
//			}
//		}
//	}
//	
//	private void setField(Object obj, Field f, Object val) throws InternalError {
//		try {
//			f.set(obj, val);
//		} catch (IllegalArgumentException er) {
//			throw new InternalError(er);
//		} catch (IllegalAccessException err) {
//			boolean a = f.isAccessible();
//			if (a) {// if it was already accessible i do not need to try it again
//				throw new InternalError(err);
//			}
//			f.setAccessible(true);
//			try {
//				f.set(obj, val);
//				f.setAccessible(false);
//			} catch (IllegalArgumentException | IllegalAccessException er) {
//				er.printStackTrace();
//				throw new InternalError(err);
//			}
//		}
//	}
//	
//	private Field[] getArgFields() throws AssertionError {
//		Field[] fields = obj.getClass().getDeclaredFields();
//		List <Object[]> zw = new ArrayList <>();
//		for (Field f : fields) {
//			Arg arg = f.getAnnotation(Arg.class);
//			if (arg == null) {
//				continue;
//			}
//			GUINormalLine normal = f.getAnnotation(GUINormalLine.class);
//			GUIOwnWindow ownWindow = f.getAnnotation(GUIOwnWindow.class);
//			if (normal == null && ownWindow == null) {
//				throw new AssertionError("Arg annotation, but no GUINormalLine or GUIOwnWindow annotation! field: " + f + " arg: " + arg);
//			} else if (normal != null && ownWindow != null) {
//				throw new AssertionError("can't have both not null: GUINormalLine='" + normal + "' GUIOwnWindow='" + ownWindow + "'");
//			} else if (normal != null) {
//				zw.add(new Object[] {normal.order(), f });
//			} else {
//				zw.add(new Object[] {ownWindow.order(), f });
//			}
//		}
//		zw.sort((a, b) -> {
//			Integer ai = (Integer) a[0], bi = (Integer) b[0];
//			return ai.compareTo(bi);
//		});
//		fields = new Field[zw.size()];
//		for (int i = 0; i < fields.length; i ++ ) {
//			fields[i] = (Field) zw.get(i)[1];
//		}
//		return fields;
//	}
//	
//	private String[] generateArgs() {
//		List <String> args = new ArrayList <>();
//		for (Field f : argFields) {
//			Arg arg = f.getAnnotation(Arg.class);
//			Object val;
//			Class <?> t = f.getType();
//			val = getValue(f);
//			if (val == null) {
//				continue;
//			}
//			String pre = arg.value();
//			if (t.isArray()) {
//				int len = Array.getLength(val);
//				for (int i = 0; i < len; i ++ ) {
//					Object o = Array.get(val, i);
//					if ( !pre.isEmpty()) {
//						args.add(pre);
//					}
//					args.add(o.toString());
//				}
//			} else if (t == String.class) {
//				if ( !pre.isEmpty()) {
//					args.add(pre);
//				}
//				args.add((String) val);
//			} else if (t == Boolean.TYPE) {
//				if ((boolean) (Boolean) val) {
//					args.add(pre);
//				}
//			} else if (t == Boolean.class) {
//				if ((boolean) (Boolean) val) {
//					if ( !pre.isEmpty()) {
//						args.add(pre);
//					}
//					args.add( ((Boolean) val).toString());
//				}
//			} else {
//				if ( !pre.isEmpty()) {
//					args.add(pre);
//				}
//				args.add(val.toString());
//			}
//		}
//		return args.toArray(new String[args.size()]);
//	}
//	
//	private Object getValue(Field f) throws InternalError {
//		Object val;
//		try {
//			val = f.get(obj);
//		} catch (IllegalArgumentException e) {
//			throw new InternalError(e);
//		} catch (IllegalAccessException e) {
//			if (f.isAccessible()) {
//				throw new InternalError(e);
//			}
//			try {
//				f.setAccessible(true);
//				val = f.get(obj);
//				f.setAccessible(false);
//			} catch (Exception e_) {
//				e_.printStackTrace();
//				throw new InternalError(e);
//			}
//		}
//		return val;
//	}
//	
//	@SuppressWarnings("unused")
//	private Object getValue(Field f, Object obj) throws InternalError {
//		Object val;
//		try {
//			val = f.get(obj);
//		} catch (IllegalArgumentException e) {
//			throw new InternalError(e);
//		} catch (IllegalAccessException e) {
//			if (f.isAccessible()) {
//				throw new InternalError(e);
//			}
//			try {
//				f.setAccessible(true);
//				val = f.get(obj);
//				f.setAccessible(false);
//			} catch (Exception e_) {
//				e_.printStackTrace();
//				throw new InternalError(e);
//			}
//		}
//		return val;
//	}
//	
//	private class SubWindow {
//		
//		private final FrameContainer fc;
//		private final Field          f;
//		
//		private SubWindow(String title, String delAllText, String addNewText, Field f) {
//			this.fc = new FrameContainer(new JFrame(title), new JButton(delAllText), new JButton(addNewText));
//			this.f = f;
//		}
//		
//		private void setup() {
//			fc.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//			fc.frame.setLayout(null);
//			fc.frame.add(fc.deleteAll);
//			fc.deleteAll.addActionListener(ae -> {
//				Object val = Array.newInstance(f.getType().getComponentType(), 0);
//				setField(f, val);
//				rebuildSubWindow();
//			});
//			fc.frame.add(fc.addNew);
//			fc.addNew.addActionListener(ae -> {
//				Object val = getValue(f);
//				int len = Array.getLength(val);
//				val = Array.newInstance(f.getType().getComponentType(), len + 1);
//				setField(f, val);
//				rebuildSubWindow();
//			});
//		}
//		
//		private void rebuildSubWindow() {
//			for (Component[] cs : fc.comps) {
//				for (Component c : cs) {
//					fc.frame.remove(c);
//				}
//			}
//			GUIOwnWindow props = f.getAnnotation(GUIOwnWindow.class);
//			final int xx;
//			final GUIArt[] arten;
//			final String[][] text;
//			if (props.secondArt() != GUIArt.nothing) {
//				if (props.thirdArt() != GUIArt.nothing) {
//					if (props.forthArt() != GUIArt.nothing) {
//						xx = x4;
//						arten = new GUIArt[4];
//					} else {
//						xx = x3;
//						arten = new GUIArt[3];
//					}
//				} else {
//					xx = x2;
//					arten = new GUIArt[2];
//				}
//			} else {
//				xx = x1;
//				arten = new GUIArt[1];
//			}
//			text = new String[arten.length][];
//			arten[0] = props.firstArt();
//			text[0] = props.firstText();
//			if (arten.length > 1) {
//				arten[1] = props.secondArt();
//				text[1] = props.secondText();
//				if (arten.length > 2) {
//					arten[2] = props.thirdArt();
//					text[2] = props.thirdText();
//					if (arten.length > 3) {
//						arten[3] = props.forthArt();
//						text[3] = props.forthText();
//					}
//				}
//			}
//			List <Component> needFileChooser = new ArrayList <>();
//			List <JButton> fileChooser = new ArrayList <>();
//			int x = empty, y = empty;
//			final Class <?> compType = f.getType().getComponentType();
//			final Object val;
//			{
//				Object zw = getValue(f);
//				if (zw == null) {
//					zw = Array.newInstance(compType, 0);
//					setField(f, zw);
//				}
//				val = zw;
//			}
//			final int len = Array.getLength(val);
//			for (int _i = 0; _i < len; _i ++ ) {
//				final int i = _i;
//				Component[] zwComps = new Component[arten.length];
//				for (int _ii = 0; _ii < arten.length; _ii ++ ) {
//					final int ii = _ii;
//					final Component comp;
//					switch (arten[ii]) {
//					case comboBoxTrueFalse:
//					case comboBoxFalseTrue: {
//						comp = new JComboBox <String>(text[ii]);
//						((JComboBox <?>) comp).addFocusListener(new FocusAdapter() {
//							
//							@Override
//							public void focusLost(FocusEvent e) {
//								setValue(obj, f);
//							}
//							
//							private void setValue(Object obj, Field field) throws InternalError {
//								Class <?> type = field.getType();
//								if (type == String.class) {
//									setField(obj, f, ((JComboBox <?>) comp).getSelectedItem());
//								} else if (type == Boolean.TYPE) {
//									int si = ((JComboBox <?>) comp).getSelectedIndex();
//									if (GUIArt.comboBoxTrueFalse == arten[ii]) {
//										setField(obj, f, si == 0);
//									} else {
//										setField(obj, f, si == 1);
//									}
//								} else if (type == Integer.TYPE) {
//									setField(obj, f, ((JComboBox <?>) comp).getSelectedIndex());
//								} else if (type == Boolean.class) {
//									int si = ((JComboBox <?>) comp).getSelectedIndex();
//									if (GUIArt.comboBoxTrueFalse == arten[ii]) {
//										setField(obj, f, si == 0 ? Boolean.TRUE : si == 1 ? Boolean.FALSE : null);
//									} else {
//										setField(obj, f, si == 1 ? Boolean.TRUE : si == 0 ? Boolean.FALSE : null);
//									}
//								} else {
//									Field[] fs = type.getDeclaredFields();
//									for (Field f : fs) {
//										ComboValue cv = f.getAnnotation(ComboValue.class);
//										if (cv == null) {
//											continue;
//										}
//										setValue(obj, f);
//										break;
//									}
//								}
//							}
//							
//						});
//						Object sval = Array.get(val, i);
//						if (sval != null) {
//							if (sval instanceof Number) {
//								int iindex = ((Number) sval).intValue();
//								((JComboBox <?>) comp).setSelectedIndex(iindex);
//							} else if (sval instanceof Boolean) {
//								if (arten[ii] == GUIArt.comboBoxTrueFalse) {
//									if ((boolean) (Boolean) sval) {
//										((JComboBox <?>) comp).setSelectedIndex(0);
//									} else {
//										((JComboBox <?>) comp).setSelectedIndex(1);
//									}
//								} else {
//									if ((boolean) (Boolean) sval) {
//										((JComboBox <?>) comp).setSelectedIndex(1);
//									} else {
//										((JComboBox <?>) comp).setSelectedIndex(0);
//									}
//								}
//							} else {
//								String name = sval.toString();
//								((JComboBox <?>) comp).setSelectedItem(name);
//							}
//						}
//						break;
//					}
//					case number: {
//						comp = new JTextField(text[ii][0]);
//						((JTextField) comp).addFocusListener(new FocusListener() {
//							
//							String oldText;
//							
//							@Override
//							public void focusGained(FocusEvent e) {
//								oldText = ((JTextField) comp).getText();
//							}
//							
//							@Override
//							public void focusLost(FocusEvent e) {
//								String newText = ((JTextField) comp).getText();
//								if (Objects.equals(newText, oldText)) {
//									return;
//								}
//								String str = newText;
//								if ( !str.replaceFirst("([0-9]+[.,][0-9]*)|([0-9]*[.,][0-9]+)|([0-9]+)", "").isEmpty()) {
//									((JTextField) comp).setText(oldText);
//									return;
//								}
//								str = str.replaceFirst("^([^,]*)[,]([^,]$)", "$1\\.$2");
//								try {
//									setNumberValue(comp, str);
//								} catch (Exception ex) {
//									ex.printStackTrace();
//									JOptionPane.showMessageDialog(window, ex.getMessage(), ex.getClass().getName(), JOptionPane.ERROR_MESSAGE);
//									((JTextField) comp).setText(oldText);
//									return;
//								}
//							}
//							
//							private void setNumberValue(Component comp, String str) throws InternalError {
//								Object num;
//								Class <?> type = compType;
//								if (type == Long.TYPE || type == Long.class) {
//									num = Long.parseLong(str);
//									Array.set(val, i, num);
//									((JTextField) comp).setText(num.toString());
//								} else if (type == Integer.TYPE || type == Integer.class) {
//									num = Integer.parseInt(str);
//									Array.set(val, i, num);
//									((JTextField) comp).setText(num.toString());
//								} else if (type == Short.TYPE || type == Short.class) {
//									num = Short.parseShort(str);
//									Array.set(val, i, num);
//									((JTextField) comp).setText(num.toString());
//								} else if (type == Byte.TYPE || type == Byte.class) {
//									num = Byte.parseByte(str);
//									Array.set(val, i, num);
//									((JTextField) comp).setText(num.toString());
//								} else if (type == Double.TYPE || type == Double.class) {
//									num = Double.parseDouble(str);
//									Array.set(val, i, num);
//									((JTextField) comp).setText(num.toString());
//								} else if (type == Float.TYPE || type == Float.class) {
//									num = Float.parseFloat(str);
//									Array.set(val, i, num);
//									((JTextField) comp).setText(num.toString());
//								} else if (type == BigInteger.class) {
//									num = new BigInteger(str);
//									Array.set(val, i, num);
//									((JTextField) comp).setText(num.toString());
//								} else if (type == BigDecimal.class) {
//									num = new BigDecimal(str);
//									Array.set(val, i, num);
//									((JTextField) comp).setText(num.toString());
//								} else if (type == String.class) {
//									num = str;
//									Array.set(val, i, num);
//								} else {
//									Field[] fields = type.getDeclaredFields();
//									for (Field ff : fields) {
//										if (ff.getAnnotation(NumberValue.class) == null) {
//											continue;
//										}
//										Class <?> fftype = ff.getType();
//										Object sval = Array.get(val, i);
//										if (sval == null) {
//											try {
//												Constructor <?> construct = compType.getDeclaredConstructor();
//												try {
//													sval = construct.newInstance();
//												} catch (IllegalAccessException e) {
//													if (construct.isAccessible()) {
//														throw new InternalError(e);
//													}
//													try {
//														construct.setAccessible(true);
//														sval = construct.newInstance();
//														construct.setAccessible(false);
//													} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
//														e1.printStackTrace();
//														throw new InternalError(e);
//													}
//												} catch (InstantiationException e) {
//													throw new InternalError(e);
//												} catch (IllegalArgumentException e) {
//													throw new InternalError(e);
//												} catch (InvocationTargetException e) {
//													throw new InternalError(e);
//												} catch (SecurityException e) {
//													throw new InternalError(e);
//												}
//											} catch (NoSuchMethodException e) {
//												throw new InternalError(e);
//											}
//										}
//										if (fftype == Long.TYPE || fftype == Long.class) {
//											num = Long.parseLong(str);
//											setField(ff, num);
//											((JTextField) comp).setText(num.toString());
//										} else if (fftype == Integer.TYPE || fftype == Integer.class) {
//											num = Integer.parseInt(str);
//											setField(ff, num);
//											((JTextField) comp).setText(num.toString());
//										} else if (fftype == Short.TYPE || fftype == Short.class) {
//											num = Short.parseShort(str);
//											setField(ff, num);
//											((JTextField) comp).setText(num.toString());
//										} else if (fftype == Byte.TYPE || fftype == Byte.class) {
//											num = Byte.parseByte(str);
//											setField(ff, num);
//											((JTextField) comp).setText(num.toString());
//										} else if (fftype == Double.TYPE || fftype == Double.class) {
//											num = Double.parseDouble(str);
//											setField(ff, num);
//											((JTextField) comp).setText(num.toString());
//										} else if (fftype == Float.TYPE || fftype == Float.class) {
//											num = Float.parseFloat(str);
//											setField(ff, num);
//											((JTextField) comp).setText(num.toString());
//										} else if (fftype == BigInteger.class) {
//											num = new BigInteger(str);
//											setField(ff, num);
//											((JTextField) comp).setText(num.toString());
//										} else if (fftype == BigDecimal.class) {
//											num = new BigDecimal(str);
//											setField(ff, num);
//											((JTextField) comp).setText(num.toString());
//										} else if (fftype == String.class) {
//											num = str;
//											setField(ff, num);
//										} else {
//											throw new AssertionError("type missmatch: expected a number-impl or String, but got: " + fftype);
//										}
//									}
//								}
//							}
//							
//						});
//						Object sval = Array.get(val, i);
//						if (sval != null) {
//							if (sval instanceof Number || sval.getClass() == String.class) {
//								((JTextField) comp).setText(sval.toString());
//							} else {
//								for (Field ff : compType.getDeclaredFields()) {
//									if (ff.getAnnotation(NumberValue.class) == null) {
//										continue;
//									}
//									((JTextField) comp).setText(sval.toString());
//									break;
//								}
//							}
//						}
//						break;
//					}
//					case choosenFileModifiable:
//					case modifiableText: {
//						comp = new JTextField(text[ii][0]);
//						((JTextField) comp).addFocusListener(new FocusListener() {
//							
//							String oldText;
//							
//							@Override
//							public void focusGained(FocusEvent e) {
//								oldText = ((JTextField) comp).getText();
//							}
//							
//							@Override
//							public void focusLost(FocusEvent e) {
//								String newText = ((JTextField) comp).getText();
//								if (Objects.equals(newText, oldText)) {
//									return;
//								}
//								if (f.getType() == String.class) {
//									setField(f, newText);
//								} else {
//									Field[] fields = f.getType().getDeclaredFields();
//									for (Field ff : fields) {
//										if (ff.getAnnotation(TextValue.class) == null) {
//											continue;
//										}
//										setField(ff, val);
//									}
//								}
//							}
//							
//						});
//						Object sval = Array.get(val, i);
//						if (null != sval) {
//							((JTextField) comp).setText(sval.toString());
//						}
//						if (GUIArt.choosenFileModifiable == arten[ii]) {
//							needFileChooser.add(comp);
//						}
//						break;
//					}
//					case choosenFileunmodifiable:
//					case unmodifiableText:
//						comp = new JTextPane();
//						((JTextPane) comp).setText(text[ii][0]);
//						((JTextPane) comp).setEditable(false);
//						if (GUIArt.choosenFileModifiable == arten[ii]) {
//							needFileChooser.add(comp);
//						}
//						break;
//					case deleteButton:
//						comp = new JButton(text[ii][0]);
//						((JButton) comp).addActionListener(ae -> {
//							Object newVal = Array.newInstance(compType, len - 1);
//							System.arraycopy(val, 0, newVal, 0, i);
//							System.arraycopy(val, i + 1, newVal, i, len - i - 1);
//							setField(f, newVal);
//							rebuildSubWindow();
//						});
//						break;
//					case fileChoose:
//						comp = new JButton(text[ii][0]);
//						fileChooser.add((JButton) comp);
//						break;
//					case nothing:
//						throw new AssertionError("'nothing' can not be here! I am already a subwindow arten: " + Arrays.deepToString(arten) + " field: " + argFields[i]);
//					default:
//						throw new AssertionError("illegal line!");
//					}
//					comp.setBounds(x, y, xx, high);
//					fc.frame.add(comp);
//					zwComps[ii] = comp;
//					x += xx + empty;
//				}
//				fc.comps.add(zwComps);
//				x = empty;
//				y += high + empty;
//			}
//			fc.addNew.setBounds(x, y, x2, high);
//			x += x2 + empty;
//			fc.deleteAll.setBounds(x, y, x2, high);
//			x = empty;
//			y += high + empty;
//			fc.frame.setBounds(0, 0, whidh + 10, y + empty + 30);
//			fc.frame.setLocationRelativeTo(null);
//			fc.frame.setVisible(true);
//		}
//		
//		private class FrameContainer {
//			
//			private final JFrame             frame;
//			private final List <Component[]> comps;
//			private final JButton            deleteAll;
//			private final JButton            addNew;
//			
//			private FrameContainer(JFrame frame, JButton delAll, JButton addNew) {
//				this.frame = frame;
//				this.comps = new ArrayList <>();
//				this.deleteAll = delAll;
//				this.addNew = addNew;
//			}
//			
//		}
//		
//	}
	
}
