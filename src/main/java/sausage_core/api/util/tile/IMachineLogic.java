package sausage_core.api.util.tile;

public interface IMachineLogic {
    /**
     * @return if the task is on
     * */
    boolean detect();
    /**
     * @return if the task has been finished
     * */
    boolean work();

    default boolean tick(boolean old) {
        return old ? detect() && (!work() || detect()) : detect();
    }
}
