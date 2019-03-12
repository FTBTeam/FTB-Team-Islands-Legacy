package com.feed_the_beast.mods.teamislands.net;

import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.mods.teamislands.TeamIslands;

/**
 * @author LatvianModder
 */
public class TeamIslandsNetHandler
{
	public static NetworkWrapper NET;

	public static void init()
	{
		NET = NetworkWrapper.newWrapper(TeamIslands.MOD_ID);
		NET.register(new MessageOpenGui());
		NET.register(new MessageSelectIsland());
	}
}