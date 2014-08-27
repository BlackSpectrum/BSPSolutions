package eu.blackspectrum.bspsolutions.adapters;

import java.lang.reflect.Type;
import java.util.Map;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

import eu.blackspectrum.bspsolutions.entities.BedBoard;



public class BedBoardAdapter implements JsonDeserializer<BedBoard>, JsonSerializer<BedBoard>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static BedBoardAdapter i = new BedBoardAdapter();
	public static BedBoardAdapter get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	@Override
	public BedBoard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return new BedBoard((Map<PS, String>) context.deserialize(json, BedBoard.MAP_TYPE));
	}

	@Override
	public JsonElement serialize(BedBoard src, Type typeOfSrc, JsonSerializationContext context)
	{
		return context.serialize(src.getMap(), BedBoard.MAP_TYPE);
	}
	
}
