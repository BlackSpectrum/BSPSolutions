package eu.blackspectrum.bspsolutions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class BSPListener implements Listener
{


	private static BSPListener	instance;
	private boolean				registered	= false;




	public static BSPListener Instance() {
		if ( instance == null )
			instance = new BSPListener();

		return instance;
	}




	public void register() {
		if ( !this.registered )
		{
			Bukkit.getPluginManager().registerEvents( instance, BSPSolutions.Instance() );
			this.registered = true;
		}
	}
}
