package sausage_core.api.core.tile;

/**
 * A simple machine's logic implementation
 * Use {@code work = tick(work)} to run it
 * */
public interface IMachineLogic {
	boolean READY = true, NOT_READY = !READY;
	boolean DONE = true, NOT_DONE = !DONE;

	/**
	 * @return if the task is ready
	 */
	boolean detect();

	/**
	 * @return if the task has done
	 */
	boolean work();

	default boolean tick(boolean old) {
		return old ? detect() && (!work() || detect()) : detect();
	}
}
