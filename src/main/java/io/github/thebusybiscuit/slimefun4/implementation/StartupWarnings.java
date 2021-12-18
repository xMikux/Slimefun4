package io.github.thebusybiscuit.slimefun4.implementation;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;

/**
 * This class stores some startup warnings we occasionally need to print.
 * If you setup your server the recommended way, you are never going to see
 * any of these messages.
 * 
 * @author TheBusyBiscuit
 *
 */
final class StartupWarnings {

    private static final String BORDER = "****************************************************";
    private static final String PREFIX = "* ";

    private StartupWarnings() {}

    @ParametersAreNonnullByDefault
    static void discourageCSCoreLib(Logger logger) {
        logger.log(Level.WARNING, BORDER);
        logger.log(Level.WARNING, PREFIX + "看起來你還在使用 CS-CoreLib.");
        logger.log(Level.WARNING, PREFIX);
        logger.log(Level.WARNING, PREFIX + "Slimefun 不再需要 CS-CoreLib");
        logger.log(Level.WARNING, PREFIX + "的安裝,截至 2021年1月30日.");
        logger.log(Level.WARNING, PREFIX + "刪除是安全的! 我們建議你立即刪除");
        logger.log(Level.WARNING, PREFIX + "CS-CoreLib 從你的伺服器.");
        logger.log(Level.WARNING, BORDER);
    }

    @ParametersAreNonnullByDefault
    static void invalidMinecraftVersion(Logger logger, int majorVersion, String slimefunVersion) {
        logger.log(Level.SEVERE, BORDER);
        logger.log(Level.SEVERE, PREFIX + "Slimefun 並未被正確安裝!");
        logger.log(Level.SEVERE, PREFIX + "你正在使用不支持的Minecraft版本!");
        logger.log(Level.SEVERE, PREFIX);
        logger.log(Level.SEVERE, PREFIX + "你正在使用Minecraft 1.{0}.x", majorVersion);
        logger.log(Level.SEVERE, PREFIX + "但 Slimefun {0} 只支持", slimefunVersion);
        logger.log(Level.SEVERE, PREFIX + "Minecraft {0}", String.join(" / ", Slimefun.getSupportedVersions()));
        logger.log(Level.SEVERE, BORDER);
    }

    @ParametersAreNonnullByDefault
    static void invalidServerSoftware(Logger logger) {
        logger.log(Level.SEVERE, BORDER);
        logger.log(Level.SEVERE, PREFIX + "Slimefun 並未被正確安裝!");
        logger.log(Level.SEVERE, PREFIX + "CraftBukkit 不再受支持!");
        logger.log(Level.SEVERE, PREFIX);
        logger.log(Level.SEVERE, PREFIX + "Slimefun 要求你使用 Spigot, Paper 或");
        logger.log(Level.SEVERE, PREFIX + "任何Spigot或Paper的受支持分支.");
        logger.log(Level.SEVERE, PREFIX + "(我們推薦Paper)");
        logger.log(Level.SEVERE, BORDER);
    }

    @ParametersAreNonnullByDefault
    static void oldJavaVersion(Logger logger, int recommendedJavaVersion) {
        int javaVersion = NumberUtils.getJavaVersion();

        logger.log(Level.WARNING, BORDER);
        logger.log(Level.WARNING, PREFIX + "你的 Java 版本 (Java {0}) 已過期.", javaVersion);
        logger.log(Level.WARNING, PREFIX);
        logger.log(Level.WARNING, PREFIX + "我們建議你更新到 Java {0}.", recommendedJavaVersion);
        logger.log(Level.WARNING, PREFIX + "從 Minecraft 1.17 開始,", recommendedJavaVersion);
        logger.log(Level.WARNING, PREFIX + "將需要使用 Java {0}, 我們希望");
        logger.log(Level.WARNING, PREFIX + "盡快利用它所帶來的所有新功能.");
        logger.log(Level.WARNING, PREFIX + "在可預見的未來, Slimefun也將需要", recommendedJavaVersion);
        logger.log(Level.WARNING, PREFIX + "Java {0}, 所以請更新!");
        logger.log(Level.WARNING, BORDER);
    }

}
