package edu.isistan.fmframework.utils;

import java.util.Random;

public class RandomUtils {

	public static Random random = new Random(100);

	public static void setSeed(long seed) {
		RandomUtils.random = new Random(seed);
	}

	public static int randomRange(int inferiorInclusive, int superiorExclusive) {
		return inferiorInclusive + random.nextInt(superiorExclusive - inferiorInclusive);
	}

	public static double random() {
		return random.nextDouble();
		// return random.nextFloat();
	}

	public static double[] randomArray(int size) {
		double[] randomValues = new double[size];
		for (int i = 0; i < size; i++)
			randomValues[i] = RandomUtils.random();
		return randomValues;
	}

	public static double randomDouble(double inferiorInclusive, double superiorExclusive) {
		return inferiorInclusive + random.nextDouble() * (superiorExclusive - inferiorInclusive);
	}

	public static int randomFromRoulette(double[] roulette) {
		double nextDouble = RandomUtils.random.nextDouble();

		double accum = 0.0;
		for (int result = 0; result < roulette.length; result++) {
			accum += roulette[result];
			if (nextDouble < accum)
				return result;
		}
		return -1;
	}

}
