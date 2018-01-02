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
	public static final TeamGuiAction LOBBY = new TeamGuiAction(new ResourceLocation(TeamIslandsFinals.MOD_ID, "lobby"), new TextComponentTranslation(TeamIslandsFinals.MOD_ID + ".action.lobby"), GuiIcons.SETTINGS, 0)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			return !TeamIslandsUniverseData.getIsland(0).isInside(player.getPlayer().posX, player.getPlayer().posZ);
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			TeamIslandsUniverseData.getIsland(0).teleport(player.getPlayer());
			FTBLibAPI.sendCloseGuiPacket(player.getPlayer());
			FTBLibAPI.sendCloseGuiPacket(player.getPlayer());
		}
	};

	public static final TeamGuiAction MY_ISLAND = new TeamGuiAction(new ResourceLocation(TeamIslandsFinals.MOD_ID, "my_island"), new TextComponentTranslation(TeamIslandsFinals.MOD_ID + ".action.my_island"), GuiIcons.SETTINGS, 0)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			Island island = TeamIslandsUniverseData.getIsland(team);
			return !island.isLobby() && !island.isInside(player.getPlayer().posX, player.getPlayer().posZ);
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			TeamIslandsUniverseData.getIsland(team).teleport(player.getPlayer());
			FTBLibAPI.sendCloseGuiPacket(player.getPlayer());
			FTBLibAPI.sendCloseGuiPacket(player.getPlayer());
		}
	};
}