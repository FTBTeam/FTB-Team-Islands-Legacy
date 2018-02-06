package com.latmod.teamislands;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = TeamIslandsFinals.MOD_ID)
@Config(modid = TeamIslandsFinals.MOD_ID, category = "")
public class TeamIslandsConfig
{
	//@Config.LangKey(GuiLang.LANG_GENERAL)
	//public static final General general = new General();
	public static final Lobby lobby = new Lobby();
	public static final Islands islands = new Islands();

	public static class General
	{
	}

	public static class Lobby
	{
		@Config.Comment("Enables 'Teleport to Lobby' button in My Team")
		public boolean enable_teleport_button = true;
	}

	public static class Islands
	{
		@Config.RangeInt(min = 3, max = 1000)
		@Config.Comment("Distance between islands in chunks")
		public int distance_chunks = 100;

		@Config.RangeInt(min = 0, max = 255)
		@Config.Comment("Height at which the islands will generate")
		public int height = 80;

		@Config.Comment("Enables 'Teleport to My Island' button in My Team")
		public boolean enable_teleport_button = true;

		@Config.Comment("See Lobby blocks comment")
		public String[] blocks = {"minecraft:obsidian 1"};

		@Config.Comment({"Radius of the spawned platform + 1", "0=1x1, 1=3x3, 2=5x5, etc."})
		@Config.RangeInt(min = 0, max = 7)
		public int platform_radius = 0;

		@Config.Comment({"Radius of the chunks to automatically claim if FTBUtilities is installed", "0 = disabled"})
		public int autoclaim_radius = 5;

		private WeightedBlockState.List blockStates = null;

		public IBlockState getState(Random random)
		{
			if (blockStates == null)
			{
				blockStates = new WeightedBlockState.List(blocks);
			}

			return blockStates.get(random);
		}
	}

	public static void sync()
	{
		ConfigManager.sync(TeamIslandsFinals.MOD_ID, Config.Type.INSTANCE);
		islands.blockStates = null;
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