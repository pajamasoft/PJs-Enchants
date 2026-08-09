package net.pajamasoft.pjenchants;

import net.pajamasoft.pjCombat.PJCombat;
/*
 * ---------------------------------------------------
 *  PJ's Enchants
 *      71 Custom Enchantments for survival Minecraft
 * ---------------------------------------------------
 * by Nathan Cook @pajamasoft, nathan@pajamasoft.net
 * ---------------------------------------------------
 */

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;

import static net.pajamasoft.pjLib.PJLib.*;

public final class PJEnchants extends JavaPlugin {

    FileConfiguration data;
    File playerdata;
    public PJEnchants pjEnchants;
    public PJCombat combat;
    List<Player> online = new ArrayList<>();
    HashMap<UUID, Boolean> magnet = new HashMap<>();
    HashMap<UUID, ItemStack> wings = new HashMap<>();
    public static final List<Enchant> all_enchants = Enchant.getAllEnchants();
    public static final List<Enchant> all_curses = Enchant.getAllCurses();
    public static final List<Enchant> sword_enchants = getAvailableEnchants(ItemType.SWORD);
    public static final List<Enchant> spear_enchants = getAvailableEnchants(ItemType.SPEAR);
    public static final List<Enchant> axe_enchants = getAvailableEnchants(ItemType.AXE);
    public static final List<Enchant> melee_enchants = Enchant.getSharedEnchants(Set.of(ItemType.SPEAR, ItemType.AXE, ItemType.SWORD));
    public static final List<Enchant> pick_enchants = getAvailableEnchants(ItemType.PICKAXE);
    public static final List<Enchant> shovel_enchants = getAvailableEnchants(ItemType.SHOVEL);
    public static final List<Enchant> hoe_enchants = getAvailableEnchants(ItemType.HOE);
    public static final List<Enchant> tool_enchants = Enchant.getSharedEnchants(Set.of(ItemType.PICKAXE, ItemType.AXE, ItemType.SHOVEL, ItemType.HOE));
    public static final List<Enchant> helmet_enchants = getAvailableEnchants(ItemType.HELMET);
    public static final List<Enchant> chestplate_enchants = getAvailableEnchants(ItemType.CHESTPLATE);
    public static final List<Enchant> leggings_enchants = getAvailableEnchants(ItemType.LEGGINGS);
    public static final List<Enchant> boots_enchants = getAvailableEnchants(ItemType.BOOTS);
    public static final List<Enchant> armor_enchants = Enchant.getSharedEnchants(Set.of(ItemType.HELMET, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.BOOTS));
    public static final List<Enchant> bow_enchants = getAvailableEnchants(ItemType.BOW);
    public static final List<Enchant> horse_enchants = getAvailableEnchants(ItemType.HORSE_ARMOR);
    public static final List<Enchant> elytra_enchants = getAvailableEnchants(ItemType.ELYTRA);
    public static final List<Enchant> wolf_enchants = getAvailableEnchants(ItemType.WOLF_ARMOR);
    public static final List<Enchant> t1_enchants = getEnchantsOfTier(1);
    public static final List<Enchant> t2_enchants = getEnchantsOfTier(2);
    public static final List<Enchant> t3_enchants = getEnchantsOfTier(3);
    final static List<Material> mVals = Arrays.stream(Material.values()).toList();
    final static List<Material> pickaxe_blocks = new ArrayList<>(mVals){{
       removeIf(m -> !m.name().toUpperCase().contains("_ORE"));
    }};
    final List<Material> axe_blocks = new ArrayList<>(mVals){{
        removeIf(m -> !m.name().toUpperCase().contains("_WOOD") || !m.name().toUpperCase().contains("_LOG"));
    }};
    final List<Material> shovel_forged_blocks = Arrays.asList(Material.SAND,Material.RED_SAND,Material.CLAY_BALL);
    final List<Material> pickaxe_forged_blocks = Arrays.asList(
            Material.GOLD_ORE,Material.DEEPSLATE_GOLD_ORE,Material.NETHER_GOLD_ORE,Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,Material.COPPER_ORE,Material.DEEPSLATE_COPPER_ORE,Material.NETHERRACK,
            Material.CRACKED_STONE_BRICKS,Material.NETHERITE_SCRAP,Material.WET_SPONGE,Material.SANDSTONE,
            Material.BASALT,Material.RAW_IRON_BLOCK,Material.RAW_GOLD_BLOCK,Material.RAW_COPPER_BLOCK,
            Material.STONE,Material.ANCIENT_DEBRIS);
    List<Player> nightrider = new ArrayList<>();
    HashMap<UUID, List<ItemStack>> armor = new HashMap<>();
    final static String[] numerals = {"I","II","III","IV","V","VI","VII","VIII","IX","X"};

