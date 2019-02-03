package sausage_core.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import sausage_core.SausageCore;

@Config(modid = SausageCore.MODID, name = SausageCore.NAME)
public class SausageCoreConfig {
    @Comment("enable spawn info card")
    @LangKey("sausage_core.general.spawnInfoCard")
    public static boolean spawnInfoCard = true;

    @Comment("enable tests of Al")
    @LangKey("sausage_core.general.testAl")
    public static boolean testAl = false;
}
