## UntamedWilds 2.2.0

### General Changes:
Players can now mount wild Boars and Buffalo with an empty hand, there is no point in doing this besides angering the wildlife, but there's advancements!
Added alternative advancement trigger for the root advancement, in case Patchouli is not installed
Allow UntamedWilds Spawn Eggs to work with Spawners (mobs from a Spawner will always be Male, and have no size variation, due to Minecraft limitations)
Added mob spotting functionality to the vanilla Spyglass, giving players a way to identify, and gauge the threat a mob can pose, from a safe distance (distance is configurable)
New Encyclopedia of Life version, still WIP, but all entries now have an extra page with some stats (when is the mob active, relative size, favourite food...)
Removed information that is no longer true from the Encyclopedia of Life
Poisonous Hemlock now only spawns in Forests
Added a couple trades involving Animal Fat and Blubber for the Butcher profession
Removed breaking time from some UntamedWilds flora

### Additions:
Added Whales, with 6 variants that can be rarely found in deep Oceans, they are an ambient mob, but will drop large amounts of food and fuel (you monster)
Added Junegrass, a new plant found in Plains

### Bug fixes:
Fixed Bison sleeping and sitting simultaneously, leading to a broken animation
Fixed Tetra support breaking sockets (credit to Unkownn66)
Fixed mobs not properly dropping Eggs
Fixed rare crash with missing ambient sounds
Fixed Leash duplication when turning a mob into NBT data

---

## UntamedWilds 2.1.3

### General Changes:
Snake hitboxes are slightly larger, and can be killed in one hit by Shovels
Contact aggression (Snakes and Tarantulas) triggers more consistently, but aggression lasts a very short time
Baby protection aggression will stop when you get far away from the baby
Cleaned up the Hunting AI
Removed Config options to disable mobs (they were not functional). The intended way to disable mobs is through datapacks
Added two dimension blacklist config options for Mobs and Features

### Bug fixes:
Fixed empty hands being a valid breeding Item for multiple species
(Potentially) Fixed rare crash during worldgen from Burrow generation
Fixed Cage Traps being weird if they catch an entity riding another entity (eg. Shulkers in Boats) 
Fixed Swimming AI being unable to leave water
Fixed crash when referencing undefined Sounds
Fixed crash when right-clicking mobs with certain Items
Fixed default name of some mobs being undefined (Credit to Phweeeee)

---

## UntamedWilds 2.1.2

### General Changes:
Exposed as a config parameter for second chance spawnings at a higher Y level, and lowered the default value. Fewer mobs should be spawning on trees

### Bug fixes:
Fixed Giant Clams not spawning
Fixed Manatee population explosions
Fixed Manatee glowing when eating Seagrass
Fixed most mobs not recognizing their favourite food

---
## UntamedWilds 2.1.1

### Bug fixes:
Fixed crash when using UntamedWilds on a dedicated server

---

## UntamedWilds 2.1.0

### General Changes:
Updated the mod to 1.18.2 (NOT compatible with 1.18.1, albeit upgrading worlds should be possible)
Features a WIP, and not-yet-documented data-driven system to define which mobs can spawn in each location

### Additions:
Added Manatee, with 5 variants that can be found in multiple Ocean and Jungle biomes, they are mostly an ambient mob, but will occasionally eat seagrass
Added Catfish, with 3 variants that can be found in River and Jungle biomes, again, ambience mob to make Rivers more lively, but they will eat any edible item that falls in the water
Added Yarrow, a thick, white flower that can be found in Forests

### Mobs:
Big Cat: Added a new orange skin for Lions
Bison: Male Bison will now randomly attack each other to assert dominance (they should never kill each other, however)
Sharks: Added turning animation for Sharks
Hyena: Added Laughing sounds to Spotted Hyenas
---

## UntamedWilds 2.0.0
This version has been marked as an alpha, despite being mostly stable, because it will receive neither support nor updates, since I am focusing on updating (and supporting) 1.18.2

### General Changes:
Updated the mod to 1.18.1 (NOT compatible with 1.18.2)
Removed a handful of config parameters relating to extinct mobs. Functionality can be replicated with a datapack
Many small improvements and bug fixes

### Additions:
Added Camels, with 3 variants that can be found in Deserts or any 'Wasteland' biome

### Blocks:
Cage Traps: Rearranged some events to give Cage Traps breaking animations
Burrows: Changed burrow logic to work as a randomly ticking block, rather than a ticking BlockEntity
Burrows: Allow burrows to generate underwater, given that the mob inside can survive underwater

### Mobs:
Amphibious mobs: Updated amphibious mob logic to work based on the vanilla amphibious implementation
Giant Salamander: Gave giant salamanders new textures, and improved animations
Newt: Gave newts new textures, and improved animations
Football Fish: Added some glow particles to the lure
Giant Clams: Will occasionally spawn bubble particles
Tarantula: Given Tarantula the wall climbing ability, and added a very WIP wall attaching animation
Big Cat: Added additional tail segment, and new 'fluffy tail' flag, currently used by Snow Leopards