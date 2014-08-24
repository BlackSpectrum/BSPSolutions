package eu.blackspectrum.bspsolutions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;

import eu.blackspectrum.bspsolutions.FMaps;
import eu.blackspectrum.bspsolutions.events.GarbageCollectEvent;
import eu.blackspectrum.bspsolutions.plugins.FMapPlugin;

public class MiscEventListener implements Listener
{


	@EventHandler(priority = EventPriority.NORMAL)
	public void onGarbageCollect( final GarbageCollectEvent event ) {
		FMaps.Instance().collectGarbage();
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onMapInitialize( final MapInitializeEvent event ) {
		FMapPlugin.onMapInitialize( event );
	}

}
