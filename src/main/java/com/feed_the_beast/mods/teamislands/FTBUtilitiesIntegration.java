package com.feed_the_beast.mods.teamislands;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftbutilities.FTBUtilitiesPermissions;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesTeamData;
import com.feed_the_beast.mods.teamislands.data.Island;
import com.feed_the_beast.mods.teamislands.data.TeamIslandsUniverseData;
import net.minecraft.util.math.ChunkPos;

/**
 * @author LatvianModder
 */
public class FTBUtilitiesIntegration
{
	public static void claimChunks(ForgePlayer player)
	{
		int r = TeamIslandsConfig.islands.autoclaim_radius;

		if (r >= 0 && ClaimedChunks.instance != null)
		{
			Island island = TeamIslandsUniverseData.INSTANCE.getIsland(player.team);

			if (!island.isLobby())
			{
				int chunks = Math.min((r * 2 + 1) * (r * 2 + 1), FTBUtilitiesTeamData.get(player.team).getMaxClaimChunks());
				int x = island.getEntitySpawnPos().getX() >> 4;
				int z = island.getEntitySpawnPos().getZ() >> 4;

				for (int i = 0; i <= chunks; i++)
				{
					ChunkPos pos0 = MathUtils.getSpiralPoint(i);
					ChunkDimPos pos = new ChunkDimPos(x + pos0.x, z + pos0.z, 0);

					if (ClaimedChunks.instance.canPlayerModify(player, pos, FTBUtilitiesPermissions.CLAIMS_OTHER_CLAIM))
					{
						ClaimedChunks.instance.claimChunk(player, pos);
					}
				}
			}
		}
	}
}