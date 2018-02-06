package com.latmod.teamislands;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public class TeamIslandsTeamData implements INBTSerializable<NBTTagCompound>
{
	private final TeamIslandsUniverseData data;
	private Island island = null;
	private final ForgeTeam team;

	public TeamIslandsTeamData(TeamIslandsUniverseData d, ForgeTeam t)
	{
		data = d;
		team = t;
	}

	public Island getIsland()
	{
		if (island == null)
		{
			island = data.getIsland(data.islands.size());
			island.creator = team.getName();
		}

		return island;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("Island", getIsland().id);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		island = data.getIsland(nbt.getInteger("Island"));
		island.creator = team.getName();
	}
}