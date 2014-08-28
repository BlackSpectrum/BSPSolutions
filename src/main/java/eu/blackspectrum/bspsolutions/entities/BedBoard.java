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




	public BedBoard() {
		this.map = new ConcurrentSkipListMap<PS, String>();
	}




	public BedBoard(final Map<PS, String> map) {
		this.map = new ConcurrentSkipListMap<PS, String>( map );
	}




	@Override
	public BSPBed getBedAt( PS ps ) {
		if ( ps == null )
			return null;
		ps = ps.getBlockCoords( true );

		final BSPBed ret = BSPBed.get( this.map.get( ps ) );

		return ret;
	}




	public Map<PS, String> getMap() {
		return Collections.unmodifiableMap( this.map );
	}




	public PS getPSForBed( final BSPBed bed ) {
		for ( final Entry<PS, String> e : this.map.entrySet() )
			if ( e.getValue().equals( bed.getId() ) )
				return e.getKey();

		return null;
	}




	@Override
	public boolean isDefault() {
		if ( this.map == null )
			return true;
		if ( this.map.isEmpty() )
			return true;
		return false;
	}




	@Override
	public BedBoard load( final BedBoard that ) {
		this.map = that.map;

		return this;
	}




	@Override
	public void removeBed( final BSPBed bed ) {
		this.removeBedAt( this.getPSForBed( bed ) );
	}




	@Override
	public void removeBedAt( final PS ps ) {
		if ( ps == null )
			return;

		final BSPBed bed = this.getBedAt( ps );

		if ( bed != null )
		{
			bed.getOwner().setBed( null );
			bed.detach();
			this.map.remove( ps );
		}

	}




	@Override
	public void setBedAt( PS ps, final BSPBed bed ) {

		ps = ps.getBlockCoords( true );

		if ( bed == null )
			this.removeBedAt( ps );
		else
			this.map.put( ps, bed.getId() );

	}

}
