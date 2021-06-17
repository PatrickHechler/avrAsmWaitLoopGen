package de.hechler.patrick.hilfen.gui.objects;

import java.util.Arrays;

import de.hechler.patrick.hilfen.gui.enums.GUIArt;

public class Line {
	
	private final GUIArt[]   arten;
	private final String[][] texte;
	
	private Line(GUIArt[] arten, String[][] texte) {
		this.arten = arten;
		this.texte = texte;
	}
	
	public static Line create(GUIArt[] arten, String[][] texte) {
		if (arten.length != texte.length) {
			throw new IllegalArgumentException("arten-len != texte-len: arten-len=" + arten.length + "");
		}
		for (int i = 0; i < arten.length; i ++ ) {
			switch (arten[i]) {
			case comboBoxFalseTrue:
			case comboBoxTrueFalse:
				if (texte[i].length < 1) {
					throw new IllegalArgumentException(
						"zu kleines array für combo box art:" + texte[i].length + " art: " + arten[i].name() + " arten: " + Arrays.deepToString(arten) + " texte: " + Arrays.deepToString(texte));
				}
				break;
			default:
				if (texte[i].length != 1) {
					throw new IllegalArgumentException(
						"zu großes  array (oder länge 0) für nicht combo box art:" + texte[i].length + " art: " + arten[i].name() + " arten: " + Arrays.deepToString(arten) + " texte: "
							+ Arrays.deepToString(texte));
				}
			}
			for (int ii = 0; ii < texte[i].length; ii ++ ) {
				if (texte[i] == null) {
					throw new NullPointerException("keine null texte erlaubt!");
				}
			}
		}
		return new Line(arten, texte);
	}
	
	public int arten() {
		return arten.length;
	}
	
	public GUIArt art(int index) {
		return arten[index];
	}
	
	public int texte() {
		return texte.length;
	}
	
	public String[] text(int index) {
		return texte[index].clone();
	}
	
}
