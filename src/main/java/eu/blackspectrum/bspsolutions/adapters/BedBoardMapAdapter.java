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

	private static BedBoardMapAdapter	i	= new BedBoardMapAdapter();




	public static BedBoardMapAdapter get() {
		return i;
	}




	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Map<PS, String> deserialize( final JsonElement json, final Type typeOfT, final JsonDeserializationContext context )
			throws JsonParseException {
		final Map<PS, String> ret = new ConcurrentSkipListMap<PS, String>();

		final JsonObject jsonObject = json.getAsJsonObject();

		for ( final Entry<String, JsonElement> entry : jsonObject.entrySet() )
		{
			final String[] blockCoordParts = entry.getKey().split( "[,\\s]+" );
			final int blockX = Integer.parseInt( blockCoordParts[0] );
			final int blockY = Integer.parseInt( blockCoordParts[1] );
			final int blockZ = Integer.parseInt( blockCoordParts[2] );
			final PS block = PS.valueOf( new Location( LocationUtil.getOverWorld(), blockX, blockY, blockZ ) ).getBlockCoords( true );

			final String id = context.deserialize( entry.getValue(), String.class );

			ret.put( block, id );
		}

		return ret;
	}




	@Override
	public JsonElement serialize( final Map<PS, String> src, final Type typeOfSrc, final JsonSerializationContext context ) {
		final JsonObject ret = new JsonObject();

		for ( final Entry<PS, String> entry : src.entrySet() )
		{
			final PS ps = entry.getKey();
			final String id = entry.getValue();

			ret.add( ps.getBlockX().toString() + "," + ps.getBlockY().toString() + "," + ps.getBlockZ().toString(),
					context.serialize( id, String.class ) );
		}

		return ret;
	}

}