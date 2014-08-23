package eu.blackspectrum.bspsolutions.util;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class ItemUtil
{


	public static void dropItemNaturally( final Entity e, final ItemStack i ) {
		if ( i != null && i.getType() != Material.AIR )
			e.getLocation().getWorld().dropItemNaturally( e.getLocation(), i );
	}




	public static void dropItemNaturally( final Entity e, final MaterialData m, final int amount ) {
		dropItemNaturally( e, m.toItemStack( amount ) );
	}

}
