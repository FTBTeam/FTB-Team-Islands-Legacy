package com.feed_the_beast.mods.teamislands;

import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.mods.teamislands.net.MessageSelectIsland;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSelectIsland extends GuiButtonListBase
{
	private final List<String> islands;
	private final List<Icon> islandIcons;

	public GuiSelectIsland(List<ITextComponent> l, List<String> ic)
	{
		setTitle(I18n.format("gui.teamislands.select_island"));

		islands = new ArrayList<>();
		islandIcons = new ArrayList<>();

		for (int i = 0; i < l.size(); i++)
		{
			islands.add(l.get(i).getFormattedText());
			islandIcons.add(Icon.getIcon(ic.get(i)));
		}
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
		for (int i = 0; i < islands.size(); i++)
		{
			int i1 = i;

			panel.add(new SimpleTextButton(panel, islands.get(i1), islandIcons.get(i1))
			{
				@Override
				public void onClicked(MouseButton button)
				{
					new MessageSelectIsland(i1).sendToServer();
					closeGui();
				}
			});
		}
	}
}