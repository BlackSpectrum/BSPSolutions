package eu.blackspectrum.bspsolutions.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.util.LocationUtil;
import eu.blackspectrum.bspsolutions.util.RNGUtil;

public class RandomTeleportCommand extends MassiveCommand
{


	public RandomTeleportCommand() {
		this.addAliases( "randomteleport" );

		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasPerm.get( "BSP.admin" ) );

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

		boolean keepTrying = true;

		final int radiusMin = BSPSolutions.config.getInt( "Locations.spawn.radiusMin" ), radiusMax = BSPSolutions.config
				.getInt( "Locations.spawn.radiusMax" );

		int x, y, z, attempts = 0;
		final int centerX = LocationUtil.getCenterOfWorld().getBlockX(), centerZ = LocationUtil.getCenterOfWorld().getBlockZ();

		final World world = LocationUtil.getOverWorld();
		Location location = null;

		while ( keepTrying )
		{
			attempts = 0;
			keepTrying = false;
			x = RNGUtil.randomSignum() * ( radiusMin + (int) ( RNGUtil.nextFloat() * ( radiusMax - radiusMin ) ) );
			z = RNGUtil.randomSignum() * ( radiusMin + (int) ( RNGUtil.nextFloat() * ( radiusMax - radiusMin ) ) );

			x += centerX;
			z += centerZ;

			location = new Location( LocationUtil.getOverWorld(), x, 0, z );

			world.getChunkAt( location ).load();

			y = world.getHighestBlockAt( location ).getY();

			location.setY( y - 1 );
			final int t = world.getBlockTypeIdAt( location );
			if ( y < 63 || t >= 8 && t <= 11 || t == 51 || t == 81 || t == 119 )
			{
				keepTrying = true;
				continue;
			}

			final Faction faction = BoardColls.get().getFactionAt( PS.valueOf( location ) );
			if ( !faction.isNone() )
			{
				keepTrying = true;
				continue;
			}
			if ( ++attempts >= 0.1 * Math.PI * ( Math.pow( radiusMax, 2 ) - Math.pow( radiusMin, 2 ) ) )
				this.sender.sendMessage( "Teleportation failed! Is the teleport area valid?" );
		}

		if ( location != null )
			target.teleport( location );
	}
}
