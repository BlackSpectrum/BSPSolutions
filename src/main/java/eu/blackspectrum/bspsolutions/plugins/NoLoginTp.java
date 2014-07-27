package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class NoLoginTp
{


	public static void onPlayerJoin( final PlayerJoinEvent event ) {
		if ( event.getPlayer().hasMetadata( "logOutPos" ) )
		{
			Location loc = null;
			for ( final MetadataValue meta : event.getPlayer().getMetadata( "logOutPos" ) )
				if ( meta.getOwningPlugin().equals( BSPSolutions.instance ) )
					loc = (Location) meta.value();

			if ( !loc.equals( event.getPlayer().getLocation() ) )
				event.getPlayer().teleport( loc );
			event.getPlayer().removeMetadata( "logOutPos", BSPSolutions.instance );

		}

	}




	public static void onPlayerLogout( final PlayerQuitEvent event ) {
		event.getPlayer().setMetadata( "logOutPos", new FixedMetadataValue( BSPSolutions.instance, event.getPlayer().getLocation() ) );
	}
}
