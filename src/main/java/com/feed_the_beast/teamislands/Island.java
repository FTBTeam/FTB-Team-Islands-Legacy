package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.lib.math.BlockDimPos;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

/**
 * @author LatvianModder
 */
public class Island
{
	public final TeamIslandsUniverseData data;
	public final int id;
	public final ChunkDimPos pos;
	public boolean active, spawned;
	public String creator;
	public BlockPos spawnPoint;

	public Island(TeamIslandsUniverseData d, int i, ChunkDimPos p, String c)
	{
		data = d;
		id = i;
		pos = p;
		active = true;
		spawned = false;
		creator = c;
		spawnPoint = new BlockPos(pos.getBlockX(), TeamIslandsConfig.islands.height, pos.getBlockZ());
	}

	public Island(TeamIslandsUniverseData d, int i, NBTTagCompound nbt)
	{
		data = d;
		id = i;
		int x = nbt.hasKey("X") ? (nbt.getInteger("X") * TeamIslandsConfig.islands.distance_chunks) : nbt.getInteger("ChunkX");
		int z = nbt.hasKey("Z") ? (nbt.getInteger("Z") * TeamIslandsConfig.islands.distance_chunks) : nbt.getInteger("ChunkZ");
		int dim = nbt.hasKey("Dim") ? nbt.getInteger("Dim") : TeamIslandsConfig.islands.dimension;
		pos = new ChunkDimPos(x, z, dim);
		active = !nbt.getBoolean("Inactive");
		spawned = nbt.getBoolean("Spawned");
		creator = nbt.getString("Creator");
		spawnPoint = new BlockPos(nbt.getInteger("SpawnX"), nbt.getInteger("SpawnY"), nbt.getInteger("SpawnZ"));
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("ChunkX", pos.posX);
		nbt.setInteger("ChunkZ", pos.posZ);
		nbt.setInteger("Dim", pos.dim);
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

	public BlockDimPos getBlockPos()
	{
		if (isLobby())
		{
			WorldServer w = data.universe.server.getWorld(TeamIslandsConfig.lobby.dimension);
			BlockPos spawnpoint = w.getSpawnPoint();

			while (w.getBlockState(spawnpoint).isFullCube())
			{
				spawnpoint = spawnpoint.up(2);
			}

			return new BlockDimPos(spawnpoint, TeamIslandsConfig.lobby.dimension);
		}

		return new BlockDimPos(spawnPoint, TeamIslandsConfig.islands.dimension);
	}

	public void teleport(Entity entity)
	{
		if (isLobby())
		{
			WorldServer w = data.universe.server.getWorld(TeamIslandsConfig.lobby.dimension);
			BlockPos spawnpoint = w.getSpawnPoint();

			while (w.getBlockState(spawnpoint).isFullCube())
			{
				spawnpoint = spawnpoint.up(2);
			}

			ServerUtils.teleportEntity(entity, spawnpoint, TeamIslandsConfig.lobby.dimension);
		}
		else
		{
			ServerUtils.teleportEntity(entity, spawnPoint.add(data.relativeSpawn), TeamIslandsConfig.islands.dimension);
		}
	}

	public boolean isInside(Entity entity)
	{
		return isInside(entity.posX, entity.posZ, entity.dimension);
	}

	public boolean isInside(double px, double pz, int dim)
	{
		if (dim != (isLobby() ? TeamIslandsConfig.lobby.dimension : TeamIslandsConfig.islands.dimension))
		{
			return true;
		}

		double s = TeamIslandsConfig.islands.distance_chunks * 8D;
		return MathUtils.distSq(px, pz, pos.getBlockX(), pos.getBlockZ()) < s * s;
	}
}