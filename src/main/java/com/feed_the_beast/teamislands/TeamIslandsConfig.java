package com.feed_the_beast.teamislands;

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
	public static final Lobby lobby = new Lobby();
	public static final Islands islands = new Islands();

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

		@Config.Comment({"Structure ID that will be generated when island is created.", "If not set, \"teamislands:teamislands_island\" will be used."})
		@Config.RequiresWorldRestart
		public String structure_id = "";

		@Config.Comment({"Radius of the chunks to automatically claim if FTBUtilities is installed.", "0 = disabled"})
		public int autoclaim_radius = 5;

		@Config.Comment("Fallback spawn X. Used if \"spawn_x\" is not found in structure NBT.")
		@Config.RequiresWorldRestart
		public int fallback_spawn_x = 0;

		@Config.Comment("Fallback spawn Y. Used if \"spawn_y\" is not found in structure NBT.")
		@Config.RequiresWorldRestart
		public int fallback_spawn_y = 0;

		@Config.Comment("Fallback spawn Z. Used if \"spawn_z\" is not found in structure NBT.")
		@Config.RequiresWorldRestart
		public int fallback_spawn_z = 0;
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