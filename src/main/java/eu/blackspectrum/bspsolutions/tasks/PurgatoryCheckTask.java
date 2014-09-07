package eu.blackspectrum.bspsolutions.tasks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.ModuloRepeatTask;

import eu.blackspectrum.bspsolutions.Consts;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;

public class PurgatoryCheckTask extends ModuloRepeatTask
{


	private static PurgatoryCheckTask	instance	= new PurgatoryCheckTask();




	public static PurgatoryCheckTask get() {
		return instance;
	}




	// Run once a minute
	@Override
	public long getDelayMillis() {
		return Consts.MILIS_IN_MINUTE;
	}




	@Override
	public void invoke( final long now ) {
		for ( final Player p : Bukkit.getOnlinePlayers() )
		{
			if ( p.getGameMode() == GameMode.CREATIVE )
				continue;

			final BSPPlayer player = BSPPlayer.get( p );
			if ( player.isInPurgatory() && player.canLeavePurgatory() )
				player.freeFromPurgatory();
		}

	}

}
