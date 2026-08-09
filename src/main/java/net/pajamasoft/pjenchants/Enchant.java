package net.pajamasoft.pjenchants;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Enchant {

    // Enchantments
    ABSORB(new EnchantData()
            .max_level(3)
            .tier(1)
            .types(ItemType.CHESTPLATE)
    ),
    ADRENALINE(new EnchantData()
            .max_level(3)
            .tier(1)
            .types(ItemType.LEGGINGS)
    ),
    ANTIDOTE(new EnchantData()
            .max_level(4)
            .tier(1)
            .types(ItemType.CHESTPLATE)
    ),
    ANTIGRAVITY(new EnchantData()
            .max_level(3)
            .tier(2)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR,ItemType.BOW,ItemType.BOOTS))
            .restricted(true)
    ),
    ARTFUL(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR))
            .restricted(true)
    ),
    BLAZE(new EnchantData()
            .max_level(3)
            .tier(3)
            .types(Set.of(ItemType.SWORD,ItemType.AXE))
            .cooldown(5000L)
            .restricted(true)
    ),
    BOLT(new EnchantData()
            .max_level(4)
            .tier(2)
            .types(ItemType.WOLF_ARMOR)
    ),
    BREEZE(new EnchantData()
            .max_level(3)
            .tier(3)
            .types(ItemType.SWORD)
            .cooldown(5000L)
            .restricted(true)
    ),
    CLUSTER(new EnchantData()
            .max_level(3)
            .tier(2)
            .types(Set.of(ItemType.PICKAXE,ItemType.AXE))
            .restricted(true)
    ),
    CONSTITUTION(new EnchantData()
            .max_level(5)
            .tier(1)
            .types(ItemType.HELMET)
    ),
    CRITICALITY(new EnchantData()
            .max_level(2)
            .tier(2)
            .types(ItemType.SPEAR)
    ),
    DARKNESS(new EnchantData()
            .max_level(4)
            .tier(1)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR))
    ),
    DASH(new EnchantData()
            .max_level(2)
            .tier(2)
            .types(ItemType.BOOTS)
    ),
    DEFUSE(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(Set.of(ItemType.SWORD,ItemType.SPEAR))
    ),
    DEVOUR(new EnchantData()
            .max_level(4)
            .tier(1)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR))
            .restricted(true)
    ),
    DISCHARGE(new EnchantData()
            .max_level(3)
            .tier(2)
            .types(ItemType.CHESTPLATE)
    ),
    DIZZY(new EnchantData()
            .max_level(3)
            .tier(1)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR))
    ),
    DRACONIC(new EnchantData()
            .max_level(1)
            .tier(3)
            .types(Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE))
            .restricted(true)
            .cooldown(1500L)
            .restricted(true)
    ),
    DRAG(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE))
    ),
    ENDEREYES(new EnchantData()
            .max_level(1)
            .tier(3)
            .types(ItemType.HELMET)
            .restricted(true)
    ),
    ERUPTION(new EnchantData()
            .max_level(3)
            .tier(3)
            .types(ItemType.HELMET)
    ),
    ESCAPE(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.BOOTS)
    ),
    FANGS(new EnchantData()
            .max_level(3)
            .tier(1)
            .types(ItemType.WOLF_ARMOR)
    ),
    FIREWALKER(new EnchantData()
            .max_level(3)
            .tier(3)
            .types(ItemType.BOOTS)
    ),
    FLING(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.WOLF_ARMOR)
    ),
    FORGING(new EnchantData()
            .max_level(1)
            .tier(3)
            .types(Set.of(ItemType.PICKAXE,ItemType.AXE,ItemType.SHOVEL,ItemType.HOE))
            .restricted(true)
    ),
    FRACTURE(new EnchantData()
            .max_level(4)
            .tier(1)
            .types(ItemType.AXE)
            .restricted(true)
    ),
    FREEZING(new EnchantData()
            .max_level(3)
            .tier(1)
            .types(ItemType.BOW)
            .restricted(true)
    ),
    FROSTBITE(new EnchantData()
            .max_level(5)
            .tier(1)
            .types(Set.of(ItemType.SWORD,ItemType.SPEAR,ItemType.AXE))
    ),
    GLIDE(new EnchantData()
            .max_level(1)
            .tier(3)
            .types(Set.of(ItemType.BOOTS,ItemType.WOLF_ARMOR))
            .restricted(true)
    ),
    GRAPPLING(new EnchantData()
            .max_level(1)
            .tier(3)
            .types(ItemType.BOW)
    ),
    GRAVITY(new EnchantData()
            .max_level(3)
            .tier(3)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.BOW))
            .cooldown(800L)
    ),
    GROUNDED(new EnchantData()
            .max_level(1)
            .tier(3)
            .types(ItemType.BOOTS)
    ),
    HALLUCINATION(new EnchantData()
            .max_level(3)
            .tier(1)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR))
    ),
    HEALING(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.BOW)
    ),
    HELLHOUND(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.WOLF_ARMOR)
    ),
    HELLISH(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.HORSE_ARMOR)
    ),
    HIVE(new EnchantData()
            .max_level(3)
            .tier(2)
            .types(ItemType.CHESTPLATE)
    ),
    HOMING(new EnchantData()
            .max_level(1)
            .tier(3)
            .types(ItemType.BOW)
    ),
    INFESTED(new EnchantData()
            .max_level(3)
            .tier(2)
            .types(ItemType.CHESTPLATE)
    ),
    JOUST(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.HORSE_ARMOR)
    ),
    LEAPING(new EnchantData()
            .max_level(3)
            .tier(1)
            .types(ItemType.LEGGINGS)
    ),
    LEECHING(new EnchantData()
            .max_level(5)
            .tier(2)
            .types(Set.of(ItemType.SWORD,ItemType.SPEAR))
    ),
    LIFT(new EnchantData()
            .max_level(2)
            .tier(2)
            .types(Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE))
    ),
    LUNAR(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE))
            .cooldown(1000L)
    ),
    MAGNETIC(new EnchantData()
            .max_level(1)
            .tier(1)
            .types(Set.of(ItemType.HELMET,ItemType.CHESTPLATE,ItemType.LEGGINGS,ItemType.BOOTS))
    ),
    METEOR(new EnchantData()
            .max_level(2)
            .tier(3)
            .types(ItemType.SWORD)
            .restricted(true)
            .cooldown(6000L)
            .restricted(true)
    ),
    MOLTEN(new EnchantData()
            .max_level(3)
            .tier(1)
            .types(Set.of(ItemType.HELMET,ItemType.CHESTPLATE,
                    ItemType.LEGGINGS,ItemType.BOOTS,ItemType.WOLF_ARMOR))
            .restricted(true)
    ),
    NEEDLES(new EnchantData()
            .max_level(2)
            .tier(2)
            .types(ItemType.SPEAR)
            .cooldown(30000L)
            .restricted(true)
    ),
    NIGHTEYE(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.HELMET)
    ),
    NIGHTRIDER(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.HORSE_ARMOR)
    ),
    NITRO(new EnchantData()
            .max_level(5)
            .tier(2)
            .types(ItemType.BOW)
            .restricted(true)
    ),
    PERMAFROST(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(Set.of(ItemType.HELMET,ItemType.CHESTPLATE,ItemType.LEGGINGS,ItemType.BOOTS))
    ),
    PHANTOM(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.SWORD)
    ),
    PLAGUE(new EnchantData()
            .max_level(5)
            .tier(2)
            .types(Set.of(ItemType.HELMET,ItemType.CHESTPLATE,ItemType.LEGGINGS,ItemType.BOOTS))
    ),
    PSYCHIC(new EnchantData()
            .max_level(3)
            .tier(3)
            .types(ItemType.HELMET)
    ),
    PULVERIZING(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(Set.of(ItemType.PICKAXE,ItemType.AXE,ItemType.SHOVEL,ItemType.HOE))
    ),
    PUNCTURE(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.SPEAR)
            .cooldown(4000L)
    ),
    RAGE(new EnchantData()
            .max_level(5)
            .tier(2)
            .types(ItemType.CHESTPLATE)
            .cooldown(6000L)
    ),
    REPULSION(new EnchantData()
            .max_level(3)
            .tier(1)
            .types(ItemType.LEGGINGS)
    ),
    RICOCHET(new EnchantData()
            .max_level(3)
            .tier(3)
            .types(ItemType.BOW)
    ),
    ROCK_CANDY(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.PICKAXE)
    ),
    RUSH(new EnchantData()
            .max_level(3)
            .tier(2)
            .types(ItemType.HORSE_ARMOR)
            .restricted(true)
    ),
    SEALEGS(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.LEGGINGS)
            .restricted(true)
    ),
    SKULLS(new EnchantData()
            .max_level(2)
            .tier(3)
            .types(ItemType.SWORD)
            .cooldown(5000L)
            .restricted(true)
    ),
    SNATCH(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.WOLF_ARMOR)
    ),
    SOLAR(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE))
            .cooldown(1000L)
    ),
    SPIKES(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.CHESTPLATE)
    ),
    SPONGE(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(ItemType.CHESTPLATE)
    ),
    STEALTH(new EnchantData()
            .max_level(3)
            .tier(2)
            .types(ItemType.LEGGINGS)
            .cooldown(5000L)
    ),
    TALENT(new EnchantData()
            .max_level(5)
            .tier(2)
            .types(Set.of(ItemType.PICKAXE,ItemType.AXE,ItemType.SWORD,ItemType.SPEAR))
    ),
    THRUST(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE))
    ),
    THUNDER(new EnchantData()
            .max_level(4)
            .tier(3)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR))
    ),
    TOXIC(new EnchantData()
            .max_level(1)
            .tier(1)
            .types(Set.of(ItemType.HELMET,ItemType.CHESTPLATE,ItemType.LEGGINGS,ItemType.BOOTS,ItemType.WOLF_ARMOR))
    ),
    UNHOLY(new EnchantData()
            .max_level(1)
            .tier(2)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR))
    ),
    UNSTABLE(new EnchantData()
            .max_level(5)
            .tier(2)
            .types(ItemType.CHESTPLATE)
    ),
    VENOM(new EnchantData()
            .max_level(5)
            .tier(1)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR,ItemType.BOW))
            .restricted(true)
    ),
    WAVERIDER(new EnchantData()
            .max_level(2)
            .tier(3)
            .types(Set.of(ItemType.BOOTS,ItemType.HORSE_ARMOR))
            .restricted(true)
    ),
    WEREWOLF(new EnchantData()
            .max_level(3)
            .tier(3)
            .types(ItemType.WOLF_ARMOR)
    ),
    WILTING(new EnchantData()
            .max_level(3)
            .tier(3)
            .types(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR))
            .restricted(true)
    ),
    WINGS(new EnchantData()
            .max_level(1)
            .tier(3)
            .types(ItemType.CHESTPLATE)
            .restricted(true)
    ),

    // Curses
    JUDGEMENT(new EnchantData()
            .types(ItemType.SWORD)
            .isCurse(true)
    ),
    RAPTURE(new EnchantData()
            .types(ItemType.SWORD)
            .isCurse(true)
    ),
    THUNDERSTORM(new EnchantData()
            .types(ItemType.SWORD)
            .isCurse(true)
    ),
    VOID(new EnchantData()
            .types(ItemType.SWORD)
            .isCurse(true)
    ),
    ;

    private final int max_level;
    private final int tier;
    private final Set<ItemType> types;
    private final boolean restricted;
    private final boolean isCurse;
    private final long cooldown;


    // Enchant Constructors
    Enchant(EnchantData data){
        this.max_level = data.max_level;
        this.tier = data.tier;
        this.types = data.types;
        this.restricted = data.restricted;
        this.isCurse = data.isCurse;
        this.cooldown = data.cooldown;
    }

    private static final class EnchantData{
        private int max_level;
        private int tier;
        private Set<ItemType> types;
        private boolean restricted;
        private boolean isCurse;
        private long cooldown;
        EnchantData max_level(int x){
            this.max_level = x;
            return this;
        }
        EnchantData tier(int x){
            this.tier = x;
            return this;
        }
        EnchantData types(Set<ItemType> x){
            this.types = x;
            return this;
        }
        EnchantData types(ItemType x){
            this.types = Set.of(x);
            return this;
        }
        EnchantData restricted(boolean x){
            this.restricted = x;
            return this;
        }
        EnchantData isCurse(boolean x){
            this.isCurse = x;
            return this;
        }
        EnchantData cooldown(long x){
            this.cooldown = x;
            return this;
        }
    }

    public int getMaxLevel(){
        return max_level;
    }

    public int getTier(){
        return tier;
    }

    public long getCooldown(){
        return cooldown;
    }

    private Set<ItemType> getTypes(){
        return types;
    }

    public static List<Enchant> getAllEnchants(){
        List<Enchant> list = new ArrayList<>(List.of(Enchant.values()));
        list.removeIf(e -> e.isCurse);
        return list;
    }

    public static List<Enchant> getAllCurses(){
        List<Enchant> list = new ArrayList<>(List.of(Enchant.values()));
        list.removeIf(e -> !e.isCurse);
        return list;
    }

    public boolean isTypeCompatible(ItemStack i){
        if(i.getType() == Material.BOOK || i.getType() == Material.ENCHANTED_BOOK)
            return true;
        for(ItemType type : types){
            if(type.isOfType(i))
                return true;
        };
        return false;
    }

    public static List<Enchant> getSharedEnchants(Set<ItemType> types){
        List<Enchant> list = new ArrayList<>();
        outer:
        for(Enchant e : values()){
            for(ItemType type : types){
                if(!e.isTypeCompatible(type)) {
                    continue outer;
                }
            }
            list.add(e);
        }
        return list;
    }

    public boolean isTypeCompatible(ItemType type){
        return types.contains(type);
    }

    public boolean isRestricted(){
        return restricted;
    }

    public boolean isCurse(){
        return isCurse;
    }
}