    @Override
    public void onEnable() {
        pjEnchants = (PJEnchants)Bukkit.getPluginManager().getPlugin("PJEnchants");

        try {
            combat = (PJCombat) Bukkit.getPluginManager().getPlugin("PJsCombat");
        }catch(Exception ex){
            //
        }

        getLogger().info("[PJEnchants] Plugin is active");
        getServer().getPluginManager().registerEvents(new listener(this), this);
        this.getCommand("pjenchants").setExecutor(new commands(this));
        runHoldingCheck();
    }

    @Override
    public void onDisable() {

        for(Player p:online)
            logout(p,false);

        getLogger().info("[PJEnchants] Plugin has been disabled");
    }

    public void logout(Player p, boolean remove){
        if(remove)
            pjEnchants.online.remove(p);
        pasteArmor(p); // if player was using stealth, they keep their armor

        if(wings.containsKey(p.getUniqueId()))
            p.getInventory().setChestplate(wings.get(p.getUniqueId()));
        p.closeInventory();
    }

    public void pasteArmor(Player p){
        UUID id = p.getUniqueId();
        if(armor.containsKey(id)) {
            p.getInventory().setHelmet(armor.get(id).get(0));
            p.getInventory().setChestplate(armor.get(id).get(1));
            p.getInventory().setLeggings(armor.get(id).get(2));
            p.getInventory().setBoots(armor.get(id).get(3));
            armor.remove(id);
        }
    }

    public static boolean isNegativeEffect(PotionEffectType p){
        List<PotionEffectType> list = new ArrayList<>(Arrays.asList(PotionEffectType.NAUSEA, PotionEffectType.WITHER, PotionEffectType.POISON, PotionEffectType.LEVITATION, PotionEffectType.HUNGER,
                PotionEffectType.BLINDNESS, PotionEffectType.SLOWNESS, PotionEffectType.MINING_FATIGUE, PotionEffectType.WEAKNESS));
        return list.contains(p);
    }

    public static boolean isLog(Block b){
        String name = b.getType().name().toUpperCase();
        return name.contains("_LOG") || name.contains("_WOOD");
    }

