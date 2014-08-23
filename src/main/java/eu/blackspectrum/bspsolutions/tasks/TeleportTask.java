package eu.blackspectrum.bspsolutions.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.TeleportingPlayers;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class TeleportTask extends BukkitRunnable
{


	private final Player	player;
	private int				timer;




	public TeleportTask(final Player player) {
		this.player = player;
		this.timer = BSPSolutions.config.getInt( "CompassTP.timerLength", 5 );
	}




	@Override
	public void run() {
		if ( !TeleportingPlayers.Instance().isTeleporting( this.player ) )
		{
			this.cancel();
			return;
		}

		if ( !LocationUtil.isCloseToCenter( this.player ) )
		{

			TeleportingPlayers.Instance().abortTeleport( this.player );
			this.cancel();
			return;
		}
		if ( this.timer > 0 )
			this.player.sendMessage( "Teleporting in " + this.timer-- + " seconds. Use again to cancel." );
		else
		{
			this.player.teleport( LocationUtil.getSpawnWorld().getSpawnLocation() );
			this.player.sendMessage( BSPSolutions.config.getString( "CompassTP.successMessage" ) );
			TeleportingPlayers.Instance().removePlayer( this.player );
			this.cancel();
		}
	}

}
