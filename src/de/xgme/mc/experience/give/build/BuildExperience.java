package de.xgme.mc.experience.give.build;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import de.xgme.mc.experience.Component;

public class BuildExperience extends Component implements Listener {

	private Map<Material, Integer> breakPoints = new HashMap<>();
	private Map<Material, Integer> placePoints = new HashMap<>();
	private Map<Player, Integer> playerPoints = new HashMap<>();

	public BuildExperience() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, plugin);
		reload();
	}

	public void reload() {
		final ConfigurationSection config = plugin.getConfig()
				.getConfigurationSection("build-experience");

		reloadPointMap(config, "break", breakPoints);
		reloadPointMap(config, "place", placePoints);
	}

	@Override
	public void disable() {
		HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		playerPoints.remove(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		final ConfigurationSection config = plugin.getConfig()
				.getConfigurationSection("build-experience");

		// get the amount of points to add to the player
		Integer pointsToAdd = breakPoints.get(event.getBlock().getType());
		if (pointsToAdd == null)
			pointsToAdd = config.getInt("break.default");

		// handle new points
		final Player player = event.getPlayer();
		final int divisor = config.getInt("divisor");
		int expToAdd = updatePlayerPoints(player, pointsToAdd, divisor);
		player.giveExp(expToAdd);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		final ConfigurationSection config = plugin.getConfig()
				.getConfigurationSection("build-experience");

		// get the amount of points to add to the player
		Integer pointsToAdd = placePoints.get(event.getBlock().getType());
		if (pointsToAdd == null)
			pointsToAdd = config.getInt("place.default");

		// handle new points
		final Player player = event.getPlayer();
		final int divisor = config.getInt("divisor");
		int expToAdd = updatePlayerPoints(player, pointsToAdd, divisor);
		player.giveExp(expToAdd);
	}

	private int updatePlayerPoints(Player player, int pointsToAdd, int divisor) {

		// get current points of the player
		Integer points = playerPoints.get(player);
		if (points == null)
			points = 0;

		// calculate new values
		int newPoints = points + pointsToAdd;
		int overflowCount = newPoints / divisor;
		newPoints = newPoints % divisor;

		// update points of the player
		playerPoints.put(player, newPoints);

		// return experience which should be given to the player
		return overflowCount;
	}

	private void reloadPointMap(ConfigurationSection config, String action,
			Map<Material, Integer> map) {

		map.clear();

		ConfigurationSection sec = config.getConfigurationSection(action);
		Set<String> keys = sec.getKeys(false);
		for (final String key : keys) {
			if (key.equals("default"))
				continue;

			final Material mat = Material.matchMaterial(key);
			if (mat == null) {
				final Logger log = plugin.getLogger();
				log.warning("Unknown material: " + key);
				log.warning("Option ignored: build-experience." + action + "."
						+ key);
			} else {
				int points = sec.getInt(key, -1);
				if (points < 0) {
					final Logger log = plugin.getLogger();
					log.warning("Invalid value: " + sec.getString(key));
					log.warning("Option ignored: build-experience." + action
							+ "." + key);
				} else {
					map.put(mat, points);
				}
			}
		}
	}
}
