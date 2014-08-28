package eu.blackspectrum.bspsolutions.entities;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

public class BedBoard extends Entity<BedBoard> implements IBedBoard
{


	public static final transient Type			MAP_TYPE	= new TypeToken<Map<PS, String>>() {
															}.getType();

	private ConcurrentSkipListMap<PS, String>	map;




	@Override
	public BedBoard load( BedBoard that ) {
		this.map = that.map;

		return this;
	}




	@Override
	public boolean isDefault() {
		if ( this.map == null )
			return true;
		if ( this.map.isEmpty() )
			return true;
		return false;
	}




	public BedBoard() {
		this.map = new ConcurrentSkipListMap<PS, String>();
	}




	public BedBoard(Map<PS, String> map) {
		this.map = new ConcurrentSkipListMap<PS, String>( map );
	}




	public Map<PS, String> getMap() {
		return Collections.unmodifiableMap( this.map );
	}




	@Override
	public BSPBed getBedAt( PS ps ) {
		if ( ps == null )
			return null;
		ps = ps.getBlockCoords( true );

		BSPBed ret = BSPBed.get( map.get( ps ), this );

		return ret;
	}




	public PS getPSForBed( BSPBed bed ) {
		for ( Entry<PS, String> e : map.entrySet() )
		{
			if ( e.getValue().equals( bed.getId() ) )
				return e.getKey();
		}

		return null;
	}




	@Override
	public void setBedAt( PS ps, BSPBed bed ) {

		ps = ps.getBlockCoords( true );

		if ( bed == null )
			removeBedAt( ps );
		else
			map.put( ps, bed.getId() );

	}




	@Override
	public void removeBed( BSPBed bed ) {
		removeBedAt( getPSForBed( bed ) );
	}




	@Override
	public void removeBedAt( PS ps ) {
		if(ps == null)
			return;
		
		BSPBed bed = getBedAt( ps );
		
		if(bed != null)
		{
			bed.getOwner().setBed( null );
			bed.detach();
			map.remove( ps );
		}
		
	}

}
