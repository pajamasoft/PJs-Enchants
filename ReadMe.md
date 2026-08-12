[helmet]: https://github.com/pajamasoft/pjs-media/blob/main/icons/helmet.png?raw=true
[chestplate]: https://github.com/pajamasoft/pjs-media/blob/main/icons/chestplate.png?raw=true
[leggings]: https://github.com/pajamasoft/pjs-media/blob/main/icons/leggings.png?raw=true
[boots]: https://github.com/pajamasoft/pjs-media/blob/main/icons/boots.png?raw=true
[sword]: https://github.com/pajamasoft/pjs-media/blob/main/icons/sword.png?raw=true
[spear]: https://github.com/pajamasoft/pjs-media/blob/main/icons/spear.png?raw=true
[axe]: https://github.com/pajamasoft/pjs-media/blob/main/icons/axe.png?raw=true
[pickaxe]: https://github.com/pajamasoft/pjs-media/blob/main/icons/pickaxe.png?raw=true
[shovel]: https://github.com/pajamasoft/pjs-media/blob/main/icons/shovel.png?raw=true
[hoe]: https://github.com/pajamasoft/pjs-media/blob/main/icons/hoe.png?raw=true
[bow]: https://github.com/pajamasoft/pjs-media/blob/main/icons/bow.png?raw=true
[horse]: https://github.com/pajamasoft/pjs-media/blob/main/icons/horse.png?raw=true
[wolf]: https://github.com/pajamasoft/pjs-media/blob/main/icons/wolf.png?raw=true
[elytra]: https://github.com/pajamasoft/pjs-media/blob/main/icons/elytra.png?raw=true

