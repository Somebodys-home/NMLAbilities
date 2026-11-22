package io.github.NoOne.nMLAbilities.abilitySystem;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityUse.UseAbilityEvent;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.AbilityChangeEvent;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLConfig.ConfigTemplate;
import io.github.NoOne.nMLDamages.enums.DamageType;
import io.github.NoOne.nMLDamages.event.CustomDamageEvent;
import io.github.NoOne.nMLPlayerStats.StatConfigManager;
import io.github.NoOne.nMLPlayerStats.statSystem.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

// the general listener for making abilities work, not expertise/style specific
public class AbilityListener implements Listener {
    private StatConfigManager statConfigManager;
    private SelectedAbilitiesManager selectedAbilitiesManager;
    private ConfigTemplate selectedAbilitiesConfig;
    private ArrayList<Player> loadedAbilitiesPlayers;

    public AbilityListener(NMLAbilities nmlAbilities) {
        statConfigManager = nmlAbilities.getStatConfigManager();
        selectedAbilitiesManager = nmlAbilities.getSelectedAbilitiesManager();
        selectedAbilitiesConfig = nmlAbilities.getSelectedAbilitiesConfig();
        loadedAbilitiesPlayers = new ArrayList<>();
    }

    @EventHandler
    public void onUseAbility(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        double currentEnergy = statConfigManager.getStats(uuid).getStatValue(PlayerStat.CURRENT_ENERGY);
        int newSlot = event.getNewSlot();
        ItemStack ability = player.getInventory().getItem(newSlot);

        if (AbilityItemManager.isAnAbility(ability)) { // if it's an ability item
            event.setCancelled(true);

            // blank ability & cooldown check
            if (player.hasCooldown(ability) ||
                ability.getType() == ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem().getType() ||
                ability.getType() == AbilityItemManager.emptyStyleAbilityItem().getType() ||
                ability.getType() == AbilityItemManager.cooldownItem().getType()) {

                return;
            }

            // guard check
            if (player.isBlocking()) {
                player.sendMessage("§c⚠ §nAbilities can't be used while guarding!§r§c ⚠");
                return;
            }

            // prerequisite check
            if (!AbilityItemManager.meetsPrerequisites(player, ability)) {
                player.sendMessage("§c⚠ §nPrerequisites not met!§r§c ⚠");
                return;
            }

            // weapon check
            if (!AbilityItemManager.isHoldingWeaponForAbility(player, ability)) {
                player.sendMessage("§c⚠ §nRequirements not met!§r§c ⚠");
                return;
            }

            if (AbilityItemManager.isToggleable(ability)) { // if it's a toggleable ability
                boolean eventualToggleState = !AbilityItemManager.getToggleState(ability);

                if (eventualToggleState) { // if it will be turned on, energy check
                    if (AbilityItemManager.getRequiredEnergy(ability) <= currentEnergy) {
                        AbilityItemManager.setToggleState(ability, true);
                        Bukkit.getPluginManager().callEvent(new UseAbilityEvent(player, ability, "expertise"));
                    } else {
                        player.sendMessage("§c⚠ §nNot enough energy!§r§c ⚠");
                    }
                } else { // if it will be turned off, put on cooldown
                    AbilityItemManager.setToggleState(ability, false);
                    Bukkit.getPluginManager().callEvent(new UseAbilityEvent(player, ability, "expertise"));
                    CooldownManager.putOnSoftCooldown(player, newSlot, AbilityItemManager.getCooldown(ability));
                }
            } else { // if it isn't a toggleable
                if (AbilityItemManager.getRequiredEnergy(ability) <= currentEnergy) { // energy check
                    Bukkit.getPluginManager().callEvent(new UseAbilityEvent(player, ability, "expertise"));
                    CooldownManager.putOnSoftCooldown(player, newSlot, AbilityItemManager.getCooldown(ability));
                } else {
                    player.sendMessage("§c⚠ §nNot enough energy!§r§c ⚠");
                }
            }
        }
    }

    @EventHandler
    public void setAbilitiesOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        SelectedAbilities selectedAbilities = selectedAbilitiesManager.getSelectedAbilities(player.getUniqueId());

