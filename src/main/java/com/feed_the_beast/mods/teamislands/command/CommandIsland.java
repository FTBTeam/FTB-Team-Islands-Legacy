package com.feed_the_beast.mods.teamislands.command;

import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.mods.teamislands.data.TeamIslandsUniverseData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CommandIsland extends CommandBase
{
	@Override
	public String getName()
	{
		return "island";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.teamislands.island.usage";
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index == 0;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length < 1)
		{
			throw new WrongUsageException(getUsage(sender));
		}

		ForgeTeam team = CommandUtils.getTeam(sender, args[0]);
		TeamIslandsUniverseData.INSTANCE.getIsland(team).teleport(getCommandSenderAsPlayer(sender));
	}
}