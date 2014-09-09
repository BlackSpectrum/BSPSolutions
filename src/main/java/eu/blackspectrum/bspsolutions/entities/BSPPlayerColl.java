package eu.blackspectrum.bspsolutions.entities;

import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.store.SenderColl;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class BSPPlayerColl extends SenderColl<BSPPlayer>
{


	private static BSPPlayerColl	i	= new BSPPlayerColl();




	public static BSPPlayerColl get() {
		return i;
	}


	public BSPPlayerColl() {
		super( "bsp_player", BSPPlayer.class, MStore.getDb(), BSPSolutions.get());
	}

}
