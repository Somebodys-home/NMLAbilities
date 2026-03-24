package io.github.NoOne.nMLAbilities.abilitySystem.abilityUse;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.annulled.AnnulledAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.assassin.AssassinAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.cavalier.CavalierAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.hallowed.HallowedAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.marauder.MarauderAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.marksman.MarksmanAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist.MartialArtistAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.primordial.PrimordialAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero.ShieldHeroAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.soldier.SoldierAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer.SorcererAbilityEffects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class AbilityEffectsListener implements Listener {
    private SelectedAbilitiesManager selectedAbilitiesManager;

    public AbilityEffectsListener(NMLAbilities nmlAbilities) {
        selectedAbilitiesManager = nmlAbilities.getSelectedManager();
    }

    @EventHandler
    public void onUseAbility(UseAbilityEvent event) {
        Player player = event.getPlayer();
        String[] selectedAbilities = selectedAbilitiesManager.getSelectedAbilities(event.getPlayer().getUniqueId()).getSelectedAbilitiesArray();
        ItemStack ability = event.getAbility();
        String abilityName = ability.getItemMeta().getDisplayName().substring(4);

        if (Arrays.asList(selectedAbilities).contains(abilityName)) {
            switch (abilityName) {
                // Soldier abilities
                case "Slash" -> SoldierAbilityEffects.slash(player);

                // Assassin abilities
                case "Slash & Dash" -> AssassinAbilityEffects.slashAndDash(player);

                // Marauder abilities
                case "Blade Tornado" -> MarauderAbilityEffects.bladeTornado(player);

                // Cavalier abilities
                case "Seismic Slam" -> CavalierAbilityEffects.seismicSlam(player);

                // Martial Artist abilities
                case "10-Hit Combo" -> MartialArtistAbilityEffects.tenHitCombo(player);

                // Shield Hero abilities
                case "Second Wind" -> ShieldHeroAbilityEffects.secondWind(player);

                // Marksman abilities
                case "Arrow Hailstorm" -> MarksmanAbilityEffects.arrowHailStorm(player);

                // Sorcerer abilities
                case "Magic Missile EX" -> SorcererAbilityEffects.magicMissileEX(player);
                case "Dragon's Breath" -> SorcererAbilityEffects.dragonsBreath(player);

                // Primordial abilities
                case "Chuck Rock" -> PrimordialAbilityEffects.chuckRock(player);
                case "Pumpkin Bomb" -> PrimordialAbilityEffects.pumpkinBomb(player);
                case "Air Ball" -> PrimordialAbilityEffects.airBall(player);

                // Hallowed abilities
                case "Halo" -> HallowedAbilityEffects.halo(player);

                // Annulled abilities
                case "Black Hole" -> AnnulledAbilityEffects.blackHole(player);
            }
        }
    }
}