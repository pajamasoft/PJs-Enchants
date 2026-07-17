package net.pajamasoft.pjenchants;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;

public class listener implements Listener {

    PJEnchants pje;
    File playerdata;
    FileConfiguration data;
    List<PotionEffectType> curses = Arrays.asList(PotionEffectType.BLINDNESS,PotionEffectType.SLOWNESS,PotionEffectType.WEAKNESS,PotionEffectType.NAUSEA,
            PotionEffectType.POISON,PotionEffectType.INSTANT_DAMAGE,PotionEffectType.WITHER,PotionEffectType.LEVITATION);
    HashMap<UUID,Boolean> doublejump = new HashMap<>();
    HashMap<UUID, Long> solartime = new HashMap<>();
    HashMap<UUID, Long> lunartime = new HashMap<>();
    HashMap<UUID, Long> stealthtime = new HashMap<>();
    HashMap<UUID, Long> draconictime = new HashMap<>();
    HashMap<UUID, Long> adrenaline = new HashMap<>();
    HashMap<UUID, Long> rage = new HashMap<>();
    HashMap<UUID, Long> skulls = new HashMap<>();
    HashMap<UUID, Boolean> spikes = new HashMap<>();
    HashMap<UUID, Long> needles = new HashMap<>();
    HashMap<UUID, Boolean> grounded = new HashMap<>();
    HashMap<UUID, Long> blaze = new HashMap<>();
    HashMap<UUID, Long> grav = new HashMap<>();
    HashMap<UUID, List<Entity>> ricochetlimit = new HashMap<>();
    HashMap<UUID, List<Monster>> ghosts = new HashMap<>();
    HashMap<UUID, Long> wolf_mending = new HashMap<>();
    HashMap<UUID, Pair<ItemStack,Long>> anvil_cooldown = new HashMap<>(); // Anvil prep event triple triggers causing dupe enchants on result
    List<Block> untouchable = new ArrayList<>();

    final int maxNumEnchants = 3;

    listener(PJEnchants pjEnchants){
        this.pje = pjEnchants;
        playerdata = pjEnchants.playerdata;
        data = pjEnchants.data;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        p.setFlying(false);
        doublejump.put(id,false);
        if(getHorseArmor(p)!=null)
            putOnHorseArmor(getHorse(p),getHorse(p).getInventory().getArmor(),p);
        pje.online.add(p);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        pje.logout(p,true);
        if(ghosts.containsKey(p.getUniqueId())){
            for(Monster mon:ghosts.get(p.getUniqueId())){
                mon.remove();
            }
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e){
        Player p = e.getPlayer();
        pje.logout(p,true);
    }

    @EventHandler
    public void onLightning(LightningStrikeEvent e){
        List<Entity> near = e.getLightning().getNearbyEntities(6,6,6);
        for(Entity ent:near){
            if(ent instanceof Player){
                Player p = (Player)ent;
                if(pje.hasEnchantment(p.getInventory().getBoots(),Enchant.GROUNDED)){
                    e.getLightning().teleport(p.getLocation());
                    return;
                }
            }
        }
    }

//    public void onChunkGen(ChunkPopulateEvent e){
//        World world = e.getWorld();
//        Chunk chunk = e.getChunk();
//        for(BlockState b:chunk.getTileEntities()){
//            if(b.getType().equals(Material.CHEST)){
//                try {
//                    World.Environment env = world.getEnvironment();
//                    Chest chest = (Chest) b.getBlock();
//                    Inventory inv = chest.getBlockInventory();
//                    List<Enchant> available = pje.sword_enchants;
//                    List<String> enchants;
//                    switch (env) {
//                        case NETHER:
//                            enchants = available.keySet().stream().toList();
//                            for (ItemStack item : inv) {
//                                if(pje.isSword(item)){
//                                    if(pje.percentChance(5))
//                                        pje.enchant(item,"judgment",1);
//                                    if(pje.percentChance(10))
//                                        pje.enchant(item,"wilting",(int)(Math.random()*3)+1);
//                                }
//                                else if(item.getType().equals(Material.ENCHANTED_BOOK)){
//                                    for(int i=0;i<3;i++) {
//                                        if (pje.percentChance(75)) {
//                                            String enchant = enchants.get((int) (Math.random() * enchants.size()));
//                                            pje.enchant(item, enchant, 1 + (int) (Math.random() * available.get(enchant)));
//                                        }
//                                    }
//                                }
//                            }
//                            break;
//                        case THE_END:
//                            for (ItemStack item : inv) {
//                                //if(sp.isEnchantable(item))
//                            }
//                            break;
//                    }
//
//                }catch(Exception ex){
//                    //
//                }
//            }
//        }
//    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent e){
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        if(p.getGameMode().equals(GameMode.SURVIVAL)) {

            if(doublejump.containsKey(id))
                if(!doublejump.get(id))
                    if(!p.getLocation().subtract(0,0.2,0). getBlock().getType().isSolid())
                        p.setAllowFlight(false);

            if (p.getInventory().getBoots() != null) {
                if (pje.hasEnchantment(p.getInventory().getBoots(), Enchant.ANTIGRAVITY) && doublejump.get(p.getUniqueId())) {
                    e.setCancelled(true);
                    p.setAllowFlight(false);
                    if(pje.hasEnchantment(p.getInventory().getChestplate(),Enchant.WINGS))
                        p.setAllowFlight(true);
                    int level = pje.getEnchantLevel(p.getInventory().getBoots(), Enchant.ANTIGRAVITY);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, level * 3));
                    doublejump.put(id, false);
                }
            }

            if(p.getInventory().getChestplate() != null){
                ItemStack chest = p.getInventory().getChestplate();
                if(pje.hasEnchantment(chest,Enchant.WINGS)){
                    pje.wings.put(id,chest);
                    doublejump.put(id, false);
                    ItemStack elytra = new ItemStack(Material.ELYTRA,1);
                    Damageable meta = (Damageable) elytra.getItemMeta();
                    meta.setDamage(elytra.getType().getMaxDurability()-20);
                    elytra.setItemMeta(meta);
                    List<Enchant> chestenchants = pje.getCustomEnchants(chest);
                    for(Enchant ench:chestenchants)     // Loops through chestplate enchants to apply any elytra enchants to the temp elytra
                        if(ench.isTypeCompatible(new ItemStack(Material.ELYTRA)))
                            pje.enchant(elytra,ench, pje.getEnchantLevel(chest,ench));
                    p.getInventory().setChestplate(elytra);
                    p.setGliding(true);
                }
                if(pje.hasEnchantment(chest,Enchant.LIFT)&&doublejump.get(id)){
                    e.setCancelled(true);
                    p.setAllowFlight(false);
                    doublejump.put(id,false);
                    int level = pje.getEnchantLevel(chest,Enchant.LIFT);
                    p.getWorld().spawnParticle(Particle.CLOUD,p.getLocation(),50);
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,0.5F,1);
                    p.setVelocity(p.getVelocity().add(p.getLocation().getDirection()).add(new Vector(0,1,0)));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje,() ->{
                        p.setGliding(true);
                    },2L);

                }
            }
        }
        if(p.getGameMode().equals(GameMode.SURVIVAL)||p.getGameMode().equals(GameMode.ADVENTURE))
            e.setCancelled(true);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e){
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        if(p.getInventory().getBoots()!=null&&!p.isSneaking()) {
            ItemStack boots = p.getInventory().getBoots();
            if (pje.hasEnchantment(boots, Enchant.ANTIGRAVITY)) {
                if(p.hasPotionEffect(PotionEffectType.LEVITATION))
                    p.removePotionEffect(PotionEffectType.LEVITATION);
            }

            if(pje.hasEnchantment(boots,Enchant.GROUNDED)){
                if(p.isInWater()) {
                    //p.sendMessage("Sinking");
                    new BukkitRunnable(){
                        public void run(){
                            if(p.isSneaking())
                                p.setVelocity(new Vector(0,-1,0));
                            else cancel();
                        }
                    }.runTaskTimer(pje,0,5L);
                }
            }

            if(pje.hasEnchantment(boots,Enchant.GLIDE)){
                if(p.hasPotionEffect(PotionEffectType.SLOW_FALLING))
                    p.removePotionEffect(PotionEffectType.SLOW_FALLING);
            }
        }

        if(p.isGliding()&&!p.isSneaking()){
            assert p.getInventory().getChestplate() != null;
            solartime.putIfAbsent(p.getUniqueId(),0L);
            lunartime.putIfAbsent(p.getUniqueId(),0L);

            if(System.currentTimeMillis()-solartime.get(p.getUniqueId())>=1000&&!p.isInWater()&& pje.hasEnchantment(p.getInventory().getChestplate(),Enchant.SOLAR)&&p.getWorld().getTime()>=0
                    &&p.getWorld().getTime()<12500&&!p.getWorld().hasStorm()&&!p.getWorld().isThundering()&&p.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                p.setVelocity(p.getVelocity().add(p.getLocation().getDirection()).multiply(0.75));
                solartime.put(p.getUniqueId(),System.currentTimeMillis());
                p.getWorld().playSound(p.getLocation(),Sound.ENTITY_SHULKER_SHOOT,0.6F,1);
                for(int i=0;i<5;i++){
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                        p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,p.getLocation(),1);
                    },5*i);
                }
            }

            if(System.currentTimeMillis()-lunartime.get(p.getUniqueId())>=500&&!p.isInWater()&& pje.hasEnchantment(p.getInventory().getChestplate(),Enchant.LUNAR)&&((pje.isNight(p.getWorld())&&!p.getWorld().hasStorm()&&!p.getWorld().isThundering())||((p.getWorld().getEnvironment().equals(World.Environment.THE_END)
            )))){
                p.setVelocity(p.getVelocity().add(p.getLocation().getDirection()).multiply(0.5));
                lunartime.put(p.getUniqueId(),System.currentTimeMillis());
                p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ENDER_DRAGON_FLAP,0.6F,1);
                for(int i=0;i<15;i++){
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                        p.getWorld().spawnParticle(Particle.GLOW,p.getLocation(),1);
                    },2*i);
                }
            }
        }

        if(hasStealthLeggings(p)){
            ItemStack legs = p.getInventory().getLeggings();
            stealthtime.putIfAbsent(p.getUniqueId(),0L);
            if(p.getVehicle() == null) {
                if (!p.isSneaking() && e.isSneaking() && !p.isGliding() && System.currentTimeMillis() - stealthtime.get(p.getUniqueId()) >= 5000) {

                    for (Entity ent : p.getNearbyEntities(20, 20, 20))
                        if (ent instanceof Monster) {
                            Monster mon = (Monster) ent;
                            if (mon.getTarget() != null)
                                if (mon.getTarget().equals(p))
                                    mon.setTarget(null);
                        }
                    p.setArrowsInBody(0);
                    p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1);
                    p.removePotionEffect(PotionEffectType.SPEED);
                    switch (pje.getEnchantLevel(legs, Enchant.STEALTH)) {
                        case 1:
                            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000, 0));
                            break;
                        case 2:
                            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000, 0));
                            cutArmor(p);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 1000, 1));
                            break;
                        case 3:
                            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000, 0));
                            cutArmor(p);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 1000, 2));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000, 2));
                            break;
                        default:
                            break;
                    }
                } else if (p.isSneaking()) {
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    if (pje.armor.containsKey(p.getUniqueId())) {
                        pje.pasteArmor(p);
                        stealthtime.put(p.getUniqueId(), System.currentTimeMillis());
                    }
                    p.removePotionEffect(PotionEffectType.RESISTANCE);
                    p.removePotionEffect(PotionEffectType.SPEED);
                }
            }
        }

        if(pje.getArmorScore(p,Enchant.MAGNETIC)>0&&!p.isSneaking()){   // if armor score is >0, at least one piece has magnetic
            int score = pje.getArmorScore(p,Enchant.MAGNETIC); // radius is proportional to number of pieces
            List<Entity> near = p.getNearbyEntities(3+score,3+score,3+score);
            List<Entity> magnetic = new ArrayList<>();
            pje.magnet.put(id,true);
            for(Entity ent:near){
                if(ent instanceof Item || ent instanceof IronGolem || ent instanceof Minecart){
                    magnetic.add(ent);
                }
                if(ent instanceof Arrow arrow){
                    Vector dist = p.getLocation().subtract(ent.getLocation()).toVector();
                    arrow.teleport(arrow.getLocation().add(dist.normalize()));
                    arrow.setVelocity(dist);
                }
                if(ent instanceof Player p2){
                    ItemStack[] armor = p2.getInventory().getArmorContents();
                    boolean hasiron = false;

                    // Ripping arrows out of opponents
                    int arrows = p2.getArrowsInBody();
                    needles.remove(p2.getUniqueId());
                    p2.setArrowsInBody(0);
                    p2.getWorld().playSound(p2.getLocation(),Sound.ENTITY_WITHER_BREAK_BLOCK,1,1);
                    p2.damage(arrows*0.5,p);
                    p.getInventory().addItem(new ItemStack(Material.ARROW,arrows));

                    for(ItemStack iron:p2.getInventory())
                        if(iron != null)
                            if(iron.getType().toString().toUpperCase().contains("IRON"))
                                hasiron = true;

                    for(ItemStack i:armor)
                        if(i.getType().toString().toUpperCase().contains("IRON"))
                            hasiron = true;

                    if(pje.hasEnchantment(p2.getEquipment().getBoots(),Enchant.GROUNDED))
                        hasiron = false;

                    if(hasiron)
                        magnetic.add(p2);
                }
                if(ent instanceof Monster mon){
                    ItemStack[] armor = Objects.requireNonNull(mon.getEquipment()).getArmorContents();
                    boolean hasiron = false;
                    for(ItemStack i:armor)
                        if(i.getType().toString().toUpperCase().contains("IRON"))
                            hasiron = true;
                    if(mon.getEquipment().getItemInMainHand().getType().toString().contains("IRON"))
                        hasiron = true;
                    if(mon.getEquipment().getItemInOffHand().getType().toString().contains("IRON"))
                        hasiron = true;
                    if(hasiron)
                        magnetic.add(mon);
                }
            }
            p.getWorld().playSound(p.getLocation(),Sound.BLOCK_BEACON_POWER_SELECT,0.2F,1);
            new BukkitRunnable(){
                public void run(){
                    p.getWorld().playSound(p.getLocation(),Sound.BLOCK_BEACON_POWER_SELECT,0.2F,1);
                    for(Entity ent:magnetic){
                        ent.setVelocity(p.getLocation().subtract(ent.getLocation()).toVector().normalize());
                        if(ent instanceof Item) {
                            if (ent.getLocation().distance(p.getLocation()) <= 2) {
                                p.getInventory().addItem(((Item) ent).getItemStack());
                                ent.remove();
                            }
                        }
                    }
                    if(!pje.magnet.containsKey(id))
                        cancel();
                    else if(!pje.magnet.get(id))
                        cancel();
                }
            }.runTaskTimer(pje,0,8L);
        }
        else if(pje.getArmorScore(p,Enchant.MAGNETIC)>0&&p.isSneaking()) pje.magnet.put(id,false);
    }

