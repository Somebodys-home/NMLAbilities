package io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldown.CooldownManager;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.AbilityChangeEvent;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.menuSystem.Menu;
import io.github.NoOne.menuSystem.PlayerMenuUtility;
import io.github.NoOne.nMLItems.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ExpertiseLoadoutMenu extends Menu {
    private final NMLAbilities nmlAbilities;
    private final SelectedAbilities selectedAbilities;
    private final SelectedAbilitiesManager selectedAbilitiesManager;
    private final Player player;
    private final ItemStack expertise1;
    private final ItemStack expertise2;
    private final ItemStack expertise3;
    private ItemStack leftClickItem1;
    private ItemStack leftClickItem2;
    private int leftClickHotbarSlot1;
    private int leftClickHotbarSlot2;
    private int leftClicks = 0;

    public ExpertiseLoadoutMenu(NMLAbilities nmlAbilities, PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        player = playerMenuUtility.getOwner();
        this.nmlAbilities = nmlAbilities;
        this.expertise1 = player.getInventory().getItem(1);
        this.expertise2 = player.getInventory().getItem(2);
        this.expertise3 = player.getInventory().getItem(3);
        selectedAbilitiesManager = nmlAbilities.getSelectedManager();
        selectedAbilities = selectedAbilitiesManager.getSelectedAbilities(player.getUniqueId());
    }

    @Override
    public String getMenuName() {
        return "§d§lYour Expertise Abilities";
    }

    @Override
    public int getSlots() {
        return 9 * 4;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);

        int slot = event.getSlot();
        ItemStack clickedItem = inventory.getItem(slot);

        switch (slot) {
            case 11, 13, 15 -> {
                switch (event.getClick()) { // editing ability loadout
                    case LEFT -> { // swapping places
                        leftClicks++;

                        if (leftClicks == 1) {
                            if (clickedItem.getItemMeta().getDisplayName().equals(expertise1.getItemMeta().getDisplayName())) {
                                leftClickItem1 = expertise1;
                                leftClickHotbarSlot1 = 1;
                            }
                            if (clickedItem.getItemMeta().getDisplayName().equals(expertise2.getItemMeta().getDisplayName())) {
                                leftClickItem1 = expertise2;
                                leftClickHotbarSlot1 = 2;
                            }
                            if (clickedItem.getItemMeta().getDisplayName().equals(expertise3.getItemMeta().getDisplayName())) {
                                leftClickItem1 = expertise3;
                                leftClickHotbarSlot1 = 3;
                            }

                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1f, 1f);
                        }

                        if (leftClicks == 2) {
                            if (clickedItem.getItemMeta().getDisplayName().equals(expertise1.getItemMeta().getDisplayName())) {
                                leftClickItem2 = expertise1;
                                leftClickHotbarSlot2 = 1;
                            }
                            if (clickedItem.getItemMeta().getDisplayName().equals(expertise2.getItemMeta().getDisplayName())) {
                                leftClickItem2 = expertise2;
                                leftClickHotbarSlot2 = 2;
                            }
                            if (clickedItem.getItemMeta().getDisplayName().equals(expertise3.getItemMeta().getDisplayName())) {
                                leftClickItem2 = expertise3;
                                leftClickHotbarSlot2 = 3;
                            }

                            player.getInventory().setItem(leftClickHotbarSlot2, leftClickItem1);
                            player.getInventory().setItem(leftClickHotbarSlot1, leftClickItem2);
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 2f, 2f);
                            Bukkit.getPluginManager().callEvent(new AbilityChangeEvent(player, "expertise" + leftClickHotbarSlot2, leftClickItem1));
                            Bukkit.getPluginManager().callEvent(new AbilityChangeEvent(player, "expertise" + leftClickHotbarSlot1, leftClickItem2));

                            new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
                        }
                    }
                    case RIGHT -> { // clearing ability
                        switch (slot) {
                            case 11 -> {
                                player.getInventory().setItem(1, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                                CooldownManager.resetCooldown(player, 1);
                                CooldownManager.removeHardCooldown(player);
                                Bukkit.getPluginManager().callEvent(new AbilityChangeEvent(player, "expertise1", ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem()));

                                new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
                            }
                            case 13 -> {
                                player.getInventory().setItem(2, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                                CooldownManager.resetCooldown(player, 2);
                                CooldownManager.removeHardCooldown(player);
                                Bukkit.getPluginManager().callEvent(new AbilityChangeEvent(player, "expertise2", ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem()));

                                new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
                            }
                            case 15 -> {
                                player.getInventory().setItem(3, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                                CooldownManager.resetCooldown(player, 3);
                                CooldownManager.removeHardCooldown(player);
                                Bukkit.getPluginManager().callEvent(new AbilityChangeEvent(player, "expertise3", ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem()));

                                new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
                            }
                        }
                    }
                }
            }
            case 22 -> { // resetting abilities
                if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    CooldownManager.resetAllCooldowns(player);
                    CooldownManager.removeHardCooldown(player);
                    player.getInventory().setItem(0, AbilityItemManager.emptyStyleAbilityItem());
                    player.getInventory().setItem(1, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                    player.getInventory().setItem(2, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                    player.getInventory().setItem(3, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                    Bukkit.getPluginManager().callEvent(new AbilityChangeEvent(player));

                    new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
                }
            }
            case 35 -> new ExpertiseMenu(nmlAbilities, playerMenuUtility).open(); // closing menu
        }
    }

    @Override
    public void handlePlayerMenu(InventoryClickEvent inventoryClickEvent) {
        inventoryClickEvent.setCancelled(true);
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(11, expertise1);
        inventory.setItem(13, expertise2);
        inventory.setItem(15, expertise3);
        inventory.setItem(22, ItemCreator.createItem( // info
                Material.BOOK,
                1,
                "§e- Left click two abilities to swap their places",
                List.of(
                        "§e- Right click an ability to remove it",
                        "§e- Shift right click this item to clear all expertise abilities"
                )
        ));
        inventory.setItem(35, ItemCreator.createItem( // Backout button
                Material.BARRIER,
                1,
                "§c§l<- §r§cGo Back",
                List.of()
        ));
    }
}
