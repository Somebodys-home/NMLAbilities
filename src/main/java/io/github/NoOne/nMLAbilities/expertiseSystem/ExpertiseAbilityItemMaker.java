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

    public ExpertiseAbilityItemMaker(NMLAbilities nmlAbilities) {
        ExpertiseAbilityItemMaker.nmlAbilities = nmlAbilities;
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

    public static ItemStack makeExpertiseAbilityItem(String name, Map<String, Integer> skillRequirements, String description, List<AbilityPrerequisite> prerequisites,
                                                     boolean toggleable, String targeting, int range, int duration, int cooldown, int cost,
                                                     List<String> damage, List<String> effects, List<ItemType> weapons, Skills playerSkills) {

        String firstExpertiseRequirement = skillRequirements.entrySet().iterator().next().getKey();
        Material abilityMaterial = getExpertiseAbilityMaterial(firstExpertiseRequirement);
        String color = getExpertiseColor(firstExpertiseRequirement);
        List<String> lore = new ArrayList<>();

        /// lore
        // skill requirements
        for (Map.Entry<String, Integer> requirementEntry : skillRequirements.entrySet()) {
            String string = requirementEntry.getKey();

            if (Objects.equals(string, "shieldhero")) string = "Shield Hero";
            if (Objects.equals(string, "martialartist")) string = "Martial Artist";

            String requirementString = "§8Lv. " + requirementEntry.getValue() + " " + string.substring(0, 1).toUpperCase() + string.substring(1);

            if (AbilityItemManager.meetsSkillRequirement(playerSkills, string, requirementEntry.getValue())) {
                requirementString += " §a✔";
            } else {
                requirementString += " §c✖";
            }

            lore.add(requirementString);
        }

        lore.add("");

        // lore
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
                        lore.add("§e- " + getItemTypeString(weapon) + " & quiver");
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
        pdc.set(AbilityItemManager.getCooldownKey(), PersistentDataType.INTEGER, cooldown); // cooldown key
        pdc.set(AbilityItemManager.getEnergyKey(), PersistentDataType.INTEGER, cost); // energy cost key

        if (toggleable) { // toggleable ability keys
            pdc.set(AbilityItemManager.getToggleKey(), PersistentDataType.BOOLEAN, false);
            pdc.set(AbilityItemManager.getOriginalItemKey(), PersistentDataType.STRING, expertiseItem.getType().toString());
        }

        if (weapons != null) { // usable weapons keys
            if (weapons.isEmpty()) {
                pdc.set(new NamespacedKey(nmlAbilities, "all_weapons"), PersistentDataType.BOOLEAN, true);
            } else {
                for (ItemType weapon : weapons) {
                    pdc.set(new NamespacedKey(nmlAbilities, getItemTypeString(weapon)), PersistentDataType.BOOLEAN, true);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : skillRequirements.entrySet()) { // skill requirements keys
            String string = entry.getKey();

            if (Objects.equals(string, "shieldhero")) string = "Shield Hero";
            if (Objects.equals(string, "martialartist")) string = "Martial Artist";
            if (!AbilityItemManager.meetsSkillRequirement(playerSkills, string, entry.getValue()) && !pdc.has(AbilityItemManager.getUnusableKey())) {
                pdc.set(AbilityItemManager.getUnusableKey(), PersistentDataType.BOOLEAN, true);
            }

            pdc.set(new NamespacedKey(nmlAbilities, entry.getKey()), PersistentDataType.INTEGER, entry.getValue());
        }

        if (prerequisites != null) { // ability prerequisite keys
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

    private static Material getExpertiseAbilityMaterial(String expertise) {
        return switch (expertise) {
            case "soldier" -> Material.DIAMOND_SWORD;
            case "assassin" -> Material.BLACK_WOOL;
            case "marauder" -> Material.GOLDEN_AXE;
            case "cavalier" -> Material.MACE;
            case "martialartist" -> Material.RED_GLAZED_TERRACOTTA;
            case "shieldhero" -> Material.SHIELD;
            case "marksman" -> Material.BOW;
            case "sorcerer" -> Material.BOOK;
            case "primordial" -> Material.OAK_SAPLING;
            case "hallowed" -> Material.OXEYE_DAISY;
            case "annulled" -> Material.CRYING_OBSIDIAN;
            default -> null;
        };
    }

    private static String getExpertiseColor(String expertise) {
        return switch (expertise) {
            case "soldier" -> "§c";
            case "assassin" -> "§8";
            case "marauder", "martialartist" -> "§4";
            case "cavalier" -> "§9";
            case "shieldhero" -> "§3";
            case "marksman" -> "§a";
            case "sorcerer" -> "§6";
            case "primordial" -> "§2";
            case "hallowed" -> "§f";
            case "annulled" -> "§5";
            default -> "";
        };
    }
}