![](https://github.com/pajamasoft/pjs-media/blob/main/images/title-small.png?raw=true)
Enchantment Guide
##
## Enchantment Tiers
All custom enchantments are assigned a tier 1-3. This impacts the enchantment's rarity, with Tier 3 being the most rare. 
| Tier Color Key |
| --- |
| $\color{#77ff77}{\text{Tier I}}$ |
| $\color{#ffff77}{\text{Tier II}}$ |
| $\color{#ffaa55}{\text{Tier III}}$ |


*The Armor Score of an enchant is the sum of that enchant's level across the whole suit of armor.
# All Custom Enchantments
| Enchantment | Max Level | Item Type | Description | Calculations |
| --- | --- | --- | --- | --- |
| **NEW** $\color{#77ff77}{\text{Absorb}}$ | 3 | ![][chestplate] | Chance to absorb incoming damage as extra hearts. | 5% chance to activate. Damage absorbed is (level / 3)*(damage) with a maximum of 5 hearts per event. |
| $\color{#77ff77}{\text{Adrenaline}}$ | 3 | ![][leggings] | Gain a speed boost when low on health. | Gain Speed 2 effect for (4 + level) seconds when health falls below 5HP. Is additive to any Speed effect already active. |
| $\color{#77ff77}{\text{Antidote}}$ | 4 | ![][chestplate] | Chance to negate infliction of Poison or Wither. | (30 + 10 * level)% chance upon receiving Poison/Wither to negate it. |
| $\color{#ffff77}{\text{Antigravity}}$ | 3 | ![][sword]![][axe]![][spear]![][bow]![][boots] | (Melee) Chance to give target levitation. (Bow) Arrows are not affected by gravity. (Boots) Double-jump to gain temporary levitation. | (Melee) 20% chance for Levitation I for (2 + level) seconds. (Boots) Double-jumping triggers Levitation (3 * level) for 2 seconds. Sneaking will cancel levitation from any source while boots are on. |
| $\color{#ffff77}{\text{Artful}}$ | 1 | ![][sword]![][axe]![][spear] | Gain permanent Haste II while held. | Also reduces the cooldown of enchantments like Blaze, Breeze, and Skulls. |
| $\color{#ffaa55}{\text{Blaze}}$ | 3 | ![][sword]![][axe] | Swinging launches a small fireball. | Cooldown of (5 - level) seconds. |
| $\color{#ffff77}{\text{Bolt}}$ | 4 | ![][wolf] | Increases wolf speed. | Speed level matches the enchantment's level. |
| $\color{#ffaa55}{\text{Breeze}}$ | 3 | ![][sword] | Swinging launches a breeze ball. | Cooldown of (5 - level) seconds. |
| $\color{#ffff77}{\text{Cluster}}$ | 3 | ![][pickaxe]![][axe] | Instantly breaks clusters of blocks. | Breaks clusters of certain blocks depending on tool type in groups of up to (6 + level). |
| $\color{#77ff77}{\text{Constitution}}$ | 5 | ![][helmet] | Gain resistance when low on health. | Gain Resistance II for (3 + level) seconds when health falls below 7HP. |
| **NEW** $\color{#ffff77}{\text{Criticality}}$ | 2 | ![][spear] | Chance to deal damage ignoring armor. | 5% chance to activate. |
| $\color{#77ff77}{\text{Darkness}}$ | 4 | ![][sword]![][axe]![][spear] | Chance to blind target. | 20% chance for Blindness I for (2 + level) seconds. |
| $\color{#ffff77}{\text{Dash}}$ | 2 | ![][boots] | Gain permanent Speed effect while worn. | Speed level matches the enchantment's level. |
| $\color{#ffff77}{\text{Defuse}}$ | 1 | ![][sword]![][spear] | Prevents creepers from exploding. | |
| $\color{#77ff77}{\text{Devour}}$ | 4 | ![][sword]![][axe]![][spear] | Regain hunger by attacking. | Feeds player by 1 hunger per hit or equivalent raw meat amount for animals. Some mobs may give additional potion effects. |
| $\color{#ffff77}{\text{Discharge}}$ | 3 | ![][chestplate] | Chance to strike yourself with lightning on hit, damaging only nearby enemies. | (5 * level)% chance to activate. Deals 10 raw damage to all nearby enemies. |
| $\color{#77ff77}{\text{Dizzy}}$ | 3 | ![][sword]v![][axe]![][spear] | Chance to randomize target's orientation. | (5 + 5 * level)% chance to activate. Temporarily de-aggros mobs. |
| $\color{#ffaa55}{\text{Draconic}}$ | 1 | ![][elytra]![][chestplate] | Arrows fired while gliding will turn into dragon fireballs. | Requires fire charges as ammunition. Costs 3 fire charges per shot. |
| $\color{#ffff77}{\text{Drag}}$ | 1 | ![][elytra]![][chestplate] | Eliminates fall damage while gliding. | |
| $\color{#ffaa55}{\text{Endereyes}}$ | 1 | ![][helmet] | Allows the wearer to look Endermen in the eyes. Sneaking while making eye contact with an Enderman will cause you to swap places. | |
| **NEW** $\color{#ffff77}{\text{Eruption}}$ | 3 | ![][helmet] | Chance to launch fireballs out in all directions when hit. | (3 * level((21 - health/10) + 1))% chance to trigger (the lower your health, the higher the chance of triggering). |
| $\color{#ffff77}{\text{Escape}}$ | 1 | ![][boots] | Teleport to a safe location if hit while sneaking with low health. | Triggers when sneaking below 10HP. Teleported to a random safe location within 10 blocks. |
| $\color{#77ff77}{\text{Fangs}}$ | 3 | ![][wolf] | Increases wolf attack damage. | Damage is an additional (2HP * level). |
| **NEW** $\color{#ffaa55}{\text{Firewalker}}$ | 2 | ![][boots] | Gain the ability to run on lava. | Speed increases with level. |
| $\color{#ffff77}{\text{Fling}}$ | 1 | ![][wolf] | Chance for wolf to toss enemy into the air. | 30% chance to activate. |
| $\color{#ffaa55}{\text{Forging}}$ | 1 | ![][pickaxe]![][axe]![][shovel]![][hoe] | Automatically smelts block drops. |  |
| $\color{#77ff77}{\text{Fracture}}$ | 4 | ![][axe] | Chance to deal additional damage to armor durability. | 33% chance to deal an additional (2 + level) durability to all target player's armor. (10 + 10 * level)% chance to instantly break a piece of target mob's armor. |
| $\color{#77ff77}{\text{Freezing}}$ | 3 | ![][bow] | Freezes the target. | Gives target freezing status for (2 + level) seconds. |
| $\color{#77ff77}{\text{Frostbite}}$ | 5 | ![][sword]![][axe]![][spear] | Chance to freeze the target. | 20% chance to apply Freezing for (2 + 2 * level) seconds. |
| $\color{#ffaa55}{\text{Glide}}$ | 1 | ![][boots]![][wolf] | Gain permanent Slow Falling while worn. | |
| $\color{#ffaa55}{\text{Grappling}}$ | 1 | ![][bow] | Grapples you towards arrow or pulls hit enemies towards you. | You must be in the air while the arrow hits a block to activate. Max range is 30 blocks. |
| $\color{#ffaa55}{\text{Gravity}}$ | 3 | ![][sword]![][axe]![][bow] | Chance to send the target into the ground. | (10 + 5 * level)% chance to activate. (Melee) Swinging your weapon will also pull nearby enemies in closer. (Bow) Arrow will draw in nearby mobs. |
| $\color{#ffaa55}{\text{Grounded}}$ | 1 | ![][boots] | Receive less knockback and negate damage from lightning, turning it into a speed boost. | Provides immunity to Antigravity. Sneak in water to descend rapidly. |
| $\color{#77ff77}{\text{Hallucination}}$ | 3 | ![][sword]![][axe]![][spear] | Chance to nauseate target. | 20% chance for Nausea I for (2 + level) seconds. |
| $\color{#ffff77}{\text{Healing}}$ | 1 | ![][bow] | Heals hit target instead of damaging. | Healing amount increases with Power enchantment by (2 + Power level)HP and removes all negative effects. |
| $\color{#ffff77}{\text{Hellhound}}$ | 1 | ![][wolf] | Makes wolf immune to fire damage. | |
| $\color{#ffff77}{\text{Hellish}}$ | 1 | ![][horse] | Makes horse immune to fire damage. | |
| $\color{#ffff77}{\text{Hive}}$ | 3 | ![][chestplate] | Chance to summon a bee when hit that will target your attacker. | (3 * level)% chance to activate. |
| $\color{#ffaa55}{\text{Homing}}$ | 1 | ![][bow] | Arrow will arc towards the nearest enemy. | Arrows will not arc towards friendly mobs. |
| $\color{#ffff77}{\text{Infested}}$ | 3 | ![][chestplate] | Chance to summon a silverfish when hit that will target your attacker. | (5 + 5 * level)% chance to activate. |
| $\color{#ffff77}{\text{Joust}}$ | 1 | ![][horse] | Rider will deal additional melee damage and knock other riders off their horse. | Increases damage by 1.25x. |
| $\color{#77ff77}{\text{Leaping}}$ | 3 | ![][leggings] | Gain permanent Jump Boost. | Jump Boost level matches the enchantment's level. |
| $\color{#ffff77}{\text{Leeching}}$ | 5 | ![][sword]![][spear] | Chance to drain target's health. | 20% chance to activate. Heal for (20 + 10 * level)% of the damage dealt. |
| $\color{#ffff77}{\text{Lift}}$ | 2 | ![][elytra]![][chestplate] | Double-jumping from the ground will launch you upwards before flight. | Upwards velocity is (1.2 * level) blocks/second. |
| $\color{#ffff77}{\text{Lunar}}$ | 1 | ![][elytra]![][chestplate] | Sneaking while gliding gives you a small boost at night. | Also works in The End. |
| $\color{#77ff77}{\text{Magnetic}}$ | 1 | ![][helmet]![][chestplate]![][leggings]![][boots] | Sneaking will pull in nearby items and metal. | Has range of (3 + #pieces) blocks. Pulls in items, arrows, and entities wearing iron armor or holding iron items in their inventories. |
| **NEW** $\color{#ffaa55}{\text{Meteor}}$ | 2 | ![][sword] | Shift + right-clicking launches a large fireball. | Level 2 creates a larger explosion. 6 second cooldown. |
| $\color{#77ff77}{\text{Molten}}$ | 3 | ![][helmet]![][chestplate]![][leggings]![][boots]![][wolf] | Chance to set the attacker ablaze. | (25 * #pieces)% chance to trigger, 2 + (armor score) seconds on fire. Decreases freezing time from Frostbite. Full set bonus: freeze immunity. |
| **NEW** $\color{#ffff77}{\text{Needles}}$ | 2 | ![][spear] | Adds needles into the opponent, slowly weakening their damage output. | Adds (level) needles per hit. Damage is reduced as damage*(1-(needles/40)), up to 30 needles. |
| $\color{#ffff77}{\text{Nighteye}}$ | 1 | ![][helmet] | Gain permanent night vision while worn. |  |
| $\color{#ffff77}{\text{Nightrider}}$ | 1 | ![][horse] | Rider will gain permanent night vision and increased melee damage at night. | Increases damage by 1.25x. |
| $\color{#ffff77}{\text{Nitro}}$ | 5 | ![][bow] | Arrows explode shortly after impact. | Arrows will explode (5 - level) seconds after impact. |
| **NEW** $\color{#ffff77}{\text{Permafrost}}$ | 3 | ![][helmet]![][chestplate]![][leggings]![][boots]![][wolf] | Chance to freeze attackers on contact. | (25 * #pieces)% chance to trigger, 2 + (armor score) seconds of freeze damage. Decreases burning time. Full set bonus: fire resistance. |
| $\color{#ffff77}{\text{Phantom}}$ | 1 | ![][sword] | Increases damage when invisible. | 25% damage boost when invisible. |
| $\color{#ffff77}{\text{Plague}}$ | 5 | ![][helmet]![][chestplate]![][leggings]![][boots] | Chance to summon a poison cloud when hit. | Radius is 5 blocks. (armor score)% chance to activate. Inflicts Poison I and Nausea I for 5 seconds. |
| $\color{#ffaa55}{\text{Psychic}}$ | 3 | ![][helmet] | Chance to automatically face your attacker when hit. | (5 * level)% chance to activate. |
| $\color{#ffff77}{\text{Pulverizing}}$ | 1 | ![][pickaxe]![][axe]![][shovel]![][hoe] | Permanent Haste V when held, but blocks broken will yield no drops. | |
| $\color{#ffff77}{\text{Puncture}}$ | 1 | ![][spear] | Chance to temporarily disable all defensive effects from opponents' armor. | 5% chance to activate. |
| $\color{#ffff77}{\text{Rage}}$ | 5 | ![][chestplate] | Gain extra strength when low on health. | Gain Strength II for (4 + level) seconds when health falls below 5HP. |
| $\color{#77ff77}{\text{Repulsion}}$ | 3 | ![][leggings] | Chance to launch back attackers when hit. | 33% chance to activate. Repulsion force increases with level. |
| $\color{#ffaa55}{\text{Ricochet}}$ | 3 | ![][bow] | Arrows will ricochet from enemy to enemy. | Arrows will bounce up to (3 + level) times. |
| **NEW** $\color{#ffff77}{\text{Rock Candy}}$ | 1 | ![][pickaxe] | Regain hunger from mining ores. | Each ore type gives a different additional potion effect. |
| $\color{#ffff77}{\text{Rush}}$ | 3 | ![][horse] | Gives the horse permanent Speed while worn. | Horse gains Speed (level). |
| $\color{#ffff77}{\text{Sealegs}}$ | 1 | ![][leggings] | Gain permanent Dolphin's Grace when in water. | |
| $\color{#ffaa55}{\text{Skulls}}$ | 2 | ![][sword] | Shift + right-clicking launches a wither skull. | 5s cooldown. Wither skull is charged at level 2. |
| $\color{#ffff77}{\text{Snatch}}$ | 1 | ![][wolf] | The wolf will disarm opponents on contact. | Monsters will drop their weapons on contact. On player opponents, there is a 15% chance that the item in their main hand will switch places in their inventory. |
| $\color{#ffff77}{\text{Solar}}$ | 1 | ![][elytra]![][chestplate] | Sneaking while gliding gives you a small boost during the day. | |
| $\color{#ffff77}{\text{Spikes}}$ | 1 | ![][chestplate] | Chance to extrude spikes when hit, damaging enemies that get too close. | 5% chance to extrude spikes for 8 seconds. Colliding with enemies will damage them 1HP every 1/2 second. |
| $\color{#ffff77}{\text{Sponge}}$ | 1 | ![][chestplate] | Gain Resistance I when in water. | |
| $\color{#ffff77}{\text{Stealth}}$ | 3 | ![][leggings] | Gain invisibility while sneaking. | 5s cooldown between toggles. Different effects depending on level. Level 2: Temporarily removes your armor as well while giving you Resistance II while invisible. Level 3: Upgrades to Resistance III with the addition of Speed III. |
| $\color{#ffff77}{\text{Talent}}$ | 5 |  ![][pickaxe]![][axe]![][sword]![][spear] | Increases XP drops. | Random amount of XP dropped from blocks or mobs increases by between 0 and (level). |
| $\color{#ffff77}{\text{Thrust}}$ | 1 | ![][elytra]![][chestplate] | Fireworks will give you a greater boost when gliding. | |
| $\color{#ffaa55}{\text{Thunder}}$ | 4 | ![][sword]![][axe]![][spear] | Chance to strike target with lightning | (5 + 5 * level)% chance to activate. Deals 10 raw damage. |
| $\color{#77ff77}{\text{Toxic}}$ | 1 | ![][helmet]![][chestplate]![][leggings]![][boots]![][wolf] | Poisons attackers. | Gives attackers poison 2 for 2 + (armor score) seconds. |
| $\color{#ffff77}{\text{Unholy}}$ | 1 | ![][sword]![][axe]![][spear] | Chance to turn slain enemies into an ally ghost. | 25% chance to activate. Only works on armorable monsters. Maximum of 8 ghosts at once. Ghosts last 4 minutes before ascending. |
| $\color{#77ff77}{\text{Unstable}}$ | 5 | ![][chestplate] | Chance to set off an explosion when hit, damaging nearby enemies. | (3 * level((21 - health/10) + 1))% chance to trigger (the lower your health, the higher the chance of triggering). |
| $\color{#77ff77}{\text{Venom}}$ | 5 | ![][sword]![][axe]![][spear]![][bow] | Chance to poison the target. | (Sword) 20% chance to give Poison 2 for (1 + level) seconds. (Bow) 30% chance to give Poison 2 for (2 + level) seconds. |
| $\color{#ffaa55}{\text{Waverider}}$ | 2 | ![][horse]![][boots] | Gain the ability to run on water. | Speed increases with level. |
| $\color{#ffaa55}{\text{Werewolf}}$ | 3 | ![][wolf] | Increases the wolf's strength and size at night when aggravated. | Wolf gains (Level < 3: Strength I, Level 3: Strength II and Speed that matches the level. |
| $\color{#ffaa55}{\text{Wilting}}$ | 3 | ![][sword]![][axe]![][spear] | Chance to give target Wither effect. | 15% chance to give Wither II for (2 + level) seconds. |
| $\color{#ffaa55}{\text{Wings}}$ | 1 | ![][chestplate] | Double-jumping temporarily replaces your chestplate with an Elytra. | |
##
## Enchantment Cross-Compatibility Charts

### Melee Enchants
| | Antigravity | Frostbite | Leeching | Wilting |
| --- | --- | --- | --- | --- |
| Gravity | ❌ | | | |
| Venom | | | ❌ | ❌ |
| Fire Aspect | | ❌ | | |
| Blaze | | ❌ | | |

### Bow Enchants
| | Antigravity | Freezing | Healing | Ricochet | Infinity | Gravity | Grappling |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Gravity | ❌ | | ❌ | | | | |
| Grappling | ❌ | | ❌ | ❌ | ❌ | ❌ | - |
| Healing | ❌ | ❌ | - | | ❌ | ❌ | - |
| Homing | | | | ❌ | ❌ | | ❌ |
| Antigravity | | | | | | | ❌ |
| Nitro | | | ❌ | | ❌ | | |
| Venom | | | ❌ | | | | |
| Flame | | ❌ | ❌ | | | | |

### Tools
| | Pulverizing | Forging |
| --- | --- | --- |
| Fortune | ❌ | |
| Silk Touch | ❌ | ❌ |
| Cluster | ❌ | ❌ |
| Talent | ❌ | |
| Forging | ❌ | - |
| Rock Candy | ❌ | |

### Elytra
| | Solar | Lunar |
| --- | --- | --- |
| Lunar | ❌ | - |
| Solar | - | ❌  |

### Armor
| | Plague | Stealth | Waverider | Molten | Permafrost |
| --- | --- | --- | --- | --- | --- |
| Toxic | ❌ | | | | | |
| Magnetic | | ❌ | | | | |
| Frost Walker | | | ❌ | | |
| Fire Walker | | | ❌ | | ❌ |
| Permafrost | | | | ❌ | |
| Eruption | | | | | ❌ |
##
## Vanilla Enchantments With Wider Applications
The following vanilla enchantments can now be applied to new items:
| Enchantment | Item Type | Description | 
| --- | --- | --- |
| Mending | ![][wolf] | Will slowly repair wolf armor over time. |
| Protection | ![][horse] | Reduces all damage taken by (100 / (level + 1))%. |

##
## Enchantment Cross-Mechanics and Recommended Combinations
Some of PJ's Enchantments interact with each other:
* Defuse hits will never trigger Unstable.
* Antigravity cannot lift those with Grounded.
* A chestplate with Wings and other Elytra enchantments will automatically apply those enchantments to your temporary Elytra when in flight.
* Discharge on your chestplate and Grounded on your boots will deal great defensive damage and give you a speed boost at the same time.
* Stealth on your leggings will activate the effects of Phantom on your sword.
* Riding your horse at night with Nightrider and Joust will increase all of your melee damage by 1.25 x 1.25 = 1.56.
* Artful combined with Blaze, Breeze, Meteor, or Skulls reduces their cooldown by 25%.
* Frostbite reduces the chance of Eruption activating
* Devour combined with Fire Aspect increases the hunger restored from hitting animals to match the cooked meat value of that animal
##
## Full-Set Bonuses
Some armor enchants have an additional effect if worn on all 4 armor pieces:

### Magnetic
* Double-jump for magnetic levitation

![Maglev](https://github.com/pajamasoft/pjs-media/blob/main/images/maglev.gif)
### Molten
* Frozen damage immunity
* Permanent fire particles

![MoltenSet](https://github.com/pajamasoft/pjs-media/blob/main/images/molten_set.gif)
### Permafrost
* Fire immunity
* Permanent snow particles

![Permafrost](https://github.com/pajamasoft/pjs-media/blob/main/images/permafrost.gif)
##
## Pet Safety
All enchantments with area effects or unpredictable targeting filters out friendly animals, villagers, and pets.
* Ricochet will not bounce to friendlies
* Homing will not lock on to friendlies
* Spikes will not prick friendlies
##
## Other Features
* Enchantments that poison undead enemies will give them the wither effect instead as to not heal them.
##
## Forging Block Conversions
Below are all of the blocks whose drops are impacted by the Forging enchant, depending on the tool used.

*Works with Fortune
### Pickaxe
| Original | Forged |
| --- | --- |
| Cobblestone | Stone |
| Stone | Smooth Stone |
| Sandstone | Smooth Sandstone |
| Basalt | Smooth Basalt |
| Copper Ore | Copper Ingot* |
| Raw Copper Block | Copper Block |
| Iron Ore | Iron Ingot* |
| Raw Iron Block | Iron Block |
| Gold Ore | Gold Ingot* |
| Raw Gold Block | Gold Block |
| Nether Gold Ore | Gold Ingot |
| Netherrack | Nether Brick* |
| Cracked Stone Bricks | Stone Bricks |
| Ancient Debris | Netherite Scrap* |
| Wet Sponge | Sponge |

### Axe
| Original | Forged |
| --- | --- |
| All Logs | Charcoal* |

### Shovel
| Original | Forged |
| --- | --- |
| Sand | Glass |
| Clay | Bricks |

### Hoe
| Original | Forged |
| --- | --- |
| Hay Block | Bread* |
| Potatoes | Baked Potatoes* |
| Wheat | Bread |
##
## Tool-Specific Blocks
Certain enchants only work on specific groups of blocks depending on tool type. These groups are referred to in the enchantments descriptions as "pickaxe blocks" or "axe blocks", etc.
| Pickaxe Blocks |
| --- |
| All ores |

| Axe Blocks |
| --- |
| All Default Logs |
| Melons |
| Pumpkins |
##
## Devour Potion Effects
| Monster | Effect |
| --- | --- |
| Spider | Night Vision |
| Blaze, Magma Cube | Fire Resistance |
| Witch | Regeneration |
| Guardian, Elder Guardian | Water Breathing |
##
## Rock Candy Potion Effects
| Ore | Effect |
| --- | --- |
| Coal | Fire Resistance |
| Copper | Strength |
| Iron | Resistance |
| Gold | Haste |
| Lapis | Water Breathing |
| Redstone | Speed |
| Diamond | Night Vision |
| Emerald | Jump Boost |
| Quartz | Slow Falling |
| Glowstone | Glowing |
##
## 
## Commands
| Command | Description |
| --- | --- |
| /pje enchant {enchantment} {level} | Enchantt the held item |
| /pje remove {enchantment} | Removes specified enchant from held item |
##
## Plugin Compatibility
PJ's Enchants works with all of my other plugins.
### PJ's Mechanics
* Right-clicking on fully-grown potato crops will automatically harvest baked potatoes.
* When Darkness activates on a mob, they will be unable to see you.
##
## Showcase
### Anvil Compatibility
![Anvil](https://github.com/pajamasoft/pjs-media/blob/main/images/anvil-test.gif)
##
### Breeze
![Breeze](https://github.com/pajamasoft/pjs-media/blob/main/images/breeze-test.gif)
##
### Cluster
![Cluster](https://github.com/pajamasoft/pjs-media/blob/main/images/cluster-test.gif)
##
### Defuse
![Defuse](https://github.com/pajamasoft/pjs-media/blob/main/images/defuse-test.gif)
##
### Endereyes
![Endereyes](https://github.com/pajamasoft/pjs-media/blob/main/images/endereyes-test.gif)
##
### Forging
![Forging](https://github.com/pajamasoft/pjs-media/blob/main/images/forging-test.gif)
##
### Glide
![Glide](https://github.com/pajamasoft/pjs-media/blob/main/images/glide-test.gif)
##
### Grappling
![Grappling](https://github.com/pajamasoft/pjs-media/blob/main/images/grappling-test.gif)
##
### Hive
![Hive](https://github.com/pajamasoft/pjs-media/blob/main/images/hive-test.gif)
##
### Homing, Antigravity
![Homing](https://github.com/pajamasoft/pjs-media/blob/main/images/homing-antigravity-test.gif)
##
### Plague
![Plague](https://github.com/pajamasoft/pjs-media/blob/main/images/plague-test.gif)
##
### Psychic
![Psychic](https://github.com/pajamasoft/pjs-media/blob/main/images/psychic-test.gif)
##
### Ricochet
![Ricochet](https://github.com/pajamasoft/pjs-media/blob/main/images/ricochet-test.gif)
##
### Spikes
![Spikes](https://github.com/pajamasoft/pjs-media/blob/main/images/spikes-test.gif)
##
### Stealth, Phantom
![Stealth](https://github.com/pajamasoft/pjs-media/blob/main/images/stealth-test.gif)
##
### Unstable
![Unstable](https://github.com/pajamasoft/pjs-media/blob/main/images/unstable-test.gif)
##
### Waverider
![Waverider](https://github.com/pajamasoft/pjs-media/blob/main/images/waverider-test.gif)
##
### Werewolf
![Werewolf](https://github.com/pajamasoft/pjs-media/blob/main/images/werewolf-test.gif)
##
### Wings, Solar
![Wings](https://github.com/pajamasoft/pjs-media/blob/main/images/wings-test.gif)
##
### Wings, Solar, Glide, Antigravity
![Wings, Glide, Antigravity](https://github.com/pajamasoft/pjs-media/blob/main/images/wings-glide-antigrav-test.gif)

##
### Absorb
![Absorb](https://github.com/pajamasoft/pjs-media/blob/main/images/absorb.gif)
##
### Eruption
![Eruption](https://github.com/pajamasoft/pjs-media/blob/main/images/eruption.gif)
##
### Firewalker
![Firewalker](https://github.com/pajamasoft/pjs-media/blob/main/images/firewalker.gif)
##
### Meteor
![Meteor](https://github.com/pajamasoft/pjs-media/blob/main/images/meteor.gif)
##
### Rock Candy
![RockCandy](https://github.com/pajamasoft/pjs-media/blob/main/images/rock_candy.gif)


##
### Special Thanks to Community Bug Testers
* erftik
