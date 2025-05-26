package org.example.jobs.jobs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.example.jobs.model.AbilityType;
import org.example.jobs.external.JobsHook;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.gamingmesh.jobs.Jobs;

import com.gamingmesh.jobs.api.JobsLeaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

/**
 * プレイヤーが特定の能力（AbilityType）を解放できるかどうかを判定するユーティリティクラス。
 * 各能力に対応する職業名と、必要なジョブレベルを定義。
 */

public class JobAPIManager {

    /**
     * プレイヤーが指定された能力を使用するのに必要な条件を満たしているかを判定します。
     *
     * @param player 判定対象のプレイヤー
     * @param ability 判定する能力タイプ
     * @return 解放条件（ジョブ名とレベル）を満たしていれば true
     */

    /**
     * 各能力に対応する職業名を返します。
     *
     * @param ability 対象の能力タイプ
     * @return 職業名（Jobsプラグインで使用される名前）
     */


    public static boolean canUnlockAbility(Player player, AbilityType ability) {
        JobsPlayer jp = Jobs.getPlayerManager().getJobsPlayer(player);
        if (jp == null) return false;

        switch (ability) {
            case MASS_DIG:
                // Diggerの範囲掘削処理（仮）
                return true;

            case INFINITE_POTION:
                // Brewerの無限ポーション処理（仮）
                return true;

            case DOUBLE_OUTPUT:
                // Crafterの成果倍増処理（仮）
                return true;

            case AIR_BUILD:
                // Builderの空中建築処理（仮）
                return true;

            case ENCHANTER:
                // Enchanterの偽エンチャ処理（仮）
                return true;

            default:
                // その他（ありえないが安全策）
                return false;
        }
    }




    /**
     * 各能力の必要ジョブレベル（仮設定）を返します。
     * 今後、能力ごとに個別設定が可能。
     *
     * @param ability 対象の能力タイプ
     * @return 必要ジョブレベル（現在はすべて2で固定）
     */

    //個別レベル設定の項目
    private int getRequiredLevel(AbilityType ability) {
        return switch (ability) {
            case INFINITE_POTION -> 1;
            case DOUBLE_OUTPUT -> 1;
            case AIR_BUILD -> 1;
            case MASS_DIG -> 1;
            case ENCHANTER -> 1;
            case TREE_FELLER -> 1;
            case AUTO_SMELT -> 1;
            case FAST_FARMING -> 1;
            case XP_BOOST -> 1;
            case SPEED_BOOST -> 1;
            case FIRE_RESIST -> 1;
            case  NIGHT_VISION -> 1;
        };
    }

    public class JobLeaveListener implements Listener {

        @EventHandler
        public void onJobLeave(JobsLeaveEvent event) {
            Player player = event.getPlayer();
            String jobName = event.getJob().getName();

            // 対応する能力を取得
            AbilityType ability = AbilityType.getByJobName(jobName);
            if (ability == null) return;

            // Jobレベルの内部記録をリセット
            AbilityPlugin.resetJobLevel(player.getUniqueId(), jobName);

            // アビリティを剥奪
            PlayerAbilityData data = AbilityPlugin.getAbilityData(player.getUniqueId());
            if (data.hasAbility(ability)) {
                data.removeAbility(ability);
                player.sendMessage(ChatColor.RED + "職業[" + jobName + "]の離脱により、アビリティ「" + ability.getDisplayName() + "」が剥奪されました。");
            }

            // 保存
            AbilityPlugin.saveAbilityData(player.getUniqueId());
        }
    }
}