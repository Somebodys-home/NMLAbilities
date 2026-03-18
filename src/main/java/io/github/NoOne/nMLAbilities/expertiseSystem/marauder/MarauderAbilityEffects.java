package io.github.NoOne.nMLAbilities.expertiseSystem.marauder;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageConverter;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class MarauderAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public MarauderAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void bladeTornado(Player user, int hotbarSlot) {
        Stats stats = profileManager.getPlayerProfile(user.getUniqueId()).getStats();
        HashMap<DamageType, Double> damage = DamageConverter.multiplyDamageMap(DamageConverter.convertPlayerStats2Damage(stats), .5);

        EnergyManager.useEnergy(user, 30);
        CooldownManager.putAllOtherAbilitiesOnCooldown(user, 6, hotbarSlot);
        AttackCooldownSystem.setOrPauseAttackCooldown(user, 6);
        user.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(1);

        new BukkitRunnable() {
            int tornadoTicks = 100;

            @Override
            public void run() {
                tornadoTicks--;

                /// tiny dash
                Vector knockback = user.getLocation().getDirection().multiply(.5);
                knockback.setY(-2);
                user.setVelocity(knockback);

                /// particles
                if (tornadoTicks % 2 == 0) {
                    AbilityEffects.horizontalParticleCircle(Particle.SWEEP_ATTACK, user.getLocation().clone().add(new Vector(0, .5, 0)), 1, 4);
                    AbilityEffects.horizontalParticleCircle(Particle.SWEEP_ATTACK, user.getLocation().clone().add(new Vector(0, 1.25, 0)), 1.5, 6);
                    AbilityEffects.horizontalParticleCircle(Particle.SWEEP_ATTACK, user.getLocation().clone().add(new Vector(0, 2, 0)), 2, 8);
                }

                if (tornadoTicks % 3 == 0) user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, .5f);

                if (tornadoTicks % 5 == 0) {
                    for (Entity entity : user.getWorld().getNearbyEntities(user.getLocation(), 2.25, 2, 2.25)) {
                        if (entity instanceof LivingEntity livingEntity && !entity.equals(user)) {
                            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damage));
                        }
                    }
                }

                if (tornadoTicks == 0) {
                    this.cancel();
                    user.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(.6);
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }
}