    public static boolean isBoots(ItemStack i){
        return ItemType.BOOTS.isOfType(i);
    }
    public static boolean isLeggings(ItemStack i){
        return ItemType.LEGGINGS.isOfType(i);
    }
    public static boolean isChestplate(ItemStack i){
        return ItemType.CHESTPLATE.isOfType(i);
    }
    public static boolean isHelmet(ItemStack i){
        return ItemType.HELMET.isOfType(i);
    }
    public static boolean isArmor(ItemStack i){
        return isHelmet(i) || isChestplate(i) || isLeggings(i) || isBoots(i);
    }
    public static boolean isTool(ItemStack i){return isPickaxe(i) || isShovel(i) || isAxe(i) || isHoe(i);}
    public static boolean isWeapon(ItemStack i){return isSword(i) || isAxe(i) || isSpear(i);}
    public static boolean isSword(ItemStack i){
        return ItemType.SWORD.isOfType(i);
    }
    public static boolean isPickaxe(ItemStack i){
        return ItemType.PICKAXE.isOfType(i);
    }
    public static boolean isAxe(ItemStack i){
        return ItemType.AXE.isOfType(i);
    }
    public static boolean isHoe(ItemStack i){
        return ItemType.HOE.isOfType(i);
    }
    public static boolean isShovel(ItemStack i){
        return ItemType.SHOVEL.isOfType(i);
    }
    public static boolean isHorseArmor(ItemStack i){
        return ItemType.HORSE_ARMOR.isOfType(i);
    }
    public static boolean isSpear(ItemStack i){
        return ItemType.SPEAR.isOfType(i);
    }
    public static boolean isElytra(ItemStack i){return ItemType.ELYTRA.isOfType(i);}
    public static boolean isBow(ItemStack i){
        return ItemType.BOW.isOfType(i);
    }
    public static boolean isCompatible(Enchant e1, Enchantment e2){
        if(e1 == Enchant.FREEZING && e2 == Enchantment.FLAME)
            return false;
        if(e1 == Enchant.FROSTBITE && e2 == Enchantment.FIRE_ASPECT)
            return false;
        if(e1 == Enchant.HEALING){
            if(e2 == Enchantment.INFINITY || e2 == Enchantment.FLAME)
                return false;
        }
        if(e1 == Enchant.FIREWALKER && e2 == Enchantment.FROST_WALKER)
            return false;
        if(e2 == Enchantment.INFINITY) {
            if(e1 == Enchant.HOMING || e1 == Enchant.GRAPPLING || e1 == Enchant.GRAVITY || e1 == Enchant.NITRO)
                return false;
        }
        if(e1 == Enchant.PULVERIZING){
            if(e2 == Enchantment.FORTUNE || e2 == Enchantment.SILK_TOUCH)
                return false;
        }
        if(e1 == Enchant.WAVERIDER && e2 == Enchantment.FROST_WALKER)
            return false;
        return true;
    }
    public static boolean isCompatible(Enchant e1, Enchant e2){
        if(e1 == e2)
            return true;
        Set<Enchant> combo = Set.of(e1,e2);

        if(combo.contains(Enchant.PERMAFROST))
            if(combo.contains(Enchant.MOLTEN) || combo.contains(Enchant.FIREWALKER)
                    || combo.contains(Enchant.ERUPTION))
                return false;
        if(combo.contains(Enchant.GRAVITY)&&combo.contains(Enchant.ANTIGRAVITY))
            return false;
        if(combo.contains(Enchant.THRUST)){
            if(combo.contains(Enchant.SOLAR)||combo.contains(Enchant.LUNAR))
                return false;
        }
        if(combo.contains(Enchant.ROCK_CANDY) && combo.contains(Enchant.PULVERIZING))
            return false;
        if(combo.contains(Enchant.FIREWALKER) && combo.contains(Enchant.WAVERIDER)){
            return false;
        }
        if(combo.contains(Enchant.BLAZE)&&combo.contains(Enchant.FROSTBITE))
            return false;
        if(combo.contains(Enchant.BLAZE)&&combo.contains(Enchant.BREEZE))
            return false;
        if(combo.contains(Enchant.HOMING)&&combo.contains(Enchant.RICOCHET))
            return false;
        if(combo.contains(Enchant.HOMING)&&combo.contains(Enchant.GRAPPLING))
            return false;
        if(combo.contains(Enchant.HEALING)){ // cover all for healing, it is ONLY compatible with the following rather than being incompatible with others:
            if(combo.contains(Enchant.ANTIGRAVITY)||combo.contains(Enchant.RICOCHET)||combo.contains(Enchant.HOMING))
                return true;
            return false;
        }
        if(combo.contains(Enchant.PULVERIZING)){
            if(combo.contains(Enchant.FORGING) || combo.contains(Enchant.CLUSTER) || combo.contains(Enchant.TALENT))
                return false;
        }
        if(combo.contains(Enchant.WILTING)&&combo.contains(Enchant.VENOM))
            return false;
        if(combo.contains(Enchant.SOLAR)&&combo.contains(Enchant.LUNAR))
            return false;
        if(combo.contains(Enchant.LEECHING)&&combo.contains(Enchant.VENOM))
            return false;
        if(combo.contains(Enchant.TOXIC)&&combo.contains(Enchant.PLAGUE))
            return false;
        if(combo.contains(Enchant.GRAPPLING)){
            if(combo.contains(Enchant.GRAVITY) || combo.contains(Enchant.ANTIGRAVITY) || combo.contains(Enchant.RICOCHET))
                return false;
        }
        if(combo.contains(Enchant.STEALTH)&&combo.contains(Enchant.MAGNETIC))
            return false;
        return true;
    }

    public static boolean isTypeCompatible(ItemStack i, Enchant enchant){
        if(i == null)
            return false;
        return enchant.isTypeCompatible(i);
    }

    public static boolean hasEnchantment(ItemStack i, Enchant enchant, boolean debug){
        if(i==null)
            return false;
        if(i.hasItemMeta()){
            ItemMeta meta = i.getItemMeta();
            assert meta != null;
            if(meta.hasLore()){
                List<String> lore = meta.getLore();
                assert lore != null;
                for(String s:lore){
                    if(s.substring(2).equalsIgnoreCase(format(enchant.name())))
                        return true;
                    else if(s.lastIndexOf(' ') > -1 && s.lastIndexOf(' ') == s.indexOf(' ')) {
                        if (s.substring(2, s.lastIndexOf(' ')).equals(format(enchant.name())))
                            return true;
                    }
                    else if(s.length() > 2)
                        if(s.substring(2).equalsIgnoreCase(format(enchant.name())))
                            return true;

                }
            }
        }
        return false;
    }

