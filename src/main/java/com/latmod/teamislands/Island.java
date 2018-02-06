package com.latmod.teamislands;

import com.feed_the_beast.ftblib.lib.math.BlockDimPos;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.WorldInfo;

/**
 * @author LatvianModder
 */
public class Island
{
	public final TeamIslandsUniverseData data;
	public final int id, x, z;
	public boolean active;
	public String creator;
	public BlockPos spawnPoint;

	public Island(TeamIslandsUniverseData d, int i, int _x, int _z, boolean a, String c)
	{
		data = d;
		id = i;
		x = _x;
		z = _z;
		active = a;
		creator = c;
		spawnPoint = new BlockPos(((x * TeamIslandsConfig.islands.distance_chunks) << 4) + 8, TeamIslandsConfig.islands.height, ((z * TeamIslandsConfig.islands.distance_chunks) << 4) + 8);
	}

	public boolean isLobby()
	{
		return id <= 0;
	}

	public BlockPos getBlockPos()
	{
		if (isLobby())
		{
			WorldInfo info = data.universe.world.getWorldInfo();
			return new BlockPos(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ());
		}

		return spawnPoint;
	}

	public void teleport(Entity entity)
	{
		ServerUtils.teleportEntity(entity, new BlockDimPos(getBlockPos().offset(EnumFacing.UP), 0));
	}

	public boolean isInside(double px, double pz)
	{
		double s = TeamIslandsConfig.islands.distance_chunks * 8D;
		return MathUtils.distSq(px, pz, (MathHelper.floor(x * TeamIslandsConfig.islands.distance_chunks) << 4) + 8D, (MathHelper.floor(z * TeamIslandsConfig.islands.distance_chunks) << 4) + 8D) < s * s;
	}
}