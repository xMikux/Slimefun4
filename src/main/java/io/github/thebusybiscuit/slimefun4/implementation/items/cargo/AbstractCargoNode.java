package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.ColoredMaterial;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * This abstract class is the super class of all cargo nodes.
 * 
 * @author TheBusyBiscuit
 *
 */
abstract class AbstractCargoNode extends SimpleSlimefunItem<BlockPlaceHandler> {

    protected static final String FREQUENCY = "frequency";

    @ParametersAreNonnullByDefault
    public AbstractCargoNode(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        new BlockMenuPreset(getId(), ChatUtils.removeColorCodes(item.getItemMeta().getDisplayName())) {

            @Override
            public void init() {
                createBorder(this);
            }

            @Override
            public void newInstance(BlockMenu menu, Block b) {
                menu.addMenuCloseHandler(p -> markDirty(b.getLocation()));
                updateBlockMenu(menu, b);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.cargo.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };
    }

    @Override
    public BlockPlaceHandler getItemHandler() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlock();

                // The owner and frequency are required by every node
                BlockStorage.addBlockInfo(b, "owner", e.getPlayer().getUniqueId().toString());
                BlockStorage.addBlockInfo(b, FREQUENCY, "0");

                onPlace(e);
            }

        };
    }

    @ParametersAreNonnullByDefault
    protected void addChannelSelector(Block b, BlockMenu menu, int slotPrev, int slotCurrent, int slotNext) {
        boolean isChestTerminalInstalled = SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled();
        int channel = getSelectedChannel(b);

        menu.replaceExistingItem(slotPrev, new CustomItem(HeadTexture.CARGO_ARROW_LEFT.getAsItemStack(), "&b頻道", "", "&e> 點擊以減少1個頻道ID"));
        menu.addMenuClickHandler(slotPrev, (p, slot, item, action) -> {
            int newChannel = channel - 1;

            if (newChannel < 0) {
                if (isChestTerminalInstalled) {
                    newChannel = 16;
                } else {
                    newChannel = 15;
                }
            }

            BlockStorage.addBlockInfo(b, FREQUENCY, String.valueOf(newChannel));
            updateBlockMenu(menu, b);
            return false;
        });

        if (channel == 16) {
            menu.replaceExistingItem(slotCurrent, new CustomItem(HeadTexture.CHEST_TERMINAL.getAsItemStack(), "&b頻道 ID: &3" + (channel + 1)));
            menu.addMenuClickHandler(slotCurrent, ChestMenuUtils.getEmptyClickHandler());
        } else {
            menu.replaceExistingItem(slotCurrent, new CustomItem(ColoredMaterial.WOOL.get(channel), "&b頻道 ID: &3" + (channel + 1)));
            menu.addMenuClickHandler(slotCurrent, ChestMenuUtils.getEmptyClickHandler());
        }

        menu.replaceExistingItem(slotNext, new CustomItem(HeadTexture.CARGO_ARROW_RIGHT.getAsItemStack(), "&b頻道", "", "&e> 點擊以增加1個頻道ID"));
        menu.addMenuClickHandler(slotNext, (p, slot, item, action) -> {
            int newChannel = channel + 1;

            if (isChestTerminalInstalled) {
                if (newChannel > 16) {
                    newChannel = 0;
                }
            } else if (newChannel > 15) {
                newChannel = 0;
            }

            BlockStorage.addBlockInfo(b, FREQUENCY, String.valueOf(newChannel));
            updateBlockMenu(menu, b);
            return false;
        });
    }

    private int getSelectedChannel(@Nonnull Block b) {
        if (!BlockStorage.hasBlockInfo(b)) {
            return 0;
        } else {
            String frequency = BlockStorage.getLocationInfo(b.getLocation(), FREQUENCY);

            if (frequency == null) {
                return 0;
            } else {
                int channel = Integer.parseInt(frequency);
                return NumberUtils.clamp(0, channel, 16);
            }
        }
    }

    protected abstract void onPlace(@Nonnull BlockPlaceEvent e);

    protected abstract void createBorder(@Nonnull BlockMenuPreset preset);

    protected abstract void updateBlockMenu(@Nonnull BlockMenu menu, @Nonnull Block b);

    protected abstract void markDirty(@Nonnull Location loc);

}
