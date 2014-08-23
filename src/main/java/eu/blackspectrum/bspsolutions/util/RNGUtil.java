package eu.blackspectrum.bspsolutions.util;

import java.util.Random;

/**
 *
 * Generic RNG stuff
 *
 */
public class RNGUtil
{


	private final static Random	RANDOM	= new Random();




	/**
	 * @return the random
	 */
	public static final Random getRandom() {
		return RANDOM;
	}




	/**
	 * @return
	 * @see java.util.Random#nextBoolean()
	 */
	public static boolean nextBoolean() {
		return RANDOM.nextBoolean();
	}




	/**
	 * @return
	 * @see java.util.Random#nextDouble()
	 */
	public static double nextDouble() {
		return RANDOM.nextDouble();
	}




	/**
	 * @return
	 * @see java.util.Random#nextFloat()
	 */
	public static float nextFloat() {
		return RANDOM.nextFloat();
	}




	/**
	 * @return
	 * @see java.util.Random#nextInt()
	 */
	public static int nextInt() {
		return RANDOM.nextInt();
	}




	/**
	 * @param bound
	 * @return
	 * @see java.util.Random#nextInt(int)
	 */
	public static int nextInt( final int bound ) {
		return RANDOM.nextInt( bound );
	}




	/**
	 * @return
	 * @see java.util.Random#nextLong()
	 */
	public static long nextLong() {
		return RANDOM.nextLong();
	}

}
