package io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLShields.GuardingSystem;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ShieldHeroAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static GuardingSystem guardingSystem;

    public ShieldHeroAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        guardingSystem = nmlAbilities.getGuardingSystem();
    }

    public static void secondWind(Player user, int hotbarSlot) {
        EnergyManager.useEnergy(user, 20);
        CooldownManager.putAllOtherAbilitiesOnCooldown(user, 2, hotbarSlot);
        AttackCooldownSystem.setOrPauseAttackCooldown(user, 2);
        user.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 30, 10, false, false, false));
        user.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 30, 255, false, false, false));
        user.playSound(user, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1f, 1f);

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                timer++;

                /// charge up
                double radius = 5 * (1 - (timer / 30.0));
                int particleCount = 75;
                Location center = user.getLocation().clone().add(0, 0.15, 0);

                AbilityEffects.horizontalParticleCircle(Particle.END_ROD, center, radius, particleCount);

                if (timer % 10 == 0 && timer != 30) {
                    user.playSound(user, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1f, 1f);
                }

                /// explosion
                if (timer == 30) {
                    AbilityEffects.expandingParticleSphere(Particle.END_ROD, user.getLocation(), 4, 30, .3);
                    user.playSound(user, Sound.ITEM_TOTEM_USE, 1f, 1f);
                    guardingSystem.fullyRegenerateGuard(user);
                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }
}