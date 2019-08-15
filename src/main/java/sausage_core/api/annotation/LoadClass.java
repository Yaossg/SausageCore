package sausage_core.api.annotation;

import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Automatically load classes
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoadClass {
	Side[] side() default { Side.SERVER, Side.CLIENT };
	When[] when();
	/**
	 * Load class only when specific mod is loaded
	 * It is an easy way to build a plugin
	 * */
	String modRequired() default "minecraft";
	/**
	 * Construct an instance by default constructor after loading
	 * It is an easy way to run a plugin
	 * */
	boolean construct() default false;

	enum When {
		PRE_INIT(AutoCall.When.PRE_INIT),
		INIT(AutoCall.When.INIT),
		POST_INIT(AutoCall.When.POST_INIT);

		private final AutoCall.When when;

		When(AutoCall.When when) {
			this.when = when;
		}

		public AutoCall.When intern() {
			return when;
		}
	}

}
