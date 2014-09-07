package eu.blackspectrum.bspsolutions.tasks;

import org.bukkit.Bukkit;

import com.massivecraft.massivecore.ModuloRepeatTask;

import eu.blackspectrum.bspsolutions.Consts;
import eu.blackspectrum.bspsolutions.events.GarbageCollectEvent;

public class GarbageCollectTask extends ModuloRepeatTask
{


	private static GarbageCollectTask	instance	= new GarbageCollectTask();




	public static GarbageCollectTask get() {
		return instance;
	}




	// Run once every 10 minutes
	@Override
	public long getDelayMillis() {
		return Consts.MILIS_IN_MINUTE * 10;
	}




	@Override
	public void invoke( final long now ) {
		Bukkit.getServer().getPluginManager().callEvent( new GarbageCollectEvent() );

	}

}
