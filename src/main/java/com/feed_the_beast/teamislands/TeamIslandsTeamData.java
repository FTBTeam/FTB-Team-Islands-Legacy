package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public class TeamIslandsTeamData implements INBTSerializable<NBTTagCompound>
{
	private Island island = null;
	private final ForgeTeam team;

	public TeamIslandsTeamData(ForgeTeam t)
	{
		team = t;
	}

	public Island getIsland()
	{
		if (island == null)
		{
			island = TeamIslandsUniverseData.INSTANCE.getIsland(TeamIslandsUniverseData.INSTANCE.islands.size());
			island.creator = team.getName();
			team.markDirty();
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
		island = TeamIslandsUniverseData.INSTANCE.getIsland(nbt.getInteger("Island"));
		island.creator = team.getName();
	}
}