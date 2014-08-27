package eu.blackspectrum.bspsolutions.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;

public class PurgatoryCheckTask implements Runnable
{

	private static PurgatoryCheckTask instance;
	private boolean isScheduled = false;
	
	public static PurgatoryCheckTask get()
	{
		if(instance == null)
			instance = new PurgatoryCheckTask();
		
		return instance;
	}
	
	private PurgatoryCheckTask() {	}
	
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
		for ( final Player p : Bukkit.getOnlinePlayers() )
		{
			final BSPPlayer player = BSPPlayer.get( p );
			if ( player.isInPurgatory() && player.canLeavePurgatory() )
				player.freeFromPurgatory();
		}
	}

}
