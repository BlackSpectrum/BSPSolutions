package eu.blackspectrum.bspsolutions.entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.WorldMap;

import org.bukkit.map.MapCanvas;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class FMap
{


	private final byte[]	pixels;
	private final short		id;
	private final WorldMap	map;
	private boolean			secondChance;
	private boolean			dumped;




	// read compressed pixels from disc
	public static byte[] readData( final short id ) {
		final byte[] data = new byte[128 * 128];

		final File file = new File( "plugins" + File.separator + BSPSolutions.getPluginName() + File.separator + "maps" + File.separator
				+ "map_" + id + ".dat" );

		if ( !file.exists() )
			return data;

		try
		{
			final FileInputStream fis = new FileInputStream( file );
			final GZIPInputStream gis = new GZIPInputStream( fis );

			final byte[] buffer = new byte[4096];
			int len;
			int pos = 0;
			while ( ( len = gis.read( buffer ) ) != -1 )
			{
				System.arraycopy( buffer, 0, data, pos, len );
				pos += len;
			}

			// close resources
			gis.close();
		}
		catch ( final IOException e )
		{
			e.printStackTrace();
		}
		return data;
	}




	// write pixels to disc with gzip
	public static void writeData( final short id, final byte[] data ) {
		final File file = new File( "plugins" + File.separator + BSPSolutions.getPluginName() + File.separator + "maps" + File.separator
				+ "map_" + id + ".dat" );

		try
		{
			if ( !file.exists() )
				file.createNewFile();

			final FileOutputStream fos = new FileOutputStream( file );
			final GZIPOutputStream gzipOS = new GZIPOutputStream( fos );

			final byte[] buffer = new byte[4096];

			int pos = 0;
			while ( pos < 128 * 128 )
			{
				System.arraycopy( data, pos, buffer, 0, buffer.length );
				gzipOS.write( buffer );
				pos += buffer.length;
			}

			// close resources
			gzipOS.flush();
			gzipOS.close();
			fos.close();
		}
		catch ( final IOException e )
		{
			e.printStackTrace();
		}
	}




	public FMap(final short id) {
		this.id = id;
		this.pixels = readData( id );

		this.secondChance = true;

		this.dumped = false;

		// get the normal map
		this.map = (WorldMap) MinecraftServer.getServer().worlds.get( 0 ).worldMaps.get( WorldMap.class, "map_" + id );

	}




	// dump to disc
	public void dump() {
		writeData( this.id, this.pixels );
		this.dumped = true;
	}




	public short getId() {
		return this.id;
	}




	public boolean isDumped() {
		return this.dumped;
	}




	/**
	 * @return the secondChance
	 */
	public final boolean isSecondChance() {
		return this.secondChance;
	}




	public void renderFactions( final MapCanvas canvas ) {
		for ( int x = 0; x < 128; x++ )
			for ( int z = 0; z < 128; z++ )
				canvas.setPixel( x, z, this.pixels[x + 128 * z] );
	}




	public void renderTerrain( final MapCanvas canvas ) {
		for ( int x = 0; x < 128; x++ )
			for ( int z = 0; z < 128; z++ )
				canvas.setPixel( x, z, this.map.colors[x + 128 * z] );
	}




	public void setPixel( final int x, final int y, final byte value ) {
		if ( x >= 0 && x < 128 && y >= 0 && y < 128 )
			this.pixels[x + 128 * y] = value;
	}




	/**
	 * @param secondChance
	 *            the secondChance to set
	 */
	public final void setSecondChance( final boolean secondChance ) {
		this.secondChance = secondChance;
	}

}
