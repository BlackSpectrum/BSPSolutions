package eu.blackspectrum.bspsolutions.util;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.nbt.NbtBase;

public class PacketUtil
{


	public static PacketContainer cloneTileEntityData( final PacketContainer packet ) {
		final PacketContainer newPacket = new PacketContainer( packet.getType() );

		int i = 0;
		for ( final Object obj : packet.getModifier().getValues() )
		{
			newPacket.getModifier().write( i, obj );
			i++;
		}

		i = 0;
		for ( final NbtBase<?> obj : packet.getNbtModifier().getValues() )
		{
			newPacket.getNbtModifier().write( i, obj.deepClone() );
			i++;
		}

		return newPacket;
	}
}
