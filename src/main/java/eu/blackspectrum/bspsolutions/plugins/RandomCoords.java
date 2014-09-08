package eu.blackspectrum.bspsolutions.plugins;

import java.util.HashSet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.injector.GamePhase;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class RandomCoords
{


	public static PacketAdapter.AdapterParameteters getParameters( final boolean server ) {
		final HashSet<PacketType> packets = new HashSet<PacketType>();
		final PacketAdapter.AdapterParameteters ret = PacketAdapter.params();
		ret.plugin( BSPSolutions.get() );
		ret.gamePhase( GamePhase.BOTH );
		if ( server )
		{

			ret.connectionSide( ConnectionSide.SERVER_SIDE );
			ret.listenerPriority( ListenerPriority.HIGHEST );

			packets.add( PacketType.Play.Server.BED );
			packets.add( PacketType.Play.Server.BLOCK_ACTION );
			packets.add( PacketType.Play.Server.BLOCK_BREAK_ANIMATION );
			packets.add( PacketType.Play.Server.BLOCK_CHANGE );
			packets.add( PacketType.Play.Server.MULTI_BLOCK_CHANGE );
			packets.add( PacketType.Play.Server.MAP_CHUNK );
			packets.add( PacketType.Play.Server.MAP_CHUNK_BULK );
			packets.add( PacketType.Play.Server.EXPLOSION );
			packets.add( PacketType.Play.Server.SPAWN_POSITION );

			packets.add( PacketType.Play.Server.RESPAWN );
			packets.add( PacketType.Play.Server.POSITION );

			packets.add( PacketType.Play.Server.WORLD_PARTICLES );
			packets.add( PacketType.Play.Server.WORLD_EVENT );

			packets.add( PacketType.Play.Server.NAMED_SOUND_EFFECT );

			packets.add( PacketType.Play.Server.NAMED_ENTITY_SPAWN );
			packets.add( PacketType.Play.Server.SPAWN_ENTITY_WEATHER );
			packets.add( PacketType.Play.Server.SPAWN_ENTITY );
			packets.add( PacketType.Play.Server.SPAWN_ENTITY_LIVING );
			packets.add( PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB );
			packets.add( PacketType.Play.Server.SPAWN_ENTITY_PAINTING );
			packets.add( PacketType.Play.Server.ENTITY_TELEPORT );

			packets.add( PacketType.Play.Server.UPDATE_SIGN );

			packets.add( PacketType.Play.Server.OPEN_SIGN_ENTITY );
			packets.add( PacketType.Play.Server.TILE_ENTITY_DATA );

		}
		else
		{
			ret.connectionSide( ConnectionSide.CLIENT_SIDE );
			ret.listenerPriority( ListenerPriority.LOWEST );

			packets.add( PacketType.Play.Client.POSITION );
			packets.add( PacketType.Play.Client.POSITION_LOOK );
			packets.add( PacketType.Play.Client.BLOCK_DIG );
			packets.add( PacketType.Play.Client.BLOCK_PLACE );
			packets.add( PacketType.Play.Client.UPDATE_SIGN );
		}

		ret.types( packets );
		return ret;
	}
}
