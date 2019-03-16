package sausage_core.api.core.ienum;

import net.minecraft.client.resources.I18n;

public interface IEnumLocalizer extends IEnum {
    default String localize(String... entries) {
        return I18n.format(String.join(".", String.join(".", entries),
                getEnum().getDeclaringClass().getSimpleName().toLowerCase(), toString().toLowerCase()));
    }
}
