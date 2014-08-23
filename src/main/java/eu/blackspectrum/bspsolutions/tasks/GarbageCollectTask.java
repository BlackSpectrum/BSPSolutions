package eu.blackspectrum.bspsolutions.tasks;

import org.bukkit.Bukkit;

import eu.blackspectrum.bspsolutions.events.GarbageCollectEvent;

public class GarbageCollectTask implements Runnable
{


	@Override
	public void run() {
		Bukkit.getServer().getPluginManager().callEvent( new GarbageCollectEvent() );
		System.gc();
	}

}
