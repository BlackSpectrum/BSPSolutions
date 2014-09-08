package eu.blackspectrum.bspsolutions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import eu.blackspectrum.bspsolutions.plugins.FalseAccessBlocker;
import eu.blackspectrum.bspsolutions.plugins.SpawnBed;

public class BlockListener extends BSPListener
{


	private static BlockListener	instance	= new BlockListener();




	public static BSPListener get() {
		return instance;
	}




	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak( final BlockBreakEvent event ) {
		FalseAccessBlocker.onBlockBreackCancelled( event );

		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		SpawnBed.onBlockBreak( event );
	}




	@EventHandler
	public void onBlockFromTo( final BlockFromToEvent event ) {
		// ************************
		// Ignore cancelled
		// ************************
		if ( event.isCancelled() )
			return;

		SpawnBed.onLiquidFromTo( event );
	}




	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace( final BlockPlaceEvent event ) {
		FalseAccessBlocker.onBlockCancelledInAir( event );
	}
}
