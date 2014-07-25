package eu.blackspectrum.bspsolutions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import eu.blackspectrum.bspsolutions.plugins.FalseAccessBlocker;



public class BlockListener implements Listener
{

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event)
	{
		FalseAccessBlocker.onBlockBreack( event );
	}
	
}
