package org.example.jobs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.jobs.external.JobsHook;
import org.example.jobs.jobs.JobAPIManager;
import org.example.jobs.storage.AbilityStorage;
import org.example.jobs.listener.JobsLevelUpListener;
import org.example.jobs.gui.AbilityGUI;
import org.example.jobs.player.PlayerAbilityData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityPlugin extends JavaPlugin {

    private static final Map<UUID, PlayerAbilityData> playerAbilityDataMap = new HashMap<>();


    private static AbilityPlugin instance;

    // 他クラスでアクセスするためのシングルトン的な取得方法
    public static AbilityPlugin getInstance() {
        return instance;
    }

    // Jobs連携用のクラス
    private JobAPIManager jobAPIManager;

    // プレイヤーデータ保存用のクラス
    private AbilityStorage abilityStorage;


    @Override
    public void onEnable() {
        // データフォルダの作成
        saveDefaultConfig();
        getDataFolder().mkdirs();

        // イベント登録
        Bukkit.getPluginManager().registerEvents(new AbilityGUI(), this);


        abilityStorage = new AbilityStorage(this);

        // ジョブレベルアップイベントのリスナーを登録
        getServer().getPluginManager().registerEvents(new JobsLevelUpListener(abilityStorage), this);


        // 他の初期化コード（JobsHook など）もここで行うと良い
        JobsHook.init(abilityStorage);

        // イベントリスナー登録
        getServer().getPluginManager().registerEvents(new JobsLevelUpListener(this.abilityStorage), this);

        instance = this;
        // 各管理クラスを初期化
        this.jobAPIManager = new JobAPIManager();
        this.abilityStorage = new AbilityStorage(this);

        // コマンド登録（plugin.yml にも記述必要）
        getCommand("ability").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof org.bukkit.entity.Player player)) {
                sender.sendMessage("このコマンドはプレイヤー専用です。");
                return true;
            }

            // GUIを開く
            AbilityGUI.openAbilityGUI(player);
            return true;
        });

        getLogger().info("AbilityPlugin が有効化されました！");

        // 他の処理もここに記述
        getLogger().info("Ability plugin enabled!");
    }


    @Override
    public void onDisable() {
        // プレイヤーデータの保存
        for (UUID uuid : playerAbilityDataMap.keySet()) {
            abilityStorage.savePlayerData(uuid); // ← インスタンスメソッドで保存
        }

        getLogger().info("AbilityPlugin が無効になりました。データを保存しました。");
    }

    /**
     * プレイヤーデータを取得（なければ読み込む）
     */
    public static PlayerAbilityData getAbilityData(UUID uuid) {
        return playerAbilityDataMap.computeIfAbsent(uuid, id -> instance.abilityStorage.load(id));
    }

    /**
     * データを手動で保存（今後必要になる可能性あり）
     */
    public static void saveAbilityData(UUID uuid) {
        PlayerAbilityData data = playerAbilityDataMap.get(uuid);
        if (data != null) {
            instance.abilityStorage.savePlayerData(uuid);  // ← 修正ポイント
        }
    }

    // ↓ saveAbilityData の外に書く（メソッド同士は並列に書く必要あり）
    public AbilityStorage getAbilityStorage() {
        return abilityStorage;
    }

    public JobAPIManager getJobAPIManager() {
        return jobAPIManager;
    }
}