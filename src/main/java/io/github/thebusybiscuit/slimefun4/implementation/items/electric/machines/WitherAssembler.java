package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

/**
 * The {@link WitherAssembler} is an electrical machine that can automatically spawn
 * a {@link Wither} if the required ingredients have been provided.
 * 
 * @author TheBusyBiscuit
 * 
 * @see IronGolemAssembler
 *
 */
public class WitherAssembler extends AbstractEntityAssembler<Wither> {

    public WitherAssembler(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public int getCapacity() {
        return 4096;
    }

    @Override
    public int getEnergyConsumption() {
        return 4096;
    }

    @Override
    public ItemStack getHead() {
        return new ItemStack(Material.WITHER_SKELETON_SKULL, 3);
    }

    @Override
    public Material getHeadBorder() {
        return Material.BLACK_STAINED_GLASS_PANE;
    }

    @Override
    public ItemStack getBody() {
        return new ItemStack(Material.SOUL_SAND, 4);
    }

    @Override
    public Material getBodyBorder() {
        return Material.BROWN_STAINED_GLASS_PANE;
    }

    @Override
    protected void constructMenu(BlockMenuPreset preset) {
        preset.addItem(1, new CustomItem(getHead(), "&7凋零骷髏頭槽", "", "&f可以放入「凋零骷髏頭」"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(7, new CustomItem(getBody(), "&7靈魂沙槽", "", "&f可以放入「靈魂沙」"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(13, new CustomItem(Material.CLOCK, "&7冷卻時間: &b30 秒", "", "&f這台機器需要運作半分鐘", "&f請給它一些時間!"), ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    public Wither spawnEntity(Location l) {
        return l.getWorld().spawn(l, Wither.class);
    }

}
