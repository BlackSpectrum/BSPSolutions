package eu.blackspectrum.bspsolutions;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.map.CraftMapRenderer;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import eu.blackspectrum.bspsolutions.entities.FMap;
import eu.blackspectrum.bspsolutions.renderers.FMapRenderer;

public class FMaps
{


	private HashMap<Short, FMap>	fMaps	= new HashMap<Short, FMap>();

	private static FMaps			instance;




	// Singleton
	public static FMaps get() {
		if ( instance == null )
			instance = new FMaps();

		return instance;
	}




	private FMaps() {
	}




	// Dump and remove those that didnt get used for some time
	public void collectGarbage() {

		if ( this.fMaps == null )
			return;

		final Iterator<FMap> it = this.fMaps.values().iterator();

		while ( it.hasNext() )
		{
			final FMap map = it.next();

			if ( !map.isSecondChance() )
			{
				map.dump();
				it.remove();
			}
			else
				map.setSecondChance( false );
		}

		if ( this.fMaps.size() == 0 )
			this.fMaps = null;
	}




	// Dump everything, but do not remove
	public void dump() {
		if ( this.fMaps == null )
			return;

		for ( final FMap fMap : this.fMaps.values() )
			fMap.dump();
	}




	public FMap getFMap( final short id ) {
		if ( this.fMaps == null )
			this.fMaps = new HashMap<Short, FMap>();

		if ( !this.fMaps.containsKey( id ) )
			this.fMaps.put( id, new FMap( id ) );

		return this.fMaps.get( id );
	}




	public Collection<FMap> getFMaps() {
		if ( this.fMaps == null )
			return new HashSet<FMap>();
		return this.fMaps.values();
	}




	public void initialize() {
		// Initialize all maps with FMaps
		// *********************************************************
		short lastedMap = -1;

		try
		{
			lastedMap = this.readLatestId();

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

				// If no renderer left add FMapRenderer
				if ( map.getRenderers().size() == 0 )
					map.addRenderer( new FMapRenderer() );

			}
		}
		// *********************************************************
		new File( "plugins" + File.separator + BSPSolutions.getPluginName() + File.separator + "maps" ).mkdirs();
	}




	// Read latest mapId from idcounts.dat
	private short readLatestId() throws IOException {
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
