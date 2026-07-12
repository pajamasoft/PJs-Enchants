package net.pajamasoft.pjenchants;

import java.util.Set;

public enum ItemGroup {

    ARMOR(Set.of(ItemType.HELMET,ItemType.CHESTPLATE,ItemType.LEGGINGS,ItemType.BOOTS)),
    MELEE(Set.of(ItemType.SWORD,ItemType.AXE,ItemType.SPEAR)),
    TOOLS(Set.of(ItemType.PICKAXE,ItemType.AXE,ItemType.SHOVEL,ItemType.HOE));


    private final Set<ItemType> group;

    ItemGroup(Set<ItemType> group){
        this.group = group;
    }

    public Set<ItemType>  getGroup(){
        return group;
    }
}
