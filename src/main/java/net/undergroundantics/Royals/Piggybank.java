package net.undergroundantics.Royals;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

// @Override
public class Piggybank implements CommandExecutor {
    private Economy economy = null;

    public void messagePlayer(Player player, String message) {
        player.sendMessage(message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                try {
                    Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    messagePlayer(player, "Invalid argument detected!");
                    return false;
                }
                Integer withdraw_amount = Integer.parseInt(args[0]);

                EconomyResponse er = economy.withdrawPlayer(player, withdraw_amount);
                if (er.transactionSuccess()) {
                    ItemStack royalitem = new ItemStack(Material.EMERALD, withdraw_amount);
                    ItemMeta meta = (ItemMeta) royalitem.getItemMeta();
                    meta.setDisplayName("Royal");
                    meta.addEnchant(Enchantment.DURABILITY, 1, false);
                    final List<String> lore = Arrays.asList("A valuable gemstone from an",
                            "era long forgotten.");
                    meta.setLore(lore);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    // Set the amount of the ItemStack
                    royalitem.setItemMeta(meta);

                    // Give the player our items (comma-seperated list of all ItemStack)
                    player.getInventory().addItem(royalitem);
                    return true;
                } else {
                    messagePlayer(player, "Not enough currency");
                    return false;
                }
            } else if (args.length == 0) {
                messagePlayer(player, "Please specify how many royals you would like to withdraw.");
                return false;
            } else {
                messagePlayer(player, "Too many arguments detected!");
                return false;
            }
        } else {
            return false;
        }
    }
}
