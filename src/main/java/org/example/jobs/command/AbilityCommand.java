package org.example.jobs.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.jobs.gui.AbilityGUI;

public class AbilityCommand implements CommandExecutor {

    // /ability コマンドが呼ばれたときの処理
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // コンソールからの実行は拒否
        if (!(sender instanceof Player player)) {
            sender.sendMessage("このコマンドはプレイヤーのみ使用できます。");
            return true;
        }

        // GUI を開く
        AbilityGUI.openAbilityGUI(player);
        return true;
    }
}

