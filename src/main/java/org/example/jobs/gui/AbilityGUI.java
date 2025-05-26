/**
 * プレイヤーに「能力解放GUI」を表示するクラス。
 * 各アビリティごとに：
 * - 解放済み（EMERALD）
 * - 解放可能（LIME）
 * - 解放不可（GRAY）
 * をアイテム表示で区別する。
 * 能力の条件チェックは AbilityStorage や JobAPIManager に委譲される。
 * GUI上では説明・状態などをロアで案内し、プレイヤーが分かりやすく選択できるようにする。
 */
package org.example.jobs.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.example.jobs.AbilityPlugin;
import org.example.jobs.jobs.JobAPIManager;
import org.example.jobs.model.AbilityType;
import org.example.jobs.player.PlayerAbilityData;

import java.util.Arrays;
import java.util.*;

public class AbilityGUI implements Listener {

    // 能力アイコンを配置するスロット（最大12個）
    private static final List<Integer> ABILITY_SLOTS = Arrays.asList(
            10, 11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24
    );

    // GUIを開く処理
    public static void openAbilityGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.GREEN + "アビリティ解放メニュー");

        PlayerAbilityData data = AbilityPlugin.getAbilityData(player.getUniqueId());
        AbilityType[] abilities = AbilityType.values();

        // 能力アイコンをスロットに配置
        for (int i = 0; i < ABILITY_SLOTS.size(); i++) {
            int slot = ABILITY_SLOTS.get(i);

            if (i < abilities.length) {
                AbilityType ability = abilities[i];
                boolean unlocked = data.hasAbility(ability);

                ItemStack icon = new ItemStack(ability.getIcon());
                ItemMeta meta = icon.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.GOLD + ability.getDisplayName());

                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.YELLOW + ability.getDescription());
                    lore.add("");
                    lore.add(unlocked
                            ? ChatColor.AQUA + "✔ 解放済み"
                            : ChatColor.GOLD + "クリックで解放！");
                    meta.setLore(lore);

                    icon.setItemMeta(meta);
                }

                inv.setItem(slot, icon);
            } else {
                // 余りスロットにはガラス板のプレースホルダーを表示
                ItemStack placeholder = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta meta = placeholder.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(" ");
                    placeholder.setItemMeta(meta);
                }
                inv.setItem(slot, placeholder);
            }
        }

        player.openInventory(inv);
    }

    // GUIクリックイベント
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (event.getView().getTitle().equals(ChatColor.GREEN + "アビリティ解放メニュー")) {
            event.setCancelled(true); // クリック無効化

            int clickedSlot = event.getSlot();
            int abilityIndex = ABILITY_SLOTS.indexOf(clickedSlot);

            if (abilityIndex == -1) return;

            AbilityType[] abilities = AbilityType.values();
            if (abilityIndex >= abilities.length) return;

            AbilityType ability = abilities[abilityIndex];
            PlayerAbilityData data = AbilityPlugin.getAbilityData(player.getUniqueId());

            if (data.hasAbility(ability)) {
                player.sendMessage(ChatColor.YELLOW + "このアビリティは既に解放されています。");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                return;
            }

            // 条件チェック
            if (!JobAPIManager.canUnlockAbility(player, ability)) {
                player.sendMessage(ChatColor.RED + "このアビリティを解放するには条件を満たしていません。");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.3f);
                return;
            }

            // 解放処理
            data.unlockAbility(ability);
            AbilityPlugin.saveAbilityData(player.getUniqueId());

            player.sendMessage(ChatColor.GREEN + "アビリティ「" + ability.getDisplayName() + "」を解放しました！");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

            // 再表示
            openAbilityGUI(player);
        }
    }
}