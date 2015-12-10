/*!
	 HandEval
	 SpecialKEval
	 <p>
	 Copyright 2010 Kenneth J. Shackleton
	 codingfeedback@gmail.com
	 http://specialk-coding.blogspot.com/
	 <p>
	 ***********************************************************************
	 An evolution of this evaluator has been released under Apple's EULA and
	 is behind the app "Poker Ace" available through iTunes Store. For more
	 details visit http://itunes.apple.com/us/app/poker-ace/id392530020?mt=8
	 ***********************************************************************
	 <p>
	 This program gives you software freedom; you can copy, convey,
	 propagate, redistribute and/or modify this program under the terms of
	 the GNU General Public License (GPL) as published by the Free Software
	 Foundation (FSF), either version 3 of the License, or (at your option)
	 any later version of the GPL published by the FSF.
	 <p>
	 This program is distributed in the hope that it will be useful, but
	 WITHOUT ANY WARRANTY; without even the implied warranty of
	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	 General Public License for more details.
	 <p>
	 You should have received a copy of the GNU General Public License along
	 with this program in a file in the toplevel directory called "GPLv3".
	 If not, see http://www.gnu.org/licenses/.
	 <p>
	 @author Kenneth J. Shackleton
	 */

package com.SpecialK.SpecialKEval;

import com.SpecialK.SpecialKEval.Constants;

public class HandEval {
	//Ranks for 7-card evaluation separated
	//into non-flushes and flushes, each with
	//their own respective keys
	private int rankArray[];
	private int flushRankArray[];
			
	//Card face values beginning with ACE_ from
	//index 0 and TWO_ from index 48.
	private long deckcardsKey[];
	private int deckcardsFlush[];
	private int deckcardsSuit[];	
			
	//Array comprising of the flush suits.
	private short flushCheckArray[];
	
	public HandEval() {
		initialiseDeck();
		initialiseRanking();
		generateFlushCheck();
	}
	
	private void initialiseDeck() {
		deckcardsKey = new long[Constants.DECK_SIZE];
		deckcardsFlush = new int[Constants.DECK_SIZE];
		deckcardsSuit = new int[Constants.DECK_SIZE];
    
		//Enter face values into arrays to later build up the
		//respective keys. The values of ACE and ACE_FLUSH etc.
		//are different.
		int[] face = {Constants.ACE, Constants.KING, Constants.QUEEN, Constants.JACK, Constants.TEN, Constants.NINE, 
				Constants.EIGHT, Constants.SEVEN, Constants.SIX,
				Constants.FIVE, Constants.FOUR, Constants.THREE, Constants.TWO};
		int[] faceflush = {Constants.ACE_FLUSH, Constants.KING_FLUSH, Constants.QUEEN_FLUSH, Constants.JACK_FLUSH, Constants.TEN_FLUSH,
				Constants.NINE_FLUSH, Constants.EIGHT_FLUSH, Constants.SEVEN_FLUSH, Constants.SIX_FLUSH, Constants.FIVE_FLUSH,
				Constants.FOUR_FLUSH, Constants.THREE_FLUSH, Constants.TWO_FLUSH};

		for (int n=0; n<13; n++) {					
			deckcardsKey[4*n]		= (face[n] << Constants.NON_FLUSH_BIT_SHIFT) + Constants.SPADE;
			deckcardsKey[4*n+1]		= (face[n] << Constants.NON_FLUSH_BIT_SHIFT) + Constants.HEART;
			deckcardsKey[4*n+2]		= (face[n] << Constants.NON_FLUSH_BIT_SHIFT) + Constants.DIAMOND;
			deckcardsKey[4*n+3]		= (face[n] << Constants.NON_FLUSH_BIT_SHIFT) + Constants.CLUB;
      
			deckcardsFlush[4*n]		= faceflush[n];
			deckcardsFlush[4*n+1]	= faceflush[n];
			deckcardsFlush[4*n+2]	= faceflush[n];
			deckcardsFlush[4*n+3]	= faceflush[n];
			
			deckcardsSuit[4*n]		= Constants.SPADE;
			deckcardsSuit[4*n+1]	= Constants.HEART;
			deckcardsSuit[4*n+2]	= Constants.DIAMOND;
			deckcardsSuit[4*n+3]	= Constants.CLUB;		
		}
	}
  
