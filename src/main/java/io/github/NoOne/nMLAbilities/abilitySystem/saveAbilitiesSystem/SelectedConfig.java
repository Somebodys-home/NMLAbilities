package io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class SelectedConfig {
    private NMLAbilities nmlAbilities;
    private File file;
    private String fileName;
    private FileConfiguration config = new YamlConfiguration();

    public SelectedConfig(NMLAbilities nmlAbilities, String filename) {
        this.nmlAbilities = nmlAbilities;
        this.fileName = filename;
        file = new File(nmlAbilities.getDataFolder(), filename + ".yml");
    }

    public void loadConfig() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            nmlAbilities.saveResource(fileName + ".yml", false);
        } try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
