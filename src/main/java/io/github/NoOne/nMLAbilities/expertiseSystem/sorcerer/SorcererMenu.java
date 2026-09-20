package io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenuTemplate;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SorcererMenu extends ExpertiseAbilityMenuTemplate {
    public SorcererMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new SorcererAbilityItemCreator(skills));
    }

    @Override
    public String getMenuName() {
        return "§6§lSorcerer Abilities";
    }
}
