package eu.blackspectrum.bspsolutions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import eu.blackspectrum.bspsolutions.plugins.AbandonPet;
import eu.blackspectrum.bspsolutions.plugins.DropAll;
import eu.blackspectrum.bspsolutions.plugins.SaferSafeZones;

public class EntityListener implements Listener
{


	@EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureSpawn( CreatureSpawnEvent event ) {
		AbandonPet.onPetBreed( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath( EntityDeathEvent event ) {
		DropAll.onEnderManDeath( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamaged( EntityDamageEvent event ) {
		SaferSafeZones.onPlayerDamaged( event );

	}
}
