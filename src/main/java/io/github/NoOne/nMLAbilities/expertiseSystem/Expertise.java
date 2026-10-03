package io.github.NoOne.nMLAbilities.expertiseSystem;

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

    public static String toString(Expertise expertise) {
        return switch (expertise) {
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

    public static Expertise fromString(String string) {
        return switch (string) {
            case "Soldier" -> SOLDIER;
            case "Assassin" -> ASSASSIN;
            case "Marauder" -> MARAUDER;
            case "Cavalier" -> CAVALIER;
            case "Martial Artist" -> MARTIAL_ARTIST;
            case "Shield Hero" -> SHIELD_HERO;
            case "Marksman" -> MARKSMAN;
            case "Sorcerer" -> SORCERER;
            case "Primordial" -> PRIMORDIAL;
            case "Hallowed" -> HALLOWED;
            case "Annulled" -> ANNULLED;
            default -> null;
        };
    }

    public static Material toMaterial(Expertise expertise) {
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

    public static String toChatColor(Expertise expertise) {
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
        };
    }
}
