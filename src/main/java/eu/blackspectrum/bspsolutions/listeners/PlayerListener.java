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

import eu.blackspectrum.bspsolutions.plugins.AbandonPet;
import eu.blackspectrum.bspsolutions.plugins.BetterLeashes;
import eu.blackspectrum.bspsolutions.plugins.CompassTeleport;
import eu.blackspectrum.bspsolutions.plugins.DieSilent;
import eu.blackspectrum.bspsolutions.plugins.DropAll;
import eu.blackspectrum.bspsolutions.plugins.FMapPlugin;
import eu.blackspectrum.bspsolutions.plugins.FalseAccessBlocker;
import eu.blackspectrum.bspsolutions.plugins.FishFix;
import eu.blackspectrum.bspsolutions.plugins.ForceOpen;
import eu.blackspectrum.bspsolutions.plugins.Purgatory;
import eu.blackspectrum.bspsolutions.plugins.SaferSafeZones;
import eu.blackspectrum.bspsolutions.util.FactionsUtil;

public class PlayerListener extends BSPListener
{


	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChangedWorld( final PlayerChangedWorldEvent event ) {
		CompassTeleport.onWorldChange( event );
		SaferSafeZones.onPlayerChangedWorld( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath( final PlayerDeathEvent event ) {
		DieSilent.onPlayerDeath( event );
		DropAll.onPlayerDeath( event );
		Purgatory.onPlayerDie( event );
	}




	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerFish( final PlayerFishEvent event ) {
		FishFix.onPlayerCatchFish( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract( final PlayerInteractEvent event ) {
		CompassTeleport.onRightClick( event );
		FMapPlugin.onPlayerRightClickMap( event );
		FalseAccessBlocker.onPlayerInteractBlockCancelled( event );
		SaferSafeZones.onPlayerUseItem( event );
		ForceOpen.onOpenChest( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEntity( final PlayerInteractEntityEvent event ) {
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
		SaferSafeZones.onPlayerChunkMove( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit( final PlayerQuitEvent event ) {

		FactionsUtil.addFaction( event.getPlayer() );
	}




	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn( final PlayerRespawnEvent event ) {
		Purgatory.onPlayerRespawn( event );
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerUnleashEntity( final PlayerUnleashEntityEvent event ) {
		BetterLeashes.onUnleash( event );
	}

}
