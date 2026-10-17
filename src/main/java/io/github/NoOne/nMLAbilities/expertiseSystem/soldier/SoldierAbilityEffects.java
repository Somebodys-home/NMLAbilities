package io.github.NoOne.nMLAbilities.expertiseSystem.soldier;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityEffects.AbilityEffectsHelper;
import org.bukkit.*;
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
        HashMap<DamageType, Double> damage = getDamageForAbility(player, 1.15);

        useEnergyAndCooldown(player, 20, 1.5);

        new BukkitRunnable() {
            int timer = 0;
            Location[] locations = new Location[6];
            Particle.DustOptions maroon = new Particle.DustOptions(Color.MAROON, 2f);

            @Override
            public void run() {
                timer++;

                Location playerLocation = player.getEyeLocation();
                Vector direction = getPlanarDirection(playerLocation);
                Vector left = pointToTheLeft(playerLocation);
                Vector right = pointToTheRight(playerLocation);
                Location midLocation = playerLocation.clone().add(direction.clone().multiply(1.75));

                if (timer <= 10) {
                    if (timer >= 1 && timer < 3) { // just so the dust of the first slash can linger for a little bit so both slashes fade out at the same time
                        if (timer == 1) { // line effects
                            Location leftBottomLocation = playerLocation.clone().add(direction).add(left).add(0, -.7, 0);
                            Location rightTopLocation = playerLocation.clone().add(direction.clone().multiply(2.5)).add(right).add(0, .75, 0);
                            Location leftBottomLocation1 = playerLocation.clone().add(direction.clone().multiply(.85)).add(left).add(0, -.7, 0);
                            Location rightTopLocation1 = playerLocation.clone().add(direction.clone().multiply(2.35)).add(right).add(0, .75, 0);

                            player.swingMainHand();
                            locations[0] = leftBottomLocation;
                            locations[1] = rightTopLocation;
                            locations[2] = leftBottomLocation1;
                            locations[3] = rightTopLocation1;
                            player.playSound(playerLocation, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);

                            for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, midLocation, 1.25)) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                            }
                        }

                        particleLine(maroon, locations[0], locations[1], 15);
                    }

                    particleLine(Particle.ELECTRIC_SPARK, locations[2], locations[3], 15);
                }

                if (timer == 10) {
                    cancel();

                    Location leftTopLocation = playerLocation.clone().add(direction.clone().multiply(2.5)).add(left).add(0, .75, 0);
                    Location rightBottomLocation = playerLocation.clone().add(direction).add(right).add(0, -.7, 0);
                    Location leftTopLocation1 = playerLocation.clone().add(direction.clone().multiply(2.35)).add(left).add(0, .75, 0);
                    Location rightBottomLocation1 = playerLocation.clone().add(direction.clone().multiply(.85)).add(right).add(0, -.7, 0);

                    player.swingMainHand();
                    locations[4] = leftTopLocation;
                    locations[5] = rightBottomLocation;
                    particleLine(maroon, leftTopLocation, rightBottomLocation, 15);
                    particleLine(Particle.ELECTRIC_SPARK, rightBottomLocation1, leftTopLocation1, 15);
                    player.playSound(playerLocation, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);

                    for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, midLocation, 1.25)) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                    }
                }
            }
        }.runTaskTimer(nmlAbilities, 0, 1);
    }
}