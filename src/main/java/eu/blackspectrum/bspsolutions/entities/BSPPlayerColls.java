package eu.blackspectrum.bspsolutions.entities;

import com.massivecraft.factions.entity.XColls;
import com.massivecraft.massivecore.Aspect;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class BSPPlayerColls extends XColls<BSPPlayerColl, BSPPlayer>
{


	private static BSPPlayerColls	i	= new BSPPlayerColls();




	public static BSPPlayerColls get() {
		return i;
	}




	@Override
	public BSPPlayerColl createColl( final String name ) {
		return new BSPPlayerColl( name );
	}




	@Override
	public Aspect getAspect() {
		return BSPSolutions.Instance().getAspect();
	}




	@Override
	public String getBasename() {
		return "bsp_player";
	}
}
