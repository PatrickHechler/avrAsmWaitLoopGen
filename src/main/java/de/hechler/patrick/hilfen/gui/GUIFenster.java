package de.hechler.patrick.hilfen.gui;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;

import de.hechler.patrick.fileparser.Arg;
import de.hechler.patrick.fileparser.serial.Deserializer;
import de.hechler.patrick.fileparser.serial.Serializer;
import de.hechler.patrick.hilfen.gui.annotations.lines.GUINormalLine;
import de.hechler.patrick.hilfen.gui.annotations.lines.GUIOwnWindow;
import de.hechler.patrick.hilfen.gui.enums.GUIArt;

public class GUIFenster {
	
	private static final Serializer   SERIALIZER   = new Serializer(false, true, false, true, true);
	private static final Deserializer DESERIALIZER = new Deserializer(Collections.emptyMap());
	
	
	private final Object           obj;
	private final String           prefix;
	private final String           suffix;
	private final MainMethod       main;
	private final int              deepth;
	private final int              whidh;
	private final int              empty;
	private final int              x1;
	private final int              x2;
	private final int              x3;
	private final int              x4;
	private final JFrame           window         = new JFrame();
	private final JButton          finishButton   = new JButton("finish");
	private final JButton          saveArgsButton = new JButton("save");
	private final JButton          loadArgsButton = new JButton("load");
	private final Field[]          argFields;
	private final List <Component> rebuildableComps;
	
	
	
	public GUIFenster(Object obj, MainMethod main) {
		this(obj, "", "", main, 20, 500, 10);
	}
	
	public GUIFenster(Object obj, String prefix, String suffix, MainMethod main, int deepth, int whidh, int empty) {
		this.obj = obj;
		this.prefix = prefix == null ? "" : prefix;
		this.suffix = suffix == null ? "" : suffix;
		this.main = main;
		this.deepth = deepth;
		this.whidh = whidh;
		this.empty = empty;
		this.x1 = whidh - (empty * 2);
		this.x2 = (whidh - (empty * 3)) / 2;
		this.x3 = (whidh - (empty * 4)) / 3;
		this.x4 = (whidh - (empty * 5)) / 4;
		this.argFields = getArgFields();
		this.rebuildableComps = new ArrayList <>(argFields.length /* minimum len */);
	}
	