    public static boolean hasEnchantment(ItemStack i, Enchant enchant){
        return hasEnchantment(i,enchant,false);
    }

    public static boolean isNight(World w){
        long time = w.getTime();
        return time<23000&&time>13000;
    }

    public static boolean isAllowedToFly(Player p){
        if(hasEnchantment(p.getEquipment().getChestplate(),Enchant.WINGS))
            return true;
        if(hasEnchantment(p.getEquipment().getBoots(),Enchant.ANTIGRAVITY))
            return true;
        if(p.getEquipment().getChestplate().getType() == Material.ELYTRA)
            return true;
        if(hasFullSet(p, Enchant.MAGNETIC))
            return true;
        return false;
    }

    public static String getTierColor(Enchant enchant){
        int tier = enchant.getTier();
        if(tier == 4)
            return "§c";
        if(enchant.getTier() == 3)
            return "§6";
        else if(enchant.getTier() == 2)
            return "§e";
        return "§a";
    }

    public static boolean hasCustomEnchants(ItemStack i){
        return !getCustomEnchants(i).isEmpty();
    }

    public static boolean hasCurse(ItemStack i){
        List<Enchant> enchants = getCustomEnchants(i);
        for(Enchant ench:enchants) {
            if(ench.isCurse())
                return true;
        }
        return false;
    }

