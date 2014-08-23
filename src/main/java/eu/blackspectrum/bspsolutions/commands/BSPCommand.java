package eu.blackspectrum.bspsolutions.commands;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class BSPCommand extends MassiveCommand
{


	private final BSPReloadCommand	bspReloadCommand	= new BSPReloadCommand();




	public BSPCommand() {
		this.addAliases( "bsp" );

		this.addSubCommand( this.bspReloadCommand );

		this.addRequirements( ReqHasPerm.get( "BSP.admin" ) );

		this.setVisibilityMode( VisibilityMode.SECRET );
	}
}
