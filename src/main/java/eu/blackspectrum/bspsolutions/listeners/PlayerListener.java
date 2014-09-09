package eu.blackspectrum.bspsolutions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

import eu.blackspectrum.bspsolutions.entities.BSPPlayer;
import eu.blackspectrum.bspsolutions.plugins.AbandonPet;
import eu.blackspectrum.bspsolutions.plugins.BetterLeashes;
import eu.blackspectrum.bspsolutions.plugins.CompassTeleport;
import eu.blackspectrum.bspsolutions.plugins.DropAll;
import eu.blackspectrum.bspsolutions.plugins.FMapPlugin;
import eu.blackspectrum.bspsolutions.plugins.FalseAccessBlocker;
import eu.blackspectrum.bspsolutions.plugins.FishFix;
import eu.blackspectrum.bspsolutions.plugins.ForceOpen;
import eu.blackspectrum.bspsolutions.plugins.Purgatory;
import eu.blackspectrum.bspsolutions.plugins.SaferSafeZones;
import eu.blackspectrum.bspsolutions.plugins.SpawnBed;
import eu.blackspectrum.bspsolutions.plugins.SpawnSafe;
import eu.blackspectrum.bspsolutions.util.FactionsUtil;

public class PlayerListener extends BSPListener
{


	private static PlayerListener	instance	= new PlayerListener();




	public static BSPListener get() {
		return instance;
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChangedWorld( final PlayerChangedWorldEvent event ) {
		final BSPPlayer bspPlayer = BSPPlayer.get( event.getPlayer() );

		CompassTeleport.onWorldChange( event );
		SaferSafeZones.onPlayerChangedWorld( event );
		SpawnSafe.onPlayerChangedWorld( event, bspPlayer );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath( final PlayerDeathEvent event ) {
		DropAll.onPlayerDeath( event );
		Purgatory.onPlayerDie( event );
	}




	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerFish( final PlayerFishEvent event ) {
		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		FishFix.onPlayerCatchFish( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract( final PlayerInteractEvent event ) {
		FalseAccessBlocker.onPlayerInteractBlockCancelled( event );
		CompassTeleport.onRightClick( event );
		FMapPlugin.onPlayerRightClickMap( event );
		SaferSafeZones.onPlayerUseItem( event );
		SpawnBed.onPlayerClickedBed( event );

		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		ForceOpen.onOpenChest( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEntity( final PlayerInteractEntityEvent event ) {
		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		AbandonPet.onPetHit( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin( final PlayerJoinEvent event ) {
		CompassTeleport.onPlayerJoin( event );
		SaferSafeZones.onPlayerJoin( event );
		FactionsUtil.removeFaction( event.getPlayer() );
		Purgatory.onPlayerJoin( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove( final PlayerMoveEvent event ) {
		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		SaferSafeZones.onPlayerChunkMove( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit( final PlayerQuitEvent event ) {
		FactionsUtil.addFaction( event.getPlayer() );
		
		BSPPlayer player = BSPPlayer.get( event.getPlayer() );
		
		if(player.isDefault())
			player.detach();
		else
			player.collapse();
	}




	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn( final PlayerRespawnEvent event ) {
		Purgatory.onPlayerRespawn( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerUnleashEntity( final PlayerUnleashEntityEvent event ) {
		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		BetterLeashes.onUnleash( event );
	}

}
