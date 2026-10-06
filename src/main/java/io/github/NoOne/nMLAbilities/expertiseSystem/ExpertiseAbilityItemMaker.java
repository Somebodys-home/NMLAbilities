package io.github.NoOne.nMLAbilities.expertiseSystem;

import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityUse.AbilityPrerequisite;
import io.github.NoOne.nMLAbilities.expertiseSystem.annulled.AnnulledAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.assassin.AssassinAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.cavalier.CavalierAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.hallowed.HallowedAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.marauder.MarauderAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.marksman.MarksmanAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist.MartialArtistAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.primordial.PrimordialAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero.ShieldHeroAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.soldier.SoldierAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer.SorcererAbilityItems;
import io.github.NoOne.nMLItems.ItemCreator;
import io.github.NoOne.nMLItems.enums.ItemType;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.NoOne.nMLItems.enums.ItemType.*;


public class ExpertiseAbilityItemMaker {
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
                                                     boolean toggleable, String targeting, int range, double duration, int cooldown, int cost,
                                                     List<String> damage, List<String> effects, List<ItemType> weapons, Skills playerSkills) {

        boolean meetsRequirements = AbilityItemManager.meetsExpertiseRequirements(playerSkills, expertiseRequirements);
        Expertise firstExpertiseRequirement = expertiseRequirements.entrySet().iterator().next().getKey();
        Material abilityMaterial = Expertise.toMaterial(firstExpertiseRequirement);
        ArrayList<String> lore = new ArrayList<>();

        if (!meetsRequirements) {
            abilityMaterial = Material.BARRIER;
        }

        // skill requirements
        for (Map.Entry<Expertise, Integer> entry : expertiseRequirements.entrySet()) {
            String string = Expertise.toString(entry.getKey());
            String requirementString = "§8Lv. " + entry.getValue() + " " + string.substring(0, 1).toUpperCase() + string.substring(1);

            if (meetsRequirements) {
                requirementString += " §a✔";
            } else {
                requirementString += " §c✖";
            }

            lore.add(requirementString);
        }

        // description
        lore.add("");
        lore.addAll(linebreak(description));
        lore.add("");

        // prerequisites
        if (prerequisites != null) {
            lore.add("§c§nPrerequisites:");

            for (AbilityPrerequisite abilityPrerequisite : prerequisites) {
                lore.add("§c- " + AbilityPrerequisite.toString(abilityPrerequisite));
            }

            lore.add("");
        }

        // info
        if (toggleable) {
            lore.add("§fTarget: §9" + targeting + ", Toggle");
        } else {
            lore.add("§fTarget: §9" + targeting);
        }

        if (range == -1) {
            lore.add("§fRange: §aDepends");
        } else if (range != 0) {
            lore.add("§fRange: §a" + range + "m");
        }

        if (duration != 0) {
            if (duration == (int) duration) {
                lore.add("§fDuration: §3" + (int) duration + "s");
            } else {
                lore.add("§fDuration: §3" + duration + "s");
            }
        }

        lore.add("§fCooldown: §b" + cooldown + "s");
        lore.add("§fCost: §6" + cost + "⚡");

        if (damage != null) {
            lore.add("§b§l-----------Damage-----------");
            lore.addAll(damage);
        }

        if (effects != null) {
            lore.add("§b§l-----------Effects-----------");
            lore.addAll(effects);
        }

        lore.add("§b§l-----------Weapons----------");

        if (weapons == null) {
            lore.add("§e- (None)");
        } else {
            if (weapons.isEmpty()) {
                lore.add("§e- (Any)");
            } else {
                for (ItemType weapon : weapons) {
                    if (weapon == BOW) {
                        lore.add("§e- Bow & Quiver");
                    } else if (weapon == GLOVE) {
                        lore.add("§e- Gloves (both)");
                    } else if (weapon == STAFF) {
                        lore.add("§e- Staves");
                    } else {
                        lore.add("§e- " + ItemType.toString(weapon) + "s");
                    }
                }
            }
        }

        ItemStack expertiseItem = ItemCreator.createItem(
                abilityMaterial,
                Expertise.toChatColor(firstExpertiseRequirement) + "§l" + name,
                lore
        );

        expertiseItem.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        AbilityItemManager.setExpertiseKeys(expertiseItem, cooldown, cost, toggleable);
        AbilityItemManager.setExpertiseRequirements(expertiseItem, expertiseRequirements);
        AbilityItemManager.setWeaponsForAbility(expertiseItem, weapons);

        if (prerequisites != null) {
            AbilityItemManager.setPrerequisites(expertiseItem, prerequisites);
        }

        return expertiseItem;
    }

    // returns the associated item stack to its name
    public static ItemStack stringToAbilityItem(Skills skills, String name) {
        SoldierAbilityItems soldierAbilityItems = new SoldierAbilityItems(skills);

        return switch (name) {
            case "Slash" -> soldierAbilityItems.slash();

            // Assassin abilities
            case "Slash & Dash" -> AssassinAbilityItems.slashAndDash();

            // Marauder abilities
            case "Blade Tornado" -> MarauderAbilityItems.bladeTornado();
            case "Stomping Tantrum" -> MarauderAbilityItems.stompingTantrum();

            // Cavalier abilities
            case "Seismic Slam" -> CavalierAbilityItems.seismicSlam();

            // Martial Artist abilities
            case "Dropkick" -> MartialArtistAbilityItems.dropKick();

            // Shield Hero abilities
            case "Second Wind" -> ShieldHeroAbilityItems.secondWind();
            case "Shield Bash" -> ShieldHeroAbilityItems.shieldBash();
            case "Shield Punch" -> ShieldHeroAbilityItems.shieldPunch();

            // Marksman abilities
            case "Arrow Hailstorm" -> MarksmanAbilityItems.arrowHailstorm();
            case "Steady Aim" -> MarksmanAbilityItems.steadyAim();

            // Sorcerer abilities
            case "Magic Missile EX" -> SorcererAbilityItems.magicMissileEX();
            case "Dragon's Breath" -> SorcererAbilityItems.dragonsBreath();

            // Primordial abilities
            case "Chuck Rock" -> PrimordialAbilityItems.chuckRock();
            case "Pumpkin Bomb" -> PrimordialAbilityItems.pumpkinBomb();
            case "Air Ball" -> PrimordialAbilityItems.airBall();

            // Hallowed abilities
            case "Halo" -> HallowedAbilityItems.halo();

            // Annulled abilities
            case "Black Hole" -> AnnulledAbilityItems.blackHole();
            default -> new ItemStack(Material.BARRIER);
        };
    }

    private static ArrayList<String> linebreak(String string) {
        ArrayList<String> breaks = new ArrayList<>();
        int startingIndex = 0;

        while (startingIndex < string.length()) {
            // the ending index of the current string break is 36 characters or the length of the string
            int end = Math.min(string.length(), startingIndex + 36);

            // if there's more to the string, and it doesn't end on a space, move the end to the previous word
            if (end < string.length() && string.charAt(end) != ' ') {
                int lastSpace = string.lastIndexOf(' ', end);

                if (lastSpace > startingIndex) {
                    end = lastSpace;
                }
            }

            // actually getting that chunk of the string and making it gray
            String chunk = "§7" + string.substring(startingIndex, end).trim();

            // censoring swear words in abilities
            while (chunk.contains("&k")) {
                int censorIndex = chunk.indexOf("&k");
                String tempChunk = chunk.substring(censorIndex);
                String censorWord = tempChunk.substring(0, tempChunk.indexOf(" ")); // the idea is the censored word would be "&kblah ", for example
                String censoredWord = censorWord.replace("&", "§") + "§r§7";

                chunk = chunk.replace(censorWord, censoredWord);
            }

            breaks.add(chunk);
            startingIndex = end;

            while (startingIndex < string.length() && string.charAt(startingIndex) == ' ') {
                startingIndex++;
            }
        }

        return breaks;
    }
}
