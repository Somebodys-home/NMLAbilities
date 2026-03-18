package io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus;

import io.github.NoOne.nMLItems.ItemCreator;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ExpertiseMenuItems {
    private final Skills skills;

    public ExpertiseMenuItems(Skills skills) {
        this.skills = skills;
    }

    public ItemStack soldier() {
        double percent = skills.getSoldierExp() / skills.getExp2LvlUpSoldier() * 100.0;
        ItemStack soldier = ItemCreator.createItem(
                Material.IRON_SWORD,
                1,
                "§fLv. " + skills.getSoldierLevel() +  " §c§lSoldier",
                List.of(
                        "§8" + skills.getSoldierExp() + " / " + skills.getExp2LvlUpSoldier() + " exp (" + percent + "%)",
                        "",
                        "§7Me when military proletarianism is kinda based actually",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
        ItemMeta itemMeta = soldier.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        soldier.setItemMeta(itemMeta);
        return soldier;
    }

    public ItemStack assassin() {
        double percent = skills.getAssassinExp() / skills.getExp2LvlUpAssassin() * 100.0;

        return ItemCreator.createItem(
                Material.BLACK_WOOL,
                1,
                "§fLv. " + skills.getAssassinLevel() +  " §0§lAssassin",
                List.of(
                        "§8" + skills.getAssassinExp() + " / " + skills.getExp2LvlUpAssassin() + " exp (" + percent + "%)",
                        "",
                        "§7\"Hey what do you got there?\"",
                        "§7\"A knife!\"",
                        "§7\"NO!\"",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
    }

    public ItemStack marauder() {
        double percent = skills.getMarauderExp() / skills.getExp2LvlUpMarauder() * 100.0;
        ItemStack marauder = ItemCreator.createItem(
                Material.IRON_AXE,
                1,
                "§fLv. " + skills.getMarauderLevel() +  " §4§lMarauder",
                List.of(
                        "§8" + skills.getMarauderExp() + " / " + skills.getExp2LvlUpMarauder() + " exp (" + percent + "%)",
                        "",
                        "§7Reject modernity, EMBRACE BARBARITY!",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
        ItemMeta itemMeta = marauder.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        marauder.setItemMeta(itemMeta);
        return marauder;
    }

    public ItemStack cavalier() {
        double percent = skills.getCavalierExp() / skills.getExp2LvlUpCavalier() * 100.0;
        ItemStack cavalier = ItemCreator.createItem(
                Material.MACE,
                1,
                "§fLv. " + skills.getCavalierLevel() +  " §1§lCavalier",
                List.of(
                        "§8" + skills.getCavalierExp() + " / " + skills.getExp2LvlUpCavalier() + " exp (" + percent + "%)",
                        "",
                        "§7Unfortunately, this has nothing to do with horses.",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
        ItemMeta itemMeta = cavalier.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        cavalier.setItemMeta(itemMeta);
        return cavalier;
    }

    public ItemStack martialArtist() {
        double percent = skills.getMartialArtistExp() / skills.getExp2LvlUpMartialArtist() * 100.0;

        return ItemCreator.createItem(
                Material.RED_GLAZED_TERRACOTTA,
                1,
                "§fLv. " + skills.getMartialArtistLevel() +  " §4§lMartial Artist",
                List.of(
                        "§8" + skills.getMartialArtistExp() + " / " + skills.getExp2LvlUpCombat() + " exp (" + percent + "%)",
                        "",
                        "§7Beating up homeless people in the back alley",
                        "§7of an Arby's™ pro max",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
    }

    public ItemStack shieldHero() {
        double percent = skills.getShieldHeroExp() / skills.getExp2LvlUpShieldHero() * 100.0;

        return ItemCreator.createItem(
                Material.SHIELD,
                1,
                "§fLv. " + skills.getShieldHeroLevel() +  " §3§lShield Hero",
                List.of(
                        "§8" + skills.getShieldHeroExp() + " / " + skills.getExp2LvlUpShieldHero() + " exp (" + percent + "%)",
                        "",
                        "§7Don't worry, you won't be hate crimed...hopefully.",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
    }

    public ItemStack marksman() {
        double percent = skills.getMarksmanExp() / skills.getExp2LvlUpMarksman() * 100.0;

        return ItemCreator.createItem(
                Material.BOW,
                1,
                "§fLv. " + skills.getMarksmanLevel() +  " §a§lMarksman",
                List.of(
                        "§8" + skills.getMarksmanExp() + " / " + skills.getExp2LvlUpMarksman() + " exp (" + percent + "%)",
                        "",
                        "§7This is the closest you're getting to a gun.",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
    }

    public ItemStack sorcerer() {
        double percent = skills.getSorcererExp() / skills.getExp2LvlUpSorcerer() * 100.0;

        return ItemCreator.createItem(
                Material.ENCHANTED_BOOK,
                1,
                "§fLv. " + skills.getSorcererLevel() +  " §6§lSorcerer",
                List.of(
                        "§8" + skills.getSorcererExp() + " / " + skills.getExp2LvlUpSorcerer() + " exp (" + percent + "%)",
                        "",
                        "§7There's no I in team, but there's 6 Is in",
                        "§7§o\"F§kuck§r§7§o it, I don't care how big the room is, I cast fireball.\"",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
    }

    public ItemStack primordial() {
        double percent = skills.getPrimordialExp() / skills.getExp2LvlUpPrimordial() * 100.0;

        return ItemCreator.createItem(
                Material.OAK_SAPLING,
                1,
                "§fLv. " + skills.getPrimordialLevel() +  " §2§lPrimordial",
                List.of(
                        "§8" + skills.getPrimordialExp() + " / " + skills.getExp2LvlUpPrimordial() + " exp (" + percent + "%)",
                        "",
                        "§7F§kuck§r§7in hippie.",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
    }

    public ItemStack hallowed() {
        double percent = skills.getHallowedExp() / skills.getExp2LvlUpHallowed() * 100.0;

        return ItemCreator.createItem(
                Material.OXEYE_DAISY,
                1,
                "§fLv. " + skills.getHallowedLevel() +  " §f§lHallowed",
                List.of(
                        "§8" + skills.getHallowedExp() + " / " + skills.getExp2LvlUpHallowed() + " exp (" + percent + "%)",
                        "",
                        "§7Please just be normal.",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
    }

    public ItemStack annulled() {
        double percent = skills.getAnnulledExp() / skills.getExp2LvlUpAnnulled() * 100.0;

        return ItemCreator.createItem(
                Material.CRYING_OBSIDIAN,
                1,
                "§fLv. " + skills.getAnnulledLevel() +  " §5§lAnnulled",
                List.of(
                        "§8" + skills.getAnnulledExp() + " / " + skills.getExp2LvlUpAnnulled() + " exp (" + percent + "%)",
                        "",
                        "§7Welcome home.",
                        "",
                        "§bCLICK TO SELECT!"
                )
        );
    }
}
