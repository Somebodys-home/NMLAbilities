package io.github.NoOne.nMLAbilities.expertiseSystem.soldier;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityEffects.AbilityEffectsHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class SoldierAbilityEffects extends AbilityEffectsHelper {
    public static void slash(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, 1.2);
        Location playerLocation = player.getLocation();
        ArrayList<LivingEntity> hitEntities = new ArrayList<>();

        useEnergyAndCooldown(player, 15, 1);
        player.playSound(playerLocation, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
        player.swingMainHand();

        for (double i = -Math.PI / 2; i <= Math.PI / 2; i += Math.PI / 10) {
            double x = Math.sin(i) * 2;
            double z = Math.cos(i) * 2;
            Vector offset = new Vector(x, 1, z).rotateAroundY(-Math.toRadians(playerLocation.getYaw()));
            Location particleLocation = playerLocation.clone().add(offset);

            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1, 0, 0, 0, 0);

            for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, particleLocation, 1)) {
                if (!hitEntities.contains(livingEntity)) {
                    Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                    hitEntities.add(livingEntity);
                }
            }
        }
    }

    public static void xSlash(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, .8);

        new BukkitRunnable() {
            int timer = 140;

            @Override
            public void run() {
                timer--;

                if (timer == 0) {
                    cancel();
                }

                player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,
                        rotateLocationAroundYaw(player.getLocation(), player.getLocation().getYaw(), 1, -.75).add(0, 1, 0),
                        1, 0, 0, 0, 0);
                player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,
                        rotateLocationAroundYaw(player.getLocation(), player.getLocation().getYaw(), 1, .75).add(0, 1, 0),
                        1, 0, 0, 0, 0);
            }
        }.runTaskTimer(nmlAbilities, 0, 1);
    }
}