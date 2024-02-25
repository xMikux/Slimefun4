package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import io.github.bakedlibs.dough.updater.BlobBuildUpdater;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.config.Config;
import io.github.bakedlibs.dough.updater.PluginUpdater;
import io.github.bakedlibs.dough.versions.PrefixedVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * This Class represents our {@link PluginUpdater} Service.
 * If enabled, it will automatically connect to https://blob.build/
 * to check for updates and to download them automatically.
 *
 * @author TheBusyBiscuit
 *
 */
public class UpdaterService {

    /**
     * Our {@link Slimefun} instance.
     */
    private final Slimefun plugin;

    /**
     * Our {@link PluginUpdater} implementation.
     */
    private final PluginUpdater<PrefixedVersion> updater;

    /**
     * The {@link SlimefunBranch} we are currently on.
     * If this is an official {@link SlimefunBranch}, auto updates will be enabled.
     */
    private final SlimefunBranch branch;

    /**
     * This will create a new {@link UpdaterService} for the given {@link Slimefun}.
     * The {@link File} should be the result of the getFile() operation of that {@link Plugin}.
     *
     * @param plugin
     *            The instance of Slimefun
     * @param version
     *            The current version of Slimefun
     * @param file
     *            The {@link File} of this {@link Plugin}
     */
    public UpdaterService(@Nonnull Slimefun plugin, @Nonnull String version, @Nonnull File file) {
        this.plugin = plugin;
        BlobBuildUpdater autoUpdater = null;

        if (version.contains("UNOFFICIAL")) {
            // This Server is using a modified build that is not a public release.
            branch = SlimefunBranch.UNOFFICIAL;
        } else if (version.startsWith("Dev - ")) {
            // If we are using a development build, we want to switch to our custom
            try {
                autoUpdater = new BlobBuildUpdater(plugin, file, "Slimefun4", "Dev");
            } catch (Exception x) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create AutoUpdater", x);
            }

            branch = SlimefunBranch.DEVELOPMENT;
        } else if (version.startsWith("RC - ")) {
            // If we are using a "stable" build, we want to switch to our custom
            try {
                autoUpdater = new BlobBuildUpdater(plugin, file, "Slimefun4", "RC");
            } catch (Exception x) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create AutoUpdater", x);
            }

            branch = SlimefunBranch.STABLE;
        } else {
            branch = SlimefunBranch.UNKNOWN;
        }

        this.updater = autoUpdater;
    }

    /**
     * This method returns the branch the current build of Slimefun is running on.
     * This can be used to determine whether we are dealing with an official build
     * or a build that was unofficially modified.
     *
     * @return The branch this build of Slimefun is on.
     */
    public @Nonnull SlimefunBranch getBranch() {
        return branch;
    }

    /**
     * This method returns the build number that this is running on (or -1 if unofficial).
     * You should combine the usage with {@link #getBranch()} in order to properly see if this is
     * a development or stable build number.
     *
     * @return The build number of this Slimefun.
     */
    public int getBuildNumber() {
        if (updater != null) {
            PrefixedVersion version = updater.getCurrentVersion();
            return version.getVersionNumber();
        }

        return -1;
    }

    public int getLatestVersion() {
        if (updater != null && updater.getLatestVersion().isDone()) {
            PrefixedVersion version;
            try {
                version = updater.getLatestVersion().get();
                return version.getVersionNumber();
            } catch (InterruptedException | ExecutionException e) {
                return -1;
            }
        }

        return -1;
    }

    public boolean isLatestVersion() {
        if (getBuildNumber() == -1 || getLatestVersion() == -1) {
            // We don't know if we're latest so just report we are
            return true;
        }
        
        return getBuildNumber() == getLatestVersion();
    }

    /**
     * This will start the {@link UpdaterService} and check for updates.
     * If it can find an update it will automatically be installed.
     */
    public void start() {
        if (updater != null) {
            updater.start();
        } else {
            printBorder();
            plugin.getLogger().log(Level.WARNING, "此版本為黏液科技繁體翻譯版!");
            plugin.getLogger().log(Level.WARNING, "因為非官方,自動更新已被禁用.");
            plugin.getLogger().log(Level.WARNING, "請勿回報此黏液科技版本遇到的任何錯誤給黏液科技官方!");
            plugin.getLogger().log(Level.WARNING, "有問題請回報在 https://github.com/xMikux/Slimefun4/issues");
            printBorder();
        }
    }

    /**
     * This returns whether the {@link PluginUpdater} is enabled or not.
     * This includes the {@link Config} setting but also whether or not we are running an
     * official or unofficial build.
     *
     * @return Whether the {@link PluginUpdater} is enabled
     */
    public boolean isEnabled() {
        return Slimefun.getCfg().getBoolean("options.auto-update") && updater != null;
    }

    /**
     * This method is called when the {@link UpdaterService} was disabled.
     */
    public void disable() {
        printBorder();
        plugin.getLogger().log(Level.WARNING, "此版本為黏液科技繁體翻譯版!");
        plugin.getLogger().log(Level.WARNING, "自動更新已關閉.");
        plugin.getLogger().log(Level.WARNING, "請勿回報此黏液科技版本遇到的任何錯誤給黏液科技官方!");
        plugin.getLogger().log(Level.WARNING, "有問題請回報在 https://github.com/xMikux/Slimefun4/issues");

        if (branch != SlimefunBranch.STABLE) {
            plugin.getLogger().log(Level.WARNING, "此訊息可忽略!");
            plugin.getLogger().log(Level.WARNING, "If you are just scared of Slimefun breaking, then please consider using a \"stable\" build instead of disabling auto-updates.");
        }

        printBorder();
    }

    private void printBorder() {
        plugin.getLogger().log(Level.WARNING, "#######################################################");
    }

}
