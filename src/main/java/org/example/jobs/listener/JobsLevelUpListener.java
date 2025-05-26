package org.example.jobs.listener;

import com.gamingmesh.jobs.api.JobsLevelUpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.example.jobs.storage.AbilityStorage;

import java.util.UUID;


/**
 * プレイヤーが職業レベルアップした際に発生するイベントを処理するリスナー。
 * レベルアップ情報を AbilityStorage に反映させることで、後の能力解放判定に活用する。
 */


public class JobsLevelUpListener implements Listener {

    private final AbilityStorage storage;

    /**
     * AbilityStorage を注入し、レベル記録の永続化や照会に利用する。
     *
     * @param storage プレイヤーの職業レベルを管理するストレージ
     */
    public JobsLevelUpListener(AbilityStorage storage) {
        this.storage = storage;
    }


    /**
     * JobsRebornのレベルアップイベントをキャッチし、該当プレイヤーの職業レベルを更新。
     *
     * @param event レベルアップ時に呼び出されるイベント
     */

    @EventHandler
    public void onJobLevelUp(JobsLevelUpEvent event) {
        // プレイヤーのUUIDを取得
        UUID playerId = event.getPlayer().getUniqueId();
        // レベルアップした職業の名前
        String jobName = event.getJob().getName();
        // 新しいレベル
        int newLevel = event.getLevel();

        // AbilityStorageにレベル更新を通知
        storage.setJobLevel(playerId, jobName, newLevel);
    }
}