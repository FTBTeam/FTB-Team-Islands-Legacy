package com.feed_the_beast.mods.teamislands.data;

import com.feed_the_beast.ftblib.lib.math.TeleporterDimPos;
import com.feed_the_beast.mods.teamislands.TeamIslandsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * @author LatvianModder
 */
public class Island
{
	public final TeamIslandsUniverseData data;
	public final int id;
	public final int x, z;
	public int template;
	public boolean active, spawned;
	public String creator;
	public BlockPos spawnPoint;

	public Island(TeamIslandsUniverseData d, int i, int _x, int _z, String c)
	{
		data = d;
		id = i;
		x = _x;
		z = _z;
		template = -1;
		active = true;
		spawned = false;
		creator = c;
		spawnPoint = new BlockPos(x * 512 + 256, TeamIslandsConfig.islands.height, z * 512 + 256);
	}

	public Island(TeamIslandsUniverseData d, int i, NBTTagCompound nbt)
	{
		data = d;
		id = i;
		x = nbt.getInteger("X");
		z = nbt.getInteger("Z");
		template = nbt.getInteger("Template");
		active = !nbt.getBoolean("Inactive");
		spawned = nbt.getBoolean("Spawned");
		creator = nbt.getString("Creator");
		spawnPoint = new BlockPos(nbt.getInteger("SpawnX"), nbt.getInteger("SpawnY"), nbt.getInteger("SpawnZ"));
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("X", x);
		nbt.setInteger("Z", z);
		nbt.setInteger("Template", template);
		nbt.setBoolean("Inactive", !active);
		nbt.setBoolean("Spawned", spawned);
		nbt.setString("Creator", creator);
		nbt.setInteger("SpawnX", spawnPoint.getX());
		nbt.setInteger("SpawnY", spawnPoint.getY());
		nbt.setInteger("SpawnZ", spawnPoint.getZ());
	}

	public boolean isLobby()
	{
		return id <= 0;
	}

	public BlockPos getBlockPos()
	{
		if (isLobby())
		{
			BlockPos spawnpoint = data.universe.world.getSpawnPoint();

			while (data.universe.world.getBlockState(spawnpoint).isFullCube())
			{
				spawnpoint = spawnpoint.up();
			}

			return spawnpoint;
		}

		return spawnPoint;
	}

	public IslandTemplate getTemplate()
	{
		return template < 0 || template >= data.islandTemplates.size() ? data.islandTemplates.get(0) : data.islandTemplates.get(template);
	}

	public BlockPos getEntitySpawnPos()
	{
		if (isLobby())
		{
			BlockPos spawnpoint = data.universe.world.getSpawnPoint();

			while (data.universe.world.getBlockState(spawnpoint).isFullCube())
			{
				spawnpoint = spawnpoint.up();
			}

			return spawnpoint;
		}

		return spawnPoint.add(getTemplate().spawn);
	}

	public void teleport(Entity entity)
	{
		TeleporterDimPos.of(getEntitySpawnPos(), 0).teleport(entity);
	}
}