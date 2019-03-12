package com.feed_the_beast.mods.teamislands.command;

import com.feed_the_beast.mods.teamislands.TeamIslandsConfig;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * @author LatvianModder
 */
public class CommandTeamIslands extends CommandTreeBase
{
	public CommandTeamIslands()
	{
		addSubcommand(new CommandIsland());
		addSubcommand(new CommandLobby());

		if (TeamIslandsConfig.general.enable_myisland_command)
		{
			addSubcommand(new CommandMyIsland());
		}
	}

	@Override
	public String getName()
	{
		return "teamislands";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "command.teamislands.usage";
	}
}