package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.papermc.lib.PaperLib;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * This is our class for the /sf versions subcommand.
 * 
 * @author TheBusyBiscuit
 * @author Walshy
 *
 */
class VersionsCommand extends SubCommand {

    VersionsCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "versions", false);
    }

    @Override
    public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        if (sender.hasPermission("slimefun.command.versions") || sender instanceof ConsoleCommandSender) {
            /*
             * After all these years... Spigot still displays as "CraftBukkit".
             * so we will just fix this inconsistency for them :)
             */
            String serverSoftware = PaperLib.isSpigot() && !PaperLib.isPaper() ? "Spigot" : Bukkit.getName();
            ComponentBuilder builder = new ComponentBuilder();

            // @formatter:off
            builder.append("此伺服器使用下列Slimefun設置:\n")
                .color(ChatColor.GRAY)
                .append(serverSoftware)
                .color(ChatColor.GREEN)
                .append(" " + Bukkit.getVersion() + '\n')
                .color(ChatColor.DARK_GREEN)
                .append("Slimefun ")
                .color(ChatColor.GREEN)
                .append(SlimefunPlugin.getVersion() + '\n')
                .color(ChatColor.DARK_GREEN);
            // @formatter:on

            if (SlimefunPlugin.getMetricsService().getVersion() != null) {
                // @formatter:off
                builder.append("Metrics-模塊 ")
                    .color(ChatColor.GREEN)
                    .append("#" + SlimefunPlugin.getMetricsService().getVersion() + '\n')
                    .color(ChatColor.DARK_GREEN);
                // @formatter:on
            }

            addJavaVersion(builder);

            if (SlimefunPlugin.getRegistry().isBackwardsCompatible()) {
                // @formatter:off
                HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                    "向後兼容會對效能產生負面影響!\n" +
                    "我們建議你關閉此設置, 除非你的伺服器 " +
                    "有遺留Slimefun的物品(來自2019夏季之前)在流通."
                ));
                // @formatter:on

                builder.append("\n向後兼容已啟用!\n").color(ChatColor.RED).event(hoverEvent);
            }

            builder.append("\n").event((HoverEvent) null);
            addPluginVersions(builder);

            sender.spigot().sendMessage(builder.create());
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

    private void addJavaVersion(@Nonnull ComponentBuilder builder) {
        String javaVer = System.getProperty("java.version");

        if (javaVer.startsWith("1.")) {
            javaVer = javaVer.substring(2);
        }

        // If it's like 11.0.1.3 or 8.0_275
        if (javaVer.indexOf('.') != -1) {
            javaVer = javaVer.substring(0, javaVer.indexOf('.'));
        }

        int version = Integer.parseInt(javaVer);

        if (version < 11) {
            // @formatter:off
            builder.append("Java " + version).color(ChatColor.RED)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                    "你的Java版本已過時!\n" +
                    "你應該使用Java 11或更高的版本.\n" +
                    "隨著Minecraft 1.17的發布, Paper將不再支持舊版本."
                )))
                .append("\n")
                .event((HoverEvent) null);
            // @formatter:on
        } else {
            builder.append("Java ").color(ChatColor.GREEN).append(version + "\n").color(ChatColor.DARK_GREEN);
        }
    }

    private void addPluginVersions(@Nonnull ComponentBuilder builder) {
        Collection<Plugin> addons = SlimefunPlugin.getInstalledAddons();

        if (addons.isEmpty()) {
            builder.append("無附加安裝").color(ChatColor.GRAY).italic(true);
            return;
        }

        builder.append("已安裝的附加: ").color(ChatColor.GRAY).append("(" + addons.size() + ")").color(ChatColor.DARK_GRAY);

        for (Plugin plugin : addons) {
            String version = plugin.getDescription().getVersion();

            HoverEvent hoverEvent = null;
            ClickEvent clickEvent = null;
            ChatColor primaryColor;
            ChatColor secondaryColor;

            if (Bukkit.getPluginManager().isPluginEnabled(plugin)) {
                primaryColor = ChatColor.GREEN;
                secondaryColor = ChatColor.DARK_GREEN;
                String authors = String.join(", ", plugin.getDescription().getAuthors());

                if (plugin instanceof SlimefunAddon && ((SlimefunAddon) plugin).getBugTrackerURL() != null) {
                    // @formatter:off
                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder()
                        .append("作者(s): ")
                        .append(authors)
                        .color(ChatColor.YELLOW)
                        .append("\n> 點擊開啟它們的錯誤追蹤器")
                        .color(ChatColor.GOLD)
                        .create()
                    ));
                    // @formatter:on

                    clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, ((SlimefunAddon) plugin).getBugTrackerURL());
                } else {
                    // @formatter:off
                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder()
                        .append("作者(s): ")
                        .append(authors)
                        .color(ChatColor.YELLOW)
                        .create()
                    ));
                    // @formatter:on
                }
            } else {
                primaryColor = ChatColor.RED;
                secondaryColor = ChatColor.DARK_RED;

                if (plugin instanceof SlimefunAddon && ((SlimefunAddon) plugin).getBugTrackerURL() != null) {
                    // @formatter:off
                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder()
                        .append("此插件已關閉.\n檢查控制台是否有錯誤.")
                        .color(ChatColor.RED)
                        .append("\n> 點此處回報給它們的問題追蹤器")
                        .color(ChatColor.DARK_RED)
                        .create()
                    ));
                    // @formatter:on

                    SlimefunAddon addon = (SlimefunAddon) plugin;

                    if (addon.getBugTrackerURL() != null) {
                        clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, addon.getBugTrackerURL());
                    }
                } else {
                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("插件已關閉. 檢查控制台是否有錯誤並回報給它們的問題追蹤器."));
                }
            }

            // @formatter:off
            // We need to reset the hover event or it's added to all components
            builder.append("\n  " + plugin.getName())
                .color(primaryColor)
                .event(hoverEvent)
                .event(clickEvent)
                .append(" v" + version)
                .color(secondaryColor)
                .append("")
                .event((ClickEvent) null)
                .event((HoverEvent) null);
            // @formatter:on
        }
    }
}
