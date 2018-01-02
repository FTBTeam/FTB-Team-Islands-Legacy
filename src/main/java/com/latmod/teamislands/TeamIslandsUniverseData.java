package com.latmod.teamislands;

import com.feed_the_beast.ftblib.events.universe.UniverseLoadedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseSavedEvent;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
@EventHandler
public class TeamIslandsUniverseData
{
	public static final ResourceLocation DATA_ID = new ResourceLocation(TeamIslandsFinals.MOD_ID, "data");
	private static final Island LOBBY = new Island(0, 0, 0, true, "server");
	public static final List<Island> ISLANDS = new ArrayList<>();

	public static Island getIsland(int id)
	{
		if (id == 0)
		{
			return LOBBY;
		}

		Island island = id < 0 || id >= ISLANDS.size() ? null : ISLANDS.get(id);

		if (island == null)
		{
			int index = id + 1, x = 0, z = 0, p = 1, ringIndex = 0;

			int s = (int) (Math.ceil(Math.sqrt(index)) + ((Math.ceil(Math.sqrt(index)) % 2 + 1) % 2));
			if (s > 1)
			{
				ringIndex = index - (s - 2) * (s - 2);
				p = s * s - (s - 2) * (s - 2);
			}

			int ri = (ringIndex + s / 2) % p;

			if (s > 1)
			{
				x = ri < (p / 4) ? ri : (ri <= (p / 4 * 2 - 1) ? (p / 4) : (ri <= (p / 4 * 3) ? ((p / 4 * 3) - ri) : 0));
			}

			if (s > 1)
			{
				z = ri < (p / 4) ? 0 : (ri <= (p / 4 * 2 - 1) ? (ri - (p / 4)) : (ri <= (p / 4 * 3) ? (p / 4) : (p - ri)));
			}

			x -= s / 2;
			z -= s / 2;

			island = new Island(ISLANDS.size(), x, z, true, "server");
			ISLANDS.add(island);
		}

		return island;
	}

	public static Island getIsland(@Nullable ForgeTeam team)
	{
		if (team == null)
		{
			return LOBBY;
		}

		return ((TeamIslandsTeamData) team.getData().get(DATA_ID)).getIsland();
	}

	@SubscribeEvent
	public static void onUniverseLoaded(UniverseLoadedEvent.Pre event)
	{
		ISLANDS.clear();
		ISLANDS.add(LOBBY);

		NBTTagCompound nbt = event.getData(DATA_ID);
		NBTTagList islands = nbt.getTagList("Islands", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < islands.tagCount(); i++)
		{
			NBTTagCompound nbt1 = islands.getCompoundTagAt(i);
			ISLANDS.add(new Island(i + 1, nbt1.getInteger("X"), nbt1.getInteger("Z"), !nbt1.getBoolean("Inactive"), nbt.getString("Creator")));
		}
	}

	@SubscribeEvent
	public static void onUniverseFinishedLoading(UniverseLoadedEvent.Finished event)
	{
		event.getWorld().setSpawnPoint(TeamIslandsUniverseData.getIsland(0).getBlockPos().offset(EnumFacing.UP));
	}

	@SubscribeEvent
	public static void onUniverseSaved(UniverseSavedEvent event)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList islands = new NBTTagList();

		for (Island island : ISLANDS)
		{
			if (!island.isLobby())
			{
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setInteger("X", island.x);
				nbt1.setInteger("Z", island.z);

				if (!island.active)
				{
					nbt1.setBoolean("Inactive", true);
				}

				nbt1.setString("Creator", island.creator);
				islands.appendTag(nbt1);
			}
		}

		nbt.setTag("Islands", islands);
		event.setData(DATA_ID, nbt);
	}
}