package eu.blackspectrum.bspsolutions.entities;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class BSPBedColl extends Coll<BSPBed>
{


	private static BSPBedColl	i	= new BSPBedColl();




	public static BSPBedColl get() {
		return i;
	}




	public BSPBedColl() {
		super( "bsp_bed", BSPBed.class, MStore.getDb(), BSPSolutions.get() );
	}

}
