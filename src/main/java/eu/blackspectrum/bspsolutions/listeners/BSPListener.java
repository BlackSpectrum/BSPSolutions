package eu.blackspectrum.bspsolutions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class BSPListener implements Listener
{


	private boolean	registered	= false;




	public void register() {
		if ( !this.registered )
		{
			Bukkit.getPluginManager().registerEvents( this, BSPSolutions.get() );
			this.registered = true;
		}
	}
}
