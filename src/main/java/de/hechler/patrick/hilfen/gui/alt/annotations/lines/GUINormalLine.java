package de.hechler.patrick.hilfen.gui.alt.annotations.lines;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.hechler.patrick.hilfen.gui.enums.GUIArt;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Deprecated
public @interface GUINormalLine {
	//
	// String[] firstText();
	//
	// GUIArt firstArt();
	//
	// String[] secondText() default "";
	//
	// GUIArt secondArt() default GUIArt.nothing;
	//
	// String[] thirdText() default "";
	//
	// GUIArt thirdArt() default GUIArt.nothing;
	//
	// String[] forthText() default "";
	//
	// GUIArt forthArt() default GUIArt.nothing;
	//
	// int order();
	//
}
