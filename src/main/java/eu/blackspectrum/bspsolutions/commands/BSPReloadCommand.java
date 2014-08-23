package eu.blackspectrum.bspsolutions.commands;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import eu.blackspectrum.bspsolutions.BSPSolutions;



public class BSPReloadCommand extends MassiveCommand
{
	
	public BSPReloadCommand() {
		this.addAliases( "reload" );

		this.addRequirements( ReqHasPerm.get( "BSP.admin" ) );
	}




	@Override
	public void perform() {
		BSPSolutions.instance.onDisable();
		BSPSolutions.instance.onEnable();
		
		sender.sendMessage( "BSPSolutions reloaded!" );
	}

}
