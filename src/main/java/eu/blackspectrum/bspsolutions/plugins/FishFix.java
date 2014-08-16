package eu.blackspectrum.bspsolutions.plugins;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

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

public class FishFix
{


	private static final Collection<PossibleFishingResult>	JUNK_RESULTS		= Arrays.asList( new PossibleFishingResult[] {
			( new PossibleFishingResult( new ItemStack( Items.LEATHER_BOOTS ), 10 ) ).a( 0.9F ),
			new PossibleFishingResult( new ItemStack( Items.LEATHER ), 10 ), new PossibleFishingResult( new ItemStack( Items.BONE ), 10 ),
			new PossibleFishingResult( new ItemStack( Items.POTION ), 10 ), new PossibleFishingResult( new ItemStack( Items.STRING ), 5 ),
			( new PossibleFishingResult( new ItemStack( Items.FISHING_ROD ), 2 ) ).a( 0.9F ),
			new PossibleFishingResult( new ItemStack( Items.BOWL ), 10 ), new PossibleFishingResult( new ItemStack( Items.STICK ), 5 ),
			new PossibleFishingResult( new ItemStack( Items.INK_SACK, 10, 0 ), 1 ),
			new PossibleFishingResult( new ItemStack( Blocks.TRIPWIRE_SOURCE ), 10 ),
			new PossibleFishingResult( new ItemStack( Items.ROTTEN_FLESH ), 10 ) } );
	private static final Collection<PossibleFishingResult>	TREASURE_RESULTS	= Arrays.asList( new PossibleFishingResult[] {
			new PossibleFishingResult( new ItemStack( Blocks.WATER_LILY ), 1 ),
			new PossibleFishingResult( new ItemStack( Items.NAME_TAG ), 1 ), new PossibleFishingResult( new ItemStack( Items.SADDLE ), 1 ),
			( new PossibleFishingResult( new ItemStack( Items.BOW ), 1 ) ).a( 0.25F ).a(),
			( new PossibleFishingResult( new ItemStack( Items.FISHING_ROD ), 1 ) ).a( 0.25F ).a(),
			( new PossibleFishingResult( new ItemStack( Items.BOOK ), 1 ) ).a() } );
	private static final Collection<PossibleFishingResult>	FISH_RESULTS		= Arrays.asList( new PossibleFishingResult[] {
			new PossibleFishingResult( new ItemStack( Items.RAW_FISH, 1, EnumFish.COD.a() ), 60 ),
			new PossibleFishingResult( new ItemStack( Items.RAW_FISH, 1, EnumFish.SALMON.a() ), 25 ),
			new PossibleFishingResult( new ItemStack( Items.RAW_FISH, 1, EnumFish.CLOWNFISH.a() ), 2 ),
			new PossibleFishingResult( new ItemStack( Items.RAW_FISH, 1, EnumFish.PUFFERFISH.a() ), 13 ) } );

	private static final Random								random				= new Random();




	public static void onPlayerCatchFish( PlayerFishEvent event ) {
		if ( event.isCancelled() || event.getState() != PlayerFishEvent.State.CAUGHT_FISH )
			return;

		event.setCancelled( true );

		EntityHuman owner = ( (CraftHumanEntity) event.getPlayer() ).getHandle();
		World playerWorld = ( (CraftEntity) event.getPlayer() ).getHandle().world;

		double hookX = event.getHook().getLocation().getX();
		double hookY = event.getHook().getLocation().getY();
		double hookZ = event.getHook().getLocation().getZ();

		EntityItem fishedItem = new EntityItem( playerWorld, hookX, hookY, hookZ, getCaughtFish( owner ) );

		Entity hook = ( (CraftEntity) event.getHook() ).getHandle();

		double playerX = event.getPlayer().getLocation().getX();
		double playerY = event.getPlayer().getLocation().getY();
		double playerZ = event.getPlayer().getLocation().getZ();

		double directionX = playerX - hookX;
		double directionY = playerY - hookY;
		double directionZ = playerZ - hookZ;
		double directionLength = (double) MathHelper.sqrt( directionX * directionX + directionY * directionY + directionZ * directionZ );

		fishedItem.motX = directionX * 0.1d;
		fishedItem.motY = directionY * 0.1d + (double) MathHelper.sqrt( directionLength ) * 0.08D;
		fishedItem.motZ = directionZ * 0.1d;

		// Spawn the fished fish
		playerWorld.addEntity( fishedItem );

		// Spawn experience
		playerWorld.addEntity( new EntityExperienceOrb( playerWorld, playerX, playerY + 0.5D, playerZ + 0.5D, event.getExpToDrop() ) );

		// event.getHook().remove();

		hook.die();
	}




	private static ItemStack getCaughtFish( EntityHuman owner ) {
		float f = random.nextFloat();

		int luckLevel = EnchantmentManager.getLuckEnchantmentLevel( owner );
		int lureLevel = EnchantmentManager.getLureEnchantmentLevel( owner );

		float junkChance = 0.1F - (float) luckLevel * 0.025F - (float) lureLevel * 0.01F;
		float treasureChance = 0.05F + (float) luckLevel * 0.01F - (float) lureLevel * 0.01F;

		junkChance = MathHelper.a( junkChance, 0.0F, 1.0F );
		treasureChance = MathHelper.a( treasureChance, 0.0F, 1.0F );

		if ( f < junkChance )
		{
			owner.a( StatisticList.A, 1 );
			return ( (PossibleFishingResult) WeightedRandom.a( random, JUNK_RESULTS ) ).a( random );
		}
		else
		{
			f -= junkChance;
			if ( f < treasureChance )
			{
				owner.a( StatisticList.B, 1 );
				return ( (PossibleFishingResult) WeightedRandom.a( random, TREASURE_RESULTS ) ).a( random );
			}
			else
			{

				owner.a( StatisticList.z, 1 );
				return ( (PossibleFishingResult) WeightedRandom.a( random, FISH_RESULTS ) ).a( random );
			}
		}
	}
}