	public GUIFenster load(String title) {
		window.setTitle(title);
		int x = empty, y = empty;
		final JFileChooser argsFC = new JFileChooser();
		{
			FileFilter argsFCFilter = new FileFilter() {
				
				@Override
				public String getDescription() {
					return "[.args] files";
				}
				
				@Override
				public boolean accept(File f) {
					if (f.isHidden()) return false;
					if (f.isDirectory()) return true;
					else if ( !f.getName().toLowerCase().endsWith(".args")) return false;
					else return true;
				}
				
			};
			argsFC.addChoosableFileFilter(argsFCFilter);
			FileFilter argsFCFilter2 = new FileFilter() {
				
				@Override
				public String getDescription() {
					return "[.args] files and hidden";
				}
				
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) return true;
					else if ( !f.getName().toLowerCase().endsWith(".args")) return false;
					else return true;
				}
				
			};
			argsFC.addChoosableFileFilter(argsFCFilter2);
			FileFilter[] allFilters = argsFC.getChoosableFileFilters();
			argsFC.setFileFilter(argsFCFilter);
			for (FileFilter check : allFilters) {
				if ( !argsFCFilter.equals(check) && !argsFCFilter2.equals(check)) {
					argsFC.removeChoosableFileFilter(check);
				}
			}
		}
		finishButton.addActionListener(e -> {
			String[] args = generateArgs();
			main.main(args);
			JOptionPane.showMessageDialog(window, "finish with the code generation from", "FINISH", JOptionPane.INFORMATION_MESSAGE);
			window.setVisible(false);
			Runtime r = Runtime.getRuntime();
			args = null;
			r.runFinalization();
			r.exit(0);
		});
		finishButton.setBounds(x, y, x3, deepth);
		window.add(finishButton);
		x += empty + x3;
		saveArgsButton.addActionListener(e -> {
			int returnVal = argsFC.showSaveDialog(window);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = argsFC.getSelectedFile();
				if (file.isDirectory()) {
					JOptionPane.showMessageDialog(window, "i can not save to a folder! ('" + file.getPath() + "')", "NO FOLDERS!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (Files.exists(file.toPath())) {
					int chose = JOptionPane.showConfirmDialog(window, "this file exists already ('" + file.getPath() + "'), should I overwrite the file?");
					if (chose != JOptionPane.OK_OPTION) {
						return;
					}
				}
				if ( !file.getName().endsWith(".args")) {
					file = new File(file.getPath() + ".args");
				}
				try {
					SERIALIZER.writeObject(new FileOutputStream(file), obj);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		saveArgsButton.setBounds(x, y, x3, deepth);
		window.add(saveArgsButton);
		x += empty + x3;
		loadArgsButton.addActionListener(e -> {
			int returnVal = argsFC.showSaveDialog(window);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = argsFC.getSelectedFile();
				if (file.isDirectory()) {
					JOptionPane.showMessageDialog(window, "i can not save to a folder! ('" + file.getPath() + "')", "NO FOLDERS!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (Files.exists(file.toPath())) {
					int chose = JOptionPane.showConfirmDialog(window, "this file exists already ('" + file.getPath() + "'), should I overwrite the file?");
					if (chose != JOptionPane.OK_OPTION) {
						return;
					}
				}
				if ( !file.getName().endsWith(".args")) {
					file = new File(file.getPath() + ".args");
				}
				try {
					DESERIALIZER.overwriteObject(new FileInputStream(file), obj);
					rebuild();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		rebuild();
		loadArgsButton.setBounds(x, y, x3, deepth);
		window.add(loadArgsButton);
		x += empty + x3;
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return this;
	}
	
	private void rebuild() {
		for (int i = 0; i < rebuildableComps.size(); i ++ ) {
			window.remove(rebuildableComps.get(i));
		}
		rebuildableComps.clear();
		int x, y = empty;
		for (int index = 0; index < argFields.length; index ++ ) {
			final int i = index;
			GUINormalLine normal = argFields[i].getAnnotation(GUINormalLine.class);
			GUIOwnWindow ownWindow = argFields[i].getAnnotation(GUIOwnWindow.class);
			int xx;
			GUIArt[] arten;
			String[][] text;
			boolean flag = false;
			if (normal != null) {
				if (normal.secondArt() != GUIArt.nothing) {
					if (normal.thirdArt() != GUIArt.nothing) {
						if (normal.firstArt() != GUIArt.nothing) {
							xx = x4;
							arten = new GUIArt[4];
						} else {
							xx = x3;
							arten = new GUIArt[3];
						}
					} else {
						xx = x2;
						arten = new GUIArt[2];
					}
				} else {
					xx = x1;
					arten = new GUIArt[1];
				}
				text = new String[arten.length][];
				arten[0] = normal.firstArt();
				text[0] = normal.firstText();
				if (arten.length > 1) {
					arten[1] = normal.secondArt();
					text[1] = normal.secondText();
					if (arten.length > 2) {
						arten[2] = normal.thirdArt();
						text[2] = normal.thirdText();
						if (arten.length > 3) {
							arten[3] = normal.forthArt();
							text[3] = normal.forthText();
						}
					}
				}
			} else {
				flag = true;
				if (ownWindow.firstArt() != GUIArt.nothing) {
					if (ownWindow.secondArt() != GUIArt.nothing) {
						if (ownWindow.thirdArt() != GUIArt.nothing) {
							if (ownWindow.firstArt() != GUIArt.nothing) {
								throw new AssertionError("no manage button (nothing)");
							} else {
								xx = x4;
								arten = new GUIArt[4];
							}
						} else {
							if (ownWindow.firstArt() != GUIArt.nothing) {
								xx = x4;
								arten = new GUIArt[4];
							} else {
								xx = x3;
								arten = new GUIArt[3];
							}
						}
					} else {
						if (ownWindow.thirdArt() != GUIArt.nothing) {
							if (ownWindow.firstArt() != GUIArt.nothing) {
								xx = x4;
								arten = new GUIArt[4];
							} else {
								xx = x3;
								arten = new GUIArt[3];
							}
						} else {
							xx = x2;
							arten = new GUIArt[2];
						}
					}
				} else {
					if (ownWindow.secondArt() != GUIArt.nothing) {
						if (ownWindow.thirdArt() != GUIArt.nothing) {
							if (ownWindow.firstArt() != GUIArt.nothing) {
								xx = x4;
								arten = new GUIArt[4];
							} else {
								xx = x3;
								arten = new GUIArt[3];
							}
						} else {
							xx = x2;
							arten = new GUIArt[2];
						}
					} else {
						xx = x1;
						arten = new GUIArt[1];
					}
				}
				text = new String[arten.length][];
				arten[0] = ownWindow.firstArt();
				text[0] = ownWindow.firstText();
				if (arten.length > 1) {
					arten[1] = ownWindow.secondArt();
					text[1] = ownWindow.secondText();
					if (arten.length > 2) {
						arten[2] = ownWindow.thirdArt();
						text[2] = ownWindow.thirdText();
						if (arten.length > 3) {
							arten[3] = ownWindow.forthArt();
							text[3] = ownWindow.forthText();
						}
					}
				}
			}
			x = empty;
			for (int ii = 0; ii < arten.length; ii ++ ) {
				final Component comp;
				switch (arten[ii]) {
				case comboBoxFalseTrue:
				case comboBoxTrueFalse:
				case comboBox:
					comp = new JComboBox <String>(text[ii]);
					break;
				case number:
					comp = new JTextField(text[ii][0]);
					((JTextField) comp).addFocusListener(new FocusListener() {
						
						String oldText;
						
						@Override
						public void focusGained(FocusEvent e) {
							oldText = ((JTextField) comp).getText();
						}
						
						@Override
						public void focusLost(FocusEvent e) {
							String newText = ((JTextField) comp).getText();
							if (Objects.equals(newText, oldText)) {
								return;
							}
							Class <?> type = argFields[i].getType();
							Number num;
							String str = newText;
							if (type == Long.TYPE || type == Long.class) {
								num = Long.parseLong(str);
							} else if (type == Integer.TYPE || type == Integer.class) {
								num = Integer.parseInt(str);
							} else if (type == Short.TYPE || type == Short.class) {
								num = Short.parseShort(str);
							} else if (type == Byte.TYPE || type == Byte.class) {
								num = Byte.parseByte(str);
							} else if (type == Double.TYPE || type == Double.class) {
								num = Double.parseDouble(str);
							} else if (type == Float.TYPE || type == Float.class) {
								num = Float.parseFloat(str);
							} else if (type == BigInteger.class) {
								num = new BigInteger(str);
							} else if (type == BigDecimal.class) {
								num = new BigDecimal(str);
							} else {
								throw new AssertionError("unsupported number-type: " + type + " of field: " + argFields[i]);
							}
							setField(i, num);
						}
						
					});
					break;
				case choosenFileModifiable:
				case modifiableText:
					comp = new JTextField(text[ii][0]);
					((JTextField) comp).addFocusListener(new FocusListener() {
						
						String oldText;
						
						@Override
						public void focusGained(FocusEvent e) {
							oldText = ((JTextField) comp).getText();
						}
						
						@Override
						public void focusLost(FocusEvent e) {
							String newText = ((JTextField) comp).getText();
							if (Objects.equals(newText, oldText)) {
								return;
							}
							setField(i, newText);
						}
						
					});
					break;
				case choosenFileunmodifiable:
				case unmodifiableText:
					comp = new JTextPane();
					((JTextPane) comp).setText(text[ii][0]);
					((JTextPane) comp).setEditable(false);
					break;
				case deleteButton:
					comp = new JButton(text[ii][0]);
					((JButton) comp).addActionListener(ae -> setField(i, null));
					break;
				case fileChoose:
					JFileChooser jFileChooser = new JFileChooser();
					comp = new JButton(text[ii][0]);
					break;
				case nothing:
					if ( !flag) {
						throw new AssertionError("'nothing' can not be here now!");
					} else {
						flag = false;
					}
					comp = new JButton(text[ii][0]);
					break;
				default:
					throw new AssertionError("illegal line!");
				}
				comp.setBounds(x, y, xx, deepth);
				x += xx + empty;
			}
			y += deepth + deepth;
		}
		
		// TODO Auto-generated method stub
		
	}
	
	private void setField(final int i, Object val) throws InternalError {
		Field f = argFields[i];
		try {
			f.set(obj, val);
		} catch (IllegalArgumentException err) {
			boolean a = f.isAccessible();
			if (a) {// if it was already accessible i do not need to try it again
				throw new InternalError(err);
			}
			f.setAccessible(true);
			try {
				f.set(obj, null);
				f.setAccessible(false);
			} catch (IllegalArgumentException | IllegalAccessException er) {
				throw new InternalError(err);
			}
		} catch (IllegalAccessException er) {
			throw new InternalError(er);
		}
	}
	
	private Field[] getArgFields() throws AssertionError {
		Field[] fields = obj.getClass().getDeclaredFields();
		List <Object[]> zw = new ArrayList <>();
		for (Field f : fields) {
			Arg arg = f.getAnnotation(Arg.class);
			if (arg == null) {
				continue;
			}
			GUINormalLine normal = f.getAnnotation(GUINormalLine.class);
			GUIOwnWindow ownWindow = f.getAnnotation(GUIOwnWindow.class);
			if (normal == null && ownWindow == null) {
				throw new AssertionError("Arg annotation, but no GUINormalLine or GUIOwnWindow annotation!");
			} else if (normal != null && ownWindow != null) {
				throw new AssertionError("can't have both not null: GUINormalLine='" + normal + "' GUIOwnWindow='" + ownWindow + "'");
			} else if (normal != null) {
				zw.add(new Object[] {normal.order(), f });
			} else {
				zw.add(new Object[] {ownWindow.order(), f });
			}
		}
		zw.sort((a, b) -> {
			Integer ai = (Integer) a[0], bi = (Integer) b[0];
			return ai.compareTo(bi);
		});
		fields = new Field[zw.size()];
		for (int i = 0; i < fields.length; i ++ ) {
			fields[i] = (Field) zw.get(i)[1];
		}
		return fields;
	}
	
	private String[] generateArgs() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
