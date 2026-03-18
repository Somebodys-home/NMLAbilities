package io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.UseAbilityEvent;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem.SelectedManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ShieldHeroListener implements Listener {
    private SelectedManager selectedManager;

    public ShieldHeroListener(NMLAbilities NMLAbilities) {
        selectedManager = NMLAbilities.getSelectedManager();
    }

    @EventHandler
    public void onUseAbility(UseAbilityEvent event) {
        Player player = event.getPlayer();
        int hotbarSlot = event.getHotbarSlot();
        String[] selectedAbilities = selectedManager.getAbilityProfile(event.getPlayer().getUniqueId()).getSelectedAbilitiesArray();
        String abilityName = event.getAbility().getItemMeta().getDisplayName();

        if (Arrays.asList(selectedAbilities).contains(abilityName)) {
            switch (abilityName) {
                case "§3§lSecond Wind" -> ShieldHeroAbilityEffects.secondWind(player, hotbarSlot);
            }
        }
    }
}