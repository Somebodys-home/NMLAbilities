package io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SelectedListener implements Listener {
    private SelectedManager selectedManager;

    public SelectedListener(NMLAbilities nmlAbilities) {
        selectedManager = nmlAbilities.getSelectedManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SelectedAbilities abilityProfile = selectedManager.getAbilityProfile(player.getUniqueId());

        if (abilityProfile == null) {
            selectedManager.createnewProfile(player);
        }
    }
}
