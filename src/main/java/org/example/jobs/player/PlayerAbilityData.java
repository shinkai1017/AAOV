package org.example.jobs.player;

import org.example.jobs.model.AbilityType;

import java.util.HashSet;
import java.util.Set;


/**
 * 各プレイヤーの能力開放状況を管理するクラス。
 * 開放された能力は内部で Set によって保持され、照会・追加・一括設定が可能。
 */


public class PlayerAbilityData {

    // 開放済みの能力を格納するセット
    private final Set<AbilityType> unlockedAbilities;

    public PlayerAbilityData() {
        this.unlockedAbilities = new HashSet<>();
    }

    public PlayerAbilityData(Set<AbilityType> unlockedAbilities) {
        this.unlockedAbilities = unlockedAbilities;
    }
    /**
     * 指定された能力がプレイヤーによって開放済みかどうかを判定します。
     *
     * @param ability 判定対象の能力
     * @return 開放されていれば true
     */
    public boolean hasAbility(AbilityType ability) {
        return unlockedAbilities.contains(ability);
    }


    /**
     * 指定された能力を開放済みリストに追加します。
     *
     * @param ability 開放する能力
     */
    public void unlockAbility(AbilityType ability) {
        unlockedAbilities.add(ability);
    }


    /**
     * 現在開放されているすべての能力を取得します。
     *
     * @return 開放済み能力のセット
     */
    public Set<AbilityType> getUnlockedAbilities() {
        return unlockedAbilities;
    }


    /**
     * 開放済み能力リストを外部から設定し直します（既存の内容はクリアされます）。
     *
     * @param abilities 新しい開放済み能力のセット
     */
    public void setUnlockedAbilities(Set<AbilityType> abilities) {
        unlockedAbilities.clear();
        unlockedAbilities.addAll(abilities);
    }
}
