package com.Ben12345rocks.VoteForwarder;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigBungeeVoting.
 */
public class Config {

	/** The instance. */
	static Config instance = new Config();

	/** The plugin. */
	static Main plugin = Main.plugin;

	/**
	 * Gets the single instance of ConfigBungeeVoting.
	 *
	 * @return single instance of ConfigBungeeVoting
	 */
	public static Config getInstance() {
		return instance;
	}

	/** The data. */
	FileConfiguration data;

	/** The d file. */
	File dFile;

	/**
	 * Instantiates a new config bungee voting.
	 */
	private Config() {
	}

	/**
	 * Instantiates a new config bungee voting.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public Config(Main plugin) {
		Config.plugin = plugin;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public FileConfiguration getData() {
		return data;
	}

	/**
	 * Gets the server IP.
	 *
	 * @param server
	 *            the server
	 * @return the server IP
	 */
	public String getServerIP(String server) {
		return getData().getString("Servers." + server + ".IP");
	}

	/**
	 * Gets the server key.
	 *
	 * @param server
	 *            the server
	 * @return the server key
	 */
	public String getServerKey(String server) {
		return getData().getString("Servers." + server + ".Key");
	}

	/**
	 * Gets the server port.
	 *
	 * @param server
	 *            the server
	 * @return the server port
	 */
	public int getServerPort(String server) {
		return getData().getInt("Servers." + server + ".Port");
	}

	/**
	 * Gets the servers.
	 *
	 * @return the servers
	 */
	public Set<String> getServers() {
		return getData().getConfigurationSection("Servers").getKeys(false);
	}

	/**
	 * Gets the server service site.
	 *
	 * @param server
	 *            the server
	 * @return the server service site
	 */
	public String getServerServiceSite(String server) {
		return getData().getString("Servers." + server + ".ServiceSite");
	}

	/**
	 * Reload data.
	 */
	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dFile);
	}

	/**
	 * Save data.
	 */
	public void saveData() {
		try {
			data.save(dFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the up.
	 *
	 * @param p
	 *            the new up
	 */
	public void setup(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		dFile = new File(p.getDataFolder(), "Config.yml");

		if (!dFile.exists()) {
			try {
				dFile.createNewFile();
				plugin.saveResource("Config.yml", true);
			} catch (IOException e) {
				Bukkit.getServer().getLogger()
						.severe(ChatColor.RED + "Could not create Config.yml!");
			}
		}

		data = YamlConfiguration.loadConfiguration(dFile);
	}

}
