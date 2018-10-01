package sausage_core.api.util.common;

import net.minecraft.client.resources.I18n;

public interface IEnumLocalization {
    default String localize(String... entries) {
        return I18n.format(String.join(".", String.join(".", entries),
                getClass().getSimpleName().toLowerCase(), toString().toLowerCase()));
    }
}
