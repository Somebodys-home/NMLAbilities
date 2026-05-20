package io.github.NoOne.nMLAbilities.expertiseSystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityPrerequisite;
import io.github.NoOne.nMLItems.ItemCreator;
import io.github.NoOne.nMLItems.enums.ItemType;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static io.github.NoOne.nMLItems.enums.ItemType.*;


public class ExpertiseAbilityItemMaker {
    private static NMLAbilities nmlAbilities;
    private static NamespacedKey anyWeaponKey;

    public ExpertiseAbilityItemMaker(NMLAbilities nmlAbilities) {
        ExpertiseAbilityItemMaker.nmlAbilities = nmlAbilities;
        anyWeaponKey = new NamespacedKey(nmlAbilities, "any_weapon");
    }

    public static ItemStack emptyExpertiseAbilityItem() {
        ItemStack expertise = ItemCreator.createItem(
                Material.MAGENTA_DYE,
                1,
                "§dEmpty Expertise Ability",
                List.of(
                        "§7An empty ability slot. Dunno why you'd put nothing here."
                )
        );

        ItemMeta meta = expertise.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(AbilityItemManager.getAbilityKey(), PersistentDataType.INTEGER, 1);
        expertise.setItemMeta(meta);
        return expertise;
    }

    public static ItemStack makeExpertiseAbilityItem(String name, Map<Expertise, Integer> expertiseRequirements, String description, List<AbilityPrerequisite> prerequisites,
                                                     boolean toggleable, String targeting, int range, int duration, int cooldown, int cost,
                                                     List<String> damage, List<String> effects, List<ItemType> weapons, Skills playerSkills) {

        Expertise firstExpertiseRequirement = expertiseRequirements.entrySet().iterator().next().getKey();
        Material abilityMaterial = getExpertiseAbilityMaterial(firstExpertiseRequirement);
        String color = getExpertiseColor(firstExpertiseRequirement);
        List<String> lore = new ArrayList<>();

        // skill requirements
        for (Map.Entry<Expertise, Integer> entry : expertiseRequirements.entrySet()) {
            String string = Expertise.getString(entry.getKey());
            String requirementString = "§8Lv. " + entry.getValue() + " " + string.substring(0, 1).toUpperCase() + string.substring(1);

            if (AbilityItemManager.meetsExpertiseRequirement(playerSkills, entry.getKey(), entry.getValue())) {
                requirementString += " §a✔";
            } else {
                requirementString += " §c✖";
            }

            lore.add(requirementString);
        }

        lore.add("");

        // description
        for (String line : linebreak(description, 33)) {
            lore.add("§7" + line);
        }

        lore.add("");

        // prerequisites
        if (prerequisites != null) {
            lore.add("§c§nPrerequisites:");

            for (AbilityPrerequisite abilityPrerequisite : prerequisites) {
                switch (abilityPrerequisite) {
                    case GROUNDED -> lore.add("§c- Grounded");
                }
            }

            lore.add("");
        }

        // info
        if (toggleable) {
            lore.add("§fTarget: §9" + targeting + ", Toggle");
        } else {
            lore.add("§fTarget: §9" + targeting);
        }

        if (range != 0) lore.add("§fRange: §a" + range + "m");
        if (duration != 0) lore.add("§fDuration: §3" + duration + "s");

        lore.add("§fCooldown: §b" + cooldown + "s");
        lore.add("§fCost: §6" + cost + "⚡");

        // damage stats
        if (damage != null) {
            lore.add("§b§l----------Damage----------");
            lore.addAll(damage);
        }

        if (effects != null) {
            lore.add("§b§l----------Effects----------");
            lore.addAll(effects);
        }

        lore.add("§b§l----------Weapons---------");
        if (weapons == null) {
            lore.add("§e- (None)");
        } else {
            if (weapons.isEmpty()) {
                lore.add("§e- (Any)");
            } else {
                for (ItemType weapon : weapons) {
                    if (weapon == BOW) {
                        lore.add("§e- " + getItemTypeString(weapon) + " & Quiver");
                    } else if (weapon == GLOVE) {
                        lore.add("§e- " + getItemTypeString(weapon) + "s (both)");
                    } else if (weapon == STAFF) {
                        lore.add("§e- Staves");
                    } else {
                        lore.add("§e- " + getItemTypeString(weapon) + "s");
                    }
                }
            }
        }

        ItemStack expertiseItem = ItemCreator.createItem(
                abilityMaterial,
                1,
                color + "§l" + name,
                lore
        );

        ItemMeta meta = expertiseItem.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        /// setting keys
        pdc.set(AbilityItemManager.getAbilityKey(), PersistentDataType.INTEGER, 1); // ability key
        pdc.set(AbilityItemManager.getExpertiseKey(), PersistentDataType.INTEGER, 1); // expertise ability key
        pdc.set(AbilityItemManager.getCooldownKey(), PersistentDataType.INTEGER, cooldown); // cooldownSystem key
        pdc.set(AbilityItemManager.getEnergyKey(), PersistentDataType.INTEGER, cost); // energy cost key

        // toggleable ability keys
        if (toggleable) {
            pdc.set(AbilityItemManager.getToggleKey(), PersistentDataType.BOOLEAN, false);
            pdc.set(AbilityItemManager.getOriginalItemKey(), PersistentDataType.STRING, expertiseItem.getType().toString());
        }

        // usable weapons keys
        if (weapons != null) {
            if (weapons.isEmpty()) {
                pdc.set(anyWeaponKey, PersistentDataType.BOOLEAN, true);
            } else {
                for (ItemType weapon : weapons) {
                    pdc.set(new NamespacedKey(nmlAbilities, getItemTypeString(weapon)), PersistentDataType.BOOLEAN, true);
                }
            }
        }

        // expertise requirements keys
        for (Map.Entry<Expertise, Integer> entry : expertiseRequirements.entrySet()) {
            if (!AbilityItemManager.meetsExpertiseRequirement(playerSkills, entry.getKey(), entry.getValue()) && !pdc.has(AbilityItemManager.getUnusableKey())) {
                pdc.set(AbilityItemManager.getUnusableKey(), PersistentDataType.BOOLEAN, true);
            }

            pdc.set(new NamespacedKey(nmlAbilities, Expertise.getString(entry.getKey())), PersistentDataType.INTEGER, entry.getValue());
        }

        // ability prerequisite keys
        if (prerequisites != null) {
            for (AbilityPrerequisite abilityPrerequisite : prerequisites) {
                switch (abilityPrerequisite) {
                    case GROUNDED -> pdc.set(AbilityItemManager.getGroundedKey(), PersistentDataType.BOOLEAN, true);
                }
            }
        }

        expertiseItem.setItemMeta(meta);
        return expertiseItem;
    }

