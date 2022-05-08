package dev.rachamon.rachamonpixelmonshowdown.configs;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.Setting;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MainConfig {
    @Setting(value = "general", comment = "General Settings")
    private final GeneralCategorySetting mainCategorySetting = new GeneralCategorySetting();

    @ConfigSerializable
    public static class GeneralCategorySetting {

        @Setting(comment = "enable debug ? [default: false]", value = "is-debug")
        protected boolean debug = false;

        public boolean isDebug() {
            return debug;
        }
    }

    public GeneralCategorySetting getMainCategorySetting() {
        return mainCategorySetting;
    }
}
