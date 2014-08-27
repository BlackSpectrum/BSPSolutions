package eu.blackspectrum.bspsolutions.entities;

import com.massivecraft.factions.entity.XColls;
import com.massivecraft.massivecore.Aspect;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class BSPBedColls extends XColls<BSPBedColl, BSPBed>
{


	private static BSPBedColls	i	= new BSPBedColls();




	public static BSPBedColls get() {
		return i;
	}




	@Override
	public Aspect getAspect() {
		return BSPSolutions.get().getAspect();
	}




	@Override
	public String getBasename() {
		return "bsp_beds";
	}




	@Override
	public BSPBedColl createColl( String name ) {
		return new BSPBedColl( name );
	}

}
