package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.events.universe.UniverseClosedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseLoadedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseSavedEvent;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = TeamIslands.MOD_ID)
public class TeamIslandsUniverseData
{
	public static TeamIslandsUniverseData INSTANCE;

	public final Universe universe;
	private final Island lobby;
	public final List<Island> islands;
	public Template islandTemplate;
	public BlockPos relativeSpawn;

	public TeamIslandsUniverseData(Universe u, NBTTagCompound nbt)
	{
		universe = u;
		lobby = new Island(this, 0, 0, 0, "server");
		lobby.spawned = true;
		islands = new ArrayList<>();
		islands.add(lobby);

		NBTTagList islandsTag = nbt.getTagList("Islands", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < islandsTag.tagCount(); i++)
		{
			islands.add(new Island(this, i + 1, islandsTag.getCompoundTagAt(i)));
		}

		if (TeamIslandsConfig.islands.custom_structure_file.trim().isEmpty())
		{
			islandTemplate = universe.world.getSaveHandler().getStructureTemplateManager().getTemplate(universe.server, new ResourceLocation(TeamIslands.MOD_ID, "teamislands_island"));
		}
		else
		{
			ResourceLocation id = new ResourceLocation(TeamIslands.MOD_ID, "teamislands_custom");
			islandTemplate = universe.world.getSaveHandler().getStructureTemplateManager().get(universe.server, id);

			if (islandTemplate == null)
			{
				islandTemplate = universe.world.getSaveHandler().getStructureTemplateManager().getTemplate(universe.server, id);

				File file = new File(Loader.instance().getConfigDir(), TeamIslandsConfig.islands.custom_structure_file.trim());

				if (file.exists() && file.isFile())
				{
					try (FileInputStream fis = new FileInputStream(file))
					{
						islandTemplate.read(CompressedStreamTools.readCompressed(fis));
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}

		int sx = islandTemplate.getSize().getX() / 2;
		int sy = islandTemplate.getSize().getY();
		int sz = islandTemplate.getSize().getZ() / 2;

		for (Map.Entry<BlockPos, String> entry : islandTemplate.getDataBlocks(BlockPos.ORIGIN, new PlacementSettings()).entrySet())
		{
			if (entry.getValue().equals("SPAWN_POINT"))
			{
				sx = entry.getKey().getX();
				sy = entry.getKey().getY();
				sz = entry.getKey().getZ();
			}
		}

		relativeSpawn = new BlockPos(sx, sy, sz);

		if (FTBLibConfig.debugging.print_more_info)
		{
			TeamIslands.LOGGER.info("Island relative spawnpoint: " + relativeSpawn);
		}
	}

	public Island getIsland(int id)
	{
		if (id == 0)
		{
			return lobby;
		}

		Island island = id < 0 || id >= islands.size() ? null : islands.get(id);

		if (island == null)
		{
			ChunkPos pos = MathUtils.getSpiralPoint(id + 1);
			island = new Island(this, islands.size(), pos.x, pos.z, "server");
			islands.add(island);
			universe.markDirty();
		}

		return island;
	}

	public Island getIsland(@Nullable ForgeTeam team)
	{
		if (team == null)
		{
			return lobby;
		}

		return ((TeamIslandsTeamData) team.getData().get(TeamIslands.MOD_ID)).getIsland();
	}

	@SubscribeEvent
	public static void onUniverseLoaded(UniverseLoadedEvent.Pre event)
	{
		INSTANCE = new TeamIslandsUniverseData(event.getUniverse(), event.getData(TeamIslands.MOD_ID));
	}

	@SubscribeEvent
	public static void onUniverseSaved(UniverseSavedEvent event)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList islands = new NBTTagList();

		for (Island island : INSTANCE.islands)
		{
			if (!island.isLobby())
			{
				NBTTagCompound nbt1 = new NBTTagCompound();
				island.writeToNBT(nbt1);
				islands.appendTag(nbt1);
			}
		}

		nbt.setTag("Islands", islands);
		event.setData(TeamIslands.MOD_ID, nbt);
	}

	@SubscribeEvent
	public static void onUniverseUnloaded(UniverseClosedEvent event)
	{
		INSTANCE = null;
	}
}