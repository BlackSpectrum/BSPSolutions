package eu.blackspectrum.bspsolutions;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.map.CraftMapRenderer;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import eu.blackspectrum.bspsolutions.renderers.FMapRenderer;
import eu.blackspectrum.bspsolutions.renderers.PictureRenderer;
import eu.blackspectrum.bspsolutions.util.PicIO;

public class Maps
{


	@SuppressWarnings("deprecation")
	public static void init() {
		// Initialize all maps with correct Renderers
		// *********************************************************
		short lastedMap = -1;

		try
		{
			lastedMap = readLatestId();

		}
		catch ( final IOException e )
		{
			e.printStackTrace();
		}

		if ( lastedMap < 0 )
			return;

		for ( short id = 0; id <= lastedMap; id++ )
		{
			final MapView map = Bukkit.getMap( id );
			if ( map != null )
			{
				// Throw away all default map renderer
				for ( final MapRenderer rend : map.getRenderers() )
					if ( rend instanceof CraftMapRenderer )
						map.removeRenderer( rend );

				// Check if it is a picture
				if ( PicIO.isPicture( id ) )
					map.addRenderer( new PictureRenderer() );
				else
					map.addRenderer( new FMapRenderer() );

			}
		}
		// *********************************************************
	}




	// Read latest mapId from idcounts.dat
	private static short readLatestId() throws IOException {
		final File f = new File( "world" + File.separator + "data" + File.separator + "idcounts.dat" );

		if ( !f.exists() )
			return -1;

		final DataInputStream dis = new DataInputStream( new FileInputStream( f ) );
		try
		{
			// Read first tag
			dis.readByte();
			// Read first tag-name
			dis.readUTF();

			// Read second tag
			dis.readByte();
			// Read second tag name
			dis.readUTF();

			// Read short
			return dis.readShort();
		}
		catch ( final IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			dis.close();

		}

		return -1;
	}
}
