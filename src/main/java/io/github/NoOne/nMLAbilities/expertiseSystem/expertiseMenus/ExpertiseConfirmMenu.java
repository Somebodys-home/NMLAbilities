package io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus;

import io.github.NoOne.menuSystem.Menu;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.AbilityChangeEvent;
import io.github.NoOne.nMLItems.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ExpertiseConfirmMenu extends Menu {
    private ItemStack expertise1;
    private ItemStack expertise2;
    private ItemStack expertise3;
    private ItemStack newAbility;
    private Menu previousMenu;

    public ExpertiseConfirmMenu(Player player, ItemStack newAbility, Menu previousMenu) {
        super(player);

        this.expertise1 = player.getInventory().getItem(1);
        this.expertise2 = player.getInventory().getItem(2);
        this.expertise3 = player.getInventory().getItem(3);
        this.newAbility = newAbility;
        this.previousMenu = previousMenu;
    }

    @Override
    public String getMenuName() {
        return "§6§lYou sure?";
    }

    @Override
    public int getSlots() {
        return 9 * 3;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 11 -> {
                if (expertise1.isSimilar(AbilityItemManager.cooldownItem())) {
                    player.sendMessage("§c⚠ §nWait for this ability to go off cooldownSystem.§r§c ⚠");
                    return;
                }

                player.getInventory().setItem(1, newAbility);
                Bukkit.getPluginManager().callEvent(new AbilityChangeEvent(player, "expertise1", newAbility));
                previousMenu.open();
            }
            case 13 -> {
                if (expertise2.isSimilar(AbilityItemManager.cooldownItem())) {
                    player.sendMessage("§c⚠ §nWait for this ability to go off cooldownSystem.§r§c ⚠");
                    return;
                }

                player.getInventory().setItem(2, newAbility);
                Bukkit.getPluginManager().callEvent(new AbilityChangeEvent(player, "expertise2", newAbility));
                previousMenu.open();
            }
            case 15 -> {
                if (expertise3.isSimilar(AbilityItemManager.cooldownItem())) {
                    player.sendMessage("§c⚠ §nWait for this ability to go off cooldownSystem.§r§c ⚠");
                    return;
                }

                player.getInventory().setItem(3, newAbility);
                Bukkit.getPluginManager().callEvent(new AbilityChangeEvent(player, "expertise3", newAbility));
                previousMenu.open();
            }
            case 22 -> previousMenu.open();
        }
    }

    @Override
    public void handlePlayerMenu(InventoryClickEvent inventoryClickEvent) {
        inventoryClickEvent.setCancelled(true);
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(4, newAbility);
        inventory.setItem(11, expertise1);
        inventory.setItem(13, expertise2);
        inventory.setItem(15, expertise3);
        inventory.setItem(22, ItemCreator.createBackoutButton());
    }
}
