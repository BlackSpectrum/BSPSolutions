package eu.blackspectrum.bspsolutions.commands;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;

public class PurgatoryFreeCommand extends MassiveCommand
{


	public PurgatoryFreeCommand() {
		this.addAliases( "free" );

		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasPerm.get( "BSP.purgatory" ) );

		this.addOptionalArg( "player", "you" );
	}




	@Override
	public void perform() {

		Player target = null;

		if ( this.arg( 0 ) != null && !this.arg( 0 ).trim().isEmpty() )
			target = BSPSolutions.getPlayer( this.arg( 0 ) );
		else
			target = (Player) this.sender;

		if ( target == null )
		{
			this.sendMessage( "None or multiple Players found for that name" );
			return;
		}

		BSPPlayer.get( target ).freeFromPurgatory();
	}
}
