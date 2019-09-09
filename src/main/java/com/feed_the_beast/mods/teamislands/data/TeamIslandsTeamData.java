package com.feed_the_beast.mods.teamislands.data;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.TeamData;
import com.feed_the_beast.mods.teamislands.TeamIslands;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author LatvianModder
 */
public class TeamIslandsTeamData extends TeamData
{
	private Island island = null;

	public TeamIslandsTeamData(ForgeTeam team)
	{
		super(team);
	}

	@Override
	public String getId()
	{
		return TeamIslands.MOD_ID;
	}

	public Island getIsland()
	{
		if (island == null)
		{
			island = TeamIslandsUniverseData.INSTANCE.getIsland(TeamIslandsUniverseData.INSTANCE.islands.size());
			island.creator = team.getId();
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
		island.creator = team.getId();
	}
}