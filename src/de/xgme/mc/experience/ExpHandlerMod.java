package de.xgme.mc.experience;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import de.xgme.mc.experience.bonus.Bonus;
import de.xgme.mc.experience.give.build.BuildExperience;

public class ExpHandlerMod extends JavaPlugin {

	/**
	 * Instance of the ExpHandlerMod plugin.
	 */
	static ExpHandlerMod plugin = null;

	/**
	 * A list of loaded components.
	 */
	private List<Component> components = new ArrayList<>();

	@Override
	public void onLoad() {
		plugin = this;
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();

		// start enabled components
		final Configuration config = getConfig();
		if (config.getBoolean("build-experience.enabled")) {
			components.add(new BuildExperience());
		}
		if (config.getBoolean("bonus.enabled")) {
			components.add(new Bonus());
		}
	}

	@Override
	public void onDisable() {
		for (Component compnent : components) {
			compnent.disable();
		}
	}
}
