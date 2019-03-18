package com.feed_the_beast.mods.teamislands;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.mods.teamislands.command.CommandTeamIslands;
import com.feed_the_beast.mods.teamislands.net.TeamIslandsNetHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
		modid = TeamIslands.MOD_ID,
		name = TeamIslands.MOD_NAME,
		version = TeamIslands.VERSION,
		acceptableRemoteVersions = "*",
		dependencies = FTBLib.THIS_DEP
)
public class TeamIslands
{
	public static final String MOD_ID = "teamislands";
	public static final String MOD_NAME = "Team Islands";
	public static final String VERSION = "0.0.0.teamislands";

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		TeamIslandsNetHandler.init();

		if (!TeamIslandsConfig.general.void_world_type_id.isEmpty())
		{
			new VoidWorldType();
		}
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandTeamIslands());
	}
}