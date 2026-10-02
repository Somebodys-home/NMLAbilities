package io.github.NoOne.nMLAbilities.expertiseSystem.marauder;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.expertiseSystem.AbilityEffectsHelper;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class MarauderAbilityEffects extends AbilityEffectsHelper {
    public static void bladeTornado(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, .25);

        useEnergyAndCooldown(player, 15, 6);
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
                    horizontalParticleCircle(Particle.SWEEP_ATTACK, playerLocation.clone().add(new Vector(0, .5, 0)), 1, 4);
                    horizontalParticleCircle(Particle.SWEEP_ATTACK, playerLocation.clone().add(new Vector(0, 1.25, 0)), 1.5, 6);
                    horizontalParticleCircle(Particle.SWEEP_ATTACK, playerLocation.clone().add(new Vector(0, 2, 0)), 2, 8);
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

    public static void stompingTantrum(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, .5, DamageType.PHYSICAL, .85);
        World world = player.getWorld();

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 2f, .3f);
        stompEffect(player, 0, damage, world);
        useEnergyAndCooldown(player, 30, 3.75);
    }

    private static void stompEffect(Player player, int stompCount, HashMap<DamageType, Double> damage, World world) {
        Vector jump = player.getLocation().getDirection().multiply(.5).setY(1);
        int heavyStompStart = 4;

        player.setVelocity(jump);
        makeKneecapsUnbreakable(player);

        // slam
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setVelocity(player.getLocation().getDirection().setY(-2.2)); // slam

                // landing effect
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnGround()) {
                            cancel();
                            makeKneecapsBreakable(player);

                            Location particleLocation = player.getLocation().clone().add(0, .1, 0);
                            Block landedBlock = world.getBlockAt(particleLocation.toBlockLocation().add(0, -1, 0));
                            Particle explosion;
                            int radius;

                            if (stompCount >= heavyStompStart) {
                                explosion = Particle.EXPLOSION_EMITTER;
                                expandingHorizontalParticleCircle(Particle.FLAME, particleLocation, 1.5, 40, .3);
                                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_HURT, 1f, 1f);
                                radius = 3;
                            } else {
                                explosion = Particle.EXPLOSION;
                                expandingHorizontalParticleCircle(Particle.FLAME, particleLocation, 1, 30, .3);
                                radius = 2;
                            }

                            world.spawnParticle(explosion, particleLocation, 1, 0, 0, 0, 0);
                            player.playSound(particleLocation, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 3f, 1f);

                            // rubble effects
                            for (int i = 0; i < 50 + (stompCount * 20); i++) {
                                double angle = ThreadLocalRandom.current().nextDouble(0, radius * Math.PI);
                                double distance = Math.sqrt(ThreadLocalRandom.current().nextDouble()) * radius;
                                double xOffset = distance * Math.cos(angle);
                                double zOffset = distance * Math.sin(angle);
                                Location randomLocation = particleLocation.clone().add(xOffset, .5, zOffset);

                                world.spawnParticle(Particle.DUST_PILLAR, randomLocation, 0, 0, .5, 0, 1, landedBlock.getBlockData());
                                world.spawnParticle(Particle.BLOCK, randomLocation, 0, 0, 1, 0, 1, landedBlock.getBlockData());
                            }

                            for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, particleLocation, radius)) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                            }

                            if (stompCount < 7) {
                                stompEffect(player, stompCount + 1, damage, world);
                            }
                        }
                    }
                }.runTaskTimer(nmlAbilities, 0L, 1L);
            }
        }.runTaskLater(nmlAbilities, (long) Math.max(3, 7 - (stompCount * .5))); // player stomps faster the more they get off
    }
}