package jpoker;

import jpoker.poker.Card;
import jpoker.poker.Operation;
import jpoker.poker.Suit;
import jpoker.poker.Valuation;

// clone of the fish program, mostly for a sanity check
class fish {
	public static final long one = 0x01;

	/*
	 * public static void main2 (String args[]) { for (int i=0; i<52; i++) { String s[] = new String[2]; s[0] = "3h"; s[1] = (new Card(i)).toString();
	 * System.out.println ("fish " + s[0] + " " + s[1] + " -r"); main2 (s); } }
	 */

	public static void main3(String args[]) {
		System.out.println("public static final int[][][] cardsOffsuited=new [][][]{");
		for (int c1 = 0; c1 < 13; c1++) {
			Card card1 = new Card();
			card1.setSuit(Suit.HEARTS);
			card1.setRank(c1);
			System.out.println("  {");
			for (int c2 = 0; c2 < 13; c2++) {
				if (c2 <= card1.rank().value())
					continue;
				Card card2 = new Card();
				card2.setSuit(Suit.DIAMONDS);
				card2.setRank(c2);
				int cards[] = new int[7];
				long dead = 0;
				cards[0] = card1.value();
				cards[1] = card2.value();
				dead |= one << cards[0];
				dead |= one << cards[1];
				// System.out.println ("//"+card1.rank()+card2.rank());
				int counts[] = Operation.calculate(cards, dead, 2, null);
				// System.out.print("public static final int[] cards"+card1.rank()+card2.rank()+"=new int[]{");
				System.out.print("/*" + card1.rank() + card2.rank() + "*/    {");
				for (int i = 0; i < counts.length; i++) {
					System.out.print(counts[i] + ",");
				}
				// System.out.println("};");
				System.out.println("    },");
			}// c2
			System.out.println("  },");
		}// c1
	}

	public static void main(String args[]) {
		// Debug.debug = true;
		long start = System.currentTimeMillis();
		int cards[] = new int[7];
		int counts[] = null;
		long dead = 0;

		// Hand h = new Hand ();
		Card card1 = null;
		Card card2 = null;
		for (int i = 0; i < args.length; i++) {
			Card card = new Card(args[i]);
			cards[i] = card.value();
			System.out.println(card.rank());
			dead |= one << cards[i];
			switch (i) {
				case 0:
					card1 = card;
					break;
				case 1:
					card2 = card;
					break;
			}
		}
		if (args.length == 2 && card1 != null && card2 != null) {
			if (card1.rank().equals(card2.rank())) {// pair
				counts = jpoker.poker.results.ResultPair.getResults(card1.rank());
			} else if (card1.suit().equals(card2.suit())) {
				counts = jpoker.poker.results.ResultSuited.getResults(card1.rank(), card2.rank());
			} else {
				counts = jpoker.poker.results.ResultOffsuit.getResults(card1.rank(), card2.rank());
			}
		}
		if (counts == null) {
			counts = Operation.calculate(cards, dead, args.length, null);
		}

		System.out.println("time: " + (System.currentTimeMillis() - start));
		int sum = 0;
		// String result="";
		for (int i = 0; i < 9; i++) {
			String rank = Valuation.StringRanking[i];
			// result+=rank + ": " + counts[i]+"\n";
			System.out.println(rank + ": " + counts[i]);
			sum += counts[i];
		}
		System.out.println("total: " + sum);
		counts = Operation.calculate(cards, dead, args.length, null);
		for (int i = 0; i < 9; i++) {
			String rank = Valuation.StringRanking[i];
			// result+=rank + ": " + counts[i]+"\n";
			System.out.println(rank + ": " + counts[i]);
			sum += counts[i];
		}
		/*
		 * 
		 * switch (args.length) { case 0: counts = runZeroCard (cards, dead); break; case 1: counts = runOneCard (cards, dead); break; case 2: counts =
		 * runTwoCard (cards, dead); break; case 3: counts = runThreeCard (cards, dead); break; case 4: counts = runFourCard (cards, dead); break; case 5:
		 * counts = runFiveCard (cards, dead); break; case 6: counts = runSixCard (cards, dead); break; case 7: counts = runSevenCard (cards, dead); break; }
		 * 
		 * //System.out.println(""); int sum = 0; for (int i=0; i<9; i++) { String rank = Valuation.StringRanking[i]; System.out.println (rank + ": " +
		 * counts[i]); sum += counts[i]; } System.out.println("total: " + sum);
		 */
	}

}
