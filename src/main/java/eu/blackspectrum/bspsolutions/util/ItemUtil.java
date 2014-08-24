package eu.blackspectrum.bspsolutions.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
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




	public static boolean isLava( final Block block ) {
		return isLava( block.getType() );
	}




	public static boolean isLava( final Material material ) {
		return material == Material.LAVA || material == Material.STATIONARY_LAVA;
	}




	public static boolean isWater( final Block block ) {
		return isWater( block.getType() );
	}




	public static boolean isWater( final Material material ) {
		return material == Material.WATER || material == Material.STATIONARY_WATER;
	}

}
