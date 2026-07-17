package net.pajamasoft.pjenchants;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

            if(!p.hasPermission(""))
                return false;

            if(args[0].equalsIgnoreCase("enchant")){
                pjEnchants.enchant(p.getInventory().getItemInMainHand(),Enchant.valueOf(args[1].toUpperCase()),Integer.parseInt(args[2]));
            }

//            else if(args[0].equalsIgnoreCase("curse"))
//                pjEnchants.addCurse(p.getInventory().getItemInMainHand());

//            if(args[0].equalsIgnoreCase("throwitem")){
//                Snowball ball = p.getWorld().spawn(p.getEyeLocation().add(p.getLocation().getDirection()),Snowball.class);
//                ball.setItem(new ItemStack(Material.IRON_SWORD,1));
//                ball.setVelocity(p.getLocation().getDirection().multiply(3));
//            }

            if(args[0].equalsIgnoreCase("iscompatible")){
                if(args.length==3){
                    p.sendMessage("§aEnchantments §e"+args[1]+"§a and §e"+args[2]+"§a are "+ (pjEnchants.isCompatible(Enchant.valueOf(args[1].toUpperCase()),Enchant.valueOf(args[2].toUpperCase())) ? "§a§lcompatible" : "§c§lnot compatible"));
                }
                else{
                    p.sendMessage("Usage: /pje iscompatible e1 e2");
                }
            }

//            if(args[0].equalsIgnoreCase("psphere")){
//                pjEnchants.particleSphere(Particle.FLAME,p.getLocation(),5,5);
//            }
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
//            if(args[0].equalsIgnoreCase("pring")){
//                if(args.length==4)
//                    pjEnchants.particleRing(Particle.FLAME,new Location(p.getWorld(),p.getLocation().getX()+Double.parseDouble(args[2]),p.getLocation().getY(),p.getLocation().getZ()+Double.parseDouble(args[3])),Double.parseDouble(args[1]),5);
//                else p.sendMessage("Format: pring {radius} {xoff} {zoff}");
//            }
//            if(args[0].equalsIgnoreCase("arrows"))
//                try{
//                    p.setArrowsInBody(Integer.parseInt(args[1]));
//                }catch(Exception ex){}
//            if(args[0].equalsIgnoreCase("books"))
//                p.sendMessage("§aTotal safe bookshelves nearby: §e"+ pjEnchants.getNearbyShelves(p.getLocation().getBlock()));

//            if(args[0].equalsIgnoreCase("rename")){
//                int x = 1;
//                String fixed = "";
//                while(args[x]!=null) {
//                    String name = args[x];
//                    for (int i = 0; i < name.length(); i++) {
//                        if (name.charAt(i) == '&')
//                            fixed += "§";
//                        else fixed += name.charAt(i);
//                    }
//                    fixed += " ";
//                    x++;
//                }
//                ItemStack item = p.getInventory().getItemInMainHand();
//                ItemMeta meta = item.getItemMeta();
//                meta.setDisplayName(fixed);
//                item.setItemMeta(meta);
//            }

            if(args[0].equalsIgnoreCase("getsharedenchants")){
                try{
                    p.sendMessage("Shared enchants: "+ Enchant.getSharedEnchants(Set.of(ItemType.valueOf(args[1].toUpperCase()),ItemType.valueOf(args[2].toUpperCase()))));
                }
                catch(Exception ex){
                    p.sendMessage("Incorrect usage.");
                    ex.printStackTrace();
                }
            }

        }catch(Exception e){
            if(e instanceof IndexOutOfBoundsException)
                sender.sendMessage(prefix+"§cNot enough arguments!");
        }
        return true;
    }
}

