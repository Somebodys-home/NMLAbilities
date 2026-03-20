package io.github.NoOne.nMLAbilities.abilitySystem.listeners;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.UseAbilityEvent;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem.SelectedManager;
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

import java.util.Arrays;

public class AbilityEffectsListener implements Listener {
    private SelectedManager selectedManager;

    public AbilityEffectsListener(NMLAbilities nmlAbilities) {
        selectedManager = nmlAbilities.getSelectedManager();
    }

    @EventHandler
    public void onUseAbility(UseAbilityEvent event) {
        Player player = event.getPlayer();
        int hotbarSlot = event.getHotbarSlot();
        String[] selectedAbilities = selectedManager.getAbilityProfile(event.getPlayer().getUniqueId()).getSelectedAbilitiesArray();
        String abilityName = event.getAbilityItem().getItemMeta().getDisplayName();

        if (Arrays.asList(selectedAbilities).contains(abilityName)) {
            switch (abilityName) {
                // Soldier abilities
                case "§c§lSlash" -> SoldierAbilityEffects.slash(player);

                // Assassin abilities
                case "§8§lSlash & Dash" -> AssassinAbilityEffects.slashAndDash(player);

                // Marauder abilities
                case "§4§lBlade Tornado" -> MarauderAbilityEffects.bladeTornado(player);

                // Cavalier abilities
                case "§9§lSeismic Slam" -> CavalierAbilityEffects.seismicSlam(player);

                // Martial Artist abilities
                case "§4§l10-Hit Combo" -> MartialArtistAbilityEffects.tenHitCombo(player);

                // Shield Hero abilities
                case "§3§lSecond Wind" -> ShieldHeroAbilityEffects.secondWind(player);

                // Marksman abilities
                case "§a§lRapid Shot" -> MarksmanAbilityEffects.rapidShot(player, hotbarSlot, event.getAbilityItem());

                // Sorcerer abilities
                case "§6§lMagic Missile EX" -> SorcererAbilityEffects.magicMissileEX(player);
                case "§6§lDragon's Breath" -> SorcererAbilityEffects.dragonsBreath(player);

                // Primordial abilities
                case "§2§lChuck Rock" -> PrimordialAbilityEffects.chuckRock(player);
                case "§2§lPumpkin Bomb" -> PrimordialAbilityEffects.pumpkinBomb(player);
                case "§2§lAir Ball" -> PrimordialAbilityEffects.airBall(player);

                // Hallowed abilities
                case "§f§lHalo" -> HallowedAbilityEffects.halo(player);

                // Annulled abilities
                case "§5§lBlack Hole" -> AnnulledAbilityEffects.blackHole(player);
            }
        }
    }
}