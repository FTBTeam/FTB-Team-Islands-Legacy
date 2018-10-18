package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CmdTreeBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdTeamIslands extends CmdTreeBase
{
	public CmdTeamIslands()
	{
		super("teamislands");
		addSubcommand(new CmdIsland());
		addSubcommand(new CmdLobby());

		if (TeamIslandsConfig.general.enable_myisland_command)
		{
			addSubcommand(new CmdMyIsand());
		}
	}

	private static class CmdIsland extends CmdBase
	{
		private CmdIsland()
		{
			super("island", Level.OP);
		}

		@Override
		public boolean isUsernameIndex(String[] args, int index)
		{
			return index == 0;
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			checkArgs(sender, args, 1);
			ForgePlayer player = CommandUtils.getForgePlayer(sender, args[0]);

			if (player.isOnline() && player.hasTeam())
			{
				TeamIslandsUniverseData.INSTANCE.getIsland(player.team).teleport(player.getPlayer());
			}
		}
	}

	private static class CmdLobby extends CmdBase
	{
		private CmdLobby()
		{
			super("lobby", Level.ALL);
		}

		@Override
		public boolean isUsernameIndex(String[] args, int index)
		{
			return index == 0;
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			checkArgs(sender, args, 1);
			TeamIslandsUniverseData.INSTANCE.getIsland(0).teleport(getPlayer(server, server, args[0]));
		}
	}

	private static class CmdMyIsand extends CmdBase
	{
		private CmdMyIsand()
		{
			super("myisland", Level.ALL);
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			checkArgs(sender, args, 1);
			ForgePlayer player = CommandUtils.getForgePlayer(sender);

			if (player.hasTeam())
			{
				TeamIslandsUniverseData.INSTANCE.getIsland(player.team).teleport(player.getPlayer());
			}
		}
	}
}