package io.github.NoOne.nMLAbilities.abilitySystem;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AbilityEffects { 
    public static void particleSphere(Particle particle, Location center, double radius, int particleCircles) {
        for (double i = 0; i <= Math.PI; i += Math.PI / particleCircles) { // vertical circles
            double r = Math.sin(i) / radius;
            double y = Math.cos(i) / radius;

            for (double a = 0; a < Math.PI * 2; a+= Math.PI / particleCircles) { // horizontal circles
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                Location particleLocation = center.clone().add(x, y, z);

                center.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
                particleLocation.subtract(x, y, z); // reset location
            }
        }
    }

    public static void particleSphere(Particle.DustOptions dustOptions, Location center, double radius, int particleCircles) {
        for (double i = 0; i <= Math.PI; i += Math.PI / particleCircles) { // vertical circles
            double r = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;

            for (double a = 0; a < Math.PI * 2; a+= Math.PI / particleCircles) { // horizontal circles
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                Location particleLocation = center.clone().add(x, y, z);

                center.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, 0, 0, 0, dustOptions);
                particleLocation.subtract(x, y, z); // reset location
            }
        }
    }

    public static void expandingParticleSphere(Particle particle, Location center, double radius, int particleCircles, double speed) {
        for (double i = 0; i <= Math.PI; i += Math.PI / particleCircles) { // vertical circles
            double r = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;

            for (double a = 0; a < Math.PI * 2; a+= Math.PI / particleCircles) { // horizontal circles
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                Location particleLocation = center.clone().add(x, y, z);
                Vector velocity = particleLocation.toVector().subtract(center.toVector()).normalize().multiply(speed);

                center.getWorld().spawnParticle(particle, particleLocation, 0, velocity.getX(), velocity.getY(), velocity.getZ());
                particleLocation.subtract(x, y, z); // reset location
            }
        }
    }

    public static void horizontalParticleCircle(Particle particle, Location center, double radius, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            Location particleLocation = center.clone().add(x, 0, z);

            center.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
        }
    }

    public static void expandingHorizontalParticleCircle(Particle particle, Location center, double radius, int particleCount, double speed) {
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Location particleLocation = center.clone().add(x, 0, z);
            Vector velocity = particleLocation.toVector().subtract(center.toVector()).normalize().multiply(speed);

            center.getWorld().spawnParticle(particle, particleLocation,0, velocity.getX(), velocity.getY(), velocity.getZ());
        }
    }

    public static void verticalParticleCircleFacingEntity(Particle.DustOptions dustOptions, Entity entity, double radius, int particleCount, double distanceFromEntity) {
        Location center = entity.getLocation().add(0, 1.5, 0).add(entity.getLocation().getDirection().multiply(distanceFromEntity)); // blocks in front
        Vector dirX = entity.getLocation().getDirection().normalize(); // Face forward vector
        Vector dirY = new Vector(0, 1, 0); // Up vector
        Vector dirZ = dirX.clone().crossProduct(dirY).normalize(); // Right vector

        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI / particleCount) * i;
            double xOffset = Math.cos(angle) * radius;
            double yOffset = Math.sin(angle) * radius;
            Location loc = center.clone().add(dirZ.clone().multiply(xOffset)).add(dirY.clone().multiply(yOffset));

            entity.getWorld().spawnParticle(Particle.DUST, loc, 1, 0, 0, 0, 0, dustOptions);
        }
    }

    public static void verticalParticleCircleBetweenEntities(Particle.DustOptions dustOptions, Entity entity1, Entity entity2, double radius, int particleCount) {
        Location e1Loc = entity1.getLocation().add(0, 1, 0);
        Location e2Loc = entity2.getLocation().add(0, 1, 0);
        Location center = e1Loc.clone().add(e2Loc.toVector().subtract(e1Loc.toVector()).multiply(0.5));
        Vector dirX = e2Loc.toVector().subtract(e1Loc.toVector()).normalize();
        Vector dirY = new Vector(0, 1, 0);
        Vector dirZ = dirX.clone().crossProduct(dirY).normalize();

        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI / particleCount) * i;
            double xOffset = Math.cos(angle) * radius;
            double yOffset = Math.sin(angle) * radius;
            Location loc = center.clone().add(dirZ.clone().multiply(xOffset)).add(dirY.clone().multiply(yOffset));

            entity1.getWorld().spawnParticle(Particle.DUST, loc, 1, 0, 0, 0, 0, dustOptions);
        }
    }

    public static void particleLine(Particle particle, Location start, Location end, int particleCount) {
        World world = start.getWorld();
        Vector startVec = start.toVector();
        Vector direction = end.toVector().subtract(startVec);

        for (int i = 0; i < particleCount; i++) {
            double t = (double) i / (particleCount - 1); // 0 → 1 inclusive

            Vector point = startVec.clone().add(direction.clone().multiply(t));
            world.spawnParticle(particle, point.toLocation(world), 1, 0, 0, 0, 0);
        }
    }
}