        if (!loadedAbilitiesPlayers.contains(player)) { // if we haven't already loaded the abilities of the player
            loadedAbilitiesPlayers.add(player);
            playerInventory.setItem(0, AbilityItemManager.emptyStyleAbilityItem());

            if (selectedAbilities == null) { // if the player doesn't have any saved abilities, then give them the default ones
                playerInventory.setItem(1, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                playerInventory.setItem(2, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                playerInventory.setItem(3, ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
                selectedAbilitiesManager.addSelectedAbilities(player, new SelectedAbilities(playerInventory));
                selectedAbilitiesManager.saveSelectedAbilitiesToConfig(player);
                selectedAbilitiesConfig.saveConfig();
            } else { // load their abilities from config as items, just in case they would have lingering cooldown items when they rejoin
                playerInventory.setItem(1, selectedAbilities.getExpertise1());
                playerInventory.setItem(2, selectedAbilities.getExpertise2());
                playerInventory.setItem(3, selectedAbilities.getExpertise3());
            }
        }
    }

    @EventHandler
    public void onChangeAbility(AbilityChangeEvent event) {
        Player player = event.getPlayer();
        ItemStack newAbility = event.getNewAbility();
        SelectedAbilities selectedAbilities = selectedAbilitiesManager.getSelectedAbilities(player.getUniqueId());

        if (event.isResettingAbilities()) {
            selectedAbilities.clearSelectedAbilities();
        } else {
            switch (event.getChangedAbility()) {
                case "expertise1" -> selectedAbilities.setExpertise1(newAbility);
                case "expertise2" -> selectedAbilities.setExpertise2(newAbility);
                case "expertise3" -> selectedAbilities.setExpertise3(newAbility);
            }
        }

        selectedAbilitiesManager.saveSelectedAbilitiesToConfig(player);
        selectedAbilitiesConfig.saveConfig();
    }

    @EventHandler
    public void damageWithAbilityArrow(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player player && arrow.hasMetadata("ability_arrow")) {
            MetadataValue meta = arrow.getMetadata("ability_arrow").get(0);
            HashMap<DamageType, Double> damageMap = (HashMap<DamageType, Double>) meta.value();

            event.setDamage(0);
            arrow.remove();
            if (event.getEntity() instanceof LivingEntity livingEntity) livingEntity.setNoDamageTicks(0);

            Bukkit.getPluginManager().callEvent(new CustomDamageEvent((LivingEntity) event.getEntity(), player, damageMap));
        }
    }

    @EventHandler
    public void noJumping(PlayerJumpEvent event) {
        if (event.getPlayer().hasMetadata("ability_no_jump")) {
            event.setCancelled(true);
        }
    }

    // all of this is for making ability items unmovable in the inventory
    @EventHandler
    public void dontDropAbilities(PlayerDropItemEvent event) {
        if (AbilityItemManager.isAnAbility(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dontInventoryClickAbilities(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (AbilityItemManager.isAnAbility(clickedItem)) {
            if (event.getClick() == ClickType.SHIFT_LEFT ||
                    event.getClick() == ClickType.SHIFT_RIGHT ||
                    event.getClick() == ClickType.DOUBLE_CLICK ||
                    event.getClick() == ClickType.NUMBER_KEY ||
                    event.getClick() == ClickType.SWAP_OFFHAND) {

                event.setCancelled(true);
            }

            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
                    event.getAction() == InventoryAction.PICKUP_ALL ||
                    event.getAction() == InventoryAction.PICKUP_HALF ||
                    event.getAction() == InventoryAction.PICKUP_SOME ||
                    event.getAction() == InventoryAction.PICKUP_ONE ||
                    event.getAction() == InventoryAction.PLACE_ALL ||
                    event.getAction() == InventoryAction.PLACE_SOME ||
                    event.getAction() == InventoryAction.PLACE_ONE ||
                    event.getAction() == InventoryAction.SWAP_WITH_CURSOR) {

                event.setCancelled(true);
            }

            if (isContainer(event.getClickedInventory().getType())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandAbilities(PlayerSwapHandItemsEvent event) {
        if (AbilityItemManager.isAnAbility(Objects.requireNonNull(event.getOffHandItem()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        for (int slot : event.getRawSlots()) {
            Inventory inventory = event.getView().getInventory(slot);

            if (inventory != null && isContainer(inventory.getType())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void dontHotkeyAbilitiesIntoContainers(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClick() == ClickType.NUMBER_KEY) {
            ItemStack item = player.getInventory().getItem(event.getHotbarButton());

            assert item != null;
            if (AbilityItemManager.isAnAbility(item)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isContainer(InventoryType type) {
        return type == InventoryType.CHEST ||
                type == InventoryType.HOPPER ||
                type == InventoryType.BARREL ||
                type == InventoryType.SHULKER_BOX ||
                type == InventoryType.DISPENSER ||
                type == InventoryType.DROPPER ||
                type == InventoryType.FURNACE ||
                type == InventoryType.BLAST_FURNACE ||
                type == InventoryType.SMOKER;
    }
}
