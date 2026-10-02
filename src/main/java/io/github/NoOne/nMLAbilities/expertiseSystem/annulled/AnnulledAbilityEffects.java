package io.github.NoOne.nMLAbilities.expertiseSystem.annulled;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.expertiseSystem.AbilityEffectsHelper;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class AnnulledAbilityEffects extends AbilityEffectsHelper {
    public static void blackHole(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, DamageType.NECROTIC, 5);
        Particle.DustOptions blackHole = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1F);

        EnergyManager.useEnergy(player, 50);
        putOnInfiniteCooldown(player);
        player.playSound(player, Sound.ENTITY_WITHER_SHOOT, 1f, 1f);

        new BukkitRunnable() {
            int timer = 100;
            Location center = player.getLocation().clone().add(0, 1, 0);
            Vector velocity = player.getLocation().getDirection().multiply(.15);

            @Override
            public void run() {
                timer--;

                center.add(velocity);
                particleSphere(blackHole, center, .5, 6); // tiny black hole

                // big black hole triggers when times up, or it hits a block or entity
                if (timer == 0 || !center.getBlock().isPassable() || !getNearbyEntitiesExcludingPlayer(player, center, .5, .5, .5).isEmpty()) {
                    cancel();
                    player.playSound(player, Sound.ITEM_ELYTRA_FLYING, 2f, 1f);
                    player.playSound(player, Sound.ENTITY_WITHER_DEATH, 1f, 1f);
                    removeInfiniteCooldown(player);

                    // black hole runnable
                    new BukkitRunnable() {
                        int detonationTimer = 155;
                        int blackHoleRadius = 1; // does change dynamically
                        int maxBlackHoleRadius = 6; // maximum size
                        int pullRadius = 9; // radius that entities will get sucked in
                        int particleCircles = 1;

                        @Override
                        public void run() {
                            detonationTimer--;

                            // black hole rapidly expands initially
                            if (detonationTimer > 55 && blackHoleRadius < maxBlackHoleRadius) {
                                blackHoleRadius++;
                            }

                            // black hole gets smaller over time
                            if (detonationTimer == 35 || detonationTimer == 45 || detonationTimer == 55) {
                                blackHoleRadius -= 2;
                                player.playSound(player, Sound.ENTITY_WITHER_HURT, 1f, 1f);
                            }

                            // black hole particles
                            particleCircles = blackHoleRadius + 10;
                            particleSphere(Particle.SQUID_INK, center, blackHoleRadius, particleCircles);

                            // pull
                            for (LivingEntity entity : getNearbyEntitiesExcludingPlayer(player, center, pullRadius)) {
                                Vector toCenter = center.clone().subtract(entity.getLocation()).toVector();
                                Vector pull = toCenter.normalize().multiply(.03).setY(.06);

                                entity.setVelocity(entity.getVelocity().add(pull));
                            }

                            // explosion
                            if (detonationTimer == 0) {
                                cancel();
                                player.stopSound(Sound.ITEM_ELYTRA_FLYING);
                                player.playSound(player, Sound.ENTITY_WITHER_SPAWN, 2f, 1f);
                                player.playSound(player, Sound.ENTITY_WITHER_DEATH, .5f, 1f);
                                expandingParticleSphere(Particle.SQUID_INK, center, 2, particleCircles * 2, 1.2);

                                // explosion particles
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < 8; i++) {
                                            double randX = ThreadLocalRandom.current().nextDouble(-maxBlackHoleRadius, maxBlackHoleRadius);
                                            double randY = ThreadLocalRandom.current().nextDouble(-maxBlackHoleRadius, maxBlackHoleRadius);
                                            double randZ = ThreadLocalRandom.current().nextDouble(-maxBlackHoleRadius, maxBlackHoleRadius);
                                            Location explosionLocation = center.clone().add(randX, randY, randZ);

                                            player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, explosionLocation, 1);
                                        }
                                    }
                                }.runTaskLater(nmlAbilities, 3);

                                for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, center, maxBlackHoleRadius)) {
                                    Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                                }
                            }
                        }
                    }.runTaskTimer(nmlAbilities, 0L, 1L);
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }
}