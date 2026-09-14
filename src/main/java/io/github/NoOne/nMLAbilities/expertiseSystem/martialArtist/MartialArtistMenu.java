package io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenu;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MartialArtistMenu extends ExpertiseAbilityMenu {
    public MartialArtistMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new MartialArtistAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§4§lMartial Artist Abilities";
    }
}
