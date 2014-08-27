package eu.blackspectrum.bspsolutions.adapters;



import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import org.bukkit.Location;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class BedBoardMapAdapter implements JsonDeserializer<Map<PS, String>>, JsonSerializer<Map<PS, String>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static BedBoardMapAdapter i = new BedBoardMapAdapter();
	public static BedBoardMapAdapter get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Map<PS, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		Map<PS, String> ret = new ConcurrentSkipListMap<PS, String>();
		
		JsonObject jsonObject = json.getAsJsonObject();
		
		for (Entry<String, JsonElement> entry : jsonObject.entrySet())
		{
			String[] blockCoordParts = entry.getKey().split("[,\\s]+");
			int blockX = Integer.parseInt(blockCoordParts[0]);
			int blockY = Integer.parseInt(blockCoordParts[1]);
			int blockZ = Integer.parseInt(blockCoordParts[2]);
			PS block = PS.valueOf( new Location( LocationUtil.getOverWorld(), blockX, blockY, blockZ ) ).getBlockCoords(true);
			
			String id = context.deserialize(entry.getValue(), String.class);
			
			ret.put(block, id);
		}
		
		return ret;
	}

	@Override
	public JsonElement serialize(Map<PS, String> src, Type typeOfSrc, JsonSerializationContext context)
	{
		JsonObject ret = new JsonObject();
		
		for (Entry<PS, String> entry : src.entrySet())
		{
			PS ps = entry.getKey();
			String id = entry.getValue();
			
			ret.add(ps.getBlockX().toString() + "," + ps.getBlockY().toString() + "," +ps.getBlockZ().toString(), context.serialize(id, String.class));
		}
		
		return ret;
	}
	
}