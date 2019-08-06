package sausage_core.api.annotation;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Automatically call all in {@link java.util.Collection}s of {@link Runnable}s in static fields
 * NOTE: if there are fields annotated @AutoCall in a class, the instances of which MUST be constructed
 * 		BEFORE {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent} is fired.
 * 		Highly recommend the static intializers in the @Mod class.
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoCall {
	When[] when();
	Side[] side() default { Side.SERVER, Side.CLIENT };

	enum When {
		PRE_INIT,
		INIT,
		POST_INIT,

		IB_REGISTER,
		LOAD_MODEL;

		public static void checkState() {
			if (Loader.instance().hasReachedState(LoaderState.PREINITIALIZATION))
				throw new IllegalStateException("see Javadoc above @AutoCall");
		}
	}
}
