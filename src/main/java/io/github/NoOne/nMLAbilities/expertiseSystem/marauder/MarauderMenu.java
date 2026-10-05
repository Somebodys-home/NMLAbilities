package io.github.NoOne.nMLAbilities.expertiseSystem.marauder;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenuTemplate;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MarauderMenu extends ExpertiseAbilityMenuTemplate {
    public MarauderMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new MarauderAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§4§lMarauder Abilities";
    }
}
