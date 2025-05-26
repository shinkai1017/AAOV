
package org.example.jobs.model;

import org.bukkit.Material;


public enum AbilityType {
    ENCHANTER("偽のアンデット特攻", "一部エンチャントの効果が変化する", Material.ENCHANTED_BOOK, "Enchanter"),
    INFINITE_POTION("無限ポーション", "ポーションとレッドストーンをクラフトして9999999tickの効果を付与", Material.POTION, "Brewer"),
    DOUBLE_OUTPUT("クラフト2倍", "建材クラフト時に成果物が2倍になる", Material.CRAFTING_TABLE, "Crafter"),
    AIR_BUILD("空中建築", "空中に浮いて建築できる特殊状態", Material.FEATHER, "Builder"),
    MASS_DIG("範囲掘削", "5×5×5の範囲を一括で掘る", Material.IRON_PICKAXE, "Digger"),
    TREE_FELLER("木こりスキル", "木を一気に倒す", Material.STONE_AXE, "Lumberjack"),
    AUTO_SMELT("自動精錬", "採掘時に自動で精錬されたアイテムを得る", Material.FURNACE, "Miner"),
    FAST_FARMING("高速収穫", "農作物の成長と収穫が高速化", Material.WHEAT, "Farmer"),
    XP_BOOST("経験値ブースト", "指定行動で得られる経験値が上昇", Material.EXPERIENCE_BOTTLE, "Scholar"),
    SPEED_BOOST("移動速度強化", "一定時間移動速度が大幅アップ", Material.SUGAR, "Runner"),
    FIRE_RESIST("火炎耐性", "一定時間炎・溶岩無効", Material.BLAZE_POWDER, "Fireman"),
    NIGHT_VISION("暗視能力", "夜間でもはっきり見える", Material.SPYGLASS, "Scout");

    private final String displayName;
    private final String description;
    private final Material icon;
    private final String requiredJobName;

    AbilityType(String displayName, String description, Material icon, String requiredJobName) {
        this.displayName = displayName;
        this.description = description;
        this.icon = icon;
        this.requiredJobName = requiredJobName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Material getIcon() {
        return icon;
    }

    public String getRequiredJobName() {
        return requiredJobName;
    }
}