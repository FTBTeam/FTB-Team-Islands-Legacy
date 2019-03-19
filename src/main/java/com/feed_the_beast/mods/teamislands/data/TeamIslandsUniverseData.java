package com.feed_the_beast.mods.teamislands.data;

import com.feed_the_beast.ftblib.events.universe.UniverseClosedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseLoadedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseSavedEvent;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.mods.teamislands.TeamIslands;
import com.feed_the_beast.mods.teamislands.TeamIslandsConfig;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
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
import java.util.HashMap;
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
	public final List<IslandTemplate> islandTemplates;
	public final Map<String, IslandTemplate> islandTemplateMap;

	public TeamIslandsUniverseData(Universe u, NBTTagCompound nbt)
	{
		universe = u;
		lobby = new Island(this, 0, 0, 0, "server");
		lobby.spawned = true;
		islands = new ArrayList<>();
		islands.add(lobby);

		islandTemplates = new ArrayList<>();
		islandTemplateMap = new HashMap<>();

		for (String s : TeamIslandsConfig.islands.structure_files)
		{
			Template template = universe.world.getSaveHandler().getStructureTemplateManager().getTemplate(universe.server, new ResourceLocation(TeamIslands.MOD_ID, "custom/" + islandTemplates.size()));

			File folder = Loader.instance().getConfigDir();
			s = s.trim();

			if (s.startsWith("/"))
			{
				folder = folder.getParentFile();
				s = s.substring(1);
			}

			File file = new File(folder, s);

			if (file.exists() && file.isFile())
			{
				try (FileInputStream fis = new FileInputStream(file))
				{
					template.read(CompressedStreamTools.readCompressed(fis));
					islandTemplates.add(new IslandTemplate(file.getName(), s, template));
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}

		if (islandTemplates.isEmpty())
		{
			Template template = universe.world.getSaveHandler().getStructureTemplateManager().getTemplate(universe.server, new ResourceLocation(TeamIslands.MOD_ID, "teamislands_island"));
			islandTemplates.add(new IslandTemplate("default", "default", template));
		}

		for (IslandTemplate t : islandTemplates)
		{
			t.spawn = new BlockPos(t.template.getSize().getX() / 2, t.template.getSize().getY(), t.template.getSize().getZ() / 2);

			for (Map.Entry<BlockPos, String> entry : t.template.getDataBlocks(BlockPos.ORIGIN, new PlacementSettings()).entrySet())
			{
				if (entry.getValue().equals("SPAWN_POINT"))
				{
					t.spawn = entry.getKey();
				}
				else if (entry.getValue().startsWith("DISPLAY_NAME="))
				{
					try
					{
						t.displayName = ITextComponent.Serializer.fromJsonLenient(entry.getValue().substring(13));
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
				else if (entry.getValue().startsWith("ICON="))
				{
					t.icon = entry.getValue().substring(5);
				}
			}
		}

		for (IslandTemplate template : islandTemplates)
		{
			islandTemplateMap.put(template.path, template);
		}

		NBTTagList islandsTag = nbt.getTagList("Islands", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < islandsTag.tagCount(); i++)
		{
			islands.add(new Island(this, i + 1, islandsTag.getCompoundTagAt(i)));
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