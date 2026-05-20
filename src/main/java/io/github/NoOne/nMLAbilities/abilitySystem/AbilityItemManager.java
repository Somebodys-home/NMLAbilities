package io.github.NoOne.nMLAbilities.abilitySystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLItems.ItemCreator;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AbilityItemManager {
    private static NMLAbilities nmlAbilities;
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
        this.nmlAbilities = nmlAbilities;
        abilityKey = new NamespacedKey(nmlAbilities, "ability");
        expertiseKey = new NamespacedKey(nmlAbilities, "expertise");
        cooldownKey = new NamespacedKey(nmlAbilities, "cooldownSystem");
        toggleKey = new NamespacedKey(nmlAbilities, "toggle");
        originalItemKey = new NamespacedKey(nmlAbilities, "originalItem");
        energyKey = new NamespacedKey(nmlAbilities, "energy");
        unusableKey = new NamespacedKey(nmlAbilities, "unusable");
        groundedKey = new NamespacedKey(nmlAbilities, "grounded");
    }

    public static ItemStack emptyStyleAbilityItem() {
        ItemStack emptyStyle =  ItemCreator.createItem(
                Material.LIGHT_BLUE_DYE,
                1,
                "§bEmpty Style Ability",
                List.of("§7An empty ability slot. Dunno why you'd put nothing here.")
        );
        ItemMeta itemMeta = emptyStyle.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

        pdc.set(abilityKey, PersistentDataType.INTEGER, 0);
        emptyStyle.setItemMeta(itemMeta);
        return emptyStyle;
    }

    public static ItemStack cooldownItem() {
        ItemStack cooldown  =  ItemCreator.createItem(
                Material.GRAY_DYE,
                1,
                "§7This ability is on cooldown!",
                List.of()
        );
        ItemMeta itemMeta = cooldown.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

        pdc.set(abilityKey, PersistentDataType.INTEGER, 0);
        cooldown.setItemMeta(itemMeta);
        return cooldown;
    }

    public static boolean meetsExpertiseRequirement(Skills skills, Expertise expertise, int levelRequirement) {
        int playerSkillLevel = 0;

        switch (expertise) {
            case SOLDIER -> playerSkillLevel = skills.getSoldierLevel();
            case ASSASSIN -> playerSkillLevel = skills.getAssassinLevel();
            case MARAUDER -> playerSkillLevel = skills.getMarauderLevel();
            case CAVALIER -> playerSkillLevel = skills.getCavalierLevel();
            case MARTIAL_ARTIST -> playerSkillLevel = skills.getMartialArtistLevel();
            case SHIELD_HERO -> playerSkillLevel = skills.getShieldHeroLevel();
            case MARKSMAN -> playerSkillLevel = skills.getMarksmanLevel();
            case SORCERER -> playerSkillLevel = skills.getSorcererLevel();
            case PRIMORDIAL -> playerSkillLevel = skills.getPrimordialLevel();
            case HALLOWED -> playerSkillLevel = skills.getHallowedLevel();
            case ANNULLED -> playerSkillLevel = skills.getAnnulledLevel();
        }

        return playerSkillLevel >= levelRequirement;
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

    public static void setToggleState(ItemStack ability, boolean toggle) {
        if (ability == null) return;

        ItemMeta meta = ability.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if (pdc.has(toggleKey)) {
            pdc.set(toggleKey, PersistentDataType.BOOLEAN, toggle);

            if (toggle) { // turning it on
                ability.setType(Material.LIME_DYE);
            } else { // turning it off
                ability.setType(getOriginalItemMaterial(ability));
            }

            ability.setItemMeta(meta);
        }
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
        if (item != null) {
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

            if (pdc.has(originalItemKey)) {
                String materialName = pdc.get(originalItemKey, PersistentDataType.STRING);

                if (materialName != null) {
                    return Material.matchMaterial(materialName);
                }
            }
        }

        return null;
    }

    public static ArrayList<Expertise> getExpertisesForAbility(ItemStack item) {
        ArrayList<Expertise> expertises = new ArrayList<>();
        PersistentDataContainer persistentDataContainer = item.getItemMeta().getPersistentDataContainer();

        for (Expertise expertise : Expertise.values()) {
            if (persistentDataContainer.has(new NamespacedKey(nmlAbilities, Expertise.getString(expertise)))) {
                expertises.add(expertise);
            }
        }

        return expertises;
    }

    public static String getRawAbilityName(ItemStack item) {
        return item.getItemMeta().getDisplayName().replaceAll("§.", "");
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
        if (isToggleable(item)) return Boolean.TRUE.equals(item.getItemMeta().getPersistentDataContainer().get(toggleKey, PersistentDataType.BOOLEAN));

        return false;
    }

    public static boolean hasPrerequisites(ItemStack item) {
        Set<NamespacedKey> keys = item.getItemMeta().getPersistentDataContainer().getKeys();

        return keys.contains(groundedKey);
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