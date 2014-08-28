package eu.blackspectrum.bspsolutions.tasks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;

public class PurgatoryCheckTask implements Runnable
{


	private static PurgatoryCheckTask	instance;
	private boolean						isScheduled	= false;




	public static PurgatoryCheckTask get() {
		if ( instance == null )
			instance = new PurgatoryCheckTask();

		return instance;
	}




	private PurgatoryCheckTask() {
	}




	@Override
	public void run() {
		for ( final Player p : Bukkit.getOnlinePlayers() )
		{
			if(p.getGameMode() == GameMode.CREATIVE)
				continue;
			
			final BSPPlayer player = BSPPlayer.get( p );
			if ( player.isInPurgatory() && player.canLeavePurgatory() )
				player.freeFromPurgatory();
		}
	}




	public void schedule( final int interval ) {
		if ( !this.isScheduled )
		{
			Bukkit.getScheduler().scheduleSyncRepeatingTask( BSPSolutions.get(), this, interval, interval );
			this.isScheduled = true;
		}
	}

}
