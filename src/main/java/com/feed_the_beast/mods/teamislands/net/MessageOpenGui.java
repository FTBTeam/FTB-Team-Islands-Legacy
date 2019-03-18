package com.feed_the_beast.mods.teamislands.net;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.mods.teamislands.GuiSelectIsland;
import com.feed_the_beast.mods.teamislands.data.IslandButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class MessageOpenGui extends MessageToClient
{
	public List<IslandButton> islands;

	public MessageOpenGui()
	{
	}

	public MessageOpenGui(List<IslandButton> i)
	{
		islands = i;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return TeamIslandsNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeCollection(islands, IslandButton.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		islands = new ArrayList<>();
		data.readCollection(islands, IslandButton.DESERIALIZER);
	}

	@Override
	public void onMessage()
	{
		new GuiSelectIsland(islands).openGui();
	}
}