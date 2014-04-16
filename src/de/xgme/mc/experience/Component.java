package de.xgme.mc.experience;

public abstract class Component {
	protected final ExpHandlerMod plugin;

	public Component() {
		this.plugin = ExpHandlerMod.plugin;
	}

	/**
	 * Disables the component. After this function is called, the component
	 * should stop working.
	 */
	public abstract void disable();
}
