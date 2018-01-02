package com.latmod.teamislands;

import com.feed_the_beast.ftblib.lib.math.BlockDimPos;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * @author LatvianModder
 */
public class Island
{
	public final int id, x, z;
	public boolean active = true;
	public String creator;

	public Island(int i, int _x, int _z, boolean a, String c)
	{
		id = i;
		x = _x;
		z = _z;
		active = a;
		creator = c;
	}

	public boolean isLobby()
	{
		return id <= 0;
	}

	public BlockPos getBlockPos(int yoff)
	{
		return new BlockPos(((x * TeamIslandsConfig.islands.distance_chunks) << 4) + 8, TeamIslandsConfig.islands.height + yoff, ((z * TeamIslandsConfig.islands.distance_chunks) << 4) + 8);
	}

	public void teleport(Entity entity)
	{
		ServerUtils.teleportEntity(entity, new BlockDimPos(getBlockPos(1), 0));
	}

	public boolean isInside(double px, double pz)
	{
		double s = TeamIslandsConfig.islands.distance_chunks * 8D;
		return MathUtils.distSq(px, pz, (MathHelper.floor(x * TeamIslandsConfig.islands.distance_chunks) << 4) + 8D, (MathHelper.floor(z * TeamIslandsConfig.islands.distance_chunks) << 4) + 8D) < s * s;
	}
}