package eu.blackspectrum.bspsolutions.tasks;

import org.bukkit.Bukkit;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.events.GarbageCollectEvent;

public class GarbageCollectTask implements Runnable
{
	private static GarbageCollectTask instance;
	private boolean isScheduled = false;
	
	public static GarbageCollectTask get()
	{
		if(instance == null)
			instance = new GarbageCollectTask();
		
		return instance;
	}
	
	private GarbageCollectTask() {	}
	
	public void schedule(int interval)
	{
		if(!isScheduled)
		{
			Bukkit.getScheduler().scheduleSyncRepeatingTask( BSPSolutions.get(), this, interval, interval );
			isScheduled = true;
		}
	}

	@Override
	public void run() {
		Bukkit.getServer().getPluginManager().callEvent( new GarbageCollectEvent() );
		System.gc();
	}

}
