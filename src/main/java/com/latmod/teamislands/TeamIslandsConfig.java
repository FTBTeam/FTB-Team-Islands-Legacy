package com.latmod.teamislands;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = TeamIslandsFinals.MOD_ID)
@Config(modid = TeamIslandsFinals.MOD_ID, category = "")
public class TeamIslandsConfig
{
	public static final Islands islands = new Islands();

	public static class Islands
	{
		@Config.RangeInt(min = 3, max = 1000)
		@Config.Comment("Distance between islands in chunks")
		public int distance_chunks = 100;

		@Config.RangeInt(min = 0, max = 255)
		@Config.Comment("Height at which islands and lobby will generate")
		public int height = 80;

		@Config.Comment("Enables 'Teleport to Lobby' and 'Teleport to My Island' buttons in My Team")
		public boolean enable_teleport_buttons = true;
	}

	public static void sync()
	{
		ConfigManager.sync(TeamIslandsFinals.MOD_ID, Config.Type.INSTANCE);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(TeamIslandsFinals.MOD_ID))
		{
			sync();
		}
	}
}