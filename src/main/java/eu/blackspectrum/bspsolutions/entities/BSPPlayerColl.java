package eu.blackspectrum.bspsolutions.entities;

import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.store.SenderColl;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class BSPPlayerColl extends SenderColl<BSPPlayer>
{


	public BSPPlayerColl(final String name) {
		super( name, BSPPlayer.class, MStore.getDb(), BSPSolutions.get() );
	}

}
