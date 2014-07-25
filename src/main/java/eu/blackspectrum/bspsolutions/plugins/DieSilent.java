package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.event.entity.PlayerDeathEvent;



public class DieSilent
{
	public static void onPlayerDeath( final PlayerDeathEvent event ) {


			event.setDeathMessage( null );
		
	}
}
