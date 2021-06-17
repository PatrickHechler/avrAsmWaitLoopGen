package de.hechler.patrick.hilfen.gui.interfaces;

import de.hechler.patrick.hilfen.gui.enums.GUIArt;
import de.hechler.patrick.hilfen.gui.objects.Line;

public interface ArgContainer {
	
	int numberOfFields();
	
	void setArg(int index, Object value);
	
	Object getArg(int index);
	
	Class <?> getArgType(int index);
	
	GUIArt[] getLineArten(int index);
	
	String[][] getLineTexte(int index);
	
	default Line getLine(int index) {
		return Line.create(getLineArten(index), getLineTexte(index));
	}
	
}
