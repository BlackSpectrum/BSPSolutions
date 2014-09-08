package eu.blackspectrum.bspsolutions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.MapInitializeEvent;

import eu.blackspectrum.bspsolutions.FMaps;
import eu.blackspectrum.bspsolutions.events.GarbageCollectEvent;
import eu.blackspectrum.bspsolutions.plugins.FMapPlugin;

public class MiscListener extends BSPListener
{


	private static MiscListener	instance	= new MiscListener();




	public static BSPListener get() {

		return instance;
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onGarbageCollect( final GarbageCollectEvent event ) {
		FMaps.get().collectGarbage();
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onMapInitialize( final MapInitializeEvent event ) {
		FMapPlugin.onMapInitialize( event );
	}

}
