package com.latmod.teamislands;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.TeamGuiAction;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author LatvianModder
 */
public class TeamIslandsTeamGuiActions
{
	public static final TeamGuiAction TP_LOBBY = new TeamGuiAction(new ResourceLocation(TeamIslands.MOD_ID, "tp_lobby"), new TextComponentTranslation(TeamIslands.MOD_ID + ".action.tp_lobby"), GuiIcons.SETTINGS, 0)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			return TeamIslandsConfig.lobby.enable_teleport_button && !TeamIslandsUniverseData.INSTANCE.getIsland(0).isInside(player.getPlayer().posX, player.getPlayer().posZ);
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			TeamIslandsUniverseData.INSTANCE.getIsland(0).teleport(player.getPlayer());
			FTBLibAPI.sendCloseGuiPacket(player.getPlayer());
			FTBLibAPI.sendCloseGuiPacket(player.getPlayer());
		}
	};

	public static final TeamGuiAction TP_MY_ISLAND = new TeamGuiAction(new ResourceLocation(TeamIslands.MOD_ID, "tp_my_island"), new TextComponentTranslation(TeamIslands.MOD_ID + ".action.tp_my_island"), GuiIcons.SETTINGS, 0)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			if (!TeamIslandsConfig.islands.enable_teleport_button)
			{
				return false;
			}

			Island island = TeamIslandsUniverseData.INSTANCE.getIsland(team);
			return !island.isLobby() && !island.isInside(player.getPlayer().posX, player.getPlayer().posZ);
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			TeamIslandsUniverseData.INSTANCE.getIsland(team).teleport(player.getPlayer());
			FTBLibAPI.sendCloseGuiPacket(player.getPlayer());
			FTBLibAPI.sendCloseGuiPacket(player.getPlayer());
		}
	};
}