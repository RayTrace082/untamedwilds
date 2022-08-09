# Untamed Wilds
___

Untamed Wilds is an open-source mod that aims to expand the exploration component of Minecraft through the introduction of high-quality mobs and world-gen

This mod requires [Citadel](https://www.curseforge.com/minecraft/mc-mods/citadel) and **will not** run without it

## General Features:

- All mobs/features are created as part of Worldgen. They also will not respawn (outside of breeding/growing)
* Mobs featured in this list encompass a multitude of Species; Species are dynamically assigned to each Biome depending on its features from a list of weighted entries, providing out of the box compatibility.
- Smaller mobs can be picked up (depending on the mob) to ease their transportation, larger mobs need to be transported by using Cage Traps. 
* While the default parameters aim to be "believable", a plethora of Config options allow the user to replicate Vanilla behaviours (such as breeding being triggered through the favourite item)
- A very WIP guidebook is provided through [Patchouli](https://www.curseforge.com/minecraft/mc-mods/patchouli) (book + any piece of hide). Content in the book has been generated from data sourced from Wikipedia, processed through the NLTK Python library and manually edited.
* Various plants and blocks spawn in the world, most can be punched, but some will require Shears. Bonemeal can be used to replicate most of them

## List of Mobs:

###🐖 Aardvark

Aardvarks are nocturnal animals that can be found in Savannas, where they will roam around, digging for food.

Occasionally, an Aardvark may dig up an Aardvark cucumber, it's favourite food.

Aardvarks provide an alternative source of Rabbit Hide in a biome where it can not be reliably found.

###🐻 Bear

There are currently 8 different species of Bear, ranging from the humble Black Bear to the elusive Cave Bear. Bears can be found in Jungles and most Cold and Temperate biomes, expect to find Polar Bears in snowy biomes, Brown Bears in mountains and Black Bears in Forests

Bears are dangerous mobs, as players disturbing sleeping Bears, or trying to approach Bear cubs in the presence of an adult Bear will result in a mauling, furthermore, some of the bigger Bears may actively hunt Players for food too. Additionally, some hungry Bears may raid your chests (or any inventory) in search of food, so protect your supplies carefully.

On death, Bears drop meat, fat, bones and their fur

###🐅 Big Cat

7 species of Big Cat can be found in the wild (with a few more NYI). Big Cats can be found in most Land Biomes.

Many species of Big Cat will actively hunt players if hungry enough, albeit their elusive nature should make these events rare. Furthermore, approaching Big Cat cubs will enrage their mother.

On death, Big Cats drop some bones and their fur (which can be used as Leather and/or turned into Carpets)

###🦛 Hippo

The Common Hippo is currently the most dangerous animal implemented in the mod. Hippos can be found in Swamps and Savannas in herds ranging from 2 to 6 individuals.

Hippos are highly territorial and will actively attack Players (and any threatening creature) that step into water, however, do note that Hippos outside of water are much more peaceful, only attacking in retaliation.

On death, Hippos drop a sizeable amount of high-quality meat, bones and animal fat

###🐟 Ocean Sunfish

2 Species of Sunfish can be found in the deeper Oceans, usually basking in the surface.

The Ocean Sunfish is a slow, passive mob, albeit it's sheer size makes it a threat to Boats if collided with. On death, Ocean Sunfish drop 1-6 Tropical Fish, providing a source of this fish outside of Tropical Biomes

###🐟 Trevally

5 Species of Trevally can be found in non-cold, non-frozen Oceans.

Trevallies are schooling fish, and can be bucketed up for transportation.

While they mostly provide ambience, they drop some Fish on death.

###🐟 Arowana

6 Species of Arowana can be found in jungles (and as a temporary measure, in Swamps), including 2 rare variants.

Arowana are fancy predatory fish with a tendency to leap out of water, and can be bucketed up for transportation.

Arowana provide ambience and are fit for aquariums, but also drop some Tropical Fish on death.

###🦈 Shark

10 Species of Shark can be found in all oceans, while most Sharks can be found in the surface, 3 variants won't leave the bottom of the Ocean.

Sharks are apex predators in the Ocean, and while not openly hostile to Players, they will hunt anything that has been wounded.

Sharks currently drop nothing.

###🕷️ Tarantula

8 Species of Tarantula will populate the warm biomes of Minecraft, and as Critters, can be picked up by right-clicking with an empty bottle.

Tarantulas are highly agressive against other Critters, and will even attack Tarantulas of different species (however, multiple Tarantulas can coexist if they are the same species). Tarantulas may drop a piece of String on death

###🐚 Giant Clam

4 Species of Giant Clam may be found in the seafloor of Lukewarm/Warm shallow Oceans, Giant Clams can be dug up and transported with a Shovel, albeit it may take multiple tries to do so.

Giant Clams make for good decoration for Tropical fish tanks, and have no predators. On death, Giant Clams may drop Pearls and/or Giant Pearls, these gemstones can be crafted into Blocks, sold to Fishermen villagers or they can be socketed into [Tetra](https://www.curseforge.com/minecraft/mc-mods/tetra) tools, providing minor protection

###🐍 Snake (small)

17 Species of Snakes inhabit most non-cold Biomes of Minecraft, it should be noted that this mob only represents smaller snakes. As Critters, they can be picked up by right-clicking with an empty hand.

Some species of Snake are venomous, with certain species being potentially deadly, and they will attack any mob that steps on them, so watch your steps.

###🐢 Softshell Turtle

7 Species of Softshell Turtle can be found in tropical Biomes, Rivers and Swamps, and as Critters, can be picked up by right-clicking with an empty hand.

Softshell Turtles are elusive animals, and surprisingly fast for a Turtle when in Water. They will attack fish if hungry enough, and will bask in sunlight during noon. Softshell Turtles drop 0-2 Turtle Meat pieces, which can be further crafted into Turtle Soup

## FAQ:

>Mod crashes on launch!

Install [Citadel](https://www.curseforge.com/minecraft/mc-mods/citadel)

Please, submit any other bug report in the associated GitHub repository

>Will this mod be released to Fabric and/or [version]?

No. Other modders are welcome to do so though

>Help, I can't find any mobs

UntamedWilds mobs will only spawn during World Generation, so you will need to either explore new chunks to find mobs, or start a new world. I am looking into implementing RetroGen in some way, however

>I don't like X, can it be changed/disabled?

A lot of config options are provided to tweak the mod to fit your needs. Mobs can be individually disabled, and options are provided to make mob breeding behave like vanilla. Disabling mobs will also disable their associated items

>Does this mod work with [Biome mod] and/or [WorldGen mod]?

It should! Mobs are dynamically assigned to each Biome based on the Biome ~~Temperature and Type~~ Category. 

## Credits

Phweeeee - Code Contributor 
Unkownn66 - Tetra support Contributor

Daniel Simion - Hyena Laughing sound asset (CC BY 3.0)