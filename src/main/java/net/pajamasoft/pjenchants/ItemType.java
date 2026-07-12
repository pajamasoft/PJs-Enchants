package net.pajamasoft.pjenchants;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Set;

public enum ItemType {

    HELMET(Set.of(Material.LEATHER_HELMET,Material.COPPER_HELMET,Material.GOLDEN_HELMET,Material.CHAINMAIL_HELMET,Material.IRON_HELMET,Material.DIAMOND_HELMET,Material.NETHERITE_HELMET,
            Material.PLAYER_HEAD,Material.TURTLE_HELMET,Material.CREEPER_HEAD,Material.SKELETON_SKULL,Material.DRAGON_HEAD,Material.PIGLIN_HEAD,Material.ZOMBIE_HEAD,Material.WITHER_SKELETON_SKULL)),
    CHESTPLATE(Set.of(Material.LEATHER_CHESTPLATE,Material.COPPER_CHESTPLATE,Material.GOLDEN_CHESTPLATE,Material.CHAINMAIL_CHESTPLATE,Material.IRON_CHESTPLATE,Material.DIAMOND_CHESTPLATE,Material.NETHERITE_CHESTPLATE)),
    LEGGINGS(Set.of(Material.LEATHER_LEGGINGS,Material.COPPER_LEGGINGS,Material.GOLDEN_LEGGINGS,Material.CHAINMAIL_LEGGINGS,Material.IRON_LEGGINGS,Material.DIAMOND_LEGGINGS,Material.NETHERITE_LEGGINGS)),
    BOOTS(Set.of(Material.LEATHER_BOOTS,Material.COPPER_BOOTS,Material.GOLDEN_BOOTS,Material.CHAINMAIL_BOOTS,Material.IRON_BOOTS,Material.DIAMOND_BOOTS,Material.NETHERITE_BOOTS)),
    SWORD(Set.of(Material.WOODEN_SWORD,Material.STONE_SWORD,Material.COPPER_SWORD,Material.GOLDEN_SWORD,Material.IRON_SWORD,Material.DIAMOND_SWORD,Material.NETHERITE_SWORD)),
    AXE(Set.of(Material.WOODEN_AXE,Material.STONE_AXE,Material.COPPER_AXE,Material.GOLDEN_AXE,Material.IRON_AXE,Material.DIAMOND_AXE,Material.NETHERITE_AXE)),
    SPEAR(Set.of(Material.WOODEN_SPEAR,Material.STONE_SPEAR,Material.COPPER_SPEAR,Material.GOLDEN_SPEAR,Material.IRON_SPEAR,Material.DIAMOND_SPEAR,Material.NETHERITE_SPEAR)),
    PICKAXE(Set.of(Material.WOODEN_PICKAXE,Material.STONE_PICKAXE,Material.COPPER_PICKAXE,Material.GOLDEN_PICKAXE,Material.IRON_PICKAXE,Material.DIAMOND_PICKAXE,Material.NETHERITE_PICKAXE)),
    SHOVEL(Set.of(Material.WOODEN_SHOVEL,Material.STONE_SHOVEL,Material.COPPER_SHOVEL,Material.GOLDEN_SHOVEL,Material.IRON_SHOVEL,Material.DIAMOND_SHOVEL,Material.NETHERITE_SHOVEL)),
    HOE(Set.of(Material.WOODEN_HOE,Material.STONE_HOE,Material.COPPER_HOE,Material.GOLDEN_HOE,Material.IRON_HOE,Material.DIAMOND_HOE,Material.NETHERITE_HOE)),
    WOLF_ARMOR(Set.of(Material.WOLF_ARMOR),Set.of(Enchantment.MENDING)),
    BOW(Set.of(Material.BOW)),
    HORSE_ARMOR(Set.of(Material.LEATHER_HORSE_ARMOR,Material.COPPER_HORSE_ARMOR,Material.GOLDEN_HORSE_ARMOR,Material.IRON_HORSE_ARMOR,Material.DIAMOND_HORSE_ARMOR,Material.NETHERITE_HORSE_ARMOR),Set.of(Enchantment.MENDING)),
    ELYTRA(Set.of(Material.ELYTRA));

    private final Set<Material> types;
    private final Set<Enchantment> compatible_real_enchantments;

    ItemType(Set<Material> types){
        this.types = types;
        this.compatible_real_enchantments = Collections.emptySet();
    }

    ItemType(Set<Material> types, Set<Enchantment> compatible_real_enchantments){
        this.types = types;
        this.compatible_real_enchantments = compatible_real_enchantments;
    }

    private Set<Material> getTypes(){
        return types;
    }

    private Set<Enchantment> getCompatibleRealEnchantments(){
        return compatible_real_enchantments;
    }

    // Returns the ItemType of a provided material or null if absent
    public static ItemType fromMaterial(Material mat){
        for(ItemType type:ItemType.values())
            if(type.getTypes().contains(mat))
                return type;
        return null;
    }

    public boolean hasNewRealEnchantments(){
        return !compatible_real_enchantments.isEmpty();
    }

    public static boolean hasNewRealEnchantments(Material mat){
        if(fromMaterial(mat) != null)
            return fromMaterial(mat).hasNewRealEnchantments();
        return false;
    }

    // Returns whether or not the provided item is of a specific ItemType
    public boolean isOfType(ItemStack i){
        if(i == null)
            return false;
        return types.contains(i.getType());
    }

}
