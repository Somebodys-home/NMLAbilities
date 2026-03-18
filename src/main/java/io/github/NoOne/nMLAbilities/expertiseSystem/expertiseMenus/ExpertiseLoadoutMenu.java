package io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem.SelectedAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem.SelectedManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.menuSystem.Menu;
import io.github.NoOne.menuSystem.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ExpertiseLoadoutMenu extends Menu {
    private final NMLAbilities nmlAbilities;
    private final SelectedAbilities selectedAbilities;
    private final SelectedManager selectedManager;
    private final Player player;
    private final ItemStack expertise1;
    private final ItemStack expertise2;
    private final ItemStack expertise3;
    private final ItemStack backout;
    private final ItemStack info;
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
        selectedManager = nmlAbilities.getSelectedManager();
        selectedAbilities = selectedManager.getAbilityProfile(player.getUniqueId());

        backout = new ItemStack(Material.BARRIER);
        ItemMeta backoutItemMeta = backout.getItemMeta();
        backoutItemMeta.setDisplayName("§4§l<= Go back");
        backout.setItemMeta(backoutItemMeta);

        info = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = info.getItemMeta();
        List<String> infoLore = new ArrayList<>();
        infoMeta.setDisplayName("§e- Left click two abilities to swap their places");
        infoLore.add("§e- Right click an ability to remove it");
        infoLore.add("§e- Shift right click this item to clear all expertise abilities");
        infoMeta.setLore(infoLore);
        info.setItemMeta(infoMeta);
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

        if (slot == 35) { // closing menu
            new ExpertiseMenu(nmlAbilities, playerMenuUtility).open();
        } else if (slot == 22) { // resetting abilities
            if (event.getClick() == ClickType.SHIFT_RIGHT) {
                CooldownManager.resetAllCooldowns(player);
                player.getInventory().setItem(0, AbilityItemManager.emptyStyleAbilityItem());
                player.getInventory().setItem(1, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                player.getInventory().setItem(2, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                player.getInventory().setItem(3, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                selectedAbilities.clearSelectedAbilities();
                selectedManager.saveProfileToConfig(player);

                new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
            }
        } else if (slot == 11 || slot == 13 || slot == 15) { // the other two things
            switch (event.getClick()) {
                case LEFT -> {
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

                        new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
                    }
                }
                case RIGHT -> {
                    switch (slot) {
                        case 11 -> {
                            player.getInventory().setItem(1, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                            selectedAbilities.setExpertise1(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem().getItemMeta().getDisplayName());
                            CooldownManager.resetCooldown(player, 1);
                            selectedAbilities.setSelectedAbilitiesFromInventory(player.getInventory());

                            new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
                        }
                        case 13 -> {
                            player.getInventory().setItem(2, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                            selectedAbilities.setExpertise2(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem().getItemMeta().getDisplayName());
                            CooldownManager.resetCooldown(player, 2);
                            selectedAbilities.setSelectedAbilitiesFromInventory(player.getInventory());

                            new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
                        }
                        case 15 -> {
                            player.getInventory().setItem(3, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                            selectedAbilities.setExpertise3(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem().getItemMeta().getDisplayName());
                            CooldownManager.resetCooldown(player, 3);
                            selectedAbilities.setSelectedAbilitiesFromInventory(player.getInventory());

                            new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
                        }
                    }
                }
            }    
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
        inventory.setItem(22, info);
        inventory.setItem(35, backout);
    }

    private int getInventorySlot(Player player, ItemStack targetItem) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack currentItem = contents[i];


            if (currentItem != null && currentItem.isSimilar(targetItem)) return i;
        }

        return -1;
    }
}