	public void initialiseRanking() {
		FiveEval fiveEval = new FiveEval();
		rankArray = new int[Constants.MAX_NONFLUSH_KEY_INT+1];
		flushRankArray = new int[Constants.MAX_KEY_INT+1];
		int[] face={Constants.ACE, Constants.KING, Constants.QUEEN, Constants.JACK, Constants.TEN, Constants.NINE, Constants.EIGHT,
				Constants.SEVEN, Constants.SIX, Constants.FIVE, Constants.FOUR, Constants.THREE, Constants.TWO};
		int[] faceFlush={Constants.ACE_FLUSH, Constants.KING_FLUSH, Constants.QUEEN_FLUSH, Constants.JACK_FLUSH, Constants.TEN_FLUSH,
				Constants.NINE_FLUSH, Constants.EIGHT_FLUSH, Constants.SEVEN_FLUSH, Constants.SIX_FLUSH, Constants.FIVE_FLUSH,
				Constants.FOUR_FLUSH, Constants.THREE_FLUSH, Constants.TWO_FLUSH};
		int i, j, k, l, m, n, p;
		
		// Clean all ranks and flush ranks
		for (i=0; i<Constants.MAX_NONFLUSH_KEY_INT+1; i++) {
			rankArray[i]=0;
		}
		for(i=0; i<Constants.MAX_FLUSH_KEY_INT+1; i++) {
			flushRankArray[i]=0;
		}
		
		// Non-flush ranks
		for (i=1; i<13; i++) {
			for (j=1; j<=i; j++) {
				for (k=1; k<=j; k++) {
					for (l=0; l<=k; l++) {
						for (m=0; m<=l; m++) {
							for (n=0; n<=m; n++) {
								for (p=0; p<=n; p++) {
									if (i!=m && j!=n && k!=p) {
										int key=face[i]+face[j]+face[k]+face[l]+face[m]+face[n]+face[p];
										//The 4*i+0 and 4*m+1 trick prevents flushes
										int rank=fiveEval.getBestRankOf(4*i, 4*j, 4*k, 4*l, 4*m+1, 4*n+1, 4*p+1);
										rankArray[key < Constants.CIRCUMFERENCE_SEVEN ? key : key - Constants.CIRCUMFERENCE_SEVEN]=rank;
									}
								}
							}
						}
					}
				}
			}
		}
		
		//Flush ranks
		//All 7 same suit:
		for (i=6; i<13; i++) {
			for (j=5; j<i; j++) {
				for (k=4; k<j; k++) {
					for (l=3; l<k; l++) {
						for (m=2; m<l; m++) {
							for (n=1; n<m; n++) {
								for (p=0; p<n; p++) {
									int key=faceFlush[i]+faceFlush[j]+faceFlush[k]+faceFlush[l]+faceFlush[m]+faceFlush[n]+faceFlush[p];				
									int rank=fiveEval.getBestRankOf(4*i, 4*j, 4*k, 4*l, 4*m, 4*n, 4*p);
									flushRankArray[key]=rank;
								}
							}
						}
					}
				}
			}
		}
		
		//Only 6 same suit:
		for (i=5; i<13; i++) {
			for (j=4; j<i; j++) {
				for (k=3; k<j; k++) {
					for (l=2; l<k; l++) {
						for (m=1; m<l; m++) {
							for (n=0; n<m; n++) {
								int key=faceFlush[i]+faceFlush[j]+faceFlush[k]+faceFlush[l]+faceFlush[m]+faceFlush[n];
								//The Two of clubs is the card at index 51, the other cards are all spades
								int rank=fiveEval.getBestRankOf(4*i, 4*j, 4*k, 4*l, 4*m, 4*n, 51);
								flushRankArray[key]=rank;
							}
						}
					}
				}
			}
		}
		
		//Only 5 same suit:
		for (i=4; i<13; i++) {
			for (j=3; j<i; j++) {
				for (k=2; k<j; k++) {
					for (l=1; l<k; l++) {
						for (m=0; m<l; m++) {
							int key=faceFlush[i]+faceFlush[j]+faceFlush[k]+faceFlush[l]+faceFlush[m];
							int rank=fiveEval.getRankOf(4*i, 4*j, 4*k, 4*l, 4*m);
							flushRankArray[key]=rank;
						}
					}
				}
			}
		}		
	}

