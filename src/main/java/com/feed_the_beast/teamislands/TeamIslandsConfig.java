package com.feed_the_beast.teamislands;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = TeamIslands.MOD_ID)
@Config(modid = TeamIslands.MOD_ID, category = "")
public class TeamIslandsConfig
{
	@Config.LangKey("stat.generalButton")
	public static final General general = new General();

	public static final Lobby lobby = new Lobby();
	public static final Islands islands = new Islands();

	public static class General
	{
		public boolean enabled_singleplayer = false;
		public boolean enabled_multiplayer = true;
		public boolean clear_inv_when_team_left = true;

		public boolean isEnabled(MinecraftServer server)
		{
			return server.isDedicatedServer() ? enabled_multiplayer : enabled_singleplayer;
		}
	}

	public static class Lobby
	{
		@Config.Comment("Auto-teleports player to their island once they join a team.")
		public boolean autoteleport_to_island = true;
	}

	public static class Islands
	{
		@Config.RangeInt(min = -1, max = 255)
		@Config.Comment({"Height at which the islands will generate.", "-1 = auto, on top of highest block in world"})
		public int height = 80;

		@Config.Comment({
				"Structure file will be loaded from config/x file.",
				"If not set, \"world/structures/island.nbt\" will be used.",
				"If that file is missing too, default island will be used."
		})
		@Config.RequiresWorldRestart
		public String custom_structure_file = "";

		@Config.Comment({"Radius of the chunks to automatically claim if FTBUtilities is installed.", "0 = disabled"})
		public int autoclaim_radius = 5;
	}

	public static void sync()
	{
		ConfigManager.sync(TeamIslands.MOD_ID, Config.Type.INSTANCE);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(TeamIslands.MOD_ID))
		{
			sync();
		}
	}
}