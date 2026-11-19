package io.github.NoOne.nMLAbilities.expertiseSystem;

import io.github.NoOne.nMLSkills.skillSystem.Skill;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Material;

public enum Expertise {
    SOLDIER,
    ASSASSIN,
    MARAUDER,
    CAVALIER,
    MARTIAL_ARTIST,
    SHIELD_HERO,
    MARKSMAN,
    SORCERER,
    PRIMORDIAL,
    HALLOWED,
    ANNULLED;

    @Override
    public String toString() {
        return switch (this) {
            case SOLDIER -> "Soldier";
            case ASSASSIN -> "Assassin";
            case MARAUDER -> "Marauder";
            case CAVALIER -> "Cavalier";
            case MARTIAL_ARTIST -> "Martial Artist";
            case SHIELD_HERO -> "Shield Hero";
            case MARKSMAN -> "Marksman";
            case SORCERER -> "Sorcerer";
            case PRIMORDIAL -> "Primordial";
            case HALLOWED -> "Hallowed";
            case ANNULLED -> "Annulled";
        };
    }

    public Material toMaterial() {
        return switch (this) {
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

    public String toChatColor() {
        return switch (this) {
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
        };
    }

    public Skill toSkill() {
        return switch (this) {
            case SOLDIER -> Skill.SOLDIER;
            case ASSASSIN -> Skill.ASSASSIN;
            case MARAUDER -> Skill.MARAUDER;
            case CAVALIER -> Skill.CAVALIER;
            case MARTIAL_ARTIST -> Skill.MARTIAL_ARTIST;
            case SHIELD_HERO -> Skill.SHIELD_HERO;
            case MARKSMAN -> Skill.MARKSMAN;
            case SORCERER -> Skill.SORCERER;
            case PRIMORDIAL -> Skill.PRIMORDIAL;
            case HALLOWED -> Skill.HALLOWED;
            case ANNULLED -> Skill.ANNULLED;
        };
    }

    public static Expertise fromString(String string) {
        return switch (string.toLowerCase().replace(" ", "")) {
            case "soldier" -> SOLDIER;
            case "assassin" -> ASSASSIN;
            case "marauder" -> MARAUDER;
            case "cavalier" -> CAVALIER;
            case "martialartist" -> MARTIAL_ARTIST;
            case "shieldhero" -> SHIELD_HERO;
            case "marksman" -> MARKSMAN;
            case "sorcerer" -> SORCERER;
            case "primordial" -> PRIMORDIAL;
            case "hallowed" -> HALLOWED;
            case "annulled" -> ANNULLED;
            default -> null;
        };
    }

    public static Expertise fromSkill(Skill skill) {
        return switch (skill) {
            case SOLDIER -> SOLDIER;
            case ASSASSIN -> ASSASSIN;
            case MARAUDER -> MARAUDER;
            case CAVALIER -> CAVALIER;
            case MARTIAL_ARTIST -> MARTIAL_ARTIST;
            case SHIELD_HERO -> SHIELD_HERO;
            case MARKSMAN -> MARKSMAN;
            case SORCERER -> SORCERER;
            case PRIMORDIAL -> PRIMORDIAL;
            case HALLOWED -> HALLOWED;
            case ANNULLED -> ANNULLED;
            default -> null;
        };
    }
}
