package com.feed_the_beast.mods.teamislands.command;

import com.feed_the_beast.mods.teamislands.data.TeamIslandsUniverseData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CommandLobby extends CommandBase
{
	@Override
	public String getName()
	{
		return "lobby";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "command.teamislands.lobby.usage";
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
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index == 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length < 1)
		{
			throw new WrongUsageException(getUsage(sender));
		}

		TeamIslandsUniverseData.INSTANCE.getIsland(0).teleport(getPlayer(server, server, args[0]));
	}
}