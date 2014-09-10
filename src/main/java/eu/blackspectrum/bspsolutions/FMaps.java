package eu.blackspectrum.bspsolutions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import eu.blackspectrum.bspsolutions.entities.FMap;

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
}
