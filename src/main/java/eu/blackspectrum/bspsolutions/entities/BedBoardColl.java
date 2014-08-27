package eu.blackspectrum.bspsolutions.entities;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.util.MUtil;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class BedBoardColl extends Coll<BedBoard> implements IBedBoard
{


	public BedBoardColl(String name) {
		super( name, BedBoard.class, MStore.getDb(),BSPSolutions.get(), false, true, true );
	}


	@Override
	public String fixId(Object oid)
	{
		if (oid == null) return null;
		if (oid instanceof String) return (String)oid;
		if (oid instanceof BedBoard) return this.getId(oid);
		
		return MUtil.extract(String.class, "worldName", oid);
	}

	@Override
	public BSPBed getBedAt( PS ps ) {
		if ( ps == null )
			return null;

		BedBoard board = this.get( ps.getWorld() );

		if ( board == null )
			return null;

		return board.getBedAt( ps );
	}




	@Override
	public void setBedAt( PS ps, BSPBed bed ) {
		if ( ps == null )
			return;

		BedBoard board = this.get( ps.getWorld() );

		if ( board == null )
			return;

		board.setBedAt( ps, bed );

	}


	@Override
	public void removeBed( BSPBed bed ) {
		if ( bed == null )
			return;

		BedBoard board = this.get( bed.getLocation().getWorld() );

		if ( board == null )
			return;

		board.removeBed( bed );
		
	}


	@Override
	public void removeBedAt( PS ps ) {
		if ( ps == null )
			return;

		BedBoard board = this.get( ps.getWorld() );

		if ( board == null )
			return;

		board.removeBedAt( ps );
		
	}

}
