package io.github.thebusybiscuit.slimefun4.core.guide.options;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class FireworksOption implements SlimefunGuideOption<Boolean> {

    @Override
    public SlimefunAddon getAddon() {
        return SlimefunPlugin.instance();
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(SlimefunPlugin.instance(), "research_fireworks");
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        if (SlimefunPlugin.getRegistry().isResearchFireworkEnabled()) {
            boolean enabled = getSelectedOption(p, guide).orElse(true);
            ItemStack item = new CustomItem(Material.FIREWORK_ROCKET, "&b煙火: &" + (enabled ? "a開啟" : "4關閉"), "", "&7研究物品時是否顯示煙火", "", "&7\u21E8 &e點擊 " + (enabled ? "關閉" : "開啟") + " 煙火顯示");
            return Optional.of(item);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void onClick(Player p, ItemStack guide) {
        setSelectedOption(p, guide, !getSelectedOption(p, guide).orElse(true));
        SlimefunGuideSettings.openSettings(p, guide);
    }

    @Override
    public Optional<Boolean> getSelectedOption(Player p, ItemStack guide) {
        NamespacedKey key = getKey();
        boolean value = !PersistentDataAPI.hasByte(p, key) || PersistentDataAPI.getByte(p, key) == (byte) 1;
        return Optional.of(value);
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, Boolean value) {
        PersistentDataAPI.setByte(p, getKey(), value.booleanValue() ? (byte) 1 : (byte) 0);
    }

}
