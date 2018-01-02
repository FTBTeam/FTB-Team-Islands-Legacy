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
	public static final Lobby lobby = new Lobby();
	public static final Islands islands = new Islands();

	public static class Lobby
	{
		@Config.RangeInt(min = 0, max = 255)
		@Config.Comment("Height at which the lobby will generate")
		public int height = 80;

		@Config.Comment("Enables 'Teleport to Lobby' button in My Team")
		public boolean enable_teleport_button = true;

		@Config.Comment({
				"Block that the structure will be made of",
				"Format: mod:block weight",
				"Format with properties: mod:block[p1=v1,p2=v2]"})
		public String[] blocks = {
				"minecraft:stonebrick 8",
				"minecraft:stonebrick[variant=cracked_stonebrick] 1",
				"minecraft:stonebrick[variant=mossy_stonebrick] 1"
		};

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

	public static class Islands
	{
		@Config.RangeInt(min = 3, max = 1000)
		@Config.Comment("Distance between islands in chunks")
		public int distance_chunks = 100;

		@Config.RangeInt(min = 0, max = 255)
		@Config.Comment("Height at which the islands will generate")
		@Config.LangKey("teamislands.lobby.height")
		public int height = 80;

		@Config.Comment("Enables 'Teleport to My Island' button in My Team")
		@Config.LangKey("teamislands.lobby.enable_teleport_button")
		public boolean enable_teleport_button = true;

		@Config.Comment("See Lobby blocks comment")
		@Config.LangKey("teamislands.lobby.blocks")
		public String[] blocks = {"minecraft:obsidian 1"};

		@Config.Comment({"Radius of the spawned platform + 1", "0=1x1, 1=3x3, 2=5x5, etc."})
		@Config.RangeInt(min = 0, max = 7)
		public int platform_radius = 0;

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
		lobby.blockStates = null;
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