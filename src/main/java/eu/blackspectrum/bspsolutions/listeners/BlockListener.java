package eu.blackspectrum.bspsolutions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import eu.blackspectrum.bspsolutions.plugins.FalseAccessBlocker;

public class BlockListener extends BSPListener
{


	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak( final BlockBreakEvent event ) {
		FalseAccessBlocker.onBlockBreackCancelled( event );
	}




	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace( final BlockPlaceEvent event ) {
		FalseAccessBlocker.onBlockCancelledInAir( event );
	}
}
