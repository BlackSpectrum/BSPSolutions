package eu.blackspectrum.bspsolutions.commands;

import java.util.List;

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
		{
			final List<Player> players = BSPSolutions.getPlayers( this.arg( 0 ) );
			if ( players.isEmpty() )
			{
				this.sendMessage( "No player found for \"" + this.arg( 0 ) + "\"" );
				return;
			}

			else if ( players.size() > 1 )
			{
				this.sendMessage( "Multiple players found for \"" + this.arg( 0 ) + "\"" );
				return;
			}
			target = players.get( 0 );
		}
		else
			target = (Player) this.sender;

		BSPPlayer.get( target ).freeFromPurgatory();
	}
}
