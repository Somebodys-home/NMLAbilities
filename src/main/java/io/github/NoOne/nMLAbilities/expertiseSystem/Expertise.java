package io.github.NoOne.nMLAbilities.expertiseSystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import org.bukkit.NamespacedKey;

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

    public static String getString(Expertise expertise) {
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

    public static NamespacedKey makeExpertiseKey(NMLAbilities nmlAbilities, Expertise expertise) {
        return new NamespacedKey(nmlAbilities, getString(expertise).replace(" ", "").toLowerCase());
    }
}
