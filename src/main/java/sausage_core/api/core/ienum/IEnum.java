package sausage_core.api.core.ienum;

import sausage_core.api.util.common.SausageUtils;

public interface IEnum {
    default <E extends Enum<E>> E getEnum() {
        return SausageUtils.rawtype(this);
    }
}
