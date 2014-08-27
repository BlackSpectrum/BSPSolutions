package eu.blackspectrum.bspsolutions.entities;

import com.massivecraft.factions.entity.XColls;
import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class BedBoardColls extends XColls<BedBoardColl, BedBoard> implements IBedBoard
{


	private static BedBoardColls	i	= new BedBoardColls();




	public static BedBoardColls get() {
		return i;
	}




	@Override
	public BedBoardColl createColl( String collName ) {
		return new BedBoardColl( collName );
	}




	@Override
	public Aspect getAspect() {
		return BSPSolutions.get().getAspect();
	}




	@Override
	public String getBasename() {
		return "bsp_bedboard";
	}




	@Override
	public BSPBed getBedAt( PS ps ) {
		BedBoardColl coll = this.getForWorld( ps.getWorld() );
		if ( coll == null )
			return null;
		return coll.getBedAt( ps );
	}




	@Override
	public void setBedAt( PS ps, BSPBed bed ) {
		BedBoardColl coll = this.getForWorld( ps.getWorld() );
		if ( coll == null )
			return;

		coll.setBedAt( ps, bed );
	}




	@Override
	public void removeBed( BSPBed bed ) {
		if ( bed == null )
			return;

		BedBoardColl coll = this.getForWorld( bed.getLocation().getWorld() );
		if ( coll == null )
			return;

		coll.removeBed( bed );

	}




	@Override
	public void removeBedAt( PS ps ) {
		if ( ps == null )
			return;

		BedBoardColl coll = this.getForWorld( ps.getWorld() );
		if ( coll == null )
			return;

		coll.removeBedAt( ps );

	}

}
