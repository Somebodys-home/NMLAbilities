package io.github.NoOne.nMLAbilities.expertiseSystem;

import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.List;

// the parent class for all the other classes that make expertise ability items, so that there is a method to get all of their ability items in a list
// it has to be a class instead of an interface cuz it will be tied to an expertise ability menu, which is another parent class, so it has to be instantiatable
public class ExpertiseAbilityItemHelper {
    protected static Skills skills;
    // despite skills not being used in any method, it has to be here because every ability item is made in respect to the player's skills

    public ExpertiseAbilityItemHelper(Skills skills) {
        ExpertiseAbilityItemHelper.skills = skills;
    }

    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of();
    }

    protected static String makeWeaponDamageString(int value) {
        return "§f§n" + value + "%" + "§r§f Weapon Damage \uD83D\uDDE1";
    }

    protected static String makeElementalDamageString(DamageType damageType, double value) {
        if (value == (int) value) {
            return DamageType.toChatColor(damageType) + "§n" + (int) value + "x" + "§r" + DamageType.toChatColor(damageType) + " " +
                    DamageType.toString(damageType) + " Damage " + DamageType.toEmoji(damageType);
        } else {
            String valueString = Double.toString(value).replace("0.", ".");

            return DamageType.toChatColor(damageType) + "§n" + valueString + "x" + "§r" + DamageType.toChatColor(damageType) + " " +
                    DamageType.toString(damageType) + " Damage " + DamageType.toEmoji(damageType);
        }
    }

    protected static String makeEverySecondString(double time) {
        String timeString = String.valueOf(time);

        if (time == (int) time) {
            timeString = String.valueOf((int) time);
        } else {
            timeString = timeString.replace(".0", "");
        }

        return " §8§o(every " + timeString + "s)";
    }

    protected static String makePerString(String string) {
        return " §8§o(per " + string + ")";
    }

    protected static String makeInvincibleString(double time) {
        if (time == (int) time) {
            return "§fBecome §nIninvincible§r§f for " + (int) time + "s";
        } else {
            return "§fBecome §nIninvincible§r§f for " + time + "s";
        }
    }

    protected static String makeTimesString(int times) {
        return " §8§o(x" + times + ")";
    }
}
