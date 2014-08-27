package eu.blackspectrum.bspsolutions.tasks;

import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class TeleportTask extends BukkitRunnable
{


	private final BSPPlayer	player;
	private int				timer;




	public TeleportTask(final BSPPlayer player) {
		this.player = player;
		this.timer = BSPSolutions.getConfig2().getInt( "CompassTP.timerLength", 5 );
	}




	@Override
	public void run() {
		if ( !this.player.isTeleporting() )
		{
			this.cancel();
			return;
		}

		if ( !LocationUtil.isCloseToCenter( this.player.getPlayer() ) )
		{

			this.player.abortTeleport();
			this.cancel();
			return;
		}
		if ( this.timer > 0 )
			this.player.sendMessage( "Teleporting in " + this.timer-- + " seconds. Use again to cancel." );
		else
		{
			this.player.getPlayer().teleport( LocationUtil.getSpawnWorld().getSpawnLocation(), TeleportCause.PLUGIN );
			this.player.sendMessage( BSPSolutions.getConfig2().getString( "CompassTP.successMessage" ) );
			this.player.setTeleporting( false );
			this.cancel();
		}
	}

}
