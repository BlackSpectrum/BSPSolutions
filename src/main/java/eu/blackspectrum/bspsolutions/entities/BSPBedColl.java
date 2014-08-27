package eu.blackspectrum.bspsolutions.entities;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import eu.blackspectrum.bspsolutions.BSPSolutions;



public class BSPBedColl extends Coll<BSPBed>
{
	

	public BSPBedColl(String name) {
		super(name, BSPBed.class, MStore.getDb(), BSPSolutions.get());
	}

}
