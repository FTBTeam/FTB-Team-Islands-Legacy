package com.feed_the_beast.mods.teamislands.data;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public class IslandButton
{
	public static final DataOut.Serializer<IslandButton> SERIALIZER = (data, island) -> {
		data.writeString(island.path);
		data.writeTextComponent(island.name);
		data.writeString(island.icon);
	};

	public static final DataIn.Deserializer<IslandButton> DESERIALIZER = data -> {
		IslandButton island = new IslandButton();
		island.path = data.readString();
		island.name = data.readTextComponent();
		island.icon = data.readString();
		return island;
	};

	public String path;
	public ITextComponent name;
	public String icon;
}