    public static List<Enchant> getCustomEnchants(ItemStack i){
        List<Enchant> enchants = new ArrayList<>();
        if(i==null)
            return enchants;
        if(i.hasItemMeta()){
            try {
                ItemMeta meta = i.getItemMeta();
                assert meta != null;
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    assert lore != null;
                    for (String s : lore) {
                        if (s.contains(" "))
                            enchants.add(Enchant.valueOf(s.substring(2, s.lastIndexOf(' ')).toUpperCase()));
                        else enchants.add(Enchant.valueOf(s.substring(2).toUpperCase()));
                    }
                }
            }catch(Exception ex){

            }
        }
        return enchants;
    }

    public void removeCustomEnchantments(ItemStack item){
        if(item == null)
            return;
        if(!item.hasItemMeta())
            return;
        ItemMeta meta = item.getItemMeta();
        if(meta.hasLore()){
            meta.getLore().removeIf(l->!isEnchantmentLine(l));
        }
    }

    public boolean isEnchantmentLine(String line){
        try{
            Enchant.valueOf(line.substring(2,line.indexOf(' ')).toUpperCase());
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public static int getEnchantLevel(ItemStack i, Enchant enchant, boolean debug){
        if(hasEnchantment(i,enchant,debug)){
            String str = "";
            if(enchant.getMaxLevel()==1)
                return 1;
            for(String s: Objects.requireNonNull(Objects.requireNonNull(i.getItemMeta()).getLore())) {
                if(s.contains(" "))
                    if (s.toLowerCase().substring(2, s.lastIndexOf(' ')).equals(enchant.name().toLowerCase()))
                        str = s;
            }
            if(str.equalsIgnoreCase(""))
                return 0;
            return getIntFromNumeral(str.substring(str.lastIndexOf(' ')+3));
        }
        return 0;
    }

    public static int getEnchantLevel(ItemStack i, Enchant enchant){
        return getEnchantLevel(i,enchant,false);
    }

    public int getNumArmorPieces(Player p, Enchant enchant){
        int count = 0;
        ItemStack[] armor = p.getInventory().getArmorContents();
        for(ItemStack i:armor){
            if(hasEnchantment(i,enchant))
                count++;
        }
        return count;
    }

    public static int getArmorScore(Player p, Enchant enchant){
        int count = 0;
        ItemStack[] armor = p.getInventory().getArmorContents();
        for(ItemStack i:armor){
            if(i!=null)
                if(hasEnchantment(i,enchant))
                    count+=getEnchantLevel(i,enchant);
        }
        return count;
    }

    public static boolean hasFullSet(Player p, Enchant en){
        for(ItemStack item:p.getEquipment().getArmorContents()){
            if(!hasEnchantment(item,en))
                return false;
        }
        return true;
    }

    public List<ItemStack> getInventoryAsList(Player p){
        PlayerInventory inv = p.getInventory();
        List<ItemStack> inventory = new ArrayList<>();
        for(int i=0;i<36;i++)
            inventory.add(inv.getItem(i));
        inventory.add(inv.getItemInOffHand());
        return inventory;
    }

    public static int invIndexOf(List<ItemStack> inv,Material mat){
        for(int i=0;i<inv.size();i++) {
            if(inv.get(i)!=null)
                if (inv.get(i).getType().equals(mat))
                    return i;
        }
        return -1;
    }

    public static int getIntFromNumeral(String str){
        for(int i=0;i<numerals.length;i++){
            if(numerals[i].equals(str)){
                return i + 1;
            }
        }
        return 0;
    }

    public static String numeralize(int num){
        if(num >= numerals.length)
            return "";
        return numerals[num-1];
    }

    public void enchant(ItemStack item, int tier){
        final List<Enchant> local_enchants = getEnchantsOfTier(tier);
        Enchant enchant = local_enchants.get((int)(Math.random()*local_enchants.size()));
        enchant(item,enchant,(int)(Math.random()*enchant.getMaxLevel() + 1));
    }

    public void enchant(ItemStack item, Enchant enchant, int level){
        if(item==null)
            return;
        assert Objects.requireNonNull(item.getItemMeta()).hasLore();
        if(level==0)
            return;

        if(pjEnchants.getCustomEnchants(item).size()>5) {
            if (item.getType().equals(Material.ENCHANTED_BOOK)) {
                if (pjEnchants.getCustomEnchants(item).size() > 7)
                    return;
                else return;
            }
            else return;
        }

        if(level>enchant.getMaxLevel())
            level = enchant.getMaxLevel();

        String tier = getTierColor(enchant);

        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if(meta!=null)
            if(meta.hasLore())
                if(meta.getLore() != null)
                    lore = meta.getLore();

        // COMPATIBILITY CHECK ------------
        List<Enchantment> re = getRealEnchants(item);
        List<Enchant> ce = getCustomEnchants(item);
        String enchantment = format(enchant.name());

        if(!isTypeCompatible(item,enchant))
            return;

        // Check compatibility with real enchants
        for(Enchantment en : re)
            if(!isCompatible(enchant, en)){
                return;
            }

        // Check compatibility with custom enchants
        for(Enchant en : ce) {
            if (!isCompatible(enchant, en)) {
                return;
            }
        }
        // ------------------------------

        if(hasEnchantment(item,enchant)){
            if(getEnchantLevel(item,enchant)==level&&level<enchant.getMaxLevel()) {
                level++;
                lore.removeIf(s -> s.toLowerCase().contains(enchant.name().toLowerCase()));
            }
            else if(level > getEnchantLevel(item,enchant)){
                lore.removeIf(s -> s.toLowerCase().contains(enchant.name().toLowerCase()));
            }
            else if(getEnchantLevel(item,enchant)>level) {
                return;
            }
            else if(getEnchantLevel(item,enchant) == level && level == enchant.getMaxLevel()){
                return;
            }
        }

        if(enchant.getMaxLevel()==1)
            lore.add(tier+enchantment);
        else lore.add(tier+enchantment+" "+tier+ pjEnchants.numeralize(level));

        meta.setLore(lore);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
    }

    public static List<Enchantment> getRealEnchants(ItemStack item){
        List<Enchantment> re = new ArrayList<>();
        if(item==null)
            return re;
        re.addAll(item.getEnchantments().keySet());
        return re;
    }

    public static List<Enchant> getEnchantsOfTier(int tier){
        List<Enchant> list = new ArrayList<>();
        for(Enchant e:all_enchants){
            if(e.getTier() == tier)
                list.add(e);
        }
        return list;
    }

    public static List<Enchant> getAvailableEnchants(ItemType type){
        List<Enchant> list = new ArrayList<>();
        if(type == null)
            return list;
        for(Enchant e:all_enchants){
            if(e.isTypeCompatible(type))
                list.add(e);
        }
        return list;
    }

    public static int random(int low, int high){
        int range = high-low;
        int end = (int)(Math.random()*range)+1+low;
        if(end<0)
            end = 0;
        return end;
    }

    public ItemStack newItem(Material mat, String name, List<String> lore, int amount){
        ItemStack item = new ItemStack(mat,amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack newItem(Material mat, String name, List<String> lore){
        return newItem(mat,name,lore,1);
    }

    public ItemStack newItem(Material mat, String name){
        return newItem(mat,name,new ArrayList<>());
    }

    public List<Block> getCluster(List<Block> a, Block b,Material mat, int level){
        if(!mat.equals(Material.AIR)) {
            a.add(b);
            if (b.getRelative(1, 0, 0).getType().equals(mat) && !a.contains(b.getRelative(1, 0, 0)) && a.size() < 6+level)
                a.addAll(getCluster(a, b.getRelative(1, 0, 0), mat,level));
            if (b.getRelative(0, 1, 0).getType().equals(mat) && !a.contains(b.getRelative(0, 1, 0)) && a.size() < 6+level)
                a.addAll(getCluster(a, b.getRelative(0, 1, 0), mat,level));
            if (b.getRelative(0, 0, 1).getType().equals(mat) && !a.contains(b.getRelative(0, 0, 1)) && a.size() < 6+level)
                a.addAll(getCluster(a, b.getRelative(0, 0, 1), mat,level));
            if (b.getRelative(-1, 0, 0).getType().equals(mat) && !a.contains(b.getRelative(-1, 0, 0)) && a.size() < 6+level)
                a.addAll(getCluster(a, b.getRelative(-1, 0, 0), mat,level));
            if (b.getRelative(0, -1, 0).getType().equals(mat) && !a.contains(b.getRelative(0, -1, 0)) && a.size() < 6+level)
                a.addAll(getCluster(a, b.getRelative(0, -1, 0), mat,level));
            if (b.getRelative(0, 0, -1).getType().equals(mat) && !a.contains(b.getRelative(0, 0, -1)) && a.size() < 6+level)
                a.addAll(getCluster(a, b.getRelative(0, 0, -1), mat,level));
        }
        return a;
    }

    public void breakWithForged(Player p, ItemStack tool, Block block){
        Material b = block.getType();
        List<ItemStack> items = block.getDrops(tool).stream().toList();
        int fortune = 1;
        int multiplier = 1;
        int exp = 1;
        boolean showflame = true;
        if(Objects.requireNonNull(tool.getItemMeta()).hasEnchant(Enchantment.FORTUNE))
            fortune = tool.getEnchantmentLevel(Enchantment.FORTUNE);
        if(fortune == 1)
            multiplier = percentChance(33) ? 2 : 1;
        if(fortune == 2)
            multiplier = percentChance(25) ? 3 : percentChance(25) ? 2 : 1;
        if(fortune == 3)
            multiplier = percentChance(20) ? 4 : percentChance(20) ? 3 : percentChance(20) ? 2 : 1;

        if (pjEnchants.isPickaxe(tool)) {
            for(ItemStack i:items) {
                Material m = i.getType();
                int amount = i.getAmount()*multiplier;
                if(block.getType() == Material.STONE)
                    i.setType(Material.SMOOTH_STONE);
                else if(m == Material.MOSSY_COBBLESTONE || m == Material.COBBLESTONE)
                    i.setType(Material.STONE);
                else if(m == Material.RAW_IRON)
                    i.setType(Material.IRON_INGOT);
                else if(m == Material.RAW_IRON_BLOCK)
                    i.setType(Material.IRON_BLOCK);
                else if(m == Material.RAW_COPPER)
                    i.setType(Material.COPPER_INGOT);
                else if(m == Material.RAW_COPPER_BLOCK)
                    i.setType(Material.COPPER_BLOCK);
                else if(m == Material.RAW_GOLD)
                    i.setType(Material.GOLD_INGOT);
                else if(m == Material.RAW_GOLD_BLOCK)
                    i.setType(Material.GOLD_BLOCK);
                else if(block.getType() == Material.NETHER_GOLD_ORE){
                    i.setType(Material.GOLD_INGOT);
                    i.setAmount(multiplier);
                }
                else if(m == Material.NETHERRACK){
                    i.setType(Material.NETHER_BRICK);
                    i.setAmount(amount);
                }
                else if(m == Material.ANCIENT_DEBRIS){
                    i.setType(Material.NETHERITE_SCRAP);
                    i.setAmount(amount);
                    exp = 2;
                }
                else if(m == Material.CRACKED_STONE_BRICKS)
                    i.setType(Material.STONE_BRICKS);
                else if(m == Material.BASALT)
                    i.setType(Material.SMOOTH_BASALT);
                else if(m == Material.WET_SPONGE)
                    i.setType(Material.SPONGE);
                else if(m == Material.SANDSTONE)
                    i.setType(Material.SMOOTH_SANDSTONE);
                else showflame = false;
                exp *= amount;
            }
        }
        else if (pjEnchants.isShovel(tool)) {
            for(ItemStack i:items){
                Material m = i.getType();
                if(m == Material.RED_SAND || m == Material.SAND)
                    i.setType(Material.GLASS);
                else if(m == Material.CLAY_BALL)
                    i.setType(Material.BRICK);
                else showflame = false;
            }
        }
        else if (pjEnchants.isAxe(tool)) {
            for(ItemStack i:items){
                Material m = i.getType();
                int amount = i.getAmount()*multiplier;
                if(pjEnchants.axe_blocks.contains(m)){
                    i.setType(Material.CHARCOAL);
                    i.setAmount(amount);
                }
                else if(m == Material.CACTUS){
                    i.setType(Material.GREEN_DYE);
                    i.setAmount(amount);
                }
                else showflame = false;
                exp *= amount;
            }
        }
        else if (pjEnchants.isHoe(tool)) {
            for(ItemStack i:items){
                Material m = i.getType();
                int amount = i.getAmount()*multiplier;
                if(m == Material.HAY_BLOCK){
                    i.setType(Material.BREAD);
                    i.setAmount(amount);
                }
                else if(m == Material.POTATOES){
                    i.setType(Material.BAKED_POTATO);
                    i.setAmount(amount);
                }
                else showflame = false;
                exp *= amount;
            }
        }
        if(showflame){
            particleCube(Particle.FLAME,block.getLocation(),3);
            p.getWorld().playSound(block.getLocation(),Sound.ITEM_FIRECHARGE_USE,0.2F,1);
            ExperienceOrb orb = block.getWorld().spawn(block.getLocation(), ExperienceOrb.class);
            orb.setExperience(exp);
        }
        for(ItemStack drop:items)
            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5,0.5,0.5),drop);
    }

    public int breakWithRockCandy(Player p, ItemStack tool, Block b){
        int items_dropped = 0;
        for(ItemStack drop:b.getDrops(tool))
            items_dropped += drop.getAmount();
        int feed = 0;
        switch(b.getType()){
            case COAL_ORE,DEEPSLATE_COAL_ORE -> {
                feed = (int)(0.5 * items_dropped);
                if(percentChance(20)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 80, 0));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_DRINK,0.5F,1);
                }
            }
            case IRON_ORE,DEEPSLATE_IRON_ORE -> {
                feed = items_dropped;
                if(percentChance(30)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 80, 0));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_DRINK,0.5F,1);
                }
            }
            case GOLD_ORE,DEEPSLATE_GOLD_ORE -> {
                feed = 2 * items_dropped;
                if(percentChance(40)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 80, 1));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_DRINK,0.5F,1);
                }
            }
            case COPPER_ORE,DEEPSLATE_COPPER_ORE -> {
                feed = (int)(0.5 * items_dropped);
                if(percentChance(20)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 60, 0));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_DRINK,0.5F,1);
                }
            }
            case DIAMOND_ORE,DEEPSLATE_DIAMOND_ORE -> {
                feed = 3 * items_dropped;
                if(percentChance(70)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 120, 0));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_DRINK,0.5F,1);
                }
            }
            case EMERALD_ORE,DEEPSLATE_EMERALD_ORE -> {
                feed = 3 * items_dropped;
                if(percentChance(60)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 80, 1));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_DRINK,0.5F,1);
                }
            }
            case REDSTONE_ORE,DEEPSLATE_REDSTONE_ORE -> {
                feed = (int)(0.5*items_dropped);
                if(percentChance(30)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 2));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_DRINK,0.5F,1);
                }
            }
            case LAPIS_ORE, DEEPSLATE_LAPIS_ORE -> {
                feed = (int)(0.3 * items_dropped);
                if(percentChance(30)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 120, 0));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_DRINK,0.5F,1);
                }
            }
            case GLOWSTONE -> {
                if(percentChance(30)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 80, 2));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GENERIC_DRINK,0.5F,1);
                }
            }
        }
        return feed;
    }

    public boolean isArmorable(Entity e){
        if(!(e instanceof Monster))
            return false;
        List<EntityType> armorable = new ArrayList<>();
        armorable.addAll(Arrays.asList(EntityType.ZOMBIE,EntityType.ZOMBIE_VILLAGER,EntityType.SKELETON,EntityType.PIGLIN,EntityType.HUSK,EntityType.BOGGED,EntityType.DROWNED,
                EntityType.PIGLIN_BRUTE,EntityType.ZOMBIFIED_PIGLIN,EntityType.PARCHED));
        return armorable.contains(e.getType());
    }

    public void runHoldingCheck(){
        new BukkitRunnable(){
            public void run(){
                for(Player p:online){
                    if(p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                        ItemStack hand = p.getInventory().getItemInMainHand();
                        if (hasCurse(hand)) {
                            Location loc = p.getLocation().add(0, 1, 0).add(p.getLocation().getDirection());
                            loc.getWorld().spawnParticle(Particle.WITCH, loc, 0);
                        }
                        if(pjEnchants.hasEnchantment(hand,Enchant.ARTFUL))
                            p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE,30,1,false,false));
                        if(pjEnchants.hasEnchantment(hand,Enchant.PULVERIZING))
                            p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE,30,3,false,false));
                    }

                    boolean fullset_molten = true;
                    boolean fullset_permafrost = true;

                    p.getInventory().getHelmet();
                    ItemStack helm = p.getInventory().getHelmet();
                    if(hasEnchantment(helm, Enchant.NIGHTEYE))
                        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0,false,false));
                    if(!hasEnchantment(helm, Enchant.MOLTEN))
                        fullset_molten = false;
                    if(!hasEnchantment(helm, Enchant.PERMAFROST))
                        fullset_permafrost = false;


                    p.getInventory().getLeggings();
                    ItemStack legs = p.getInventory().getLeggings();
                    if(hasEnchantment(legs,Enchant.SEALEGS)&&p.isInWater())
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE,50,0,false,false));
                    if(hasEnchantment(legs,Enchant.LEAPING))
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST,60,getEnchantLevel(legs,Enchant.LEAPING)-1,false,false));
                    if(!hasEnchantment(legs, Enchant.MOLTEN))
                        fullset_molten = false;
                    if(!hasEnchantment(legs, Enchant.PERMAFROST))
                        fullset_permafrost = false;

                    p.getInventory().getBoots();
                    ItemStack boots = p.getInventory().getBoots();
                    Material below = p.getLocation().subtract(0,1.75,0).getBlock().getType();
                    Material feet = p.getLocation().subtract(0,0.75,0).getBlock().getType();

                    boolean isInAir = (below == Material.AIR || below == Material.CAVE_AIR || below == Material.WATER) && (feet == Material.AIR || feet == Material.CAVE_AIR || feet == Material.WATER);
                    if (hasEnchantment(boots, Enchant.GLIDE)) {
                        if(!p.isSneaking()&&isInAir)
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 0,false,false));
                        if(p.hasPotionEffect(PotionEffectType.SLOW_FALLING) && p.isSprinting() && !p.isInWater()) {
                            p.setVelocity(p.getVelocity().multiply(new Vector(0,1,0)).add(p.getLocation().getDirection().multiply(new Vector(0.5,0,0.5))));
                            particleRing(Particle.CLOUD,p.getLocation(),0.75,5);
                        }

                    }
                    if(hasEnchantment(boots,Enchant.DASH))
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,60,getEnchantLevel(boots,Enchant.DASH)-1,false,false));
                    if(hasEnchantment(boots,Enchant.FIREWALKER)) {
                        if (p.isInWater()){
                            p.getWorld().spawnParticle(Particle.BUBBLE_COLUMN_UP,p.getLocation(),0);
                        }
                    }
                    if(!hasEnchantment(boots, Enchant.MOLTEN))
                        fullset_molten = false;
                    if(!hasEnchantment(boots, Enchant.PERMAFROST))
                        fullset_permafrost = false;

                    p.getInventory().getChestplate();
                    ItemStack chest = p.getInventory().getChestplate();
                    if(hasEnchantment(chest,Enchant.SPONGE))
                        if(p.isInWater())
                            p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,50,0,false,false));
                    if(!hasEnchantment(chest, Enchant.MOLTEN))
                        fullset_molten = false;
                    if(!hasEnchantment(chest, Enchant.PERMAFROST))
                        fullset_permafrost = false;

                    if(p.getVehicle() instanceof Horse){
                        Horse h = (Horse)p.getVehicle();
                        if(h.getInventory().getArmor()!=null){
                            ItemStack armor = h.getInventory().getArmor();
                            if(hasEnchantment(armor,Enchant.NIGHTRIDER))
                                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0,false,false));
                            if(hasEnchantment(armor,Enchant.RUSH))
                                h.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,80,getEnchantLevel(armor,Enchant.RUSH)-1,false,false));
                        }
                    }

                    if(!p.isSneaking()){
                        magnet.remove(p.getUniqueId());
                    }

                    if(fullset_molten){
                        particleRing(Particle.LAVA,p.getLocation(),0.75,120);
                        particleRing(Particle.LAVA,p.getLocation().add(0,1,0),0.75,120);
                    }
                    if(fullset_permafrost){
                        particleRing(Particle.SNOWFLAKE,p.getLocation(),0.75,90);
                        particleRing(Particle.SNOWFLAKE,p.getLocation().add(0,1,0),0.75,90);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,60,0,false,false));
                    }
                }

            }
        }.runTaskTimer(this,0,5L);
    }
}
