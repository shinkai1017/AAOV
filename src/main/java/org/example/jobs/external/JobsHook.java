// このクラスは AbilityStorage を通じて職業レベル情報を取得するヘルパーです。
// 初期化時に AbilityStorage のインスタンスを受け取り、他のクラスから簡単に getJobLevel を呼び出せるようにします。
//
// イベントリスナーではなく、既に保存されたレベル情報にアクセスするためのラッパー（Hook）です。

package org.example.jobs.external;

import org.bukkit.entity.Player;
import org.example.jobs.storage.AbilityStorage;
import com.gamingmesh.jobs.container.JobsPlayer;

import java.util.UUID;

public class JobsHook {

    private static AbilityStorage storage;

    public static void init(AbilityStorage abilityStorage) {
        storage = abilityStorage;
    }
    public static int getJobLevel(Player player, String jobName) {
        if (player == null || jobName == null) return 0;
        UUID playerId = player.getUniqueId();
        return storage.getJobLevel(playerId, jobName);
    }
}