package io.github.NoOne.nMLAbilities.abilitySystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityUse.AbilityPrerequisite;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLItems.ItemCreator;
import io.github.NoOne.nMLItems.ItemSystem;
import io.github.NoOne.nMLItems.enums.ItemType;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class AbilityItemManager {
    private static NMLAbilities nmlAbilities = NMLAbilities.getInstance();
    private static NamespacedKey abilityKey = new NamespacedKey(nmlAbilities, "ability");
    private static NamespacedKey expertiseRequirementsKey = new NamespacedKey(nmlAbilities, "expertiseRequirements");
    private static NamespacedKey cooldownKey = new NamespacedKey(nmlAbilities, "cooldown");
    private static NamespacedKey toggleKey = new NamespacedKey(nmlAbilities, "toggle");
    private static NamespacedKey originalItemKey = new NamespacedKey(nmlAbilities, "originalItem");
    private static NamespacedKey energyKey = new NamespacedKey(nmlAbilities, "energy");
    private static NamespacedKey prerequisitesKey = new NamespacedKey(nmlAbilities, "prerequisites");
    private static NamespacedKey weaponsKey = new NamespacedKey(nmlAbilities, "weapons");

    public static ItemStack emptyStyleAbilityItem() {
        return makeAbilityItem(ItemCreator.createItem(
                Material.LIGHT_BLUE_DYE,
                "§bEmpty Style Ability",
                List.of("§7An empty ability slot. Dunno why you'd put nothing here.")
        ));
    }

    public static ItemStack cooldownItem() {
        return makeAbilityItem(ItemCreator.createItem(Material.GRAY_DYE, "§7This ability is on cooldown!"));
    }

    public static ItemStack makeAbilityItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

        pdc.set(abilityKey, PersistentDataType.BOOLEAN, true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void setExpertiseKeys(ItemStack ability, int cooldown, int energyCost, boolean toggleable) {
        ItemMeta itemMeta = ability.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

        pdc.set(abilityKey, PersistentDataType.BOOLEAN, true);
        pdc.set(cooldownKey, PersistentDataType.INTEGER, cooldown);
        pdc.set(energyKey, PersistentDataType.INTEGER, energyCost);

        if (toggleable) {
            pdc.set(toggleKey, PersistentDataType.BOOLEAN, false);
            pdc.set(originalItemKey, PersistentDataType.STRING, ability.getType().toString());
        }

        ability.setItemMeta(itemMeta);
    }

    public static void setExpertiseRequirements(ItemStack ability, Map<Expertise, Integer> requirements) {
        ItemMeta itemMeta = ability.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        String expertiseString = "";

        for (Map.Entry<Expertise, Integer> entry : requirements.entrySet()) {
            expertiseString += Expertise.toString(entry.getKey()) + "-" + entry.getValue() + "/";
        }

        pdc.set(expertiseRequirementsKey, PersistentDataType.STRING, expertiseString);
        ability.setItemMeta(itemMeta);
    }

    public static void setPrerequisites(ItemStack ability, List<AbilityPrerequisite> prerequisites) {
        ItemMeta itemMeta = ability.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        String prerequisitesString = "";

        for (AbilityPrerequisite abilityPrerequisite : prerequisites) {
            prerequisitesString += AbilityPrerequisite.toString(abilityPrerequisite) + "/";
        }

        pdc.set(prerequisitesKey, PersistentDataType.STRING, prerequisitesString);
        ability.setItemMeta(itemMeta);
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

    public static void setWeaponsForAbility(ItemStack ability, List<ItemType> weapons) {
        ItemMeta itemMeta = ability.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        String weaponsString = "";

        if (weapons == null) {
            weaponsString = "none";
        } else {
            if (weapons.isEmpty()) {
                weaponsString = "any";
            } else {
                for (ItemType itemType : weapons) {
                    weaponsString += ItemType.toString(itemType) + "/";
                }
            }
        }

        pdc.set(weaponsKey, PersistentDataType.STRING, weaponsString);
        ability.setItemMeta(itemMeta);
    }

    public static boolean meetsExpertiseRequirements(Skills skills, Map<Expertise, Integer> requirements) {
        for (Map.Entry<Expertise, Integer> entry : requirements.entrySet()) {
            int playerSkillLevel = switch (entry.getKey()) {
                case SOLDIER -> skills.getSoldierLevel();
                case ASSASSIN -> skills.getAssassinLevel();
                case MARAUDER -> skills.getMarauderLevel();
                case CAVALIER -> skills.getCavalierLevel();
                case MARTIAL_ARTIST -> skills.getMartialArtistLevel();
                case SHIELD_HERO -> skills.getShieldHeroLevel();
                case MARKSMAN -> skills.getMarksmanLevel();
                case SORCERER -> skills.getSorcererLevel();
                case PRIMORDIAL -> skills.getPrimordialLevel();
                case HALLOWED -> skills.getHallowedLevel();
                case ANNULLED -> skills.getAnnulledLevel();
            };

            if (playerSkillLevel < entry.getValue()) {
                return false;
            }
        }

        return true;
    }

    public static boolean meetsExpertiseRequirements(Skills skills, ItemStack ability) {
        PersistentDataContainer pdc = ability.getItemMeta().getPersistentDataContainer();
        HashMap<Expertise, Integer> requirements = new HashMap<>(){{
            if (pdc.has(expertiseRequirementsKey, PersistentDataType.STRING)) {
                String requirements = pdc.get(expertiseRequirementsKey, PersistentDataType.STRING);

                for (String requirement : requirements.split("/")) {
                    String[] splits = requirement.split("-");

                    put(Expertise.fromString(splits[0]), Integer.parseInt(splits[1]));
                }
            }
        }};

        return meetsExpertiseRequirements(skills, requirements);
    }

    public static boolean meetsExpertiseRequirement(Skills skills, Expertise expertise, int level) {
        int playerSkillLevel = switch (expertise) {
            case SOLDIER -> skills.getSoldierLevel();
            case ASSASSIN -> skills.getAssassinLevel();
            case MARAUDER -> skills.getMarauderLevel();
            case CAVALIER -> skills.getCavalierLevel();
            case MARTIAL_ARTIST -> skills.getMartialArtistLevel();
            case SHIELD_HERO -> skills.getShieldHeroLevel();
            case MARKSMAN -> skills.getMarksmanLevel();
            case SORCERER -> skills.getSorcererLevel();
            case PRIMORDIAL -> skills.getPrimordialLevel();
            case HALLOWED -> skills.getHallowedLevel();
            case ANNULLED -> skills.getAnnulledLevel();
        };

        return playerSkillLevel >= level;
    }

    public static boolean meetsPrerequisites(Player player, ItemStack ability) {
        ArrayList<AbilityPrerequisite> prerequisites = getPrerequisitesForAbility(ability);

        if (!prerequisites.isEmpty()) { // if there's no prerequisites, then the player meets them by default
            for (AbilityPrerequisite abilityPrerequisite : prerequisites) {
                switch (abilityPrerequisite) {
                    case GROUNDED -> {
                        if (!player.isOnGround()) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public static boolean isHoldingWeaponForAbility(Player player, ItemStack abilityItem) {
        ArrayList<ItemType> requiredWeapons = getWeaponsForAbility(abilityItem);
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offhand = player.getInventory().getItemInOffHand();

        if (requiredWeapons == null || requiredWeapons.isEmpty()) { // if an ability doesnt use weapons
            return true;
        } else {
            for (ItemType itemType : requiredWeapons) { // iterates thru all the weapon types, returning true if the player matches one of em
                if (itemType == GLOVE) {
                    if (ItemSystem.isItemType(mainHand, GLOVE) && ItemSystem.isItemType(offhand, GLOVE)) {
                        return true;
                    }
                } else if (itemType == BOW) {
                    if (ItemSystem.isItemType(mainHand, BOW) && ItemSystem.isItemType(offhand, QUIVER)) {
                        return true;
                    }
                } else if (itemType == SHIELD) {
                    if (ItemSystem.isItemType(mainHand, SHIELD) || ItemSystem.isItemType(offhand, SHIELD)) {
                        return true;
                    }
                } else {
                    if (ItemSystem.isItemType(mainHand, itemType)) {
                        return true;
                    }
                }
            }
        }

        return false;
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

    public static String getRawAbilityName(ItemStack item) {
        return ChatColor.stripColor(item.getItemMeta().getDisplayName());
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
            if (persistentDataContainer.has(new NamespacedKey(nmlAbilities, Expertise.toString(expertise)))) {
                expertises.add(expertise);
            }
        }

        return expertises;
    }

    public static ArrayList<AbilityPrerequisite> getPrerequisitesForAbility(ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        return new ArrayList<>(){{
            if (pdc.has(prerequisitesKey, PersistentDataType.STRING)) {
                for (String string : pdc.get(prerequisitesKey, PersistentDataType.STRING).split("/")) {
                    add(AbilityPrerequisite.fromString(string));
                }
            }
        }};
    }

    public static ArrayList<ItemType> getWeaponsForAbility(ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        if (pdc.has(weaponsKey, PersistentDataType.STRING)) {
            String weaponString = pdc.get(weaponsKey, PersistentDataType.STRING);

            if (weaponString.equals("none")) {
                return new ArrayList<>();
            } else if (weaponString.equals("any")) {
                return new ArrayList<>(List.of(SWORD, DAGGER, AXE, HAMMER, SPEAR, GLOVE, BOW, WAND, STAFF, CATALYST, SHIELD));
            } else {
                ArrayList<ItemType> weapons = new ArrayList<>(){{
                    for (String string : weaponString.split("/")) {
                        add(ItemType.fromString(string));
                    }
                }};

                return weapons;
            }
        }

        return null;
    }
}