//    @EventHandler
//    public void onHorseJump(HorseJumpEvent e){
//        if(!(e.getEntity() instanceof Horse))
//            return;
//        if(e.getEntity().getPassengers().isEmpty())
//            return;
//        if(!(e.getEntity().getPassengers().get(0) instanceof Player))
//            return;
//
//        Player p = (Player)e.getEntity().getPassengers().get(0);
//
//        pje.getLogger().info("Horse jumped!");
//        Horse h = (Horse)e.getEntity();
//        ItemStack ha = h.getInventory().getArmor();
//        if(pje.hasEnchantment(ha,Enchant.HURDLE)){
//            double power = pje.getEnchantLevel(ha,Enchant.HURDLE) * (1+e.getPower());
//            pje.getLogger().info("Hurdle power: " + power);
//            pje.getLogger().info("Horse velocity: " + h.getVelocity());
//            pje.getLogger().info("Player velocity: " + p.getVelocity());
//            h.setVelocity(p.getLocation().getDirection().add(new Vector(0,power,0)));
//        }
//    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        EntityDamageEvent.DamageCause cause = e.getCause();

        if(e.getEntity() instanceof Horse h){
            ItemStack ha = h.getInventory().getArmor();
            if(ha!=null){
//                if(pje.hasEnchantment(ha,Enchant.HURDLE)) {
//                    if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
//                        e.setDamage(e.getDamage() / pje.getEnchantLevel(ha, Enchant.HURDLE));
//                        if (e.getDamage() < 1)
//                            e.setCancelled(true);
//                    }
//                }
                if(ha.getEnchantments().containsKey(Enchantment.PROTECTION)){
                    e.setDamage(e.getDamage() / ha.getEnchantmentLevel(Enchantment.PROTECTION));
                    if(e.getDamage() < 0.25)
                        e.setDamage(0.25);
                }
            }
        }

        if(e.getEntity() instanceof Wolf wolf){
            ItemStack armor = wolf.getEquipment().getItem(EquipmentSlot.BODY);
            if(!armor.isEmpty()){
                if(pje.hasEnchantment(armor,Enchant.HELLHOUND)) {
                    if(e.getCause() == EntityDamageEvent.DamageCause.LAVA || e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                        e.setCancelled(true);
                    }
                }
                if(armor.getEnchantments().containsKey(Enchantment.UNBREAKING)){
                    if(pje.percentChance(30*armor.getEnchantmentLevel(Enchantment.UNBREAKING))){
                        e.setCancelled(true);
                    }
                }
                if(pje.hasEnchantment(armor,Enchant.WEREWOLF)){
                    Damageable meta = (Damageable)armor.getItemMeta();
                    if(meta.getDamage() >= Material.WOLF_ARMOR.getMaxDurability() - 1){
                        if(wolf.getTarget() != null){
                            wolf.getAttribute(Attribute.SCALE).setBaseValue(1);
                            wolf.removePotionEffect(PotionEffectType.STRENGTH);
                            wolf.removePotionEffect(PotionEffectType.SPEED);
                            wolf.getWorld().playSound(wolf.getLocation(),Sound.ENTITY_WITHER_BREAK_BLOCK,1,0.2F);
                            wolf.getWorld().spawnParticle(Particle.LARGE_SMOKE,wolf.getLocation(),10,0,0,0);
                        }
                    }
                }
            }
        }
        if(e.getEntity() instanceof Horse horse){
            ItemStack armor = horse.getInventory().getArmor();
            if(pje.hasEnchantment(armor,Enchant.HELLISH)){
                if(e.getCause() == EntityDamageEvent.DamageCause.LAVA || e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                    e.setCancelled(true);
                }
            }
            if(armor != null) {
                if (armor.getEnchantments().containsKey(Enchantment.PROTECTION)) {
                    int level = armor.getEnchantments().get(Enchantment.PROTECTION);
                    e.setDamage(e.getDamage() / (level + 1));
                }
            }
        }

        if(e.getEntity() instanceof Player p) {
            if(cause.equals(EntityDamageEvent.DamageCause.FALL)) {
                if (pje.hasEnchantment(p.getInventory().getChestplate(), Enchant.DRAG) && p.isGliding())
                    e.setCancelled(true);
            }
            if(pje.hasEnchantment(p.getInventory().getChestplate(),Enchant.RAGE)){
                rage.putIfAbsent(p.getUniqueId(),0L);
                if(p.getHealth() > 5 && p.getHealth() - e.getDamage() < 5 && System.currentTimeMillis() - rage.get(p.getUniqueId()) >= 6000){
                    int level = pje.getEnchantLevel(p.getInventory().getChestplate(),Enchant.RAGE);
                    rage.put(p.getUniqueId(),System.currentTimeMillis());
                    p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH,80+20*level,1));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ENDER_DRAGON_GROWL,1,1);
                }
            }
            if(pje.hasEnchantment(p.getInventory().getLeggings(),Enchant.ADRENALINE)){
                adrenaline.putIfAbsent(p.getUniqueId(),0L);
                if(p.getHealth() > 5 && p.getHealth() - e.getDamage() < 5 && System.currentTimeMillis() - adrenaline.get(p.getUniqueId()) >= 6000){
                    int level = pje.getEnchantLevel(p.getInventory().getLeggings(),Enchant.ADRENALINE);
                    int currentspeed = 0;
                    adrenaline.put(p.getUniqueId(),System.currentTimeMillis());
                    if(p.hasPotionEffect(PotionEffectType.SPEED))
                        currentspeed = p.getPotionEffect(PotionEffectType.SPEED).getAmplifier();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,80+20*level,currentspeed + 1));
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_SHULKER_SHOOT,0.5F,1);
                }
            }

            if(pje.hasEnchantment(p.getInventory().getHelmet(),Enchant.CONSTITUTION)&&p.getHealth()<7) {
                int level = pje.getEnchantLevel(p.getInventory().getHelmet(),Enchant.CONSTITUTION);
                boolean active = false;
                if(p.hasPotionEffect(PotionEffectType.RESISTANCE))
                    if(p.getPotionEffect(PotionEffectType.RESISTANCE).getAmplifier()<1)
                        active = true;
                if (!p.hasPotionEffect(PotionEffectType.RESISTANCE))
                    active = true;
                if(active){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60 + 20 * level, 1));
                    p.getWorld().playSound(p.getLocation(),Sound.BLOCK_ANVIL_LAND,0.5F,1);
                    pje.particleRing(Particle.ENCHANT,p.getEyeLocation(),0.8,2);
                }
            }
        }
    }

    @EventHandler
    public void onBoost(PlayerElytraBoostEvent e){
        Player p = e.getPlayer();
        ItemStack elytra = p.getInventory().getChestplate();
        if(pje.hasEnchantment(elytra,Enchant.THRUST)){
            int level = pje.getEnchantLevel(elytra,Enchant.THRUST);
            Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                p.setVelocity(p.getVelocity().multiply(level*0.75));
                p.playSound(p.getLocation(),Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1,1);
                p.getWorld().spawnParticle(Particle.FLAME,p.getLocation(),30);
            },10);
        }
    }

    @EventHandler
    public void onJump(PlayerJumpEvent e){
        Player p = e.getPlayer();
        p.setAllowFlight(true);
        if(p.hasPotionEffect(PotionEffectType.SLOW_FALLING) && p.getGameMode().equals(GameMode.SURVIVAL))
            Bukkit.getScheduler().scheduleSyncDelayedTask(pje, ()->{
                p.setAllowFlight(false);
            },60L);
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent e){
        EntityType type = e.getEntityType();

        if(type == EntityType.WOLF){
            Wolf wolf = (Wolf)e.getEntity();
            if(!wolf.isTamed())
                return;
            if(!wolf.getEquipment().getItem(EquipmentSlot.BODY).isEmpty()){
                ItemStack armor = wolf.getEquipment().getItem(EquipmentSlot.BODY);
                if(armor.getEnchantments().containsKey(Enchantment.MENDING)){
                    UUID wid = wolf.getUniqueId();
                    wolf_mending.putIfAbsent(wid,0L);
                    if(System.currentTimeMillis() - wolf_mending.get(wid) >= 10000){
                        if(armor.hasItemMeta())
                            if(armor.getItemMeta() instanceof Damageable){
                                Damageable meta = (Damageable)armor.getItemMeta();
                                if(meta.getDamage() > 1) {
                                    meta.setDamage(meta.getDamage() - 1);
                                    armor.setItemMeta(meta);
                                }
                            }
                        wolf_mending.put(wid,System.currentTimeMillis());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        if(p.getLocation().subtract(0,0.2,0).getBlock().getType().isSolid())
            doublejump.put(p.getUniqueId(), true);

        //Elytra enchants
        if(!p.isGliding()&&p.getInventory().getChestplate()!=null){
            ItemStack elytra = p.getInventory().getChestplate();
            if(pje.wings.containsKey(id)){
                p.getInventory().setChestplate(pje.wings.get(id));
                pje.wings.remove(id);
            }
//            if(p.getVelocity().length()>1&& pje.hasEnchantment(elytra,"comet")){
//                comettime.putIfAbsent(id,0L);
//                if(System.currentTimeMillis()-comettime.get(id)>=200){
//                    comettime.put(id,System.currentTimeMillis());
//                    List<Entity> near = p.getNearbyEntities(2,2,2);
//                    pje.particleRing(Particle.FLAME,p.getEyeLocation(),1,5);
//                    for(Entity ent:near)
//                        if(!ent.equals(p))
//                            if(ent instanceof LivingEntity)
//                                ((LivingEntity)ent).damage(2);
//                }
//            }
        }


        // Boots enchants
        if(p.getInventory().getBoots()!=null){
            if(pje.hasEnchantment(p.getInventory().getBoots(),Enchant.WAVERIDER)){
                int level = pje.getEnchantLevel(p.getInventory().getBoots(),Enchant.WAVERIDER);
                if(p.getLocation().subtract(0,1,0).getBlock().getType().equals(Material.WATER)&&p.isSprinting())
                    if(p.getLocation().getBlock().getType().equals(Material.AIR)) {
                        p.setVelocity(p.getLocation().getDirection().multiply(0.35*level).multiply(new Vector(1,0.75,1)));
                        pje.particleRing(Particle.FISHING,p.getLocation(),1.5,5);
                        p.getWorld().playSound(p.getLocation(),Sound.ENTITY_AXOLOTL_SWIM,0.2F,1);
                    }
            }
        }

        if(p.getVehicle() instanceof Horse){
            Horse horse = (Horse)p.getVehicle();
            if(getHorseArmor(p)!=null){
                ItemStack horsearmor = getHorseArmor(p);
                if(pje.hasEnchantment(horsearmor,Enchant.WAVERIDER)){
                    int level = pje.getEnchantLevel(horsearmor,Enchant.WAVERIDER);
                    if(horse.getLocation().subtract(0,0.4,0).getBlock().getType().equals(Material.WATER)) {
                        if (horse.getLocation().getBlock().getType().equals(Material.AIR)||horse.getLocation().add(0,1,0).getBlock().getType().equals(Material.AIR)) {
                            horse.setVelocity(p.getLocation().getDirection().multiply(0.3 * level).multiply(new Vector(2, 0, 2)).add(new Vector(0, 0.3, 0)));
                            pje.particleRing(Particle.FISHING, horse.getLocation(), 2, 5);
                            horse.getWorld().playSound(p.getLocation(), Sound.ENTITY_AXOLOTL_SWIM, 0.2F, 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEndermanAggro(EntityTargetLivingEntityEvent e){
        if(!(e.getEntity() instanceof Enderman))
            return;
        if(!(e.getTarget() instanceof Player))
            return;
        Player p = (Player)e.getTarget();
        Enderman end = (Enderman)e.getEntity();

        ItemStack helmet = p.getInventory().getHelmet();
        if(pje.hasEnchantment(helmet,Enchant.ENDEREYES) && e.getReason() != EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY) {
            e.setCancelled(true);
            if(p.isSneaking()){
                Location loc = end.getLocation();
                end.teleport(p.getLocation());
                p.teleport(loc);
                p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
                end.getWorld().playSound(end.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            }
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e){
        Player p = null;
        if(e.getEntity() instanceof Player)
            p = (Player)e.getEntity();
        Entity proj = e.getProjectile();
        ItemStack bow = e.getBow();
        if(e.getEntity() instanceof Skeleton){
            Skeleton skele = (Skeleton)e.getEntity();
            bow = skele.getEquipment().getItemInMainHand();
        }
        assert bow != null;

//        if(!Objects.requireNonNull(bow.getItemMeta()).hasLore()) {
//            Bukkit.broadcastMessage("Entity "+e.getEntity().getType()+" shot arrow from bow without LORE");
//            return;
//        }

        if(p!=null) {
            proj.setCustomName(p.getName() + "%");

            if (p.getInventory().getChestplate() != null) {
                if (p.getInventory().getChestplate().getType().equals(Material.ELYTRA)) {
                    ItemStack elytra = p.getInventory().getChestplate();
                    if (pje.hasEnchantment(elytra, Enchant.DRACONIC) && p.isGliding() && p.getInventory().contains(Material.FIRE_CHARGE)) {
                        int index = pje.invIndexOf(pje.getInventoryAsList(p), Material.FIRE_CHARGE);
                        draconictime.putIfAbsent(p.getUniqueId(), 0L);
                        Vector vel = proj.getVelocity();
                        Location loc = proj.getLocation();
                        if (System.currentTimeMillis() - draconictime.get(p.getUniqueId()) > 1500 && p.getInventory().getItem(index).getAmount() > 2) {
                            p.getInventory().getItem(index).setAmount(p.getInventory().getItem(index).getAmount() - 3);
                            DragonFireball fb = p.getWorld().spawn(loc, DragonFireball.class);
                            proj.remove();
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PARROT_IMITATE_ENDER_DRAGON, 0.5F, 1);
                            fb.setVelocity(vel);
                            draconictime.put(p.getUniqueId(), System.currentTimeMillis());
                            for (int i = 0; i < 40; i++) {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(pje, () -> {
                                    if (!fb.isDead())
                                        fb.setVelocity(vel);
                                }, 5 * i);
                            }
                            return;
                        }
                    }
                }
            }
        }
        Enchant[] bowce = pje.getAvailableEnchants(ItemType.BOW).toArray(new Enchant[0]);
        for(int i=0;i<bowce.length;i++){
            if(pje.hasEnchantment(bow,bowce[i])){
                String prefix = "";
                if(proj.getCustomName()!=null)
                    if(proj.getCustomName().contains("%"))
                        prefix = proj.getCustomName();

                proj.setCustomName(prefix+bowce[i].name().toLowerCase()+pje.getEnchantLevel(bow,bowce[i])+"%"); // adds new enchant to arrow name in order of "{name}{level}%"
            }
        }

        if(bow.getEnchantments().containsKey(Enchantment.POWER))
            proj.setCustomName(proj.getCustomName()+"power"+bow.getEnchantments().get(Enchantment.POWER)+"%"); // if bow has power, acts like it is a custom enchant

        if(pje.hasEnchantment(bow,Enchant.ANTIGRAVITY)){
            int level = pje.getEnchantLevel(bow,Enchant.ANTIGRAVITY);
            proj.setVelocity(proj.getVelocity().multiply(2));
            proj.setGravity(false);
            Bukkit.getScheduler().scheduleSyncDelayedTask(pje, ()->{
                proj.setGravity(true);
            },40L + 40L*level);
        }
        if(pje.hasCustomEnchants(bow)){
            for(int i=0;i<24;i++) {
                ItemStack finalBow = bow;
                Bukkit.getScheduler().scheduleSyncDelayedTask(pje, () -> {
                    if(!proj.isDead()) {
                        if (pje.hasEnchantment(finalBow, Enchant.ANTIGRAVITY))
                            proj.getWorld().spawnParticle(Particle.GLOW, proj.getLocation(), 0);
                        if(pje.hasEnchantment(finalBow,Enchant.HEALING))
                            proj.getWorld().spawnParticle(Particle.HEART,proj.getLocation(),0);
                        if(pje.hasEnchantment(finalBow,Enchant.GRAVITY))
                            proj.getWorld().spawnParticle(Particle.WITCH,proj.getLocation(),0);
                        if (pje.hasEnchantment(finalBow, Enchant.FREEZING)&&!proj.isVisualFire())
                            proj.getWorld().spawnParticle(Particle.SNOWFLAKE, proj.getLocation(), 0);
                        if (pje.hasEnchantment(finalBow, Enchant.VENOM))
                            proj.getWorld().spawnParticle(Particle.FALLING_SPORE_BLOSSOM, proj.getLocation(), 0);
                        if (pje.hasEnchantment(finalBow, Enchant.NITRO))
                            proj.getWorld().spawnParticle(Particle.LAVA, proj.getLocation(), 0);
                        if(proj.isInWater()&& pje.hasEnchantment(finalBow,Enchant.FREEZING))
                            proj.getLocation().getBlock().setType(Material.FROSTED_ICE);
                    }
                },2*i);
            }
            if(pje.hasEnchantment(bow,Enchant.GRAVITY)){
                for(int i=0;i<150;i++) {
                    ItemStack finalBow1 = bow;
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pje,()->{
                        double range = pje.getEnchantLevel(finalBow1,Enchant.GRAVITY)/3.0;
                        List<Entity> near = proj.getNearbyEntities(range,range,range);
                        for(Entity ent:near)
                            if(ent instanceof Monster && !proj.isDead() && !proj.isOnGround())
                                ent.setVelocity(proj.getLocation().subtract(ent.getLocation()).toVector().normalize().multiply(1.0/proj.getLocation().distance(ent.getLocation())));
                    },3*i);
                }
            }
            if(pje.hasEnchantment(bow,Enchant.HOMING)){
                final boolean hasHealing = pje.hasEnchantment(bow,Enchant.HEALING);
                for(int i=0;i<150;i++){
                    Player finalP = p;
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pje,()->{
                        LivingEntity nearest = null;
                        double dist = 100;
                        for(Entity ent:proj.getNearbyEntities(3,3,3))
                            if(ent.getLocation().distance(proj.getLocation())<dist&&ent instanceof LivingEntity){
                                dist = ent.getLocation().distance(proj.getLocation());
                                nearest = (LivingEntity)ent;
                            }
                        if(nearest!=null) {
                            boolean rightType = false;
                            if(nearest instanceof Player){
                                if(hasHealing && nearest instanceof Animals)
                                    rightType = true;
                            }
                            else if(nearest instanceof Monster)
                                rightType = true;
                            if(!nearest.equals(finalP)&&rightType) {
                                Vector v = proj.getVelocity();
                                proj.setGravity(false);
                                proj.setVelocity(v.add(nearest.getEyeLocation().subtract(proj.getLocation()).toVector().normalize().multiply(0.7)));
                            }
                        }
                    },i);
                }
            }
        }
    }

//    @EventHandler
//    public void onMobSpawn(EntitySpawnEvent e){
//        if(e.getEntity() instanceof Skeleton){
//            Skeleton mon = (Skeleton)e.getEntity();
//            ItemStack weapon = Objects.requireNonNull(mon.getEquipment()).getItemInMainHand();
//            if(sp.percentChance(100)&&sp.isWeapon(weapon)) {
//                mon.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100000,1));
//                Map<String, Integer> ce = sp.bow_enchants;;
//                ItemStack item = new ItemStack(Material.BOW,1);
//
//                ItemMeta meta = item.getItemMeta();
//
//                List<Object> cef = Arrays.stream(ce.keySet().toArray()).toList();
//                String re = cef.get((int)(Math.random()*cef.size())).toString();
//                sp.enchant(item,re,(int)(Math.random()*sp.getMaxEnchantLevel(re)));
//
//                item.addEnchantment(Enchantment.DURABILITY,3);
//                item.setItemMeta(meta);
//                mon.getEquipment().setItemInMainHand(item);
//            }
//
//        }
//    }

    @EventHandler
    public void onFall(PlayerMoveEvent e){
        Player p = e.getPlayer();
        float fall_height = p.getFallDistance();
        int limit = 3;
        if(pje.hasEnchantment(p.getEquipment().getChestplate(),Enchant.WINGS))
            limit = 5;
        if(p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE){
            if(fall_height > limit){
                p.setAllowFlight(false);
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e){
        Projectile proj = e.getEntity();

        if(e.isCancelled())
            return;

        if(!(e.getEntity() instanceof Arrow))
            return;
        Arrow arrow = (Arrow)e.getEntity();
        if(arrow.getCustomName()==null)
            return;
        String name = arrow.getCustomName();        // format: enchant(#)%enchant(#)%....
        final String fname = name;
        HashMap<String,Integer> enchants = new HashMap<>();
        if(!name.contains("%"))
            return;
        Player p = Bukkit.getPlayer(name.substring(0,name.indexOf('%')));

        name = name.substring(name.indexOf('%')+1);
        int numindex = 0;
        int level = 0;

        while(name.contains("%")){
            for(int i=0;i<name.length();i++) {
                try {
                    level = Integer.parseInt(String.valueOf(name.charAt(i)));
                    numindex = i;
                    break;
                }
                catch (NumberFormatException ex) { }
            }
            enchants.put(name.substring(0,numindex),level);
            if(name.indexOf('%')==name.lastIndexOf('%'))
                name = "";
            else name = name.substring(name.indexOf('%')+1);
        }

        if(p!=null)
            if(p.equals(e.getHitEntity()))
                return;

        // Total list of enchants gathered, activate each effect here

        if(enchants.containsKey("freezing")){
            if((e.getHitEntity() instanceof LivingEntity)&&!arrow.isVisualFire()) {
                LivingEntity target = (LivingEntity) e.getHitEntity();
                int lvl = enchants.get("freezing");
                int freezeticks = 460 + 20 * lvl;

                if(Objects.requireNonNull(Objects.requireNonNull(target.getEquipment()).getHelmet()).getType().equals(Material.AIR))
                    target.getEquipment().setHelmet(new ItemStack(Material.ICE,1));

                if (target instanceof Player) {
                    Player p2 = (Player) target;
                    if (pje.getArmorScore(p2, Enchant.MOLTEN) > 0) {
                        int score = pje.getArmorScore(p2, Enchant.MOLTEN);
                        freezeticks = 460 + 20 * lvl - 20 * score;
                    }
                }
                for (int i = 0; i < freezeticks / 20; i++) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje, () -> {
                        if (!target.isDead()) {
                            if (target.getFreezeTicks() > 200) {
                                Location loc = target.getLocation();
                                pje.particleDisc(Particle.SNOWFLAKE, new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 0.5), 0.5, 5);
                                target.setFireTicks(0);
                            }
                        }
                    }, 20 * i);
                }
                target.setFreezeTicks(freezeticks);
                target.setFireTicks(0);
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 0.3F, 1);
            }
        }

        if(enchants.containsKey("gravity")){
            if(e.getHitEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) e.getHitEntity();
                if(!(target instanceof EnderDragon)){

                    int lvl = enchants.get("gravity");
                    if (pje.percentChance(15+5*lvl)) {
                        target.teleport(target.getLocation().subtract(0, 1, 0));
                        target.removePotionEffect(PotionEffectType.LEVITATION);
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 50, 2, false, true));
                        BlockData dat = target.getLocation().subtract(0, 1, 0).getBlock().getBlockData();
                        target.getWorld().spawnParticle(Particle.BLOCK, target.getLocation(), 5, 0, 0, 0, dat);
                        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 1, 0F);
                        for (int i = 0; i < 10; i++)
                            Bukkit.getScheduler().scheduleSyncDelayedTask(pje, () -> {
                                target.setVelocity(new Vector(0, -10, 0));
                            }, 5L * i);
                    }
                }
            }
        }

        if(enchants.containsKey("healing")){
            if(e.getHitEntity() instanceof LivingEntity){
                LivingEntity target = (LivingEntity) e.getHitEntity();
                int power = 0;
                if(enchants.containsKey("power"))
                    power = enchants.get("power");
                pje.particleRing(Particle.HEART,target.getLocation().add(0,2,0),1,5);
                try {
                    target.setHealth(target.getHealth() +2+power);
                }catch(Exception ex){}
                e.setCancelled(true);
                e.getEntity().remove();
                for(int i=0;i<3;i++) {
                    int finalI = i;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                        target.getWorld().playSound(target.getLocation(),Sound.BLOCK_NOTE_BLOCK_HARP,1F,1F* finalI);
                    },5*i);
                }
                for(PotionEffect pot:target.getActivePotionEffects())
                    if(pje.isNegativeEffect(pot.getType()))
                        target.removePotionEffect(pot.getType());
            }
        }

        if(enchants.containsKey("nitro")){
            int lvl = enchants.get("nitro");
            if(e.getHitEntity()!=null){
                if(e.getHitEntity() instanceof LivingEntity) {
                    LivingEntity ent = (LivingEntity)e.getHitEntity();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje, () -> {
                        boolean canbreak = true;
                        if (!ent.isDead() && canbreak)
                            ent.getWorld().createExplosion(ent.getLocation(), 0.5F, false, true);
                        arrow.remove();
                    }, 100L - 20L * lvl);
                }
            }
            if(e.getHitBlock()!=null){
                Block b = e.getHitBlock();
                if(arrow.isVisualFire()) // sets block on fire if arrow is on fire
                    if(b.getRelative(Objects.requireNonNull(e.getHitBlockFace())).getType().isAir())
                        b.getRelative(Objects.requireNonNull(e.getHitBlockFace())).setType(Material.FIRE);
                Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                    if(arrow.isInBlock()) {
                        boolean canbreak = true;
                        if(canbreak)
                            b.getWorld().createExplosion(b.getLocation(), 1, false, true);
                        arrow.remove();
                    }
                },110L-20L*lvl);
            }
        }

        if(enchants.containsKey("venom")){
            int lvl = enchants.get("venom");
            if(pje.percentChance(30)) {
                if (e.getHitEntity() instanceof LivingEntity) {
                    LivingEntity ent = (LivingEntity) e.getHitEntity();
                    if(ent instanceof Zombie || ent instanceof Skeleton)
                        ent.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 + 20 * lvl, 1, false, true));
                    else ent.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 + 20 * lvl, 1, false, true));
                    ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
                }
            }
        }

        if(enchants.containsKey("ricochet")){
            if(e.getHitEntity()!=null&&p!=null){
                if(e.getHitEntity() instanceof LivingEntity) {
                    int lvl = enchants.get("ricochet");
                    List<Entity> hitents = new ArrayList<>();
                    ricochetlimit.putIfAbsent(p.getUniqueId(),hitents);
                    if(ricochetlimit.containsKey(p.getUniqueId()))
                        hitents=ricochetlimit.get(p.getUniqueId());
                    LivingEntity ent = (LivingEntity)e.getHitEntity();
                    List<Entity> near = ent.getNearbyEntities(20,20,20);
                    double dist = 20*20;
                    LivingEntity nearest = null;
                    for(Entity a:near) {
                        if(((a instanceof Player)&&!a.equals(p))||((a instanceof Monster)&&!a.equals(ent))){
                            if(a.getLocation().distance(ent.getLocation())<dist&&!ricochetlimit.get(p.getUniqueId()).contains(a)) {
                                dist = a.getLocation().distance(ent.getLocation());
                                nearest = (LivingEntity)a;
                            }
                        }
                    }
                    if(nearest != null){
                        hitents.add(nearest);
                        ricochetlimit.put(p.getUniqueId(),hitents);
                        if(ricochetlimit.get(p.getUniqueId()).size()<lvl+3 && !ent.isDead()) {
                            Vector v = (nearest.getEyeLocation().toVector()).subtract(ent.getEyeLocation().toVector()).normalize();
                            Arrow newarrow = ent.getWorld().spawnArrow(ent.getEyeLocation().add(v), v.multiply(2), 1F, 0);
                            if (fname.contains("antigravity"))
                                newarrow.setGravity(false);
                            newarrow.setCustomName(fname);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(pje, newarrow::remove,300L);
                            arrow.remove();
                        }
                        else{
                            if(ricochetlimit.containsKey(p.getUniqueId()))
                                ricochetlimit.remove(p.getUniqueId());
                        }
                    }
                }
            }
        }

        if(enchants.containsKey("grappling")){
            assert p != null;
            if(e.getHitEntity() instanceof LivingEntity){
                LivingEntity ent = (LivingEntity) e.getHitEntity();
                Vector v = p.getLocation().subtract(ent.getLocation()).toVector().normalize().multiply(4);
                try {
                    ent.setVelocity(v);
                }catch(Exception ex){}
            }
            assert p != null;
            if(e.getHitBlock() != null && e.getHitEntity() == null && !p.isGliding() && !p.isSneaking()) {
                if (p.getLocation().add(0,-0.2,0).getBlock().getType().equals(Material.AIR)) {
                    Vector v = e.getHitBlock().getLocation().toVector().subtract(p.getLocation().toVector());
                    double dist = v.length();
                    v.normalize();
                    if(dist<30) {
                        v.multiply(dist / 10);
                        v.add(new Vector(0, 1, 0));
                    }
                    p.setVelocity(v);
                }
            }
        }
    }

    @EventHandler
    public void prepEnchant(PrepareItemEnchantEvent e){
        Player p = e.getEnchanter();
        ItemStack item = e.getItem();
        EnchantingInventory inv = (EnchantingInventory) e.getInventory();
        Block b = e.getEnchantBlock();
        if(pje.hasCustomEnchants(item)){
            e.getOffers()[0]=null;
            e.getOffers()[1]=null;
            e.getOffers()[2]=null;
            return;
        }
        if(inv.getSecondary()==null)
            return;
//        if(item.getType().equals(Material.ELYTRA)||pje.isHorseArmor(item)||item.getType().equals(Material.WOLF_ARMOR)){
//            Enchantment enc = Enchantment.UNBREAKING;
//    //            if(sp.isHorseArmor(item))
//    //                enc = Enchantment.PROTECTION_ENVIRONMENTAL;
//            int amount = inv.getSecondary().getAmount();
//            int level = 2*pje.getNearbyShelves(b);
//            if(level>30)
//                level = 30;
//            p.sendMessage("Level: "+level+", amount: "+amount);
//            if(amount>0)
//                e.getOffers()[0] = new EnchantmentOffer(enc,1,level/3);
//            if(amount>1)
//                e.getOffers()[1] = new EnchantmentOffer(enc,2,2*(level/3));
//            if(amount>2)
//                e.getOffers()[2] = new EnchantmentOffer(enc,3,level);
//        }

    }

    @EventHandler
    public void onEnchant(EnchantItemEvent e){
        ItemStack item = e.getItem();
        int level = e.whichButton() + 1;
        int random_index;
        int random_level;
        Player p = e.getEnchanter();
        List<Enchant> custom_enchants = new ArrayList<>();

        if(!pje.getCustomEnchants(item).isEmpty()) {
            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BASS,1,1);
            e.setCancelled(true);
            return;
        }

        if(pje.isSword(item)){
            custom_enchants = new ArrayList<>(List.copyOf(pje.sword_enchants));
        }
        else if(pje.isBow(item)){
            custom_enchants = new ArrayList<>(List.copyOf(pje.bow_enchants));
        }
        else if(pje.isHelmet(item)){
            custom_enchants = new ArrayList<>(List.copyOf(pje.helmet_enchants));
        }
        else if(pje.isChestplate(item)){
            custom_enchants = new ArrayList<>(List.copyOf(pje.chestplate_enchants));
        }
        else if(pje.isLeggings(item)){
            custom_enchants = new ArrayList<>(List.copyOf(pje.leggings_enchants));
        }
        else if(pje.isBoots(item)){
            custom_enchants = new ArrayList<>(List.copyOf(pje.boots_enchants));
        }
        else if(item.getType().equals(Material.BOOK)) {
            custom_enchants = new ArrayList<>(List.copyOf(pje.all_enchants));
        }
        else if(pje.isPickaxe(item)){
            custom_enchants = new ArrayList<>(List.copyOf(pje.pick_enchants));
        }
        else if(pje.isAxe(item)) {
            custom_enchants = new ArrayList<>(List.copyOf(pje.axe_enchants));
        }
        else if(pje.isElytra(item)){
            custom_enchants = new ArrayList<>(List.copyOf(pje.elytra_enchants));
        }
        else if(pje.isHoe(item)){
            custom_enchants = new ArrayList<>(List.copyOf(pje.hoe_enchants));
        }
        else if(pje.isSpear(item))
            custom_enchants = new ArrayList<>(List.copyOf(pje.spear_enchants));

        addEnchants:
        for(int i=0;i<level;i++){ // Enchants with numce random enchantments assuming meets all criteria
            if(custom_enchants.isEmpty())
                break;
            random_index = (int)(Math.random()*custom_enchants.size()); // chooses a random index from all possible custom enchants
            Enchant enchant = custom_enchants.get(random_index);
            int tier = enchant.getTier();
            int chanceToAdd = switch (tier) {
                case 2 -> 50;
                case 3 -> 20;
                default -> 80;
            };

            // Choose level # custom enchants from all possible enchants. Depending on the tier of the random enchant, determine the odds of it actually being applied.
            if(pje.percentChance(100-chanceToAdd))
                continue;
            for(Enchantment real:e.getEnchantsToAdd().keySet()) // Only enchants if compatible with all real enchants on list, favors real over custom
                if(!pje.isCompatible(enchant, real))
                    continue addEnchants;

            custom_enchants.remove(random_index);    // no duplicate enchants
            random_level = pje.random(1,enchant.getMaxLevel()+(int)(level/3.0)); // random level from 1 to max, skew to max by adding 1 if level 30 enchant
            if(random_level>enchant.getMaxLevel())
                random_level = enchant.getMaxLevel();
            if(random_level == 0)
                random_level = 1;

            pje.enchant(item, enchant, random_level);
        }
    }

    @EventHandler
    public void onPrepAnvil(PrepareAnvilEvent e){
        Player p = (Player)e.getView().getPlayer();

        ItemStack i2 = e.getView().getItem(1);
        ItemStack i1 = e.getView().getItem(0);

        if(i1 == null || i2 == null || e.getResult() == null)
            return;

        Map<Enchantment, Integer> final_enchants = new HashMap<>();

        if(e.getResult().hasItemMeta()) {
            if (e.getResult().getItemMeta().hasEnchants()){
                final_enchants = e.getResult().getItemMeta().getEnchants();
            }
        }

        int ogcost = e.getView().getRepairCost();

        List<Enchant> cenchants1 = pje.getCustomEnchants(i1);
        List<Enchant> cenchants2 = pje.getCustomEnchants(i2);
        ItemStack result = i1.clone();

//        for (Enchant en : cenchants1) {
//            pje.enchant(result, en, pje.getEnchantLevel(i1, en));
//        }
        for (Enchant en : cenchants2) {
            pje.enchant(result, en, pje.getEnchantLevel(i2, en));
        }

        result.removeEnchantments();
        result.addUnsafeEnchantments(final_enchants);

        int endench = pje.getCustomEnchants(result).size()+final_enchants.size();
        int cost = endench*3; // Calculating cost, depends on how many enchants are added in the end
        if(cost == 0)
            cost = ogcost;
        if(e.getView().getRenameText().length()>0)
            cost++;
        e.getView().setRepairCost(cost);
        e.setResult(result);
    }

    @EventHandler
    public void onArmorHotswap(PlayerInteractEvent e){
        Action a = e.getAction();
        Player p = e.getPlayer();

        if(a.isRightClick()) {
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (!hand.isEmpty())
                if (hand.getType() == Material.ELYTRA || pje.isChestplate(hand)) {
                    if(pje.wings.containsKey(p.getUniqueId())){
                        e.setCancelled(true);
                    }
                }
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getClickedBlock()!=null){
            if(untouchable.contains(e.getClickedBlock()))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block block = e.getBlock();
        Material b = e.getBlock().getType();
        ItemStack tool = p.getInventory().getItemInMainHand();
        if(tool.getType().equals(Material.AIR))
            return;
        boolean silk = false;
        int fortune = 1;
        int exp = e.getExpToDrop();
        if(tool.hasItemMeta())
            if(tool.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH))
                silk = true;
        if(p.getGameMode().equals(GameMode.SURVIVAL)) {

            if(pje.hasEnchantment(tool,Enchant.TALENT)) {
                exp = exp + (int) (Math.random() * (pje.getEnchantLevel(tool, Enchant.TALENT) + 1));
                if(pje.isAxe(tool))
                    if(pje.axe_blocks.contains(b))
                        exp = (int)(Math.random()*3);
                if(pje.isPickaxe(tool))
                    if(pje.pickaxe_blocks.contains(b))
                        exp = (int)(Math.random()*3);
            }

            if (pje.hasEnchantment(tool, Enchant.FORGING)) {
                e.setDropItems(false);
                if (pje.hasEnchantment(tool, Enchant.CLUSTER) && !p.isSneaking()) {
                    List<Block> cluster = new ArrayList<>();
                    List<Material> clusterable = new ArrayList<>();
                    int level = pje.getEnchantLevel(tool, Enchant.CLUSTER);
                    if (pje.isPickaxe(tool))
                        clusterable = List.copyOf(pje.pickaxe_blocks);
                    if (pje.isAxe(tool))
                        clusterable = List.copyOf(pje.axe_blocks);
                    if(pje.pickaxe_forged_blocks.contains(b) || pje.isLog(block))
                        e.setDropItems(false);
                    if (clusterable.contains(b)) {
                        cluster.addAll(pje.getCluster(new ArrayList<>(), block, b, level));
                        for (Block a : cluster) {
                            if (pje.isPickaxe(tool) && pje.pickaxe_forged_blocks.contains(a.getType())) {
                                pje.breakWithForged(p, tool, a);
                                exp+=(int)(Math.random()*5);
                            }
                            else if (pje.isAxe(tool) && pje.axe_blocks.contains(a.getType())) {
                                pje.breakWithForged(p, tool, a);
                                exp+=(int)(Math.random()*4);
                            }
                            else if(pje.isShovel(tool) && pje.shovel_forged_blocks.contains(a.getType())){
                                pje.breakWithForged(p, tool, a);
                                exp+=(int)(Math.random()*3);
                            }
                            else {
                                List<ItemStack> drops = block.getDrops(tool).stream().toList();
                                for(ItemStack drop:drops) {
                                    if (tool.getItemMeta().hasEnchant(Enchantment.FORTUNE)) {
                                        fortune = tool.getItemMeta().getEnchantLevel(Enchantment.FORTUNE) + 1;
                                        drop.setAmount(drop.getAmount() * (int) (Math.random() * fortune + 2));
                                        //drop.setAmount(1);
                                    }
                                    if (!drop.isEmpty()) {
                                        a.getWorld().dropItemNaturally(a.getLocation(), drop);
                                    }
                                }
                            }
                            a.setType(Material.AIR);
                        }
                    }
                    else pje.breakWithForged(p, tool, block);
                }
                else pje.breakWithForged(p, tool, block);
            }
            else if (pje.hasEnchantment(tool, Enchant.CLUSTER)) {
                List<Block> cluster = new ArrayList<>();
                List<Material> clusterable = new ArrayList<>();
                int level = pje.getEnchantLevel(tool, Enchant.CLUSTER);
                if (pje.isAxe(tool))
                    clusterable = List.copyOf(pje.axe_blocks);
                if (pje.isPickaxe(tool))
                    clusterable = List.copyOf(pje.pickaxe_blocks);
                if (clusterable.contains(b)) {
                    cluster.addAll(pje.getCluster(new ArrayList<>(), block, b, level)); // generates recursive cluster
                    for (Block a : cluster) {
                        List<ItemStack> drops = a.getDrops(tool).stream().toList();
                        for (ItemStack drop : drops) {
                            if (!drop.getType().equals(Material.AIR))
                                if (a.getType().equals(Material.MELON) && !silk) {
                                    fortune = tool.getItemMeta().getEnchantLevel(Enchantment.FORTUNE);
                                    drop = new ItemStack(Material.MELON_SLICE, 1 + (int) (Math.random() * fortune + 6));
                                }
                            a.setType(Material.AIR);
                            if (!drop.getType().equals(Material.AIR))
                                a.getWorld().dropItemNaturally(a.getLocation(), drop);
                        }
                    }
                }
            }
            else if(pje.hasEnchantment(tool,Enchant.PULVERIZING)){
                if(block.getState() instanceof InventoryHolder)
                    e.setCancelled(true);
                else e.setDropItems(false);
            }
        }
        e.setExpToDrop(exp);
        if(!e.isDropItems()&&exp>0)
            (p.getWorld().spawn(p.getLocation(), ExperienceOrb.class)).setExperience(exp);
    }

    @EventHandler
    public void onSwing(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action a = e.getAction();
        UUID id = p.getUniqueId();

        if(p.getInventory().getItemInMainHand().getType().equals(Material.AIR))
            return;

        ItemStack item = p.getInventory().getItemInMainHand();

        if(a.equals(Action.LEFT_CLICK_AIR)||a.equals(Action.LEFT_CLICK_BLOCK)|| a == Action.PHYSICAL){
            if(pje.isSword(item)){
                if(pje.hasEnchantment(item,Enchant.GRAVITY)){
                    int level = pje.getEnchantLevel(item,Enchant.GRAVITY);
                    grav.putIfAbsent(id,0L);
                    if(System.currentTimeMillis()-grav.get(id)>=800) {
                        grav.put(id,System.currentTimeMillis());
                        List<Entity> near = p.getNearbyEntities(level+1, level+1, level+1);
                        for (Entity n : near)
                            if (n instanceof Monster || n instanceof Player) {
                                Vector dif = p.getLocation().subtract(n.getLocation()).toVector().normalize();
                                n.setVelocity(n.getVelocity().add(dif.multiply(0.3*level*(1.5/p.getLocation().distance(n.getLocation())))));
                            }
                    }
                }
                if(pje.hasEnchantment(item,Enchant.ANTIGRAVITY)){
                    int level = pje.getEnchantLevel(item,Enchant.ANTIGRAVITY);
                    grav.putIfAbsent(id,0L);
                    if(System.currentTimeMillis()-grav.get(id)>=800) {
                        grav.put(id,System.currentTimeMillis());
                        List<Entity> near = p.getNearbyEntities(level+1, level+1, level+1);
                        for (Entity n : near)
                            if (n instanceof Monster || n instanceof Player) {
                                Vector dif = n.getLocation().subtract(p.getLocation()).toVector().normalize();
                                n.setVelocity(n.getVelocity().add(dif.multiply(0.3*level*(1.5/p.getLocation().distance(n.getLocation())))));
                            }
                    }
                }
                if(pje.hasEnchantment(item, Enchant.BLAZE) && e.getAction() == Action.LEFT_CLICK_AIR){
                    blaze.putIfAbsent(id,0L);
                    int level = pje.getEnchantLevel(item,Enchant.BLAZE);
                    double cooldown = 5000- 1000L*level;
                    if(pje.hasEnchantment(item, Enchant.ARTFUL))
                        cooldown *= .75;
                    if(System.currentTimeMillis()-blaze.get(id)>=cooldown){
                        blaze.put(id,System.currentTimeMillis());
                        SmallFireball fireball = p.getWorld().spawn(p.getEyeLocation().add(p.getLocation().getDirection()), SmallFireball.class);
                        fireball.setIsIncendiary(false);
                        fireball.setVelocity(p.getLocation().getDirection());
                        fireball.setShooter(p);
                        p.getWorld().playSound(p.getLocation(),Sound.ENTITY_BLAZE_SHOOT,1,1);
                    }
                }
                if(pje.hasEnchantment(item, Enchant.BREEZE) && (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)){
                    blaze.putIfAbsent(id,0L);
                    int level = pje.getEnchantLevel(item,Enchant.BREEZE);
                    double cooldown = 5000- 1000L*level;
                    if(pje.hasEnchantment(item, Enchant.ARTFUL))
                        cooldown *= .75;
                    if(System.currentTimeMillis()-blaze.get(id)>=cooldown){
                        blaze.put(id,System.currentTimeMillis());
                        WindCharge fireball = p.getWorld().spawn(p.getEyeLocation().add(p.getLocation().getDirection()), WindCharge.class);
                        fireball.setVelocity(p.getLocation().getDirection());
                        fireball.setShooter(p);
                        p.getWorld().playSound(p.getLocation(),Sound.ENTITY_BREEZE_SHOOT,1,1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e){

        Player p = e.getPlayer();
        Action a = e.getAction();
        UUID id = p.getUniqueId();
        boolean rc = a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK;

        if(p.getInventory().getItemInMainHand().getItemMeta() == null)
            return;

        ItemStack weapon = p.getInventory().getItemInMainHand();

        if(pje.hasEnchantment(weapon,Enchant.SKULLS)&& a == Action.RIGHT_CLICK_AIR){
            skulls.putIfAbsent(id,0L);
            double cooldown = 3000.0;
            if(pje.hasEnchantment(weapon,Enchant.ARTFUL))
                cooldown *= .75;
            if(System.currentTimeMillis()-skulls.get(id)>cooldown) {
                int level = pje.getEnchantLevel(weapon,Enchant.SKULLS) - 1;
                WitherSkull skull = p.launchProjectile(WitherSkull.class,p.getLocation().getDirection());
                Bukkit.getScheduler().scheduleSyncDelayedTask(pje,() -> {
                    if(!skull.isDead())
                        skull.remove();
                },120L);

                skulls.put(id,System.currentTimeMillis());
                if(level==1)
                    skull.setCharged(true);
                p.getWorld().playSound(p.getLocation(),Sound.ENTITY_WITHER_SHOOT,1,1);
                skull.setShooter(p);
            }
        }
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e){
        if(e.getTarget() instanceof Player) {       // Prevents a ghost from targeting its owner
            Player p = (Player)e.getTarget();
            if (ghosts.containsKey(p.getUniqueId()))
                if (ghosts.get(p.getUniqueId()).contains(e.getEntity()))
                    if (p.equals(e.getTarget()) || ghosts.get(p.getUniqueId()).contains(e.getTarget()))
                        e.setCancelled(true);
        }

        if(e.getEntity() instanceof Wolf){
            Wolf wolf = (Wolf)e.getEntity();
            ItemStack wolf_armor = wolf.getEquipment().getItem(EquipmentSlot.BODY);
            boolean aggro = e.getTarget() != null;
            if(!wolf_armor.isEmpty()){
                if(pje.hasEnchantment(wolf_armor,Enchant.WEREWOLF)){
                    Damageable meta = (Damageable)wolf_armor.getItemMeta();
                    if(meta.getDamage() < Material.WOLF_ARMOR.getMaxDurability() - 1) {
                        int werewolf_level = pje.getEnchantLevel(wolf_armor, Enchant.WEREWOLF);
                        if (aggro) {
                            wolf.getAttribute(Attribute.SCALE).setBaseValue(1 + 0.4 * werewolf_level);
                            wolf.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 600, werewolf_level == 3 ? 1 : 0));
                            wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, werewolf_level - 1));
                            wolf.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_WOLF_WHINE, 1, 0.6F);
                            wolf.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_WOLF_ANGRY_GROWL, 1, 0.6F);
                            wolf.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 1, 0.6F);
                            wolf.getWorld().spawnParticle(Particle.LARGE_SMOKE, wolf.getLocation(), 10, 0, 0, 0);
                        } else {
                            wolf.getAttribute(Attribute.SCALE).setBaseValue(1);
                            wolf.removePotionEffect(PotionEffectType.STRENGTH);
                            wolf.removePotionEffect(PotionEffectType.SPEED);
                            wolf.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 1, 0.6F);
                            wolf.getWorld().spawnParticle(Particle.LARGE_SMOKE, wolf.getLocation(), 10, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent e){
        Player p = e.getPlayer();
        if(e.getEntity() instanceof Wolf){
            Wolf wolf = (Wolf)e.getEntity();
            if(wolf.isTamed()){
                if(!p.equals(wolf.getOwner()))
                    e.setCancelled(true);
                if(wolf.isAngry())
                    e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void hitEntity(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player))
            return;
        Player p = (Player)e.getDamager();
        UUID id = p.getUniqueId();
        if(!(e.getEntity() instanceof LivingEntity))
            return;
        LivingEntity ent = (LivingEntity)e.getEntity();

        if(pje.nightrider.contains(p)&& pje.isNight(p.getWorld()))
            e.setDamage(e.getDamage() * 1.25);

        if(needles.containsKey(id)){
            if(System.currentTimeMillis() - needles.get(id) >= 30000){
                needles.remove(id);
            }
            else {
                e.setDamage(e.getDamage() * (1 - (p.getArrowsInBody() > 30.0 ? 30.0 : p.getArrowsInBody()) / 60.0));
            }
        }

        if(getHorse(p)!=null){ // both are on horses
            e.setDamage(e.getDamage() * 1.25); // Deal more damage regardless if on horseback
            if(getHorse(ent)!=null) {
                if (getHorseArmor(p) != null) {
                    ItemStack armor = getHorseArmor(p);
                    if (pje.hasEnchantment(armor, Enchant.JOUST) && pje.percentChance(25)) {
                        ent.getVehicle().removePassenger(ent);
                    }
                }
            }
        }

        ItemStack weapon;
        if(p.getInventory().getItemInMainHand().getType().equals(Material.AIR))
            return;
        weapon = p.getInventory().getItemInMainHand();
        if(!(pje.isSword(weapon)|| pje.isAxe(weapon) || pje.isSpear(weapon)))
            return;

        if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            return;

        if(ent instanceof Monster mon && pje.isArmorable(mon)){
            if(mon.getHealth()-e.getDamage()<=0){
                ghosts.putIfAbsent(p.getUniqueId(),new ArrayList<>());
                if(pje.hasEnchantment(weapon,Enchant.UNHOLY)&&!ghosts.get(p.getUniqueId()).contains(mon) && ghosts.get(p.getUniqueId()).size()<9&& pje.percentChance(25)){
                    p.getWorld().playSound(ent.getLocation(),Sound.ENTITY_GHAST_SCREAM,0.8F,1);
                    pje.particleCube(Particle.SMOKE,mon.getLocation(),5);
                    pje.particleCube(Particle.SMOKE,mon.getLocation().add(0,1,0),5);
                    e.setCancelled(true);
                    mon.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,10000,0,false,false));
                    mon.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,10000,0,false,false));
                    mon.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,10000,0,false,false));
                    mon.setHealth(mon.getHealth()+12 > 20 ? 20 : mon.getHealth()+12);
                    mon.setSilent(true);
                    mon.getEquipment().setHelmet(new ItemStack(Material.SKELETON_SKULL,1));
                    //mon.setTarget(null);
                    ghosts.get(p.getUniqueId()).add(mon);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                        if(!mon.isDead()) {
                            mon.getWorld().playSound(mon.getLocation(),Sound.ENTITY_GHAST_DEATH,0.5F,1);
                            mon.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,10000,5));
                        }
                    },4800L);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                        if(!mon.isDead()) {
                            mon.remove();
                        }
                        ghosts.get(p.getUniqueId()).remove(mon);
                    },4900L);
                }
            }
        }

        if(pje.hasCurse(weapon)){

            Damageable meta = (Damageable) weapon.getItemMeta();
            short max = weapon.getType().getMaxDurability();
            assert meta != null;
            if (meta.getDamage() == max) {
                if (pje.hasEnchantment(weapon,Enchant.RAPTURE)) {
                    ent.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100000, 10));
                    ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
                    ent.setVelocity(new Vector(0, 0, 0));
                    for (int i = 1; i < 25; i++)
                        pje.particleRing(Particle.FIREWORK, ent.getLocation().add(0, i, 0), 1, 5);

                    if (pje.percentChance(90)) {
                        meta.setDamage(weapon.getType().getMaxDurability() - 5);
                        weapon.setItemMeta(meta);
                    }
                }

                if (pje.hasEnchantment(weapon, Enchant.VOID)) {
                    if (!(ent instanceof EnderDragon) && !(ent instanceof Wither)) {
                        try {
                            ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
                            pje.particleCube(Particle.SQUID_INK, ent.getLocation(), 5);
                            pje.particleCube(Particle.SQUID_INK, ent.getLocation().add(0, 1, 0), 5);
                            ent.teleport(new Location(pje.getServer().getWorld("world_the_end"), 0, 0, 0));
                            if (pje.percentChance(90)) {
                                meta.setDamage(weapon.getType().getMaxDurability() - 5);
                                weapon.setItemMeta(meta);
                            }
                        } catch (Exception ex) {
                            pje.getLogger().info("§4§lThere must be a world called 'world_the_end' for the Void curse to work!");
                        }
                    }
                }

                if (pje.hasEnchantment(weapon, Enchant.JUDGEMENT)) {
                    if (!(ent instanceof EnderDragon) && !(ent instanceof Wither)) {
                        try {
                            List<Block> blocks = new ArrayList<>();
                            List<Material> mats = new ArrayList<>();
                            for (int x = -1; x < 2; x++) {
                                for (int y = -3; y < 0; y++) {
                                    for (int z = -1; z < 2; z++) {
                                        Block b = ent.getLocation().add(x, y, z).getBlock();
                                        blocks.add(b);
                                        mats.add(b.getType());
                                        if (!b.getType().equals(Material.CHEST)) {
                                            if (y < -1)
                                                b.setType(Material.LAVA);
                                            if (y == -1)
                                                b.setType(Material.AIR);
                                            untouchable.add(b);
                                        }
                                    }
                                }
                            }
                            ent.setFireTicks(100000);
                            ent.getWorld().createExplosion(ent.getEyeLocation(), 0, true, false);
                            ent.teleport(ent.getLocation().subtract(0, 1, 0));
                            ent.getLocation().getBlock().setType(Material.LAVA);
                            ent.getLocation().subtract(0, 1, 0).getBlock().setType(Material.LAVA);
                            ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 2));
                            ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 90, 2));
                            ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 2));
                            ent.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 90, 2));
                            if (pje.percentChance(90)) {
                                meta.setDamage(weapon.getType().getMaxDurability() - 5);
                                weapon.setItemMeta(meta);
                            }
                            for (int i = 0; i < 9; i++) {
                                int finalI = i;
                                Bukkit.getScheduler().scheduleSyncDelayedTask(pje, () -> {
                                    if (!ent.isDead()) {
                                        pje.particleRing(Particle.FLAME, ent.getLocation().add(0, 1, 0), 1 - 0.1 * finalI, 5);
                                        p.getWorld().playSound(ent.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1, 1);
                                        ent.getWorld().spawnParticle(Particle.LAVA, ent.getLocation(), 20);
                                        ent.setVelocity(new Vector(0, -0.2, 0));
                                    }
                                }, 10 * i);
                            }
                            Bukkit.getScheduler().scheduleSyncDelayedTask(pje, () -> {
                                if (!ent.isDead()) {
                                    ent.getWorld().createExplosion(ent.getLocation(), 0, false, false);
                                    ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_ENDERMAN_DEATH, 1, 1);
                                    ent.teleport(new Location(pje.getServer().getWorld("world_nether"), 666, 130, 666));
                                }
                                for (int i = 0; i < blocks.size(); i++) {
                                    blocks.get(i).setType(mats.get(i));
                                    untouchable.remove(blocks.get(i));
                                }
                            }, 90);
                        } catch (Exception ex) {
                            pje.getLogger().info("§4§lThere must be a world called 'world_nether' for the Void curse to work!");
                        }
                    }
                }
                if (pje.hasEnchantment(weapon, Enchant.THUNDERSTORM)) {
                    p.getWorld().setWeatherDuration(500);
                    p.getWorld().playSound(ent.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);

                    for (int i = 0; i < 10; i++) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(pje, () -> {
                            List<Entity> near = ent.getNearbyEntities(10, 10, 10);
                            for (Entity a : near) {
                                if (a instanceof Player b) {
                                    if (!b.equals(p))
                                        if (b.getInventory().getBoots() != null) {
                                            b.getWorld().strikeLightningEffect(b.getLocation());
                                            if (!pje.hasEnchantment(b.getInventory().getBoots(), Enchant.GROUNDED))
                                                b.damage(6,p);
                                            else {
                                                b.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                                                b.damage(0,p);
                                            }
                                        }
                                } else if (a instanceof Monster mon) {
                                    mon.damage(6,p);
                                    a.getWorld().strikeLightningEffect(a.getLocation());
                                    if(a instanceof Creeper creep){
                                        creep.setPowered(true);
                                    }
                                }
                            }
                        }, 10 * i);
                    }
                    if (pje.percentChance(90)) {
                        meta.setDamage(weapon.getType().getMaxDurability() - 5);
                        weapon.setItemMeta(meta);
                    }
                }
            }
            else e.setCancelled(true);
        }

        if(pje.hasEnchantment(weapon,Enchant.FROSTBITE)){
            if(pje.percentChance(20)) {
                int level = pje.getEnchantLevel(weapon, Enchant.FROSTBITE);
                ent.setFreezeTicks(460 + 60 * level);
                if(ent instanceof Player){
                    Player p2 = (Player)ent;
                    if(pje.getArmorScore(p2,Enchant.MOLTEN)>0){
                        int score = pje.getArmorScore(p2,Enchant.MOLTEN);
                        ent.setFreezeTicks(460+20*level-20*score);
                    }
                }
                pje.particleDisc(Particle.SNOWFLAKE, ent.getLocation().add(-0.5, 1.5, 0.5), 1, 5);
                p.getWorld().playSound(ent.getLocation(),Sound.ENTITY_ZOMBIE_VILLAGER_CURE,0.5F,1);
            }
        }

        if(ghosts.containsKey(id)){
            List<Monster> g = ghosts.get(id);
            for(Monster m:g)
                if(m.getTarget()==null)
                    if(!e.getEntity().equals(m))
                        m.setTarget((LivingEntity)e.getEntity());
        }

        if(pje.hasEnchantment(weapon,Enchant.VENOM)) {
            if (pje.percentChance(20)) {
                int level = pje.getEnchantLevel(weapon, Enchant.VENOM);
                if(ent instanceof Zombie || ent instanceof Skeleton)
                    ent.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40 + 20 * level, 1, false, true));
                ent.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40 + 20 * level, 1, false, true));
                p.getWorld().playSound(ent.getLocation(),Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH,1,1);
                for (int i = 0; i < 8; i++)
                    p.getWorld().spawnParticle(Particle.SPORE_BLOSSOM_AIR, p.getEyeLocation().add(p.getLocation().getDirection().multiply((double) i / 5)), 3, 0, 0, 0);
            }
        }

        if(pje.hasEnchantment(weapon,Enchant.NEEDLES)){
            if(e.getEntity() instanceof Player p2){
                if(!spikes.containsKey(p2.getUniqueId())){
                    needles.put(p2.getUniqueId(),System.currentTimeMillis());
                    p2.setArrowsInBody(p2.getArrowsInBody() + 1);
                    p2.getWorld().playSound(p2.getLocation(),Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH,0.6F,1);
                }
            }
        }

        if(pje.hasEnchantment(weapon,Enchant.FRACTURE)){
            int level = pje.getEnchantLevel(weapon,Enchant.FRACTURE);

            if(pje.percentChance(33)){
                if(ent instanceof Player p2){
                    ItemStack[] armor = p.getInventory().getArmorContents();
                    for(int i=0;i<armor.length;i++)
                        if(armor[i].hasItemMeta()){
                            Damageable item = (Damageable) armor[i].getItemMeta();
                            item.setDamage(item.getDamage()+level+2);
                            armor[i].setItemMeta(item);
                        }
                    p2.getInventory().setArmorContents(armor);
                    p.getWorld().playSound(p2.getLocation(),Sound.ENTITY_ITEM_BREAK,0.3F,1);
                }
            }
            if(ent instanceof Monster){
                Monster mon = (Monster)ent;
                ItemStack[] armor = Objects.requireNonNull(mon.getEquipment()).getArmorContents();
                for(int i=0;i<armor.length;i++)
                    if(pje.percentChance(10+10*level)&&!armor[i].getType().equals(Material.AIR)){
                        armor[i].setType(Material.AIR);
                        mon.getEquipment().setArmorContents(armor);
                        p.getWorld().playSound(mon.getLocation(),Sound.ENTITY_ITEM_BREAK,1,1);
                        break;
                    }
            }
        }

        if(pje.hasEnchantment(weapon,Enchant.THUNDER)){
            int level = pje.getEnchantLevel(weapon,Enchant.THUNDER);
            if(pje.percentChance(5+5*level)){
                p.getWorld().strikeLightningEffect(ent.getLocation());
                List<Entity> near = ent.getNearbyEntities(3,3,3);
                for(Entity a:near){
                    if(a instanceof Player p2){
                        if(!p2.equals(p))
                            if(p2.getInventory().getBoots()!=null) {
                                if (!pje.hasEnchantment(p2.getInventory().getBoots(), Enchant.GROUNDED))
                                    p2.damage(10,p);
                                else {
                                    p2.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                                }
                            }
                    }
                    else if(a instanceof Monster mon)
                        mon.damage(10,p);
                }
            }
        }

        if(pje.hasEnchantment(weapon, Enchant.DARKNESS)){
            if(pje.percentChance(20)) {
                int level = pje.getEnchantLevel(weapon, Enchant.DARKNESS);
                ent.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40 + 20 * level, 0, false, true));
                pje.particleDisc(Particle.SQUID_INK, ent.getEyeLocation(), 1, 30);
                p.getWorld().playSound(ent.getLocation(),Sound.ENTITY_SQUID_DEATH,1,1);

                if(ent instanceof Monster){
                    Monster mon = (Monster)ent;
                    mon.setTarget(null);
                }

            }
        }

        if(pje.hasEnchantment(weapon, Enchant.ANTIGRAVITY)){
            if(pje.percentChance(20)) {
                int level = pje.getEnchantLevel(weapon, Enchant.ANTIGRAVITY);
                ent.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40 + 20 * level, 0, false, true));
                pje.particleRing(Particle.FIREWORK, ent.getLocation().add(0, 0.75, 0), 1, 5);
                p.getWorld().playSound(ent.getLocation(),Sound.BLOCK_BREWING_STAND_BREW,1,1);
            }
        }

        if(pje.hasEnchantment(weapon, Enchant.DEVOUR)){
            if(pje.percentChance(33)) {
                int level = pje.getEnchantLevel(weapon, Enchant.DEVOUR);
                double multiplier = 1;
                ItemStack item = new ItemStack(Material.COOKED_BEEF, 64);
                switch (e.getEntity().getType()) {
                    case SKELETON, WITHER_SKELETON -> multiplier = 0.5;
                    case SPIDER -> {
                        if (pje.percentChance(10))
                            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300, 0));
                    }
                    case SNOW_GOLEM -> {
                        if (pje.percentChance(20))
                            p.setFreezeTicks(120);
                    }
                    case BLAZE, MAGMA_CUBE -> {
                        if (pje.percentChance(10))
                            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 0));
                    }
                    case WITCH -> {
                        if (pje.percentChance(10))
                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
                    }
                    case GUARDIAN, ELDER_GUARDIAN -> {
                        if (pje.percentChance(10))
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 0));
                    }
                }
                p.setFoodLevel((int) (p.getFoodLevel() + (1 + 0.2 * level * e.getDamage() * multiplier)));
                p.setSaturation(1F);
                p.getWorld().spawnParticle(Particle.ITEM, p.getEyeLocation(), 0, 0, 0, 0, item);
                p.getWorld().playSound(ent.getLocation(), Sound.ENTITY_PLAYER_BURP, 1F, 1);
            }
        }

        if(pje.hasEnchantment(weapon, Enchant.WILTING)){
            if(pje.percentChance(20)){
                int level = pje.getEnchantLevel(weapon, Enchant.WILTING);
                ent.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40 + 20 * level, 1, false, true));
                pje.particleRing(Particle.SOUL,ent.getLocation().add(0,2,0),0.5,20);
                p.getWorld().playSound(ent.getLocation(),Sound.ENTITY_WITHER_HURT,0.3F,1);
            }
        }

        if(pje.hasEnchantment(weapon, Enchant.LEECHING)){
            if(pje.percentChance(20)) {
                int level = pje.getEnchantLevel(weapon, Enchant.LEECHING);
                double max = p.getAttribute(Attribute.MAX_HEALTH).getValue();
                double newhealth = p.getHealth()+(0.2+0.1*level)*e.getDamage();
                if(newhealth > max)
                    newhealth = max;
                p.setHealth(newhealth);
                p.getWorld().playSound(ent.getLocation(),Sound.ENTITY_FOX_BITE,1,1);
                for (int i = 0; i < 8; i++)
                    p.getWorld().spawnParticle(Particle.HEART, ent.getLocation().add(0,1,0).subtract(p.getLocation().getDirection().multiply((double) i / 5)), 0);
            }
        }

        if(pje.hasEnchantment(weapon, Enchant.GRAVITY)){
            if(!ent.getType().equals(EntityType.ENDER_DRAGON)&&!ent.getType().equals(EntityType.WITHER)) {
                int level = pje.getEnchantLevel(weapon, Enchant.GRAVITY);
                if (pje.percentChance(10 + 5 * level)) {
                    ent.teleport(ent.getLocation().subtract(0, 1, 0));
                    ent.removePotionEffect(PotionEffectType.LEVITATION);
                    ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 50, 2, false, true));
                    BlockData dat = ent.getLocation().subtract(0, 1, 0).getBlock().getBlockData();
                    p.getWorld().spawnParticle(Particle.BLOCK, ent.getLocation(), 5, 0, 0, 0, dat);
                    p.getWorld().playSound(ent.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 1, 0F);
                    for (int i = 0; i < 10; i++)
                        Bukkit.getScheduler().scheduleSyncDelayedTask(pje, () -> {
                            ent.setVelocity(new Vector(0, -10, 0));
                        }, 5L * i);
                    grav.putIfAbsent(id,0L);
                    if(System.currentTimeMillis()-grav.get(id)>=800) {
                        grav.put(id, System.currentTimeMillis());
                        List<Entity> near = p.getNearbyEntities(level + 1, level + 1, level + 1);
                        for (Entity n : near)
                            if (n instanceof Monster || n instanceof Player) {
                                Vector dif = p.getLocation().subtract(n.getLocation()).toVector().normalize();
                                n.setVelocity(n.getVelocity().add(dif.multiply(0.3 * level * (1.5 / p.getLocation().distance(n.getLocation())))));
                            }
                    }
                }
            }
        }

        if(pje.hasEnchantment(weapon, Enchant.DEFUSE)){
            if(ent instanceof Creeper) {
                Creeper creeper = (Creeper) ent;
                if(creeper.getMaxFuseTicks()-creeper.getFuseTicks()<60){
                    Item fuse = p.getWorld().dropItemNaturally(creeper.getLocation().add(0,2,0),new ItemStack(Material.STRING,1));
                    fuse.setPickupDelay(10000);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje, fuse::remove,40L);
                    p.getWorld().playSound(ent.getLocation(),Sound.ENTITY_SHEEP_SHEAR,1,1);
                    p.getWorld().playSound(ent.getLocation(),Sound.ENTITY_CHICKEN_EGG,1,1);
                }
                creeper.setMaxFuseTicks(200);
                creeper.setFuseTicks(0);
            }
        }

        if(pje.hasEnchantment(weapon, Enchant.HALLUCINATION)){
            if(pje.percentChance(20)) {
                int level = pje.getEnchantLevel(weapon, Enchant.HALLUCINATION);
                ent.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 40 + 20 * level, 0, false, true));
                pje.particleRing(Particle.SOUL_FIRE_FLAME,ent.getLocation().add(0,1.5,0),0.5,5);
                ent.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, ent.getLocation().add(0, 1, 0), 10);
                p.getWorld().playSound(ent.getLocation(),Sound.ENTITY_PLAYER_BREATH,1,1);

                if(ent instanceof Monster){ // Sets monsters target to random other entity
                    List<Entity> near = ent.getNearbyEntities(10,5,10);
                    near.removeIf(a -> !(a instanceof Monster));
                    int rand = (int)(Math.random()*near.size());
                    if(rand<near.size())
                        if(near.get(rand)!=null)
                            ((Monster) ent).setTarget((LivingEntity)near.get(rand));
                }

            }
        }

        if(pje.hasEnchantment(weapon, Enchant.DIZZY)){
            int level = pje.getEnchantLevel(weapon, Enchant.DIZZY);
            if(pje.percentChance(5+5*level)) {
                for(int i=0;i<6;i++) {
                    int finalI = i;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                        Location loc = ent.getLocation();
                        if(ent instanceof Monster)
                            ((Monster) ent).setTarget(null);
                        double x = loc.getX();
                        double y = loc.getY();
                        double z = loc.getZ();
                        loc.setYaw((float) Math.random() * 360);
                        loc.setPitch((float) Math.random() * 360);
                        ent.teleport(loc);
                        loc.getWorld().spawnParticle(Particle.FIREWORK,new Location(loc.getWorld(),x+Math.cos(finalI)*0.8,y,z+Math.sin(finalI)*0.8+(0.15+.1*0.8)),0);
                    },10*i);
                }
                //sp.particleRing(Particle.VILLAGER_ANGRY,ent.getLocation().add(0,1.75,0),1,60);
                p.getWorld().playSound(ent.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            }
        }

        if(pje.hasEnchantment(weapon,Enchant.PHANTOM)){
            if(p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                e.setDamage(e.getDamage() * 1.25);
                pje.particleRing(Particle.WITCH,ent.getLocation(),1,5);
                p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GHAST_WARN,0.5F,0.1F);
            }
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity().getPlayer();
        if(hasStealthLeggings(p)&&p.isSneaking())
            pje.pasteArmor(p);
    }

    @EventHandler
    public void onPotion(EntityPotionEffectEvent e){

        if(e.getEntity() instanceof Monster){
            if(e.getNewEffect() == null)
                return;
            Monster mon = (Monster)e.getEntity();
            if(e.getNewEffect().getType().equals(PotionEffectType.BLINDNESS))
                mon.setTarget(null);
        }
        if(e.getEntity() instanceof Player p) {
            if(e.getNewEffect() == null)
                return;
            try {
                if (e.getNewEffect().getType().equals(PotionEffectType.POISON) || e.getNewEffect().getType().equals(PotionEffectType.WITHER)) {
                    if (p.getInventory().getChestplate() != null) {
                        ItemStack chestplate = p.getInventory().getChestplate();
                        if (pje.hasEnchantment(chestplate, Enchant.ANTIDOTE)) {
                            if (pje.percentChance(30 + 10 * pje.getEnchantLevel(chestplate, Enchant.ANTIDOTE))) {
                                e.setCancelled(true);
                                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 0.5F, 1);
                                pje.particleRing(Particle.HAPPY_VILLAGER, p.getEyeLocation(), 1, 5);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                //
            }
            if(e.getNewEffect().getType().equals(PotionEffectType.LEVITATION)) {
                if(pje.hasEnchantment(p.getEquipment().getBoots(), Enchant.GROUNDED)){
                    e.setCancelled(true);
                    p.getWorld().playSound(p.getLocation(),Sound.BLOCK_ANVIL_STEP,1,1);
                }
            }
        }
    }

    @EventHandler
    public void onTakeDamage(EntityDamageByEntityEvent e){

        if(e.getEntity() instanceof Wolf wolf){
            if(e.getDamager() instanceof LivingEntity enemy){
                ItemStack armor = wolf.getEquipment().getItem(EquipmentSlot.BODY);
                if(!armor.isEmpty()){
                    if(pje.hasEnchantment(armor,Enchant.MOLTEN)){
                        e.getDamager().setFireTicks(40+20* pje.getEnchantLevel(armor,Enchant.MOLTEN));
                    }
                    if(pje.hasEnchantment(armor,Enchant.TOXIC)){
                        if(e.getEntity() instanceof Monster)
                            enemy.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,60,1));
                        else enemy.addPotionEffect(new PotionEffect(PotionEffectType.POISON,60,1));
                    }
                }
            }
            if(e.getDamager() instanceof WitherSkull || e.getDamager() instanceof SmallFireball){
                Projectile proj = (Projectile)e.getDamager();
                if(proj.getShooter() instanceof Player p){
                    if(wolf.getOwner() != null){
                        if(wolf.getOwner().getUniqueId().equals(p.getUniqueId())){
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }

        if(e.getDamager() instanceof Wolf wolf){
            ItemStack armor = wolf.getEquipment().getItem(EquipmentSlot.BODY);
            if(!armor.isEmpty()){
                if(pje.hasEnchantment(armor,Enchant.SNATCH)){
                    if(e.getEntity() instanceof Monster mon) {
                        if (!mon.getEquipment().getItemInMainHand().isEmpty()) {
                            ItemStack weapon = mon.getEquipment().getItemInMainHand().clone();
                            mon.getEquipment().setItemInMainHand(null);
                            mon.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                            mon.getWorld().dropItemNaturally(wolf.getLocation(), weapon);
                        }
                    }
                    if(e.getEntity() instanceof Player p){
                        if(pje.percentChance(15)){
                            int mainslot = p.getInventory().getHeldItemSlot();
                            int swapslot = mainslot + 1;
                            if(mainslot > 1){
                                swapslot = mainslot - 1;
                            }
                            ItemStack primary = p.getInventory().getItem(mainslot);
                            ItemStack secondary = p.getInventory().getItem(swapslot);
                            p.getInventory().setItem(mainslot,secondary);
                            p.getInventory().setItem(swapslot,primary);
                        }
                    }
                }
                if(pje.hasEnchantment(armor,Enchant.FANGS)){
                    e.setDamage(e.getDamage() + 2* pje.getEnchantLevel(armor,Enchant.FANGS));
                    wolf.getWorld().playSound(wolf.getLocation(),Sound.ENTITY_PANDA_BITE,1,1);
                }
                if(pje.hasEnchantment(armor,Enchant.FLING)){
                    if(e.getEntity() instanceof LivingEntity ent) {
                        if (pje.percentChance(30)) {
                            wolf.getWorld().playSound(wolf.getLocation(),Sound.ENTITY_BREEZE_SHOOT,1,1);
                            ent.setVelocity(ent.getVelocity().add(new Vector(0,6,0)));
                        }
                    }
                }
            }
        }

        if(!(e.getEntity() instanceof Player))
            return;

        Player p = (Player)e.getEntity();

        if(!(e.getDamager() instanceof LivingEntity attacker))
            return;
        UUID id = p.getUniqueId();
        EntityDamageEvent.DamageCause cause = e.getCause();

        if(cause.equals(EntityDamageEvent.DamageCause.LIGHTNING))
            if(p.getInventory().getBoots()!=null)
                if(pje.hasEnchantment(p.getInventory().getBoots(),Enchant.GROUNDED))
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,2));

        if(e.isCancelled())
            return;

        if(pje.getNumArmorPieces(p,Enchant.TOXIC) > 0) {
            if (attacker instanceof Monster) {
                attacker.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40 +20 * pje.getNumArmorPieces(p, Enchant.TOXIC), 1)); // Poisons attacker if player has toxic

            }
            else attacker.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40 + 20 * pje.getNumArmorPieces(p, Enchant.TOXIC), 1)); // Poisons attacker if player has toxic

        }

        if(ghosts.containsKey(id))
            if(e.getDamager() instanceof Monster) // If attacked by a monster, all ghosts will attack that monster
                for(Monster mon:ghosts.get(id))
                    mon.setTarget((LivingEntity)e.getDamager());

        if(pje.percentChance(25* pje.getNumArmorPieces(p,Enchant.MOLTEN)))       // burns attacker if player has molten
            attacker.setFireTicks(40+20* pje.getArmorScore(p,Enchant.MOLTEN));

        if(p.getInventory().getBoots()!=null){
            ItemStack boots = p.getInventory().getBoots();
            if(pje.hasEnchantment(boots,Enchant.ESCAPE)&&p.isSneaking()) { // teleports player to nearby safe location if sneaking on hit below 5 hearts
                if (p.getHealth() <= 10) {
                    for(int i=0;i<10;i++) {
                        Location free = p.getLocation().add(Math.random() * 10, 0, Math.random() * 10);
                        if(free.add(0,1,0).getBlock().isPassable()&&free.getBlock().isPassable()){
                            pje.particleRing(Particle.PORTAL, p.getLocation().add(0, 1, 0), 1, 5);
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                            p.teleport(free);
                            break;
                        }
                        if(i==9)
                            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BASS,0.7F,1);
                    }
                }
            }
            if(pje.hasEnchantment(boots,Enchant.GROUNDED)){
                Vector v = p.getVelocity();
                p.setVelocity(v.multiply(0.1));
                p.getWorld().playSound(p.getLocation(),Sound.ITEM_SHIELD_BLOCK,1,1);
                //p.sendMessage("Grounded");
            }
        }

        if(pje.percentChance(pje.getArmorScore(p,Enchant.PLAGUE))){
            Location loc = p.getLocation();
            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_GHAST_WARN,0.3F,0.1F);
            for(int i=0;i<12;i++){
                Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                    Collection<Entity> near = Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc,5,1,5);
                    pje.particleDisc(Particle.SPORE_BLOSSOM_AIR,loc,5,15);
                    for(Entity ent:near){
                        if(ent instanceof LivingEntity lent) {
                            if (ent instanceof Player) {
                                if (!ent.equals(p)) {
                                    lent.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
                                    lent.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA,100,0));
                                }
                            }
                            else if(ent instanceof Monster mon) {
                                mon.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
                            }
                        }
                    }
                },10*i);
            }
        }

        if(p.getInventory().getChestplate()!=null){
            ItemStack chest = p.getInventory().getChestplate();

            if(pje.hasEnchantment(chest,Enchant.UNSTABLE)){
                int level = pje.getEnchantLevel(chest,Enchant.UNSTABLE);

                double defuse = 1; // multiplier for if attacker has defuse on their sword
                if(e.getDamager() instanceof Player){
                    Player p2 = (Player)e.getDamager();
                    if(pje.hasEnchantment(p2.getInventory().getItemInMainHand(),Enchant.DEFUSE))
                        defuse = 0;
                }
                double chance = 2*level*((int)(((21-p.getHealth())/10+1)*defuse));
                if(pje.percentChance(chance)){
                    p.getWorld().spawnParticle(Particle.EXPLOSION,p.getLocation().add(0.5,1,0.5),1);
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1,1);

                    List<Entity> near = p.getNearbyEntities(5,5,5);
                    for(Entity ent:near){
                        if(ent instanceof Player p2) {
                            if (!p2.equals(p))
                                p2.damage(5,p);
                        }
                        else if(ent instanceof Monster mon)
                            mon.damage(5,p);
                    }
                }
            }

            if(pje.hasEnchantment(chest,Enchant.SPIKES)){
                if(pje.percentChance(5)&&!spikes.containsKey(id)){
                    p.setArrowsInBody(300);
                    spikes.put(id,true);
                    needles.remove(id);
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_WITHER_BREAK_BLOCK,1,1);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                        p.setArrowsInBody(0);
                        spikes.remove(id);
                    },160);
                    new BukkitRunnable(){
                        public void run(){
                            List<Entity> near = p.getNearbyEntities(1,1,1);
                            for(Entity ent:near)
                                if(ent instanceof Monster || ent instanceof Player){
                                    LivingEntity t = (LivingEntity)ent;
                                    t.damage(1,p);
                                    t.getWorld().playSound(t.getLocation(),Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH,1,1);
                                }
                            if(!spikes.containsKey(id))
                                cancel();
                        }
                    }.runTaskTimer(pje,0,10);
                }
            }

            if(pje.hasEnchantment(chest,Enchant.DISCHARGE)){
                int level = pje.getEnchantLevel(chest,Enchant.DISCHARGE);
                if(pje.percentChance(5*level)){
                    List<Entity> near = p.getNearbyEntities(3,3,3);
                    for(Entity a:near) {
                        if (a instanceof LivingEntity ent) {
                            double dmg = 0;
                            if (!a.equals(p))
                                dmg = 10;
                            if (a instanceof Player p2) {
                                if (p2.getInventory().getBoots() != null)
                                    if (pje.hasEnchantment(p2.getInventory().getBoots(), Enchant.GROUNDED)) {
                                        dmg = 0;
                                        p2.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,2));
                                    }
                            }
                            ent.damage(dmg,p);
                        }
                    }
                }
            }

            if(pje.hasEnchantment(chest,Enchant.INFESTED)){
                int level = pje.getEnchantLevel(chest,Enchant.INFESTED);
                if(pje.percentChance(5+5*level)){
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_SILVERFISH_HURT,1,1);
                    p.getWorld().spawnParticle(Particle.CLOUD,p.getLocation(),10);
                    Monster mob = p.getWorld().spawn(p.getLocation(), Silverfish.class);
                    mob.setTarget((LivingEntity) e.getDamager());
                    for(int i=0;i<10;i++)
                        Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                            if(e.getDamager().isDead())
                                mob.remove();
                            else mob.setTarget((LivingEntity)e.getDamager());
                        },20*i);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                        pje.particleDisc(Particle.CLOUD,mob.getLocation(),0.5,5);
                        mob.remove();
                    },200);
                }
            }

            if(pje.hasEnchantment(chest,Enchant.HIVE)){
                int level = pje.getEnchantLevel(chest,Enchant.HIVE);
                if(pje.percentChance(3*level)){
                    p.getWorld().playSound(p.getLocation(),Sound.BLOCK_BEEHIVE_EXIT,1,1);
                    Bee bee = p.getWorld().spawn(p.getLocation().add(0,2,0),Bee.class);
                    bee.setAnger(1000);
                    bee.setCannotEnterHiveTicks(1000);
                    bee.setAgeLock(true);
                    bee.setBaby();
                    bee.setFlower(null);
                    bee.setTarget((LivingEntity)e.getDamager());
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pje,()->{
                        bee.getWorld().playSound(bee.getLocation(),Sound.BLOCK_BEEHIVE_ENTER,1,1);
                        bee.getWorld().spawnParticle(Particle.LANDING_HONEY,bee.getLocation(),30);
                        bee.remove();
                    },400);
                    for(int i=0;i<20;i++)
                        Bukkit.getScheduler().scheduleSyncDelayedTask(pje,()->{
                            if(!bee.isDead())
                                bee.setTarget((LivingEntity) e.getDamager());
                        },20*i);
                }
            }
        }

        if(p.getInventory().getHelmet()!=null){
            ItemStack helmet = p.getInventory().getHelmet();

            if(pje.hasEnchantment(helmet,Enchant.PSYCHIC)){
                int level = pje.getEnchantLevel(helmet,Enchant.PSYCHIC);
                if(pje.percentChance(level*5)) {
                    Vector dif = attacker.getEyeLocation().toVector().subtract(p.getEyeLocation().toVector());
                    Location loc = p.getLocation();
                    loc.setDirection(dif);
                    p.teleport(loc);
                    pje.particleRing(Particle.REVERSE_PORTAL,p.getLocation().add(0,2,0),1,2);
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ENDER_EYE_DEATH,1,1);
                }
            }
        }

        if(p.getInventory().getLeggings()!=null){
            ItemStack legs = p.getInventory().getLeggings();
            if(pje.hasEnchantment(legs,Enchant.REPULSION)&& pje.percentChance(33)){
                if(!pje.hasEnchantment(attacker.getEquipment().getBoots(),Enchant.GROUNDED)) {
                    int level = pje.getEnchantLevel(legs, Enchant.REPULSION);
                    Vector v = ((p.getLocation().subtract(attacker.getLocation())).toVector()).normalize();
                    attacker.setVelocity(v.multiply(-level * 0.3));
                    p.getWorld().playSound(p.getLocation(),Sound.BLOCK_PISTON_EXTEND,1f,1.1f);
                }
            }
        }

    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e){
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        Material mat = item.getType();
        if(mat.equals(Material.MILK_BUCKET)){
            if(hasStealthLeggings(p)&&p.isSneaking())
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent e){
        Entity ent = e.getEntity();
        Player p = e.getEntity().getKiller();
        if(p==null)
            return;
        if(!p.getInventory().getItemInMainHand().hasItemMeta())
            return;
        ItemStack weapon = p.getInventory().getItemInMainHand();
        int exp = e.getDroppedExp();

        for(Player p2: pje.online){
            if (ghosts.containsKey(p2.getUniqueId())){  // checks thru ghosts to see if the killed mob belongs to anyone
                if(ghosts.get(p2.getUniqueId()).contains(ent)){ // if the mob IS on the list it is removed and drops nothing
                    ghosts.get(p2.getUniqueId()).remove(ent);
                    e.setDroppedExp(0);
                    e.getDrops().clear();
                }
            }
        }

        if(pje.hasEnchantment(weapon,Enchant.TALENT) && !pje.isSword(weapon)) {
            e.setDroppedExp(exp + (int) (Math.random() * (pje.getEnchantLevel(weapon, Enchant.TALENT)+1)));
            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_CHIME,0.2F,1);
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Player p = (Player)e.getWhoClicked();
        UUID id = p.getUniqueId();
        InventoryType type = e.getView().getType();
        int slot = e.getRawSlot();
        Inventory inv = e.getInventory();
        ItemStack item = e.getCurrentItem();

        if(item!=null&&type.equals(InventoryType.GRINDSTONE)) {
            if (pje.hasCurse(item)|| pje.hasCustomEnchants(item)) {
                e.setCancelled(true);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F, 1);
            }
        }

        // Anvil functionality
        if (item != null && type.equals(InventoryType.ANVIL)) {
            if(pje.hasCurse(item)||(pje.getCustomEnchants(item).size()>6&&!item.getType().equals(Material.ENCHANTED_BOOK))) {
                e.setCancelled(true);
                return;
            }
            if(slot==2){
                if(inv.getItem(2)!=null) {
                    if (!inv.getItem(2).getType().equals(Material.AIR)) {
                        ItemStack prod = inv.getItem(2);

                        if(prod.equals(inv.getItem(0))){
                            e.setCancelled(true);
                            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BASS,1,1);
                            return;
                        }

                        inv.setItem(0, null);
                        inv.setItem(1, null);
                        e.getView().setCursor(prod);
                        inv.setItem(2, null);
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
                    }
                }
            }
        }

        if(inv instanceof HorseInventory){

            //p.sendMessage("Horse Inventory");
            Horse horse = (Horse)e.getInventory().getHolder();

            if(slot==1) {
                if (inv.getItem(1) != null) {
                    ItemStack armor = inv.getItem(1);
                    takeOffHorseArmor(horse,armor);
                    //p.sendMessage("Took off horse armor");
                }
                else if(e.getView().getCursor()!=null){
                    if(pje.isHorseArmor(e.getView().getCursor())){
                        ItemStack armor = e.getView().getCursor();
                        putOnHorseArmor(horse,armor,p);
                        //p.sendMessage("Put on horse armor");
                    }
                }
            }
            else if(inv.getItem(1)==null&&e.getCurrentItem()!=null&&(e.getClick().equals(ClickType.SHIFT_LEFT)||e.getClick().equals(ClickType.SHIFT_RIGHT))){
                ItemStack armor = e.getCurrentItem();
                if(pje.isHorseArmor(armor)) {
                    putOnHorseArmor(horse, armor,p);
                    //p.sendMessage("Put on horse armor");
                }
            }
        }

        if(type.equals(InventoryType.CRAFTING)){
            if(slot==5){
                if(inv.getItem(slot)!=null){
                    if(pje.hasEnchantment(inv.getItem(slot),Enchant.NIGHTEYE))
                        p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                }
            }
            if(slot==6) {
                if (pje.wings.containsKey(id)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    public void cutArmor(Player p){
        UUID id = p.getUniqueId();
        ItemStack helmet = p.getInventory().getHelmet();
        ItemStack chestplate = p.getInventory().getChestplate();
        ItemStack leggings = p.getInventory().getLeggings();
        ItemStack boots = p.getInventory().getBoots();
        List<ItemStack> savearmor = Arrays.asList(helmet,chestplate,leggings,boots);
        pje.armor.put(id,savearmor);
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        for(Entity e:p.getNearbyEntities(3,3,3)){
            if(e instanceof Arrow)
                e.remove();
        }
    }

    public void putOnHorseArmor(Horse horse, ItemStack armor, Player p){
        if(pje.isHorseArmor(armor)){

            if(pje.hasEnchantment(armor,Enchant.HELLISH))
                horse.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,100000,0));

            if(pje.hasEnchantment(armor,Enchant.NIGHTRIDER)&&!pje.nightrider.contains(p)){
                if(horse.getPassengers().contains(p)){
                    pje.nightrider.add(p);
                }
            }
        }
    }

    public void takeOffHorseArmor(Horse horse, ItemStack armor){
        if(pje.isHorseArmor(armor)) {
            if(pje.hasEnchantment(armor,Enchant.HELLISH))
                horse.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            if(pje.hasEnchantment(armor,Enchant.GLIDE))
                horse.removePotionEffect(PotionEffectType.SLOW_FALLING);
        }
    }

    public ItemStack getHorseArmor(LivingEntity p){
        ItemStack i = null;
        if(getHorse(p)!=null) {
            Horse h = getHorse(p);
            if(h.getInventory().getArmor() != null)
                i = h.getInventory().getArmor();
        }
        return i;
    }

    public Horse getHorse(LivingEntity p){
        Horse h = null;
        if(p.getVehicle()!=null)
            if(p.getVehicle() instanceof Horse)
                h = (Horse)p.getVehicle();
        return h;
    }

    public boolean hasStealthLeggings(Player p){
        if(pje.armor.containsKey(p.getUniqueId())){
            List<ItemStack> a = pje.armor.get(p.getUniqueId());
            for(ItemStack i:a){
                if(pje.isLeggings(i)){
                    if(pje.hasEnchantment(i,Enchant.STEALTH))
                        return true;
                }
            }
        }
        if(p.getInventory().getLeggings()!=null){
            if(pje.hasEnchantment(p.getInventory().getLeggings(),Enchant.STEALTH))
                return true;
        }
        return false;
    }

}
