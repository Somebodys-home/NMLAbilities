package io.github.NoOne.nMLAbilities.abilitySystem.abilityUse;

public enum AbilityPrerequisite {
    GROUNDED;

    public static String toString(AbilityPrerequisite abilityPrerequisite) {
        return switch (abilityPrerequisite) {
            case GROUNDED -> "Grounded";
        };
    }

    public static AbilityPrerequisite fromString(String string) {
        return switch (string) {
            case "Grounded" -> GROUNDED;
            default -> null;
        };
    }
}
