package de.hechler.patrick.hilfen.gui.annotations.lines;

import de.hechler.patrick.hilfen.gui.enums.GUIArt;

public @interface GUINormalLine {
	
	String[] firstText();
	
	GUIArt firstArt();
	
	String[] secondText() default "";
	
	GUIArt secondArt() default GUIArt.nothing;
	
	String[] thirdText() default "";
	
	GUIArt thirdArt() default GUIArt.nothing;
	
	String[] forthText() default "";
	
	GUIArt forthArt() default GUIArt.nothing;
	
	int order();
	
}