	private void generateFlushCheck() {
		flushCheckArray = new short[Constants.MAX_FLUSH_CHECK_SUM + 1];
		int card_1, card_2, card_3, card_4, card_5, card_6, card_7;
		
		//Begin with spades and run no further than clubs
		int SUIT_KEY=Constants.SPADE;
		int[] suits = {Constants.SPADE, Constants.HEART, Constants.DIAMOND, Constants.CLUB};
		
		//Initialise all entries of flushCheckArray[] to UNVERIFIED, as yet unchecked.
		//memset(&flushCheckArray[0], UNVERIFIED, sizeof(int)*(MAX_FLUSH_CHECK_SUM+1));
		for ( int i = 0 ; i < Constants.MAX_FLUSH_CHECK_SUM+1 ; i++ ) { flushCheckArray[i]=Constants.UNVERIFIED; }
		
		//7-card
		for (card_1=0; card_1<Constants.NUMBER_OF_SUITS; card_1++) {
			for (card_2=0; card_2<=card_1; card_2++) {
				for (card_3=0; card_3<=card_2; card_3++) {
					for (card_4=0; card_4<=card_3; card_4++) {
						for (card_5=0; card_5<=card_4; card_5++) {
							for (card_6=0; card_6<=card_5; card_6++) {
								for (card_7=0; card_7<=card_6; card_7++) {
									int SUIT_COUNT=0, FLUSH_SUIT_INDEX=-1, CARDS_MATCHED_SO_FAR=0;
									SUIT_KEY = suits[card_1] + suits[card_2] + suits[card_3] + suits[card_4] +
											   suits[card_5] + suits[card_6] + suits[card_7];
									if (flushCheckArray[SUIT_KEY] == Constants.UNVERIFIED) {
										do {
											FLUSH_SUIT_INDEX++;
											SUIT_COUNT = (suits[card_1] == suits[FLUSH_SUIT_INDEX] ? 1 : 0) +
													 	 (suits[card_2] == suits[FLUSH_SUIT_INDEX] ? 1 : 0) +
														 (suits[card_3] == suits[FLUSH_SUIT_INDEX] ? 1 : 0) +
														 (suits[card_4] == suits[FLUSH_SUIT_INDEX] ? 1 : 0) +
														 (suits[card_5] == suits[FLUSH_SUIT_INDEX] ? 1 : 0) +
														 (suits[card_6] == suits[FLUSH_SUIT_INDEX] ? 1 : 0) +
														 (suits[card_7] == suits[FLUSH_SUIT_INDEX] ? 1 : 0);
											CARDS_MATCHED_SO_FAR += SUIT_COUNT;
										} while(CARDS_MATCHED_SO_FAR < 3 && FLUSH_SUIT_INDEX < 4);
										
										// 7-card flush check means flush
										if (SUIT_COUNT > 4) {
											flushCheckArray[SUIT_KEY] = (short)suits[FLUSH_SUIT_INDEX];
										} else {
											flushCheckArray[SUIT_KEY] = (short)Constants.NOT_A_FLUSH;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
		
	public double[] computePreFlopEquityForSpecificHoleCards(int[] holeCards, int number_of_players) {
		if (holeCards == null || number_of_players <= 0 || holeCards.length != 2*number_of_players) {
			return null;
		}
		
		// Count and list the undealt cards, i.e. the remaining deck.
		int[] undealtCards = new int[Constants.DECK_SIZE];
		int number_of_undealt_cards = 0;
		
		for (int i = 0; i < Constants.DECK_SIZE; i++) {
			boolean bool_add_card = true;
			for(int j = 0; j < 2*number_of_players; j++) {
				if (holeCards[j] == i) {
					bool_add_card = false;
				}
			}
			if (bool_add_card == true) {
				undealtCards[number_of_undealt_cards] = i;
				number_of_undealt_cards++;
			}
		}
		////////////
		
		// Cumulative equities
		long[] equity = new long[Constants.MAX_NUMBER_OF_PLAYERS];
		for (int i = 0; i < Constants.MAX_NUMBER_OF_PLAYERS; i++) {
			equity[i] = 0;
		}
		////////////
		
		// Record player_rank
		int[] player_rank = new int[Constants.MAX_NUMBER_OF_PLAYERS];
		for(int i = 0; i < Constants.MAX_NUMBER_OF_PLAYERS; i++) {
			player_rank[i] = 0;
		}
		////////////
		
		// Possible shares in equity
		int[] equity_share = new int[Constants.MAX_NUMBER_OF_PLAYERS + 1];
		// The 0th entry is redundant
		equity_share[0] = -1;
		for (int i = 1; i < Constants.MAX_NUMBER_OF_PLAYERS + 1; i++) {
			equity_share[i] = Constants.CAKE/i;
		}
		///////////
		
		// Player table key and suit stems
		long[] player_key = new long[Constants.MAX_NUMBER_OF_PLAYERS];
		int[][] player_flush_stem = new int[Constants.MAX_NUMBER_OF_PLAYERS][Constants.CLUB+1];
		int[] player_suit_stem = new int[Constants.MAX_NUMBER_OF_PLAYERS];
		
		for(int p = 0; p < number_of_players; p++) {
			int P = 2*p;
			
			int card_1 = holeCards[P];
			int card_2 = holeCards[P+1];
			
			player_key[p] = deckcardsKey[card_1] + deckcardsKey[card_2];
			
			player_flush_stem[p][Constants.SPADE] = (deckcardsSuit[card_1] == Constants.SPADE ? deckcardsFlush[card_1] : 0) +
	                                   	  (deckcardsSuit[card_2] == Constants.SPADE ? deckcardsFlush[card_2] : 0);
			
			player_flush_stem[p][Constants.HEART]	= (deckcardsSuit[card_1] == Constants.HEART ? deckcardsFlush[card_1] : 0) +
	                                   	  (deckcardsSuit[card_2] == Constants.HEART ? deckcardsFlush[card_2] : 0);
			
			player_flush_stem[p][Constants.DIAMOND] = (deckcardsSuit[card_1] == Constants.DIAMOND ? deckcardsFlush[card_1] : 0) +
										    (deckcardsSuit[card_2] == Constants.DIAMOND ? deckcardsFlush[card_2] : 0);
			
			player_flush_stem[p][Constants.CLUB] = (deckcardsSuit[card_1] == Constants.CLUB ? deckcardsFlush[card_1] : 0) +
	                                  	 (deckcardsSuit[card_2] == Constants.CLUB ? deckcardsFlush[card_2] : 0);
			
			player_suit_stem[p] = deckcardsSuit[holeCards[P]] + deckcardsSuit[holeCards[P+1]];
		}
                
		////////
		
		int player_defect = number_of_players-1;
		int number_of_players_sharing_the_pot = 1;
		int current_best_rank;
		int next_rank;
		
		// Begin calculating equities by dealing out all possible
		// combinations of five table cards, adding up the incremental
		// equities to find the total integral equities.
		
		int i = 4;
		do {
			int table_card_1 = undealtCards[i];
			int j = 3;
			do {
				int table_card_2 = undealtCards[j];
				long table_key_cumulative_to_2 = deckcardsKey[table_card_1] + deckcardsKey[table_card_2];
				int k = 2;
				do {
					int table_card_3 = undealtCards[k];
					long table_key_cumulative_to_3 = table_key_cumulative_to_2 + deckcardsKey[table_card_3];
					int l = 1;
					do {
						int table_card_4 = undealtCards[l];
						long table_key_cumulative_to_4 = table_key_cumulative_to_3 + deckcardsKey[table_card_4];
						int m = 0;
						do {
							int table_card_5 = undealtCards[m];
							long table_key = table_key_cumulative_to_4 + deckcardsKey[table_card_5];
							// Reset table_flush_key.
							int table_flush_key = Constants.UNVERIFIED;
							// Reset number of interested players.
							number_of_players_sharing_the_pot = 1;
							////////
							
							long KEY = table_key + player_key[0];
							
							int FLUSH_CHECK_KEY = (int) (KEY & Constants.SUIT_BIT_MASK);
							int FLUSH_SUIT = flushCheckArray[FLUSH_CHECK_KEY];
							
							if (FLUSH_SUIT < 0) {
								KEY = (KEY >> Constants.NON_FLUSH_BIT_SHIFT);
								current_best_rank = rankArray[KEY < Constants.CIRCUMFERENCE_SEVEN ? (int) KEY : (int) KEY - Constants.CIRCUMFERENCE_SEVEN];
								player_rank[0] = current_best_rank;
							} else {					
								table_flush_key = (table_flush_key == Constants.UNVERIFIED ?
														(deckcardsSuit[table_card_1] == FLUSH_SUIT ? deckcardsFlush[table_card_1] : 0) +
														(deckcardsSuit[table_card_2] == FLUSH_SUIT ? deckcardsFlush[table_card_2] : 0) +
														(deckcardsSuit[table_card_3] == FLUSH_SUIT ? deckcardsFlush[table_card_3] : 0) +
														(deckcardsSuit[table_card_4] == FLUSH_SUIT ? deckcardsFlush[table_card_4] : 0) +
														(deckcardsSuit[table_card_5] == FLUSH_SUIT ? deckcardsFlush[table_card_5] : 0) : table_flush_key);
								int player_flush_key = table_flush_key + player_flush_stem[0][FLUSH_SUIT];
								current_best_rank = flushRankArray[player_flush_key];
								player_rank[0] = current_best_rank;
							}
							
							int n = 1;
							do {
								KEY = table_key + player_key[n];
								
								FLUSH_CHECK_KEY = (int) (KEY & Constants.SUIT_BIT_MASK);
								FLUSH_SUIT = flushCheckArray[FLUSH_CHECK_KEY];
								
								if (FLUSH_SUIT == Constants.NOT_A_FLUSH) {
									KEY = (KEY >> Constants.NON_FLUSH_BIT_SHIFT);
									next_rank = rankArray[((int) KEY < Constants.CIRCUMFERENCE_SEVEN ? (int) KEY : (int) KEY - Constants.CIRCUMFERENCE_SEVEN)];
									player_rank[n] = next_rank;
								} else {
									//** This could be a good place to optimize **
									table_flush_key = (table_flush_key == Constants.UNVERIFIED ?
															(deckcardsSuit[table_card_1] == FLUSH_SUIT ? deckcardsFlush[table_card_1] : 0) +
															(deckcardsSuit[table_card_2] == FLUSH_SUIT ? deckcardsFlush[table_card_2] : 0) +
															(deckcardsSuit[table_card_3] == FLUSH_SUIT ? deckcardsFlush[table_card_3] : 0) +
															(deckcardsSuit[table_card_4] == FLUSH_SUIT ? deckcardsFlush[table_card_4] : 0) +
															(deckcardsSuit[table_card_5] == FLUSH_SUIT ? deckcardsFlush[table_card_5] : 0) : table_flush_key);
									int player_flush_key = table_flush_key + player_flush_stem[n][FLUSH_SUIT];
									next_rank = flushRankArray[player_flush_key];
									player_rank[n] = next_rank;
								}
								
								//// Compare the player_rank
								//
								// Case: new outright strongest player
								if (current_best_rank < next_rank) {
									//Update current best rank
									current_best_rank = next_rank;
									//Reset number of players interested
									number_of_players_sharing_the_pot = 1;
								} else if (current_best_rank == next_rank) { //Case: pot shared with another player
									//Increment by 1 the number of interest players
									number_of_players_sharing_the_pot++;
								}
								//Last Case: new player loses
								//There is nothing we need to do.
								///////
								n++;
							} while (n < player_defect);
							
							// Calculate incremental equity
							int incremental_equity = equity_share[number_of_players_sharing_the_pot];
							
							// Add to the cumulative equities
							int p = 0;
							do {
								if (player_rank[p] == current_best_rank) {
									equity[p] += incremental_equity;
								}
								p++;
							} while (p < player_defect);
							m++;
						} while (m < l); l++;
					} while (l < k); k++;
				} while (k < j); j++;
			} while (j < i); i++;
		} while (i < number_of_undealt_cards);
				
		double[] equity_percentage = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		int Equity[] = new int[] {Constants.EQUITY_TWO, Constants.EQUITY_THREE, Constants.EQUITY_FOUR, Constants.EQUITY_FIVE, Constants.EQUITY_SIX, Constants.EQUITY_SEVEN, Constants.EQUITY_EIGHT};
		double equity_sum = 0.0;
		for (i = 0; i < Constants.MAX_NUMBER_OF_PLAYERS; i++) {
			equity_percentage[i] = 100 * (equity[i] + 0.0) / Equity[number_of_players-2];
			equity_sum += equity_percentage[i];
		}
		equity_percentage[player_defect] = 100.0f - equity_sum;
		return equity_percentage;
		/*String s = String.format("%.8f_%.8f_%.8f_%.8f_%.8f_%.8f_%.8f_%.8f",
						equity_percentage[0],
						equity_percentage[1],
						equity_percentage[2],
						equity_percentage[3],
						equity_percentage[4],
						equity_percentage[5],
						equity_percentage[6],
						equity_percentage[7]);
		
		return s;*/		
	}
  
  /*
	public String computeFlopEquityForSpecificCards(int[] holeCards, int[] tableCards, int number_of_players) {
		return null;
	}

	public String computeTurnEquityForSpecificCards(int[] holeCards, int[] table_cards, int number_of_players) {
		return null;
	}

	public String computeRiverEquityForSpecificCards(int[] holeCards, int[] table_cards, int number_of_players) {
		return null;
	}
   */
	
	/*void timeRankMethod() {
		//Testing only.
		int BIG_NUMBER=6750000*7;
		byte[] hands_array = new byte[BIG_NUMBER];
		
		int rank = 0;
		
		// Generate lots of random hands.
		Random random=new Random(System.currentTimeMillis());
		for (int n = 0; n < BIG_NUMBER/7; n++) {
			byte[] temp_array = new byte[7];
			temp_array[0] = (byte) Math.round(random.nextDouble()*51);
			for (int m = 1; m < 7; m++) {
				boolean seen = true;
				while (seen) {
					seen = false;
					temp_array[m] = (byte) Math.round(random.nextDouble()*51);
					for (int k = 0; k < m && seen == false; k++) {
						seen = (temp_array[m] == temp_array[k] ? true : false);
					}
				}
			}
			int N = 7*n;
			hands_array[N]		= temp_array[0];
			hands_array[N+1]	= temp_array[1];
			hands_array[N+2]	= temp_array[2];
			hands_array[N+3]	= temp_array[3];
			hands_array[N+4]	= temp_array[4];
			hands_array[N+5]	= temp_array[5];
			hands_array[N+6]	= temp_array[6];
		}
		// Time how long it takes to enumerate the hands. I'm not sure if this really affects the result much.
		long start_adjust = System.currentTimeMillis();
		for (int N = 0; N < BIG_NUMBER; N += 7) {
		    N++; N++; N++; N++; N++; N++; N++;
		}
		long finish_adjust = System.currentTimeMillis();
		  
		long start = System.currentTimeMillis();
		// The incremental additions (N++ and N+=7) here aren't really the work of the evaluator and should be subtracted from the total time to get a fairer
		// estimate of the work, although this doesn't materially affect the outcome.
		for (int N = 0; N < BIG_NUMBER; N += 7) {
			int card_1 = hands_array[N++];
			int card_2 = hands_array[N++];
			int card_3 = hands_array[N++];
			int card_4 = hands_array[N++];
			int card_5 = hands_array[N++];
			int card_6 = hands_array[N++];
			int card_7 = hands_array[N++];
			long KEY = deckcardsKey[card_1]+deckcardsKey[card_2]+deckcardsKey[card_3]+deckcardsKey[card_4]+deckcardsKey[card_5]+deckcardsKey[card_6]+
		                    deckcardsKey[card_7];
			int FLUSH_SUIT = flushCheckArray[SUIT_BIT_MASK & (int)KEY];
			if (FLUSH_SUIT < 0) {
					KEY = (KEY >> NON_FLUSH_BIT_SHIFT);
					rank = rankArray[KEY < CIRCUMFERENCE_SEVEN ? (int)KEY : (int)KEY - CIRCUMFERENCE_SEVEN];
			} else {
				int FLUSH_KEY = (deckcardsSuit[card_1] == FLUSH_SUIT ? deckcardsFlush[card_1] : 0)+
		                        (deckcardsSuit[card_2] == FLUSH_SUIT ? deckcardsFlush[card_2] : 0)+
		                        (deckcardsSuit[card_3] == FLUSH_SUIT ? deckcardsFlush[card_3] : 0)+
		                        (deckcardsSuit[card_4] == FLUSH_SUIT ? deckcardsFlush[card_4] : 0)+
		                        (deckcardsSuit[card_5] == FLUSH_SUIT ? deckcardsFlush[card_5] : 0)+
		                        (deckcardsSuit[card_6] == FLUSH_SUIT ? deckcardsFlush[card_6] : 0)+
		                        (deckcardsSuit[card_7] == FLUSH_SUIT ? deckcardsFlush[card_7] : 0);
				rank = flushRankArray[FLUSH_KEY];
			}
		}
		long finish = System.currentTimeMillis();
			
		System.out.println("\nlast rank was " + rank);
		System.out.println("\ntime taken: " + (finish - start - (finish_adjust - start_adjust)) + " millis");
		
		float rate = BIG_NUMBER/7*(1000+0.0f)/(finish-start-(finish_adjust - start_adjust)+0.0f);
		//System.out.println("\nevaluations per second: " + Math.round(rate));
		
		System.out.print("\n100x more evaluations needed to hit cruising speed\n");
	}
	*/
}
