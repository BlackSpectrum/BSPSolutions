package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Material;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class AbandonPet
{


	public static void onPetBreed( final CreatureSpawnEvent event ) {
		if ( event.getEntity() instanceof Tameable && event.getSpawnReason() == SpawnReason.BREEDING )
			( (Tameable) event.getEntity() ).setTamed( false );
	}




	public static void onPetHit( final PlayerInteractEntityEvent event ) {
		if ( event.getRightClicked() instanceof Tameable && event.getPlayer().getItemInHand().getType() == Material.STICK )
			if ( ( (Tameable) event.getRightClicked() ).getOwner().equals( event.getPlayer() ) )
				( (Tameable) event.getRightClicked() ).setTamed( false );
	}
}
