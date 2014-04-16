package de.xgme.mc.experience.bonus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.xgme.mc.experience.Component;

public class Bonus extends Component implements Listener {

	private final Map<Player, Integer> unpaidHundredth = new HashMap<>();
	private final Set<Integer> bonuses;

	public Bonus() {
		this.bonuses = new TreeSet<>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return Integer.compare(o2, o1);
			}
		});
		reload();
	}

	@Override
	public void disable() {
		HandlerList.unregisterAll(this);
	}

	public void reload() {
		final ConfigurationSection config = plugin.getConfig()
				.getConfigurationSection("bonus");

		bonuses.clear();

		for (int bonus : config.getIntegerList("available")) {
			bonuses.add(bonus);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		unpaidHundredth.remove(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerExpChange(PlayerExpChangeEvent event) {
		if (event.getAmount() <= 0)
			return;

		final Player player = event.getPlayer();

		for (int bonus : bonuses) {
			if (player.hasPermission("experience.bonus." + bonus)) {
				Integer unpaid = this.unpaidHundredth.get(player);
				unpaid += bonus * event.getAmount();

				event.setAmount(event.getAmount() + unpaid / 100);
				this.unpaidHundredth.put(player, unpaid % 100);
				return;
			}
		}
	}
}
