package com.feed_the_beast.mods.teamislands.command;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.command.CommandBase;
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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args)
	{
		for (ForgeTeam team : Universe.get().getTeams())
		{
			ITextComponent title = team.getTitle().createCopy();
			title.getStyle().setClickEvent(null);
			title.getStyle().setHoverEvent(null);
			ITextComponent component = new TextComponentString("- ").appendSibling(title);
			component.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/teamislands island " + team.toString()));
			sender.sendMessage(component);
		}
	}
}