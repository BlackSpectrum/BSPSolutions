package eu.blackspectrum.bspsolutions.entities;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.util.MUtil;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class BedBoardColl extends Coll<BedBoard> implements IBedBoard
{


	private static BedBoardColl	i	= new BedBoardColl();




	public static BedBoardColl get() {
		return i;
	}




	public BedBoardColl() {
		super( "bsp_bedboard", BedBoard.class, MStore.getDb(), BSPSolutions.get(), false, true, true );
	}




	@Override
	public String fixId( final Object oid ) {
		if ( oid == null )
			return null;
		if ( oid instanceof String )
			return (String) oid;
		if ( oid instanceof BedBoard )
			return this.getId( oid );

		return MUtil.extract( String.class, "worldName", oid );
	}




	@Override
	public BSPBed getBedAt( final PS ps ) {
		if ( ps == null )
			return null;

		final BedBoard board = this.get( ps.getWorld() );

		if ( board == null )
			return null;

		return board.getBedAt( ps );
	}




	@Override
	public void removeBed( final BSPBed bed ) {
		if ( bed == null )
			return;

		final BedBoard board = this.get( bed.getLocation().getWorld() );

		if ( board == null )
			return;

		board.removeBed( bed );

	}




	@Override
	public void removeBedAt( final PS ps ) {
		if ( ps == null )
			return;

		final BedBoard board = this.get( ps.getWorld() );

		if ( board == null )
			return;

		board.removeBedAt( ps );

	}




	@Override
	public void setBedAt( final PS ps, final BSPBed bed ) {
		if ( ps == null )
			return;

		final BedBoard board = this.get( ps.getWorld() );

		if ( board == null )
			return;

		board.setBedAt( ps, bed );

	}

}
