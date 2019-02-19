package sausage_core.api.core.ienum;

import net.minecraft.client.resources.I18n;

public interface IEnumLocalizer {
    default String localize(String... entries) {
        return I18n.format(String.join(".", String.join(".", entries),
                ((Enum)this).getDeclaringClass().getSimpleName().toLowerCase(), toString().toLowerCase()));
    }
}
