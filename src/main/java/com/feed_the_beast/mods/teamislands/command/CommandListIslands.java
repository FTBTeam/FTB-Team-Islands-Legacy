package com.feed_the_beast.mods.teamislands.command;

import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.mods.teamislands.data.TeamIslandsUniverseData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;

/**
 * @author LatvianModder
 */
public class CommandListIslands extends CommandBase
{
	@Override
	public String getName()
	{
		return "list";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.teamislands.list.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		for (ForgeTeam team : Universe.get().getTeams())
		{
			ITextComponent component = new TextComponentString("- ").appendSibling(team.getTitle().createCopy());
			component.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/teamislands island " + team.toString()));
			sender.sendMessage(component);
		}

		ForgePlayer player = CommandUtils.getForgePlayer(sender, args[0]);

		if (player.isOnline() && player.hasTeam())
		{
			TeamIslandsUniverseData.INSTANCE.getIsland(player.team).teleport(player.getPlayer());
		}
	}
}