package io.github.NoOne.nMLAbilities.abilitySystem.abilityUse;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLItems.ItemSystem;
import io.github.NoOne.nMLItems.enums.ItemType;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;

import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class AbilityListener implements Listener {
    private static NMLAbilities nmlAbilities;
    private ItemSystem itemSystem;
    private ProfileManager profileManager;
    private ItemStack expertiseAbilityItem = ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem();
    private ItemStack styleAbilityItem = AbilityItemManager.emptyStyleAbilityItem();

    public AbilityListener(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        this.profileManager = nmlAbilities.getProfileManager();
        itemSystem = nmlAbilities.getItemSystem();
    }

    @EventHandler
    public void onUseAbility(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        double currentEnergy = profileManager.getPlayerProfile(uuid).getStats().getCurrentEnergy();
        int newSlot = event.getNewSlot();
        ItemStack ability = player.getInventory().getItem(newSlot);
        ItemStack weapon = player.getInventory().getItem(event.getPreviousSlot());

        if (AbilityItemManager.isAnAbility(ability)) { // if it's an ability item
            event.setCancelled(true);

            // blank ability / hard cooldownSystem check
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
            if (AbilityItemManager.hasPrerequisites(ability) && !AbilityItemManager.meetsPrerequisites(player, ability)) {
                player.sendMessage("§c⚠ §nPrerequisites not met!§r§c ⚠");
                return;
            }

            // weapon check
            if (!isHoldingWeaponForAbility(player, ability)) {
                player.sendMessage("§c⚠ §nRequirements not met!§r§c ⚠");
                return;
            }

            if (AbilityItemManager.isToggleable(ability)) { // if it's a toggleable ability
                boolean eventualToggleState = !AbilityItemManager.getToggleState(ability);

                if (eventualToggleState) { // if it will be turned on, energy check
                    if (AbilityItemManager.getRequiredEnergy(ability) <= currentEnergy) {
                        AbilityItemManager.setToggleState(ability, true);
                        Bukkit.getPluginManager().callEvent(new UseAbilityEvent(player, weapon, ability, newSlot));
                    } else {
                        player.sendMessage("§c⚠ §nNot enough energy!§r§c ⚠");
                    }
                } else { // if it will be turned off, put on cooldownSystem
                    AbilityItemManager.setToggleState(ability, false);
                    Bukkit.getPluginManager().callEvent(new UseAbilityEvent(player, weapon, ability, newSlot));
                    CooldownManager.putOnCooldown(player, newSlot, AbilityItemManager.getCooldown(ability));
                }
            } else { // if it isnt a toggleable
                if (AbilityItemManager.getRequiredEnergy(ability) <= currentEnergy) { // energy check
                    Bukkit.getPluginManager().callEvent(new UseAbilityEvent(player, weapon, ability, newSlot));
                    CooldownManager.putOnCooldown(player, newSlot, AbilityItemManager.getCooldown(ability));
                } else {
                    player.sendMessage("§c⚠ §nNot enough energy!§r§c ⚠");
                }
            }
        }
    }

    @EventHandler
    public void blankAbilitiesOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();

        if (playerInventory.getItem(0) == null) player.getInventory().setItem(0, styleAbilityItem);
        if (playerInventory.getItem(1) == null) player.getInventory().setItem(1, expertiseAbilityItem);
        if (playerInventory.getItem(2) == null) player.getInventory().setItem(2, expertiseAbilityItem);
        if (playerInventory.getItem(3) == null) player.getInventory().setItem(3, expertiseAbilityItem);
    }

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
    public void fallingFromAbilityUse(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (player.hasMetadata("ability_falling")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onFireworkDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework firework) {
            if (firework.hasMetadata("ability_firework")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void noJumping(PlayerJumpEvent event) {
        if (event.getPlayer().hasMetadata("ability_no_jump")) {
            event.setCancelled(true);
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

    private List<ItemType> getWeaponsForAbility(ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        List<ItemType> weapons = new ArrayList<>(List.of(SWORD, DAGGER, AXE, HAMMER, SPEAR, GLOVE, BOW, WAND, STAFF, CATALYST, SHIELD));
        List<ItemType> weaponsToRemove = new ArrayList<>();

        if (item.getItemMeta().getPersistentDataContainer().has(ExpertiseAbilityItemMaker.getAnyWeaponKey())) {
            return weapons;
        }

        for (ItemType weapon : weapons) {
            NamespacedKey weaponKey = new NamespacedKey(nmlAbilities, ItemType.getItemTypeString(weapon));

            if (!pdc.has(weaponKey)) {
                weaponsToRemove.add(weapon);
            }
        }

        weapons.removeAll(weaponsToRemove);
        return weapons;
    }

    private boolean isHoldingWeaponForAbility(Player player, ItemStack abilityItem) {
        List<ItemType> requiredWeapons = getWeaponsForAbility(abilityItem);
        ItemStack mainhand = player.getInventory().getItemInMainHand();
        ItemStack offhand = player.getInventory().getItemInOffHand();

        // if an ability uses any weapon
        if (abilityItem.getItemMeta().getPersistentDataContainer().has(ExpertiseAbilityItemMaker.getAnyWeaponKey())) {
            for (ItemType itemType : requiredWeapons) {
                if (itemSystem.isItemType(mainhand, itemType)) {
                    return true;
                }
            }

            return false;
        }

        if (requiredWeapons.contains(GLOVE)) {
            return itemSystem.isItemType(mainhand, GLOVE) && itemSystem.isItemType(offhand, GLOVE);
        } else if (requiredWeapons.contains(BOW)) {
            return itemSystem.isItemType(mainhand, BOW) && itemSystem.isItemType(offhand, QUIVER);
        } else if (requiredWeapons.contains(SHIELD)) {
            return itemSystem.isItemType(mainhand, SHIELD) || itemSystem.isItemType(offhand, SHIELD);
        } else {
            for (ItemType itemType : requiredWeapons) {
                if (itemSystem.isItemType(mainhand, itemType)) {
                    return true;
                }
            }

            return false;
        }
    }
}
