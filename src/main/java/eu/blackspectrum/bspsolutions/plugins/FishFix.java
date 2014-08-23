package eu.blackspectrum.bspsolutions.plugins;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.EnchantmentManager;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityExperienceOrb;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityItem;
import net.minecraft.server.v1_7_R4.EnumFish;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.Items;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PossibleFishingResult;
import net.minecraft.server.v1_7_R4.StatisticList;
import net.minecraft.server.v1_7_R4.WeightedRandom;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity;
import org.bukkit.event.player.PlayerFishEvent;

import eu.blackspectrum.bspsolutions.util.RNGUtil;

public class FishFix
{


	private static final Collection<PossibleFishingResult>	JUNK_RESULTS		= Arrays.asList( new PossibleFishingResult[] {
			new PossibleFishingResult( new ItemStack( Items.LEATHER_BOOTS ), 10 ).a( 0.9F ),
			new PossibleFishingResult( new ItemStack( Items.LEATHER ), 10 ), new PossibleFishingResult( new ItemStack( Items.BONE ), 10 ),
			new PossibleFishingResult( new ItemStack( Items.POTION ), 10 ), new PossibleFishingResult( new ItemStack( Items.STRING ), 5 ),
			new PossibleFishingResult( new ItemStack( Items.FISHING_ROD ), 2 ).a( 0.9F ),
			new PossibleFishingResult( new ItemStack( Items.BOWL ), 10 ), new PossibleFishingResult( new ItemStack( Items.STICK ), 5 ),
			new PossibleFishingResult( new ItemStack( Items.INK_SACK, 10, 0 ), 1 ),
			new PossibleFishingResult( new ItemStack( Blocks.TRIPWIRE_SOURCE ), 10 ),
			new PossibleFishingResult( new ItemStack( Items.ROTTEN_FLESH ), 10 ) } );
	private static final Collection<PossibleFishingResult>	TREASURE_RESULTS	= Arrays.asList( new PossibleFishingResult[] {
			new PossibleFishingResult( new ItemStack( Blocks.WATER_LILY ), 1 ),
			new PossibleFishingResult( new ItemStack( Items.NAME_TAG ), 1 ), new PossibleFishingResult( new ItemStack( Items.SADDLE ), 1 ),
			new PossibleFishingResult( new ItemStack( Items.BOW ), 1 ).a( 0.25F ).a(),
			new PossibleFishingResult( new ItemStack( Items.FISHING_ROD ), 1 ).a( 0.25F ).a(),
			new PossibleFishingResult( new ItemStack( Items.BOOK ), 1 ).a()	} );
	private static final Collection<PossibleFishingResult>	FISH_RESULTS		= Arrays.asList( new PossibleFishingResult[] {
			new PossibleFishingResult( new ItemStack( Items.RAW_FISH, 1, EnumFish.COD.a() ), 60 ),
			new PossibleFishingResult( new ItemStack( Items.RAW_FISH, 1, EnumFish.SALMON.a() ), 25 ),
			new PossibleFishingResult( new ItemStack( Items.RAW_FISH, 1, EnumFish.CLOWNFISH.a() ), 2 ),
			new PossibleFishingResult( new ItemStack( Items.RAW_FISH, 1, EnumFish.PUFFERFISH.a() ), 13 ) } );




	public static void onPlayerCatchFish( final PlayerFishEvent event ) {
		if ( event.isCancelled() || event.getState() != PlayerFishEvent.State.CAUGHT_FISH )
			return;

		event.setCancelled( true );

		final EntityHuman owner = ( (CraftHumanEntity) event.getPlayer() ).getHandle();
		final World playerWorld = ( (CraftEntity) event.getPlayer() ).getHandle().world;

		final double hookX = event.getHook().getLocation().getX();
		final double hookY = event.getHook().getLocation().getY();
		final double hookZ = event.getHook().getLocation().getZ();

		final EntityItem fishedItem = new EntityItem( playerWorld, hookX, hookY, hookZ, getCaughtFish( owner ) );

		final Entity hook = ( (CraftEntity) event.getHook() ).getHandle();

		final double playerX = event.getPlayer().getLocation().getX();
		final double playerY = event.getPlayer().getLocation().getY();
		final double playerZ = event.getPlayer().getLocation().getZ();

		final double directionX = playerX - hookX;
		final double directionY = playerY - hookY;
		final double directionZ = playerZ - hookZ;
		final double directionLength = MathHelper.sqrt( directionX * directionX + directionY * directionY + directionZ * directionZ );

		fishedItem.motX = directionX * 0.1d;
		fishedItem.motY = directionY * 0.1d + MathHelper.sqrt( directionLength ) * 0.08D;
		fishedItem.motZ = directionZ * 0.1d;

		// Spawn the fished fish
		playerWorld.addEntity( fishedItem );

		// Spawn experience
		playerWorld.addEntity( new EntityExperienceOrb( playerWorld, playerX, playerY + 0.5D, playerZ + 0.5D, event.getExpToDrop() ) );

		// event.getHook().remove();

		hook.die();
	}




	private static ItemStack getCaughtFish( final EntityHuman owner ) {
		float f = RNGUtil.nextFloat();

		final int luckLevel = EnchantmentManager.getLuckEnchantmentLevel( owner );
		final int lureLevel = EnchantmentManager.getLureEnchantmentLevel( owner );

		float junkChance = 0.1F - luckLevel * 0.025F - lureLevel * 0.01F;
		float treasureChance = 0.05F + luckLevel * 0.01F - lureLevel * 0.01F;

		junkChance = MathHelper.a( junkChance, 0.0F, 1.0F );
		treasureChance = MathHelper.a( treasureChance, 0.0F, 1.0F );

		if ( f < junkChance )
		{
			owner.a( StatisticList.A, 1 );
			return ( (PossibleFishingResult) WeightedRandom.a( RNGUtil.getRandom(), JUNK_RESULTS ) ).a( RNGUtil.getRandom() );
		}
		else
		{
			f -= junkChance;
			if ( f < treasureChance )
			{
				owner.a( StatisticList.B, 1 );
				return ( (PossibleFishingResult) WeightedRandom.a( RNGUtil.getRandom(), TREASURE_RESULTS ) ).a( RNGUtil.getRandom() );
			}
			else
			{

				owner.a( StatisticList.z, 1 );
				return ( (PossibleFishingResult) WeightedRandom.a( RNGUtil.getRandom(), FISH_RESULTS ) ).a( RNGUtil.getRandom() );
			}
		}
	}
}
