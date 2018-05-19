package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.events.universe.UniverseClosedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseLoadedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseSavedEvent;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.FileUtils;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
@EventHandler
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
		lobby = new Island(this, 0, new ChunkDimPos(0, 0, TeamIslandsConfig.lobby.dimension), "server");
		lobby.spawned = true;
		islands = new ArrayList<>();
		islands.add(lobby);

		NBTTagList islandsTag = nbt.getTagList("Islands", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < islandsTag.tagCount(); i++)
		{
			islands.add(new Island(this, i + 1, islandsTag.getCompoundTagAt(i)));
		}

		ResourceLocation structureId;

		if (TeamIslandsConfig.islands.structure_id.trim().isEmpty())
		{
			structureId = new ResourceLocation(TeamIslands.MOD_ID, "teamislands_island");
		}
		else
		{
			structureId = new ResourceLocation(TeamIslandsConfig.islands.structure_id);
		}

		islandTemplate = universe.world.getSaveHandler().getStructureTemplateManager().getTemplate(universe.server, structureId);

		NBTTagCompound nbt1 = FileUtils.readNBT(new File(universe.world.getSaveHandler().getWorldDirectory(), "structures/" + structureId.getResourcePath() + ".nbt"));

		if (nbt1 == null)
		{
			try (InputStream stream = MinecraftServer.class.getResourceAsStream("/assets/" + structureId.getResourceDomain() + "/structures/" + structureId.getResourcePath() + ".nbt"))
			{
				nbt1 = CompressedStreamTools.readCompressed(stream);
			}
			catch (Exception ex)
			{
			}
		}

		int sx = nbt1 != null && nbt1.hasKey("spawn_x") ? nbt1.getInteger("spawn_x") : TeamIslandsConfig.islands.fallback_spawn_x;
		int sy = nbt1 != null && nbt1.hasKey("spawn_y") ? nbt1.getInteger("spawn_y") : TeamIslandsConfig.islands.fallback_spawn_y;
		int sz = nbt1 != null && nbt1.hasKey("spawn_z") ? nbt1.getInteger("spawn_z") : TeamIslandsConfig.islands.fallback_spawn_z;
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
			island = new Island(this, islands.size(), new ChunkDimPos(pos.x * TeamIslandsConfig.islands.distance_chunks, pos.z * TeamIslandsConfig.islands.distance_chunks, TeamIslandsConfig.islands.dimension), "server");
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

		return ((TeamIslandsTeamData) team.getData(TeamIslands.MOD_ID)).getIsland();
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