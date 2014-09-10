package eu.blackspectrum.bspsolutions.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

import eu.blackspectrum.bspsolutions.plugins.Pic2Map;

public class Pic2MapCommand extends MassiveCommand
{


	public Pic2MapCommand() {
		this.addAliases( "paint" );

		this.addRequirements( ReqIsPlayer.get() );
		this.addRequiredArg( "URL" );
	}




	@Override
	public void perform() {
		final Player target = (Player) this.sender;

		if ( target.getItemInHand().getType().equals( Material.EMPTY_MAP ) )
			Pic2Map.paintMap( this.args.get( 0 ), target );
	}
}
