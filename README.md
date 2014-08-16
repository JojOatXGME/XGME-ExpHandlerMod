XGME-ExpHandlerMod
==================

Some advanced settings for the way to get experience in Minecraft.

### Features ###

* Give experience for building something.
* Give players a specified bonus on experience.

Configuration
-------------

When you start the plugin first time, it will generate a configuration file with
default configuration.

### Build Experience ###

The configuration section `build-experience` is used configure how much
experience players get for placeing or breaking blocks.

* **`enabled`** If the component should be enabled or not. Set it to true if you
  want to use this feature.
* **`divisor`** The *divisor* which is used before the experience is given to
  the player. If you set the amount of points which is given to a player by
  placing a block to 2 and the *divisor* is 20 (default), the player have to
  place 10 blocks to get one experience point.
* **`place`**
    * **`default`** How much experience a player get for placing a block by
      default.
    * **`<material>`** How much experience a player get for placing a block of
      the specified material.
* **`break`**
    * **`default`** How much experience a player get for breaking a block by
      default.
    * **`<material>`** How much experience a player get for breaking a block of
      the specified material.

### Bonus ###

The configuration section `bonus` is used to give players with specific
permissions additional experience points.

* **`enabled`** If the component should be enabled or not. Set it to true if you
  want to use this feature.
* **`available`** A list of available bonuses. You can give somebody a specified
  bonus by giving the permission `experience.bonus.<bonus>` to it. If you want
  to give somebody a bonus of +50%, add `50` to the list and add the permission
  `experience.bonus.50`. A player will always get the highest available bonus.
