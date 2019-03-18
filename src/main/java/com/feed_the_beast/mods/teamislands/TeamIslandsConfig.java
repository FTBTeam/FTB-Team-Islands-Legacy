package com.feed_the_beast.mods.teamislands;

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
		public boolean enable_myisland_command = true;
		public String void_world_type_id = "void";

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
				"Structure files will be loaded from config/x file.",
				"If not set, builtin island will be used.",
		})
		@Config.RequiresWorldRestart
		public String[] structure_files = { };

		@Config.Comment({"Radius of the chunks to automatically claim if FTBUtilities is installed.", "-1 = disabled", "0 = 1x1", "1 = 3x3", "4 = 9x9"})
		public int autoclaim_radius = 4;

		@Config.Comment("Allow selection of the island type, if set to false, then islands will be randomized.")
		public boolean select_islands = true;
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