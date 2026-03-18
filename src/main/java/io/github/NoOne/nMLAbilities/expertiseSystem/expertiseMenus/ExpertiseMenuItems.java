package io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus;

import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ExpertiseMenuItems {
    private final Skills skills;

    public ExpertiseMenuItems(Skills skills) {
        this.skills = skills;
    }

    public ItemStack soldier() {
        double percent = skills.getSoldierExp() / skills.getExp2LvlUpSoldier() * 100.0;
        ItemStack soldier = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = soldier.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName("§fLv. " + skills.getSoldierLevel() +  " §c§lSoldier");
        lore.add("§8" + skills.getCombatExp() + " / " + skills.getExp2LvlUpCombat() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7Me when military proletarianism is kinda based actually");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        soldier.setItemMeta(meta);

        return soldier;
    }

    public ItemStack assassin() {
        double percent = skills.getAssassinExp() / skills.getExp2LvlUpAssassin() * 100.0;
        ItemStack assassin = new ItemStack(Material.BLACK_WOOL);
        ItemMeta meta = assassin.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.setDisplayName("§fLv. " + skills.getAssassinLevel() +  " §0§lAssassin");
        lore.add("§8" + skills.getAssassinExp() + " / " + skills.getExp2LvlUpAssassin() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7\"Hey what do you got there?\"");
        lore.add( "§7\"A knife!\"");
        lore.add("§7\"NO!\"");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        assassin.setItemMeta(meta);

        return assassin;
    }

    public ItemStack marauder() {
        double percent = skills.getMarauderExp() / skills.getExp2LvlUpMarauder() * 100.0;
        ItemStack marauder = new ItemStack(Material.IRON_AXE);
        ItemMeta meta = marauder.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName("§fLv. " + skills.getMarauderLevel() +  " §4§lMarauder");
        lore.add("§8" + skills.getMarauderExp() + " / " + skills.getExp2LvlUpMarauder() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7Reject modernity, EMBRACE BARBARITY!");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        marauder.setItemMeta(meta);

        return marauder;
    }

    public ItemStack cavalier() {
        double percent = skills.getCavalierExp() / skills.getExp2LvlUpCavalier() * 100.0;
        ItemStack cavalier = new ItemStack(Material.MACE);
        ItemMeta meta = cavalier.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName("§fLv. " + skills.getCavalierLevel() +  " §1§lCavalier");
        lore.add("§8" + skills.getCavalierExp() + " / " + skills.getExp2LvlUpCavalier() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7Unfortunately, this has nothing to do with horses.");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        cavalier.setItemMeta(meta);

        return cavalier;
    }

    public ItemStack martialArtist() {
        double percent = skills.getMartialArtistExp() / skills.getExp2LvlUpMartialArtist() * 100.0;
        ItemStack martialArtist = new ItemStack(Material.RED_GLAZED_TERRACOTTA);
        ItemMeta meta = martialArtist.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.setDisplayName("§fLv. " + skills.getMartialArtistLevel() +  " §4§lMartial Artist");
        lore.add("§8" + skills.getMartialArtistExp() + " / " + skills.getExp2LvlUpCombat() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7Beating up homeless people in the back alley of an Arby's™ pro max");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        martialArtist.setItemMeta(meta);

        return martialArtist;
    }

    public ItemStack shieldHero() {
        double percent = skills.getShieldHeroExp() / skills.getExp2LvlUpShieldHero() * 100.0;
        ItemStack shieldHero = new ItemStack(Material.SHIELD);
        ItemMeta meta = shieldHero.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.setDisplayName("§fLv. " + skills.getShieldHeroLevel() +  " §3§lShield Hero");
        lore.add("§8" + skills.getShieldHeroExp() + " / " + skills.getExp2LvlUpShieldHero() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7Don't worry, you won't be hate crimed...hopefully.");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        shieldHero.setItemMeta(meta);

        return shieldHero;
    }

    public ItemStack marksman() {
        double percent = skills.getMarksmanExp() / skills.getExp2LvlUpMarksman() * 100.0;
        ItemStack marksman = new ItemStack(Material.BOW);
        ItemMeta meta = marksman.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.setDisplayName("§fLv. " + skills.getMarksmanLevel() +  " §a§lMarksman");
        lore.add("§8" + skills.getMarksmanExp() + " / " + skills.getExp2LvlUpMarksman() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7This is the closest you're getting to a gun.");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        marksman.setItemMeta(meta);

        return marksman;
    }

    public ItemStack sorcerer() {
        double percent = skills.getSorcererExp() / skills.getExp2LvlUpSorcerer() * 100.0;
        ItemStack sorcerer = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = sorcerer.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.setDisplayName("§fLv. " + skills.getSorcererLevel() +  " §6§lSorcerer");
        lore.add("§8" + skills.getSorcererExp() + " / " + skills.getExp2LvlUpSorcerer() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7There's no I in team, but there's 6 Is in");
        lore.add("§7§o\"F§kuck§r§7§o it, I don't care how big the room is, I cast fireball.\"");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        sorcerer.setItemMeta(meta);

        return sorcerer;
    }

    public ItemStack primordial() {
        double percent = skills.getPrimordialExp() / skills.getExp2LvlUpPrimordial() * 100.0;
        ItemStack primordial = new ItemStack(Material.OAK_SAPLING);
        ItemMeta meta = primordial.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.setDisplayName("§fLv. " + skills.getPrimordialLevel() +  " §2§lPrimordial");
        lore.add("§8" + skills.getPrimordialExp() + " / " + skills.getExp2LvlUpPrimordial() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7F§kuck§r§7in hippie.");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        primordial.setItemMeta(meta);

        return primordial;
    }

    public ItemStack hallowed() {
        double percent = skills.getHallowedExp() / skills.getExp2LvlUpHallowed() * 100.0;
        ItemStack hallowed = new ItemStack(Material.OXEYE_DAISY);
        ItemMeta meta = hallowed.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.setDisplayName("§fLv. " + skills.getHallowedLevel() +  " §f§lHallowed");
        lore.add("§8" + skills.getHallowedExp() + " / " + skills.getExp2LvlUpHallowed() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7Please just be normal.");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        hallowed.setItemMeta(meta);

        return hallowed;
    }

    public ItemStack annulled() {
        double percent = skills.getAnnulledExp() / skills.getExp2LvlUpAnnulled() * 100.0;
        ItemStack annulled = new ItemStack(Material.CRYING_OBSIDIAN);
        ItemMeta meta = annulled.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        meta.setDisplayName("§fLv. " + skills.getAnnulledLevel() +  " §5§lAnnulled");
        lore.add("§8" + skills.getAnnulledExp() + " / " + skills.getExp2LvlUpAnnulled() + " exp (" + percent + "%)");
        lore.add("");
        lore.add("§7Welcome home.");
        lore.add("");
        lore.add("§bCLICK TO SELECT!");
        meta.setLore(lore);
        annulled.setItemMeta(meta);

        return annulled;
    }
}
