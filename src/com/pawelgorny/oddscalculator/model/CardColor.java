package com.pawelgorny.oddscalculator.model;

public class CardColor {
	public static final int Spades = 1;
	public static final int Hearts = 2;
	public static final int Diamonds = 0;
	public static final int Clubs = 3;

	public final static int length = 4;

	public final static String[] name = new String[] { "diamond", "spades", "hearts", "clubs" };

	public static String getName(int color) {
		int card = ((color - 1) / CardNumber.length);
		return name[card];
	}
}
