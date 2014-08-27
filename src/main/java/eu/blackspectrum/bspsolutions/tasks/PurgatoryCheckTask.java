package eu.blackspectrum.bspsolutions.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.blackspectrum.bspsolutions.entities.BSPPlayer;

public class PurgatoryCheckTask implements Runnable
{


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
