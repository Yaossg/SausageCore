package sausage_core.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Automatically inject loggers for public static fields.
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectLogger {
	/**
	 * Represent the Logger's name.
	 * Only necessary if this annotation is on a class without @Mod.
	 * */
	String value() default "";
}
