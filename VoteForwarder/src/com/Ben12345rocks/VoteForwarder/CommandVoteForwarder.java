package com.Ben12345rocks.VoteForwarder;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.vexsoftware.votifier.model.Vote;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandVote.
 */
public class CommandVoteForwarder implements CommandExecutor {

	/** The instance. */
	private static CommandVoteForwarder instance = new CommandVoteForwarder();

	/** The plugin. */
	private static Main plugin;

	/**
	 * Gets the single instance of CommandVote.
	 *
	 * @return single instance of CommandVote
	 */
	public static CommandVoteForwarder getInstance() {
		return instance;
	}

	/**
	 * Instantiates a new command vote.
	 */
	private CommandVoteForwarder() {
	}

	/**
	 * Instantiates a new command vote.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public CommandVoteForwarder(Main plugin) {
		CommandVoteForwarder.plugin = plugin;
	}

	

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender
	 * , org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (!sender.hasPermission("VoteForwarder.Admin")) {
			sender.sendMessage("Not enough permissions!");
			return true;
		}
		
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("reload")) {
				plugin.reload();
				sender.sendMessage("Plugin reloaded");
				return true;
			}
		}
		
		if (args.length >= 3) {
			if (args[0].equalsIgnoreCase("SendVote")) {
				String player = args[1];
				String service = args[2];
				Vote vote = new Vote();
				vote.setUsername(player);
				vote.setServiceName(service);
				VoteForward.getInstance().sendVote(vote);
				return true;
			}
		}

		// invalid command
		sender.sendMessage(ChatColor.RED
				+ "No valid arguments, see plugin page for command list!");
		return true;
	}

	

}
