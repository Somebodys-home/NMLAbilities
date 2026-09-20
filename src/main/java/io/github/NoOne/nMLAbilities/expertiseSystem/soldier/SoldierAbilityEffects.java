package io.github.NoOne.nMLAbilities.expertiseSystem.soldier;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseEffectsHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class SoldierAbilityEffects extends ExpertiseEffectsHelper {
    public static void slash(Player player) {
        HashMap<DamageType, Double> damageStats = getDamageForAbility(player, 1.2);
        Location location = player.getLocation();

        useEnergyAndCooldown(player, 15, 1);
        player.playSound(location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

        for (double i = -Math.PI / 2; i <= Math.PI / 2; i += Math.PI / 10) {
            double x = Math.sin(i) * 2;
            double z = Math.cos(i) * 2;
            Vector offset = new Vector(x, 1, z).rotateAroundY(-Math.toRadians(location.getYaw()));
            Location particleLocation = location.clone().add(offset);

            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1);

            for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, particleLocation, 1.5)) {
                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
            }
        }
    }
}