package io.github.NoOne.nMLAbilities.expertiseSystem.cavalier;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.UseAbilityEvent;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem.SelectedManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class CavalierListener implements Listener {
    private SelectedManager selectedManager;

    public CavalierListener(NMLAbilities nmlAbilities) {
        selectedManager = nmlAbilities.getSelectedManager();
    }

    @EventHandler
    public void onUseAbility(UseAbilityEvent event) {
        if (event.getWeapon() != null) {
            Player player = event.getPlayer();
            int hotbarSlot = event.getHotbarSlot();
            String[] selectedAbilities = selectedManager.getAbilityProfile(event.getPlayer().getUniqueId()).getSelectedAbilitiesArray();
            String abilityName = event.getAbility().getItemMeta().getDisplayName();

            if (Arrays.asList(selectedAbilities).contains(abilityName)) {
                switch (abilityName) {
                    case "§9§lSeismic Slam" -> CavalierAbilityEffects.seismicSlam(player, hotbarSlot);
                }
            }
        }
    }
}