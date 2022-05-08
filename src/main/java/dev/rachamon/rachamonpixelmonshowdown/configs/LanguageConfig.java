package dev.rachamon.rachamonpixelmonshowdown.configs;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.Setting;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class LanguageConfig {
    @Setting(value = "general", comment = "General Battle Messages")
    private final GeneralLanguageBattle generalLanguageBattle = new GeneralLanguageBattle();

    @ConfigSerializable
    public static class GeneralLanguageBattle {
    }

    public GeneralLanguageBattle getGeneralLanguageBattle() {
        return generalLanguageBattle;
    }
}