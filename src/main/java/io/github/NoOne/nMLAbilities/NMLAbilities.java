package io.github.NoOne.nMLAbilities;

import io.github.NoOne.menuSystem.MenuListener;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityListener;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityEffects.AbilityEffectsTracker;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesConfig;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesListener;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesManager;
import io.github.NoOne.nMLAbilities.commands.ExpertiseCommand;
import io.github.NoOne.nMLAbilities.commands.SetRotationCommand;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityEffectsListener;
import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLShields.GuardingSystem;
import io.github.NoOne.nMLShields.NMLShields;
import io.github.NoOne.nMLSkills.NMLSkills;
import io.github.NoOne.nMLSkills.skillSetSystem.SkillSetManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NMLAbilities extends JavaPlugin {
    private static NMLAbilities instance;
    private ProfileManager profileManager;
    private SkillSetManager skillSetManager;
    private GuardingSystem guardingSystem;
    private SelectedAbilitiesManager selectedAbilitiesManager;
    private SelectedAbilitiesConfig selectedAbilitiesConfig;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;

        profileManager = JavaPlugin.getPlugin(NMLPlayerStats.class).getProfileManager();
        skillSetManager = JavaPlugin.getPlugin(NMLSkills.class).getSkillSetManager();
        guardingSystem = JavaPlugin.getPlugin(NMLShields.class).getGuardingSystem();

        selectedAbilitiesConfig = new SelectedAbilitiesConfig(this, "abilities");
        selectedAbilitiesConfig.loadConfig();

        selectedAbilitiesManager = new SelectedAbilitiesManager(this);
        selectedAbilitiesManager.loadSelectedAbilitiesFromConfig();

        cooldownManager = new CooldownManager(this);
        cooldownManager.start();

        AbilityEffectsTracker.startDisplayTracker();

        getCommand("expertise").setExecutor(new ExpertiseCommand(this));
        getCommand("setRotation").setExecutor(new SetRotationCommand());
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new SelectedAbilitiesListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new ExpertiseAbilityEffectsListener(this), this);
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            AbilityEffectsTracker.removeAllOngoingAbilityEffects(player, profileManager.getPlayerProfile(player.getUniqueId()).getStats());
        }

        AbilityEffectsTracker.removeAllDisplays();
        cooldownManager.stop();
        selectedAbilitiesManager.saveAllSelectedAbilitiesToConfig();
        selectedAbilitiesConfig.saveConfig();
    }

    public static NMLAbilities getInstance() {
        return instance;
    }

    public SelectedAbilitiesManager getSelectedAbilitiesManager() {
        return selectedAbilitiesManager;
    }

    public SkillSetManager getSkillSetManager() {
        return skillSetManager;
    }

    public SelectedAbilitiesConfig getSelectedAbilitiesConfig() {
        return selectedAbilitiesConfig;
    }

    public GuardingSystem getGuardingSystem() {
        return guardingSystem;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }
}
