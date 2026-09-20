package io.github.NoOne.nMLAbilities.expertiseSystem.marauder;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseEffectsHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class MarauderAbilityEffects extends ExpertiseEffectsHelper {
    public static void bladeTornado(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, .25);

        useEnergyAndCooldown(player, 30, 6);
        player.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(1);
        breakKneecaps(player);

        new BukkitRunnable() {
            int tornadoTicks = 100;

            @Override
            public void run() {
                tornadoTicks--;

                Location playerLocation = player.getLocation();

                playerLocation.setPitch(0);

                Vector direction = playerLocation.getDirection();
                Vector tinyDash = direction.clone().multiply(.5).setY(-.125);

                player.setVelocity(tinyDash);
                playerLocation.add(direction); // so the particles are properly on the player instead of like, one block behind

                if (tornadoTicks % 3 == 0) {
                    player.playSound(playerLocation, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, .5f);
                } else if (tornadoTicks % 4 == 0) {
                    AbilityEffects.horizontalParticleCircle(Particle.SWEEP_ATTACK, playerLocation.clone().add(new Vector(0, .5, 0)), 1, 4);
                    AbilityEffects.horizontalParticleCircle(Particle.SWEEP_ATTACK, playerLocation.clone().add(new Vector(0, 1.25, 0)), 1.5, 6);
                    AbilityEffects.horizontalParticleCircle(Particle.SWEEP_ATTACK, playerLocation.clone().add(new Vector(0, 2, 0)), 2, 8);
                } else if (tornadoTicks % 5 == 0) {
                    for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, playerLocation, 2.25, 2, 2.25)) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                    }
                }

                if (tornadoTicks == 0) {
                    this.cancel();
                    player.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(.6);
                    fixKneecaps(player);
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }
}