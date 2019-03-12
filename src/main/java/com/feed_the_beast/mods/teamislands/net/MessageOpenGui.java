package com.feed_the_beast.mods.teamislands.net;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.mods.teamislands.GuiSelectIsland;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class MessageOpenGui extends MessageToClient
{
	public List<ITextComponent> islands;
	public List<String> icons;

	public MessageOpenGui()
	{
	}

	public MessageOpenGui(List<ITextComponent> l, List<String> i)
	{
		islands = l;
		icons = i;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return TeamIslandsNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeCollection(islands, DataOut.TEXT_COMPONENT);
		data.writeCollection(icons, DataOut.STRING);
	}

	@Override
	public void readData(DataIn data)
	{
		islands = new ArrayList<>();
		data.readCollection(islands, DataIn.TEXT_COMPONENT);
		icons = new ArrayList<>();
		data.readCollection(icons, DataIn.STRING);
	}

	@Override
	public void onMessage()
	{
		new GuiSelectIsland(islands, icons).openGui();
	}
}