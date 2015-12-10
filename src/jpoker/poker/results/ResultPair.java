package jpoker.poker.results;

import jpoker.poker.Rank;

public class ResultPair {
	
	private static final int[] cardsA=new int[] {
		0, 762300, 840456, 249458, 25816, 41562, 181104, 17848, 216
	};
	private static final int[] cardsK=new int[] {
		0, 762300, 840456, 249458, 25816, 41562, 181104, 17848, 216
	};
	private static final int[] cardsQ=new int[] {
		0, 756360, 838944, 248952, 33774, 41474, 181104, 17848, 304
	};
	private static final int[] cardsJ=new int[] {
		0, 750420, 837432, 248446, 41732, 41386, 181104, 17848, 392
	};
	private static final int[] cardsT=new int[] {
		0, 744480, 835920, 247940, 49690, 41298, 181104, 17848, 480
	};
	private static final int[] cards9=new int[] {
		0, 745470, 835920, 247940, 48700, 41300, 181104, 17848, 478
	};
	private static final int[] cards8=new int[] {
		0, 745470, 835920, 247940, 48700, 41300, 181104, 17848, 478
	};
	private static final int[] cards7=new int[] {
		0, 745470, 835920, 247940, 48700, 41300, 181104, 17848, 478
	};
	private static final int[] cards6=new int[] {
		0, 745470, 835920, 247940, 48700, 41300, 181104, 17848, 478
	};
	private static final int[] cards5=new int[] {
		0, 744480, 835920, 247940, 49690, 41298, 181104, 17848, 480
	};
	private static final int[] cards4=new int[] {
		0, 750420, 837432, 248446, 41732, 41386, 181104, 17848, 392
	};
	private static final int[] cards3=new int[] {
		0, 762300, 840456, 249458, 25816, 41562, 181104, 17848, 216
	};
	private static final int[] cards2=new int[] {
		0, 762300, 840456, 249458, 25816, 41562, 181104, 17848, 216
	};
	
	public static final int[] getResults(Rank rank)
	{
		switch (rank.value()){
		case 12:
			return cardsA;
		case 11:
			return cardsK;
		case 10:
			return cardsQ;
		case 9:
			return cardsJ;
		case 8:
			return cardsT;
		case 7:
			return cards9;
		case 6:
			return cards8;
		case 5:
			return cards7;
		case 4:
			return cards6;
		case 3:
			return cards5;
		case 2:
			return cards4;
		case 1:	
			return cards3;
		case 0:
			return cards2;
		}
		return null;
	}
}
