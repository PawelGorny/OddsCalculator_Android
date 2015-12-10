package jpoker.poker.evaluator;

/**
 * While
 */
public class HandEvaluatorTables
{
  static
  {
    init_nRanksTable ();	// 8K table
    init_straightTable ();	// 8K table
    init_flushTable ();		// 4K table
    init_topFiveTable ();	// 8K table
    init_topThreeTable ();	// 8K table
    init_twoRanksTable ();
    init_topRankTable ();
  }
  /*
   *
   *		These functions were used to create
   *		the lookup tables to allow a "fantastic"
   *		increase in speed.
   *
   *		Timing before table inclusion (with javac -O)
   *		$ java fish
   *		real	26m49
   *		
   *		after adding nRanks, straight, flush, topFive
   *		% java fish
   *		real	18m38
   */

  /**
   * The straight table maps the 13 bit mask of the ranks
   * of a hand into a value which represents straights and
   * straight draws.
   *
   * return values:
   *
   * 12:	A high straight
   *  .
   *  .
   *  .
   *  4:	6 high straight
   *  3:	5 high straight
   *
   *  0:	no straight no draw
   * -1:	inside (1 rank/4 card?) straight draw
   * -2:	open/double gut (2 ranks/8 card?) straight draw)
   * -3:	runner runner
   */

  /**
   * create a table for looking up flush draws givin
   * a suit hash.  The suits are hashed into 3 bit chunks
   * of an int.  This implies that there can be no more than 7
   * cards of a suit.
   *
   * The return values are encoded as integers which are offset
   * from the "natural" flush suit.  This is because the 0 value
   * is used here to indicate no flush/flush draw, but 0 is
   * normall a suit (CLUBS).
   * 
   * The offset is 1.
   * Negative values are draws.
   * 4-card flushes are -(offset suit)
   * 3-card flushes are -(offset suit)<<4
   *
   * return values:
   *
   *   4:		flush in suit "3"
   *   3:		flush in suit "2"
   *   2:		flush in suit "1"
   *   1:		flush in suit "0"
   *
   *   0:		no flush
   *
   * - 1:		4-card flush draw in suit "0"
   * - 2:		4-card flush draw in suit "1"
   * - 3:		4-card flush draw in suit "2"
   * - 4:		4-card flush draw in suit "3"
   *
   * -16:		3-card flush draw in suit "0"
   * -32:		3-card flush draw in suit "1"
   * -48:		3-card flush draw in suit "2"
   * -64:		3-card flush draw in suit "3"
   *
   */


  public static int topThreeTable[];
  public static int getTopThreeTable(int index)
  {
	  if (index>topThreeTable.length)
		  return 7168;
	  return topThreeTable[index];
  }
  public static void init_topThreeTable ()
  {
     topThreeTable=HandEvaluatorTables5.topThreeTable;
  }

  public static int twoRanksTable[];
  public static void init_twoRanksTable ()
  {
    twoRanksTable = HandEvaluatorTables7.twoRanksTable;
  }

  public static int topRankTable[];
  public static void init_topRankTable ()
  {
	  topRankTable=HandEvaluatorTables6.topRankTable;
  }

  public static int straightTable[];
  public static void init_straightTable ()
  {
	  straightTable=HandEvaluatorTables2.straightTable;
  }

  public static int nRanksTable[];
  public static void init_nRanksTable ()
  {
    nRanksTable=HandEvaluatorTables1.nRanksTable;
  }
  
  public static int flushTable[];
  public static void init_flushTable ()
  {
	  flushTable=HandEvaluatorTables3.flushTable;
  }

  public static int topFiveTable[];
  public static int getTopFiveTable(int index)
  {
	  if (index>topFiveTable.length)
		  return 7936;
	  return topFiveTable[index];
  }
  public static void init_topFiveTable ()
  {
    topFiveTable=HandEvaluatorTables4.topFiveTable;
  }


};