    private static List<String> linebreak(String string, int size) {
        List<String> breaks = new ArrayList<>();
        int i = 0;

        while (i < string.length()) {
            int end = Math.min(string.length(), i + size);

            if (end < string.length() && string.charAt(end) != ' ') {
                int lastSpace = string.lastIndexOf(' ', end);
                if (lastSpace > i) {
                    end = lastSpace; // move break point to last space
                }
            }

            String chunk = string.substring(i, end).trim();
            if (!chunk.isEmpty()) {
                breaks.add(chunk);
            }

            i = end;
            while (i < string.length() && string.charAt(i) == ' ') {
                i++;
            }
        }

        return breaks;
    }

    private static Material getExpertiseAbilityMaterial(Expertise expertise) {
        return switch (expertise) {
            case SOLDIER -> Material.DIAMOND_SWORD;
            case ASSASSIN -> Material.BLACK_WOOL;
            case MARAUDER -> Material.GOLDEN_AXE;
            case CAVALIER -> Material.MACE;
            case MARTIAL_ARTIST -> Material.RED_GLAZED_TERRACOTTA;
            case SHIELD_HERO -> Material.SHIELD;
            case MARKSMAN -> Material.TARGET;
            case SORCERER -> Material.BOOK;
            case PRIMORDIAL -> Material.OAK_SAPLING;
            case HALLOWED -> Material.OXEYE_DAISY;
            case ANNULLED -> Material.CRYING_OBSIDIAN;
        };
    }

    private static String getExpertiseColor(Expertise expertise) {
        return switch (expertise) {
            case SOLDIER -> "§c";
            case ASSASSIN -> "§8";
            case MARAUDER, MARTIAL_ARTIST -> "§4";
            case CAVALIER -> "§9";
            case SHIELD_HERO -> "§3";
            case MARKSMAN -> "§a";
            case SORCERER -> "§6";
            case PRIMORDIAL -> "§2";
            case HALLOWED -> "§f";
            case ANNULLED -> "§5";
            default -> "";
        };
    }

    public static NamespacedKey getAnyWeaponKey() {
        return anyWeaponKey;
    }
}
