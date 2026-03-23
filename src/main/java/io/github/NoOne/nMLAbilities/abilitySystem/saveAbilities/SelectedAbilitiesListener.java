package io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class SelectedAbilitiesListener implements Listener {
    private SelectedAbilitiesManager selectedAbilitiesManager;
    private SelectedAbilitiesConfig selectedAbilitiesConfig;

    public SelectedAbilitiesListener(NMLAbilities nmlAbilities) {
        selectedAbilitiesManager = nmlAbilities.getSelectedManager();
        selectedAbilitiesConfig = nmlAbilities.getSelectedConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SelectedAbilities selectedAbilities = selectedAbilitiesManager.getSelectedAbilities(player.getUniqueId());

        if (selectedAbilities == null) {
            selectedAbilitiesManager.createDefaultSelectedAbilities(player);
        }
    }

    @EventHandler
    public void onChangeAbility(AbilityChangeEvent event) {
        Player player = event.getPlayer();
        ItemStack newAbility = event.getNewAbility();
        SelectedAbilities selectedAbilities = selectedAbilitiesManager.getSelectedAbilities(player.getUniqueId());

        if (event.isResettingAbilities()) {
            selectedAbilities.clearSelectedAbilities();
        } else {
            switch (event.getChangedAbility()) {
                case "expertise1" -> selectedAbilities.setExpertise1(newAbility.getItemMeta().getDisplayName());
                case "expertise2" -> selectedAbilities.setExpertise2(newAbility.getItemMeta().getDisplayName());
                case "expertise3" -> selectedAbilities.setExpertise3(newAbility.getItemMeta().getDisplayName());
            }
        }

        selectedAbilitiesManager.saveSelectedAbilitiesToConfig(player);
    }
}
