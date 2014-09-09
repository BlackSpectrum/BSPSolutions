package eu.blackspectrum.bspsolutions.commands;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class PurgatoryCommand extends MassiveCommand
{


	private final PurgatoryFreeCommand	purgatoryFreeCommand	= new PurgatoryFreeCommand();




	public PurgatoryCommand() {
		this.addAliases( "pur" );

		this.addSubCommand( this.purgatoryFreeCommand );

		this.addRequirements( ReqHasPerm.get( "BSP.purgatory" ) );

		this.setVisibilityMode( VisibilityMode.SECRET );
	}
}
