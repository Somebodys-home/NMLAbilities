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
        switch (expertise) {
            case SOLDIER: return "Soldier";
            case ASSASSIN: return "Assassin";
            case MARAUDER: return "Marauder";
            case CAVALIER: return "Cavalier";
            case MARTIAL_ARTIST: return "Martial Artist";
            case SHIELD_HERO: return "Shield Hero";
            case MARKSMAN: return "Marksman";
            case SORCERER: return "Sorcerer";
            case PRIMORDIAL: return "Primordial";
            case HALLOWED: return "Hallowed";
            case ANNULLED: return "Annulled";
            default: return "";
        }
    }

    public static NamespacedKey makeExpertiseKey(NMLAbilities nmlAbilities, Expertise expertise) {
        return new NamespacedKey(nmlAbilities, getString(expertise).replace(" ", "").toLowerCase());
    }
}
