package io.github.NoOne.nMLAbilities.abilitySystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLItems.ItemCreator;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AbilityItemManager {
    // defining keys
    private static NamespacedKey abilityKey;
    private static NamespacedKey expertiseKey;
    private static NamespacedKey cooldownKey;
    private static NamespacedKey toggleKey;
    private static NamespacedKey originalItemKey;
    private static NamespacedKey energyKey;
    private static NamespacedKey unusableKey;

    // prerequisite keys
    private static NamespacedKey groundedKey;

    public AbilityItemManager(NMLAbilities nmlAbilities) {
        abilityKey = new NamespacedKey(nmlAbilities, "ability");
        expertiseKey = new NamespacedKey(nmlAbilities, "expertise");
        cooldownKey = new NamespacedKey(nmlAbilities, "cooldown");
        toggleKey = new NamespacedKey(nmlAbilities, "toggle");
        originalItemKey = new NamespacedKey(nmlAbilities, "originalItem");
        energyKey = new NamespacedKey(nmlAbilities, "energy");
        unusableKey = new NamespacedKey(nmlAbilities, "unusable");
        groundedKey = new NamespacedKey(nmlAbilities, "grounded");
    }

    public static ItemStack emptyStyleAbilityItem() {
        return ItemCreator.createItem(
                Material.LIGHT_BLUE_DYE,
                1,
                "§bEmpty Style Ability",
                List.of("§7An empty ability slot. Dunno why you'd put nothing here.")
        );
    }

    public static ItemStack cooldownItem() {
        return ItemCreator.createItem(
                Material.GRAY_DYE,
                1,
                "§7This ability is on cooldown!",
                List.of()
        );
    }

    public static void toggleAbility(ItemStack ability) {
        PersistentDataContainer pdc = ability.getItemMeta().getPersistentDataContainer();

        if (pdc.has(toggleKey)) {
            boolean inverseState = !pdc.get(toggleKey, PersistentDataType.BOOLEAN);

            pdc.set(toggleKey, PersistentDataType.BOOLEAN, inverseState);

            if (inverseState) { // turning it on
                ability.setType(Material.LIME_DYE);
            } else { // turning it off
                ability.setType(getOriginalItemMaterial(ability));
            }
        }
    }

    public static boolean meetsSkillRequirement(Skills skills, String skill, int levelRequirement) {
        int playerSkillLevel = 0;

        switch (skill.toLowerCase().replaceAll(" ", "")) {
            case "soldier" -> playerSkillLevel = skills.getSoldierLevel();
            case "assassin" -> playerSkillLevel = skills.getAssassinLevel();
            case "marauder" -> playerSkillLevel = skills.getMarauderLevel();
            case "cavalier" -> playerSkillLevel = skills.getCavalierLevel();
            case "martialartist" -> playerSkillLevel = skills.getMartialArtistLevel();
            case "shieldhero" -> playerSkillLevel = skills.getShieldHeroLevel();
            case "marksman" -> playerSkillLevel = skills.getMarksmanLevel();
            case "sorcerer" -> playerSkillLevel = skills.getSorcererLevel();
            case "primordial" -> playerSkillLevel = skills.getPrimordialLevel();
            case "hallowed" -> playerSkillLevel = skills.getHallowedLevel();
            case "annulled" -> playerSkillLevel = skills.getAnnulledLevel();
        }

        return playerSkillLevel >= levelRequirement;
    }

    public static boolean hasPrerequisites(ItemStack item) {
        Set<NamespacedKey> keys = item.getItemMeta().getPersistentDataContainer().getKeys();

        return keys.contains(groundedKey);
    }

    public static boolean meetsPrerequisites(Player player, ItemStack item) {
        PersistentDataContainer persistentDataContainer = item.getItemMeta().getPersistentDataContainer();
        ArrayList<NamespacedKey> prerequisiteKeys = new ArrayList<>();
        boolean met = false;

        if (persistentDataContainer.has(groundedKey)) {
            prerequisiteKeys.add(groundedKey);
        }

        for (NamespacedKey namespacedKey : prerequisiteKeys) {
            if (namespacedKey == groundedKey) {
                met = player.isOnGround();
            }

            if (!met) break;
        }

        return met;
    }

    public static boolean isAnAbility(ItemStack item) {
        if (item != null && item.hasItemMeta()) return item.getItemMeta().getPersistentDataContainer().has(abilityKey);

        return false;
    }

    public static boolean isToggleable(ItemStack item) {
        if (item != null && item.hasItemMeta()) return item.getItemMeta().getPersistentDataContainer().has(toggleKey);

        return false;
    }

    public static boolean getToggleState(ItemStack item) {
        if (isToggleable(item)) return item.getItemMeta().getPersistentDataContainer().get(toggleKey, PersistentDataType.BOOLEAN);

        return false;
    }

    public static int getCooldown(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

            if (pdc.has(cooldownKey)) {
                return pdc.get(cooldownKey, PersistentDataType.INTEGER);
            }
        }

        return -1;
    }

    public static int getRequiredEnergy(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

            if (pdc.has(energyKey)) {
                return pdc.get(energyKey, PersistentDataType.INTEGER);
            }
        }

        return -1;
    }

    public static Material getOriginalItemMaterial(ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        if (pdc.has(originalItemKey)) {
            String materialName = pdc.get(originalItemKey, PersistentDataType.STRING);
            if (materialName != null) {
                Material material = Material.matchMaterial(materialName);

                return material;
            }
        }

        return null;
    }

    public static NamespacedKey getAbilityKey() {
        return abilityKey;
    }

    public static NamespacedKey getExpertiseKey() {
        return expertiseKey;
    }

    public static NamespacedKey getCooldownKey() {
        return cooldownKey;
    }

    public static NamespacedKey getToggleKey() {
        return toggleKey;
    }

    public static NamespacedKey getOriginalItemKey() {
        return originalItemKey;
    }

    public static NamespacedKey getEnergyKey() {
        return energyKey;
    }

    public static NamespacedKey getUnusableKey() {
        return unusableKey;
    }

    public static NamespacedKey getGroundedKey() {
        return groundedKey;
    }
}