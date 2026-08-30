package io.github.NoOne.nMLAbilities.expertiseSystem.cavalier;

import io.github.NoOne.menuSystem.Menu;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseConfirmMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseMenu;
import io.github.NoOne.nMLItems.ItemCreator;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CavalierMenu extends Menu {
    private NMLAbilities nmlAbilities;
    private SelectedAbilities selectedAbilities;
    private Skills skills;

    public CavalierMenu(NMLAbilities nmlAbilities, Player player) {
        super(player);
        this.nmlAbilities = nmlAbilities;
        selectedAbilities = nmlAbilities.getSelectedManager().getSelectedAbilities(playerMenuUtility.getOwner().getUniqueId());
        skills = nmlAbilities.getSkillSetManager().getSkillSet(playerMenuUtility.getOwner().getUniqueId()).getSkills();
    }

    @Override
    public String getMenuName() {
        return "§1§lCavalier Abilities";
    }

    @Override
    public int getSlots() {
        return 9 * 4;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        ItemStack selected = event.getCurrentItem();
        assert selected != null;

        if (event.getSlot() == 35) {
            new ExpertiseMenu(nmlAbilities, player).open();
        } else {
            if (Arrays.stream(selectedAbilities.getSelectedAbilitiesArray()).anyMatch(element -> element.equals(Objects.requireNonNull(selected.getItemMeta()).getDisplayName()))) {
                playerMenuUtility.getOwner().sendMessage("§c⚠ §nYou already have this ability selected!§r§c ⚠");
                playerMenuUtility.getOwner().playSound(playerMenuUtility.getOwner(), Sound.BLOCK_NOTE_BLOCK_BASS, 2f, .5f);
                return;
            }
            if (selected.getItemMeta().getPersistentDataContainer().has(AbilityItemManager.getUnusableKey())) {
                playerMenuUtility.getOwner().sendMessage("§c⚠ §nYou are too inexperienced for this ability!§r§c ⚠");
                playerMenuUtility.getOwner().playSound(playerMenuUtility.getOwner(), Sound.BLOCK_NOTE_BLOCK_BASS, 2f, .5f);
                return;
            }

            new ExpertiseConfirmMenu(player, selected, this).open();
        }
    }

    @Override
    public void handlePlayerMenu(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(10, CavalierAbilityItems.seismicSlam(skills));
        inventory.setItem(35, ItemCreator.createItem(Material.BARRIER, 1, "§c§l<- Go Back", List.of()));
    }
}
