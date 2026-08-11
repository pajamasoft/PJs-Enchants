package net.pajamasoft.pjenchants;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.pajamasoft.pjLib.PJLib.format;
import static net.pajamasoft.pjenchants.PJEnchants.removeCustomEnchantments;

public class commands implements CommandExecutor {
    PJEnchants pjEnchants;
    File playerdata;
    FileConfiguration data;
    String prefix = "§7[§bPJ§5Enchants§7] ";

    commands(PJEnchants pjEnchants){
        this.pjEnchants = pjEnchants;
        playerdata = pjEnchants.playerdata;
        data = pjEnchants.data;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        try{
            Player p = (Player) sender;

            if(!p.isOp())
                return false;

            if(args[0].equalsIgnoreCase("enchant")){
                pjEnchants.enchant(p.getInventory().getItemInMainHand(),Enchant.valueOf(args[1].toUpperCase()),args[2] == null ? 1 : Integer.parseInt(args[2]));
            }

            if(args[0].equalsIgnoreCase("iscompatible")){
                if(args.length==3){
                    p.sendMessage("§aEnchantments §e"+args[1]+"§a and §e"+args[2]+"§a are "+ (pjEnchants.isCompatible(Enchant.valueOf(args[1].toUpperCase()),Enchant.valueOf(args[2].toUpperCase())) ? "§a§lcompatible" : "§c§lnot compatible"));
                }
                else{
                    p.sendMessage("Usage: /pje iscompatible e1 e2");
                }
            }

            if(args[0].equalsIgnoreCase("remove")){
                ItemStack hand = p.getInventory().getItemInMainHand();
                ItemMeta meta = hand.getItemMeta();
                List<String> lore = hand.getItemMeta().getLore();
                List<String> newlore = new ArrayList<>();
                for(String ce:lore) {
                    if (!ce.toLowerCase().contains(args[1].toLowerCase()))
                        newlore.add(ce);
                }
                if(lore.size() > newlore.size())
                    p.sendMessage(prefix + "Successfully removed enchant.");
                meta.setLore(newlore);
                hand.setItemMeta(meta);
                p.getInventory().setItemInMainHand(hand);
            }

            if(args[0].equalsIgnoreCase("removeall")){
                ItemStack hand = p.getInventory().getItemInMainHand();
                removeCustomEnchantments(hand);
            }

            if(args[0].equalsIgnoreCase("getsharedenchants")){
                try{
                    p.sendMessage("Shared enchants: "+ Enchant.getSharedEnchants(Set.of(ItemType.valueOf(args[1].toUpperCase()),ItemType.valueOf(args[2].toUpperCase()))));
                }
                catch(Exception ex){
                    p.sendMessage("Incorrect usage.");
                    ex.printStackTrace();
                }
            }
            if(args[0].equalsIgnoreCase("format")){
                try{
                    p.sendMessage(format(args[1]));
                }catch(Exception ex){
                    //
                }
            }

        }catch(Exception e){
            if(e instanceof IndexOutOfBoundsException)
                sender.sendMessage(prefix+"§cNot enough arguments!");
        }
        return true;
    }
}

