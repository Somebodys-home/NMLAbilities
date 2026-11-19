package io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus;

import io.github.NoOne.menuSystem.Menu;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.annulled.AnnulledMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.assassin.AssassinMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.cavalier.CavalierMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.hallowed.HallowedMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.marauder.MarauderMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.marksman.MarksmanMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist.MartialArtistMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.primordial.PrimordialMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero.ShieldHeroMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.soldier.SoldierMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer.SorcererMenu;
import io.github.NoOne.nMLItems.ItemCreator;
import io.github.NoOne.nMLSkills.skillSystem.Skill;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExpertiseMainMenu extends Menu {
    private NMLAbilities nmlAbilities;
    private Skills skills;

    public ExpertiseMainMenu(NMLAbilities nmlAbilities, Player player) {
        super(player);

        this.nmlAbilities = nmlAbilities;
        skills = nmlAbilities.getSaveSkillsManager().getSkills(player.getUniqueId());
    }

    @Override
    public String getMenuName() {
        return "§d§lCHOOSE YOUR EXPERTISE!";
    }

    @Override
    public int getSlots() {
        return 9 * 5;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        ItemStack clickedItem =  event.getCurrentItem();

        event.setCancelled(true);

        switch (event.getSlot()) {
            case 10 -> new SoldierMenu(nmlAbilities, player, skills, clickedItem).open();
            case 11 -> new AssassinMenu(nmlAbilities, player, skills, clickedItem).open();
            case 12 -> new MarauderMenu(nmlAbilities, player, skills, clickedItem).open();
            case 14 -> new CavalierMenu(nmlAbilities, player, skills, clickedItem).open();
            case 15 -> new MartialArtistMenu(nmlAbilities, player, skills, clickedItem).open();
            case 16 -> new ShieldHeroMenu(nmlAbilities, player, skills, clickedItem).open();
            case 22 -> new MarksmanMenu(nmlAbilities, player, skills, clickedItem).open();
            case 29 -> new SorcererMenu(nmlAbilities, player, skills, clickedItem).open();
            case 30 -> new PrimordialMenu(nmlAbilities, player, skills, clickedItem).open();
            case 32 -> new HallowedMenu(nmlAbilities, player, skills, clickedItem).open();
            case 33 -> new AnnulledMenu(nmlAbilities, player, skills, clickedItem).open();
            case 44 -> new ExpertiseLoadoutMenu(nmlAbilities, player).open();
        }
    }

    @Override
    public void handlePlayerMenu(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(10, createExpertiseButton(Skill.SOLDIER));
        inventory.setItem(11, createExpertiseButton(Skill.ASSASSIN));
        inventory.setItem(12, createExpertiseButton(Skill.MARAUDER));
        inventory.setItem(14, createExpertiseButton(Skill.CAVALIER));
        inventory.setItem(15, createExpertiseButton(Skill.MARTIAL_ARTIST));
        inventory.setItem(16, createExpertiseButton(Skill.SHIELD_HERO));
        inventory.setItem(22, createExpertiseButton(Skill.MARKSMAN));
        inventory.setItem(29, createExpertiseButton(Skill.SORCERER));
        inventory.setItem(30, createExpertiseButton(Skill.PRIMORDIAL));
        inventory.setItem(32, createExpertiseButton(Skill.HALLOWED));
        inventory.setItem(33, createExpertiseButton(Skill.ANNULLED));
        inventory.setItem(44, ItemCreator.createItem(
                Material.STRUCTURE_BLOCK,
                "§7§lChange Ability Loadout",
                null
        ));
    }

    private ItemStack createExpertiseButton(Skill skill) {
        double percent = skills.calculateExpPercent(skill) * 100.0;
        Material material = switch (skill) {
            case SOLDIER -> Material.DIAMOND_SWORD;
            case ASSASSIN -> Material.BLACK_WOOL;
            case MARAUDER -> Material.GOLDEN_AXE;
            case CAVALIER -> Material.MACE;
            case MARTIAL_ARTIST -> Material.RED_GLAZED_TERRACOTTA;
            case SHIELD_HERO -> Material.SHIELD;
            case MARKSMAN -> Material.BOW;
            case SORCERER -> Material.ENCHANTED_BOOK;
            case PRIMORDIAL -> Material.OAK_SAPLING;
            case HALLOWED -> Material.OXEYE_DAISY;
            case ANNULLED -> Material.CRYING_OBSIDIAN;
            default -> null;
        };
        String color = switch (skill) {
            case SOLDIER -> "§c";
            case ASSASSIN -> "§0";
            case MARAUDER, MARTIAL_ARTIST -> "§4";
            case CAVALIER -> "§1";
            case SHIELD_HERO -> "§3";
            case MARKSMAN -> "§a";
            case SORCERER -> "§6";
            case PRIMORDIAL -> "§2";
            case HALLOWED -> "§f";
            case ANNULLED -> "§5";
            default -> null;
        };
        List<String> blurb = switch (skill) {
            case SOLDIER -> List.of("§7Me when military proletarianism is kinda based actually");
            case ASSASSIN -> List.of(
                    "§7\"Hey what do you got there?\"",
                    "§7\"A knife!\"",
                    "§7\"NO!\""
            );
            case MARAUDER -> List.of("§7Reject modernity, EMBRACE BARBARITY!");
            case CAVALIER -> List.of("§7Unfortunately, this has nothing to do with horses.");
            case MARTIAL_ARTIST -> List.of(
                    "§7Beating up homeless people in the back alley",
                    "§7of an Arby's™ pro max"
            );
            case SHIELD_HERO -> List.of("§7Don't worry, you won't be hate crimed...hopefully.");
            case MARKSMAN -> List.of("§7This is the closest you're getting to a gun.");
            case SORCERER -> List.of(
                    "§7There's no I in team, but there's 6 Is in",
                    "§7§o\"F§kuck§r§7§o it, I don't care how big the room is, I cast fireball.\""
            );
            case PRIMORDIAL -> List.of("§7F§kuck§r§7in hippie.");
            case HALLOWED -> List.of("§7Please just be normal.");
            case ANNULLED -> List.of("§7Welcome home.");
            default -> List.of();
        };
        ItemStack button = ItemCreator.createItem(
                material,
                "§fLv. " + skills.getSkillLevel(skill) +  " " + color + "§l" + skill.toTitle(),
                new ArrayList<>() {{
                    addAll(List.of(
                            "§8" + skills.getSkillExp(skill) + " / " + skills.calculateExpToLvlUpSkill(skill) + " exp (" + percent + "%)",
                            ""
                    ));
                    addAll(blurb);
                    addAll(List.of(
                            "",
                            "§bCLICK TO SELECT!"
                    ));
                }}
        );

        button.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return button;
    }
}
