/**
 * AbilityStorage:
 * - 各プレイヤーの職業レベルと解放済みアビリティを管理する。
 * - 永続化は plugins/YourPlugin/data/UUID.yml に保存される。
 * - 職業レベルは static Map で管理され、クラス全体で共有される。
 * - AbilityType に対応するデータ保存とロード処理を行い、PlayerAbilityData を介して操作する。
 */

package org.example.jobs.storage;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.jobs.model.AbilityType;
import org.example.jobs.player.PlayerAbilityData;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AbilityStorage {

    private final JavaPlugin plugin;
    private final Map<UUID, PlayerAbilityData> playerDataMap = new HashMap<>();

    // 職業レベル保存用マップ
    private static final Map<UUID, Map<String, Integer>> playerJobLevels = new HashMap<>();

    public AbilityStorage(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // プレイヤーの職業レベルを保存
    public void setJobLevel(UUID playerId, String jobName, int level) {
        playerJobLevels
                .computeIfAbsent(playerId, k -> new HashMap<>())
                .put(jobName.toLowerCase(), level);
    }

    // プレイヤーの職業レベルを取得
    public int getJobLevel(UUID playerId, String jobName) {
        return playerJobLevels
                .getOrDefault(playerId, Collections.emptyMap())
                .getOrDefault(jobName.toLowerCase(), 0);
    }

    // プレイヤーデータ保存
    public void savePlayerData(UUID uuid) {
        PlayerAbilityData data = playerDataMap.get(uuid);
        if (data == null) return;

        File file = new File(plugin.getDataFolder(), "data/" + uuid + ".yml");
        file.getParentFile().mkdirs();
        YamlConfiguration config = new YamlConfiguration();

        List<String> unlocked = new ArrayList<>();
        for (AbilityType ability : data.getUnlockedAbilities()) {
            unlocked.add(ability.name());
        }
        config.set("unlocked", unlocked);

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save player data: " + uuid);
        }
    }

    // プレイヤーデータ読み込み
    public PlayerAbilityData load(UUID uuid) {
        File file = new File(plugin.getDataFolder(), "data/" + uuid + ".yml");
        PlayerAbilityData data = new PlayerAbilityData();

        if (!file.exists()) return data;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> unlocked = config.getStringList("unlocked");

        Set<AbilityType> abilitySet = new HashSet<>();
        for (String name : unlocked) {
            try {
                abilitySet.add(AbilityType.valueOf(name));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Unknown ability in data: " + name);
            }
        }

        data.setUnlockedAbilities(abilitySet);
        playerDataMap.put(uuid, data);
        return data;
    }

    // 全プレイヤーデータ保存
    public void saveAll() {
        for (UUID uuid : playerDataMap.keySet()) {
            savePlayerData(uuid);
        }
    }
}
