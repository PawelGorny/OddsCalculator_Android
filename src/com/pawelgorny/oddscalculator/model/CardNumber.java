package com.pawelgorny.oddscalculator.model;

public class CardNumber {
	public static final int Ace = 12;
	public static final int Two = 0;
	public static final int Three = 1;
	public static final int Four = 2;
	public static final int Five = 3;
	public static final int Six = 4;
	public static final int Seven = 5;
	public static final int Eight = 6;
	public static final int Nine = 7;
	public static final int Ten = 8;
	public static final int Jack = 9;
	public static final int Quenn = 10;
	public static final int King = 11;

	public final static int length = 13;

	public final static String[] name = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace" };

	public static String getName(int number) {
		final int card = ((number - 1) % length);
		return name[card];
	}

}
