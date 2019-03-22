package com.feed_the_beast.mods.teamislands.command;

import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.mods.teamislands.data.TeamIslandsUniverseData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CommandMyIsland extends CommandBase
{
	@Override
	public String getName()
	{
		return "myisland";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.teamislands.myisland.usage";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		ForgePlayer player = CommandUtils.getForgePlayer(sender);

		if (player.hasTeam())
		{
			TeamIslandsUniverseData.INSTANCE.getIsland(player.team).teleport(player.getPlayer());
		}
	}
}