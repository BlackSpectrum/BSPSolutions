package eu.blackspectrum.bspsolutions.listeners;

import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.plugins.AbandonPet;
import eu.blackspectrum.bspsolutions.plugins.CompassTeleport;
import eu.blackspectrum.bspsolutions.plugins.DropAll;
import eu.blackspectrum.bspsolutions.plugins.SaferSafeZones;
import eu.blackspectrum.bspsolutions.plugins.SpawnBed;
import eu.blackspectrum.bspsolutions.plugins.SpawnSafe;
import eu.blackspectrum.bspsolutions.util.FactionsUtil;

public class EntityListener extends BSPListener
{


	private static EntityListener	instance;




	public static BSPListener get() {
		if ( instance == null )
			instance = new EntityListener();

		return instance;
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureSpawn( final CreatureSpawnEvent event ) {
		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		AbandonPet.onPetBreed( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamaged( final EntityDamageEvent event ) {
		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		SaferSafeZones.onPlayerDamaged( event );
		CompassTeleport.onPlayerHurt( event );
		SpawnSafe.onEntityDamage( event, null );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamagedByEntity( final EntityDamageByEntityEvent event ) {
		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		SpawnSafe.onEntityDamageByEntity( event, null );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath( final EntityDeathEvent event ) {
		DropAll.onEnderManDeath( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityExplode( final EntityExplodeEvent event ) {
		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		if ( event.getEntityType() != null && event.getEntityType() == EntityType.CREEPER )
		{
			final Iterator<Block> it = event.blockList().iterator();
			while ( it.hasNext() )
			{
				final Block block = it.next();

				if ( FactionsUtil.isFactionOffline( PS.valueOf( block ) ) )
					it.remove();
			}
		}

		SpawnBed.onEntityExplode( event );
	}
}
