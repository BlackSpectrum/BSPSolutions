package eu.blackspectrum.bspsolutions.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.util.LocationUtil;
import eu.blackspectrum.bspsolutions.util.RNGUtil;

public class RandomTeleportCommand extends MassiveCommand
{


	public RandomTeleportCommand() {
		this.addAliases( "randomteleport" );
		this.addAliases( "rtp" );

		this.addRequirements( ReqHasPerm.get( "BSP.admin" ) );

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
			if(sender instanceof Player)
				target = (Player) this.sender;
			else
			{
				this.sendMessage( "Please specify a player." );
				return;
			}
		final int radiusMin = BSPSolutions.getConfig2().getInt( "Locations.spawn.radiusMin" ), radiusMax = BSPSolutions.getConfig2()
				.getInt( "Locations.spawn.radiusMax" );

		int x, y, z;
		final int centerX = LocationUtil.getCenterOfWorld().getBlockX(), centerZ = LocationUtil.getCenterOfWorld().getBlockZ();

		final World world = LocationUtil.getOverWorld();
		Location location = null;

		while ( true )
		{

			x = RNGUtil.randomSignum() * ( radiusMin + (int) ( RNGUtil.nextFloat() * ( radiusMax - radiusMin ) ) );
			z = RNGUtil.randomSignum() * ( radiusMin + (int) ( RNGUtil.nextFloat() * ( radiusMax - radiusMin ) ) );

			x += centerX;
			z += centerZ;

			location = new Location( LocationUtil.getOverWorld(), x, 0, z );

			y = world.getHighestBlockAt( location ).getY();

			location.setY( y );

			if ( y < 63 || !LocationUtil.isLocationSafe( location ) )
				continue;

			final Faction faction = BoardColls.get().getFactionAt( PS.valueOf( location ) );
			if ( !faction.isNone() )
				continue;

			break;
		}

		if ( location != null )
		{
			location.getChunk().load();
			target.teleport( location, TeleportCause.PLUGIN );
		}
	}
}
