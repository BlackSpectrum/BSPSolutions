package eu.blackspectrum.bspsolutions.tasks;

import eu.blackspectrum.bspsolutions.Purgatory;

public class PurgatoryCheckTask implements Runnable
{


	@Override
	public void run() {
		Purgatory.Instance().checkPlayers();
	}

}
