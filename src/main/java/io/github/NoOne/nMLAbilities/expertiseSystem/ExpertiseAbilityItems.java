package io.github.NoOne.nMLAbilities.expertiseSystem;

import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.List;

// the parent class for all the other classes that make expertise ability items, so that there is a method to get all of their ability items in a list
// it has to be a class instead of an interface cuz it will be tied to an expertise ability menu, which is another parent class, so it has to be instantiatable
public class ExpertiseAbilityItems {
    protected static Skills skills;
    // despite skills not being used in the method, it has to be here for every ability item is made in respect to the player's skills

    public ExpertiseAbilityItems(Skills skills) {
        ExpertiseAbilityItems.skills = skills;
    }

    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of();
    }
}
