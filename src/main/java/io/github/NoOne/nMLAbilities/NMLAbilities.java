package io.github.NoOne.nMLAbilities;

import io.github.NoOne.menuSystem.MenuListener;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityEffects.AbilityEffectsTracker;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityUse.AbilityListener;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesListener;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesManager;
import io.github.NoOne.nMLAbilities.commands.ExpertiseCommand;
import io.github.NoOne.nMLAbilities.commands.SetRotationCommand;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityEffectsListener;
import io.github.NoOne.nMLConfig.ConfigTemplate;
import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.StatConfigManager;
import io.github.NoOne.nMLShields.GuardingSystem;
import io.github.NoOne.nMLShields.NMLShields;
import io.github.NoOne.nMLSkills.NMLSkills;
import io.github.NoOne.nMLSkills.SaveSkillsManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NMLAbilities extends JavaPlugin {
    private static NMLAbilities instance;
    private StatConfigManager statConfigManager;
    private SaveSkillsManager saveSkillsManager;
    private GuardingSystem guardingSystem;
    private SelectedAbilitiesManager selectedAbilitiesManager;
    private ConfigTemplate selectedAbilitiesConfig;

    @Override
    public void onEnable() {
        instance = this;

        statConfigManager = JavaPlugin.getPlugin(NMLPlayerStats.class).getStatConfigManager();
        saveSkillsManager = JavaPlugin.getPlugin(NMLSkills.class).getSaveSkillsManager();
        guardingSystem = JavaPlugin.getPlugin(NMLShields.class).getGuardingSystem();

        selectedAbilitiesConfig = new ConfigTemplate(this, "abilities");
        selectedAbilitiesConfig.loadConfig();

        selectedAbilitiesManager = new SelectedAbilitiesManager(this);
        selectedAbilitiesManager.loadSelectedAbilitiesFromConfig();

        CooldownManager.start();
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
            AbilityEffectsTracker.removeAllOngoingAbilityEffects(player, statConfigManager.getStats(player.getUniqueId()));
        }

        AbilityEffectsTracker.removeAllDisplays();
        selectedAbilitiesManager.saveAllSelectedAbilitiesToConfig();
        selectedAbilitiesConfig.saveConfig();
    }

    public static NMLAbilities getInstance() {
        return instance;
    }

    public SelectedAbilitiesManager getSelectedAbilitiesManager() {
        return selectedAbilitiesManager;
    }

    public SaveSkillsManager getSaveSkillsManager() {
        return saveSkillsManager;
    }

    public ConfigTemplate getSelectedAbilitiesConfig() {
        return selectedAbilitiesConfig;
    }

    public GuardingSystem getGuardingSystem() {
        return guardingSystem;
    }

    public StatConfigManager getStatConfigManager() {
        return statConfigManager;
    }
}
