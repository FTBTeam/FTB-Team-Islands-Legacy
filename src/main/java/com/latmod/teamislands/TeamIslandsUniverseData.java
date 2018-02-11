package com.latmod.teamislands;

import com.feed_the_beast.ftblib.events.universe.UniverseClosedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseLoadedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseSavedEvent;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
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
	public static TeamIslandsUniverseData INSTANCE;

	public final Universe universe;
	private final Island lobby;
	public final List<Island> islands;

	public TeamIslandsUniverseData(Universe u, NBTTagCompound nbt)
	{
		universe = u;
		lobby = new Island(this, 0, 0, 0, true, "server");
		islands = new ArrayList<>();
		islands.add(lobby);

		NBTTagList islandsTag = nbt.getTagList("Islands", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < islandsTag.tagCount(); i++)
		{
			NBTTagCompound nbt1 = islandsTag.getCompoundTagAt(i);
			Island island = new Island(this, i + 1, nbt1.getInteger("X"), nbt1.getInteger("Z"), !nbt1.getBoolean("Inactive"), nbt.getString("Creator"));

			if (nbt1.hasKey("SpawnX") && nbt1.hasKey("SpawnY") && nbt1.hasKey("SpawnZ"))
			{
				island.spawnPoint = new BlockPos(nbt1.getInteger("SpawnX"), nbt1.getInteger("SpawnY"), nbt1.getInteger("SpawnZ"));
			}

			islands.add(island);
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

			island = new Island(this, islands.size(), x, z, true, "server");
			islands.add(island);
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
				nbt1.setInteger("X", island.x);
				nbt1.setInteger("Z", island.z);

				if (!island.active)
				{
					nbt1.setBoolean("Inactive", true);
				}

				nbt1.setString("Creator", island.creator);
				nbt1.setInteger("SpawnX", island.spawnPoint.getX());
				nbt1.setInteger("SpawnY", island.spawnPoint.getY());
				nbt1.setInteger("SpawnZ", island.spawnPoint.getZ());
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