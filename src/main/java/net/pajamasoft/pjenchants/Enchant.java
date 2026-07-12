package net.pajamasoft.pjenchants;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public enum Enchant {

    // Enchantments
    ADRENALINE(3,1,ItemType.LEGGINGS),
    ANTIDOTE(4,1,ItemType.CHESTPLATE),
    ANTIGRAVITY(3,2,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR,ItemType.BOW,ItemType.BOOTS)),
    ARTFUL(1,2,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR)),
    BLAZE(3,3,Set.of(ItemType.SWORD,ItemType.AXE)),
    BOLT(4,2,ItemType.WOLF_ARMOR),
    BREEZE(3,3,ItemType.SWORD),
    CLUSTER(3,2,Set.of(ItemType.PICKAXE,ItemType.AXE)),
    CONSTITUTION(5,1,ItemType.HELMET),
    DARKNESS(4,1,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR)),
    DASH(2,2,ItemType.BOOTS),
    DEFUSE(1,2,Set.of(ItemType.SWORD,ItemType.SPEAR)),
    DEVOUR(4,1,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR)),
    DISCHARGE(3,2,ItemType.CHESTPLATE),
    DIZZY(3,1,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR)),
    DRACONIC(1,3,Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE),true),
    DRAG(1,2,Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE)),
    ENDEREYES(1,3,ItemType.HELMET),
    ESCAPE(1,2,ItemType.BOOTS),
    FANGS(3,1,ItemType.WOLF_ARMOR),
    FLING(1,2,ItemType.WOLF_ARMOR),
    FORGING(1,3,Set.of(ItemType.PICKAXE,ItemType.AXE,ItemType.SHOVEL,ItemType.HOE)),
    FRACTURE(4,1,ItemType.AXE),
    FREEZING(3,1,ItemType.BOW),
    FROSTBITE(5,1,Set.of(ItemType.SWORD,ItemType.SPEAR,ItemType.AXE)),
    GLIDE(1,3,Set.of(ItemType.BOOTS,ItemType.WOLF_ARMOR)),
    GRAPPLING(1,3,ItemType.BOW),
    GRAVITY(3,3,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.BOW)),
    GROUNDED(1,3,ItemType.BOOTS),
    HALLUCINATION(3,1,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR)),
    HEALING(1,2,ItemType.BOW),
    HELLHOUND(1,2,ItemType.WOLF_ARMOR),
    HELLISH(1,2,ItemType.HORSE_ARMOR),
    HIVE(3,2,ItemType.CHESTPLATE),
    HOMING(1,3,ItemType.BOW),
    INFESTED(3,2,ItemType.CHESTPLATE),
    JOUST(1,2,ItemType.HORSE_ARMOR),
    LEAPING(3,1,ItemType.LEGGINGS),
    LEECHING(5,2,Set.of(ItemType.SWORD,ItemType.SPEAR)),
    LIFT(2,2,Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE)),
    LUNAR(1,2,Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE)),
    MAGNETIC(1,1,Set.of(ItemType.HELMET,ItemType.CHESTPLATE,ItemType.LEGGINGS,ItemType.BOOTS)),
    MOLTEN(3,1,Set.of(ItemType.HELMET,ItemType.CHESTPLATE,ItemType.LEGGINGS,ItemType.BOOTS,ItemType.WOLF_ARMOR)),
    NIGHTEYE(1,2,ItemType.HELMET),
    NIGHTRIDER(1,2,ItemType.HORSE_ARMOR),
    NITRO(5,2,ItemType.BOW),
    PHANTOM(1,2,ItemType.SWORD),
    PLAGUE(5,2,Set.of(ItemType.HELMET,ItemType.CHESTPLATE,ItemType.LEGGINGS,ItemType.BOOTS)),
    PSYCHIC(3,3,ItemType.HELMET),
    PULVERIZING(1,2,Set.of(ItemType.PICKAXE,ItemType.AXE,ItemType.SHOVEL,ItemType.HOE)),
    RAGE(5,2,ItemType.CHESTPLATE),
    REPULSION(3,1,ItemType.LEGGINGS),
    RICOCHET(3,3,ItemType.BOW),
    RUSH(3,2,ItemType.HORSE_ARMOR),
    SEALEGS(1,2,ItemType.LEGGINGS),
    SKULLS(2,3,ItemType.SWORD),
    SNATCH(1, 2,ItemType.WOLF_ARMOR),
    SOLAR(1,2,Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE)),
    SPIKES(1,2,ItemType.CHESTPLATE),
    SPONGE(1,2,ItemType.CHESTPLATE),
    STEALTH(3,2,ItemType.LEGGINGS),
    TALENT(5,1,Set.of(ItemType.PICKAXE,ItemType.AXE,ItemType.SWORD,ItemType.SPEAR)), // Unique case of Pickaxe + Melee
    THRUST(1,2,Set.of(ItemType.ELYTRA,ItemType.CHESTPLATE)),
    THUNDER(4,3,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR)),
    TOXIC(1,1,Set.of(ItemType.HELMET,ItemType.CHESTPLATE,ItemType.LEGGINGS,ItemType.BOOTS,ItemType.WOLF_ARMOR)),
    UNHOLY(1,2,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR)),
    UNSTABLE(5,1,ItemType.CHESTPLATE),
    VENOM(5,1,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR,ItemType.BOW)),
    WAVERIDER(2,3,Set.of(ItemType.BOOTS,ItemType.HORSE_ARMOR)),
    WEREWOLF(3,3,ItemType.WOLF_ARMOR),
    WILTING(3,3,Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR)),
    WINGS(1,3,ItemType.CHESTPLATE,true),

    // Curses
    JUDGEMENT(ItemType.SWORD),
    RAPTURE(ItemType.SWORD),
    THUNDERSTORM(ItemType.SWORD),
    VOID(ItemType.SWORD);


    private final int max_level;
    private final int tier;
    private final Set<ItemType> types;
    private final boolean restricted;
    private final boolean isCurse;

    // Enchant Constructors
    Enchant(int max_level, int tier, Set<ItemType> types){
        this.max_level = max_level;
        this.tier = tier;
        this.types = types;
        this.restricted = false;
        isCurse = false;
    }

    Enchant(int max_level, int tier, ItemType type){
        this.max_level = max_level;
        this.tier = tier;
        types = Set.of(type);
        restricted = false;
        isCurse = false;
    }

    Enchant(int max_level, int tier, ItemType type, boolean restricted){
        this.max_level = max_level;
        this.tier = tier;
        types = Set.of(type);
        this.restricted = restricted;
        isCurse = false;
    }

    Enchant(int max_level, int tier, Set<ItemType> types, boolean restricted){
        this.max_level = max_level;
        this.tier = tier;
        this.types = types;
        this.restricted = restricted;
        isCurse = false;
    }

    // Curse Constructors
    Enchant(Set<ItemType> types){
        this.types = types;
        this.max_level = 1;
        this.tier = 4;
        this.restricted = true;
        isCurse = true;
    }

    Enchant(ItemType type){
        this.types = Set.of(type);
        this.max_level = 1;
        this.tier = 4;
        this.restricted = true;
        isCurse = true;
    }

    public int getMaxLevel(){
        return max_level;
    }

    public int getTier(){
        return tier;
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
        if(i.getType() == Material.BOOK)
            return true;
        for(ItemType type : types){
            if(type.isOfType(i))
                return true;
        };
        return false;
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
