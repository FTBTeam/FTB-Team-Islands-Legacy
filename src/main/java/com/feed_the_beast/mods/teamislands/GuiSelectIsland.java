package com.feed_the_beast.mods.teamislands;

import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.mods.teamislands.data.IslandButton;
import com.feed_the_beast.mods.teamislands.net.MessageSelectIsland;
import net.minecraft.client.resources.I18n;

import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSelectIsland extends GuiButtonListBase
{
	private final List<IslandButton> islands;

	public GuiSelectIsland(List<IslandButton> i)
	{
		setTitle(I18n.format("gui.teamislands.select_island"));
		islands = i;
	}

	@Override
	public boolean onClosedByKey(int key)
	{
		return false;
	}

	@Override
	public void onBack()
	{
	}

	@Override
	public void addButtons(Panel panel)
	{
		for (IslandButton island : islands)
		{
			panel.add(new SimpleTextButton(panel, island.name.getFormattedText(), Icon.getIcon(island.icon))
			{
				@Override
				public void onClicked(MouseButton button)
				{
					new MessageSelectIsland(island.path).sendToServer();
					closeGui();
				}
			});
		}
	}
}