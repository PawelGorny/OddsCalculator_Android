package jpoker.poker;

public class Operation {

	public static final long one = 0x01;

	public static int[] calculate(int[] cards, long dead, int cardsNumber, UpdateProgress updateProgress) {
		int counts[] = null;
		// Hand h = new Hand ();
		switch (cardsNumber) {
			case 0:
				counts = Operation.runZeroCard(cards, dead);
				break;
			case 1:
				counts = Operation.runOneCard(cards, dead);
				break;
			case 2:
				counts = Operation.runTwoCard(cards, dead);
				break;
			case 3:
				counts = Operation.runThreeCard(cards, dead, updateProgress);
				break;
			case 4:
				counts = Operation.runFourCard(cards, dead, updateProgress);
				break;
			case 5:
				counts = Operation.runFiveCard(cards, dead);
				break;
			case 6:
				counts = Operation.runSixCard(cards, dead);
				break;
			case 7:
				counts = Operation.runSevenCard(cards, dead);
				break;
		}
		return counts;
	}

	public static int[] runZeroCard(int[] c, long dead) {
		int res[] = new int[Valuation.NUM_RANKINGS];
		for (int i = 0; i < 52; i++)
			if (((one << i) & dead) == 0)
				for (int j = i + 1; j < 52; j++)
					if (((one << j) & dead) == 0)
						for (int k = j + 1; k < 52; k++)
							if (((one << k) & dead) == 0)
								for (int l = k + 1; l < 52; l++)
									if (((one << l) & dead) == 0)
										for (int m = l + 1; m < 52; m++)
											if (((one << m) & dead) == 0)
												for (int n = m + 1; n < 52; n++)
													if (((one << n) & dead) == 0)
														for (int o = n + 1; o < 52; o++)
															if (((one << o) & dead) == 0) {
																c[0] = i;
																c[1] = j;
																c[2] = k;
																c[3] = l;
																c[4] = m;
																c[5] = n;
																c[6] = o;
																Hand h = new Hand(c);
																res[Valuation.value(h.evaluate())]++;
															}
		return res;
	}

	public static int[] runOneCard(int[] c, long dead) {
		int res[] = new int[Valuation.NUM_RANKINGS];
		for (int i = 0; i < 52; i++)
			if (((one << i) & dead) == 0)
				for (int j = i + 1; j < 52; j++)
					if (((one << j) & dead) == 0)
						for (int k = j + 1; k < 52; k++)
							if (((one << k) & dead) == 0)
								for (int l = k + 1; l < 52; l++)
									if (((one << l) & dead) == 0)
										for (int m = l + 1; m < 52; m++)
											if (((one << m) & dead) == 0)
												for (int n = m + 1; n < 52; n++)
													if (((one << n) & dead) == 0) {
														c[1] = i;
														c[2] = j;
														c[3] = k;
														c[4] = l;
														c[5] = m;
														c[6] = n;
														Hand h = new Hand(c);
														res[Valuation.value(h.evaluate())]++;
													}
		return res;
	}

	public static int[] runTwoCard(int[] c, long dead) {
		int res[] = new int[Valuation.NUM_RANKINGS];
		for (int i = 0; i < 52; i++)
			if (((one << i) & dead) == 0)
				for (int j = i + 1; j < 52; j++)
					if (((one << j) & dead) == 0)
						for (int k = j + 1; k < 52; k++)
							if (((one << k) & dead) == 0)
								for (int l = k + 1; l < 52; l++)
									if (((one << l) & dead) == 0)
										for (int m = l + 1; m < 52; m++)
											if (((one << m) & dead) == 0) {
												c[2] = i;
												c[3] = j;
												c[4] = k;
												c[5] = l;
												c[6] = m;
												Hand h = new Hand(c);
												res[Valuation.value(h.evaluate())]++;
											}
		return res;
	}

	public static int[] runThreeCard(int[] c, long dead, UpdateProgress updateProgress) {
		int res[] = new int[Valuation.NUM_RANKINGS];
		int lastProgress = 0;
		for (int i = 0; i < 52; i++) {
			int progress = (100 * i) / 52;
			if (lastProgress != progress) {
				lastProgress = progress;

				if (updateProgress != null) {
					updateProgress.updateProgress(progress);
				}
			}
			if (((one << i) & dead) == 0)
				for (int j = i + 1; j < 52; j++)
					if (((one << j) & dead) == 0)
						for (int k = j + 1; k < 52; k++)
							if (((one << k) & dead) == 0)
								for (int l = k + 1; l < 52; l++)
									if (((one << l) & dead) == 0) {
										c[3] = i;
										c[4] = j;
										c[5] = k;
										c[6] = l;
										Hand h = new Hand(c);
										res[Valuation.value(h.evaluate())]++;
									}
		}
		return res;
	}

	public static int[] runFourCard(int[] c, long dead, UpdateProgress updateProgress) {
		int res[] = new int[Valuation.NUM_RANKINGS];
		int lastProgress = 0;
		for (int i = 0; i < 52; i++) {
			int progress = (100 * i) / 52;
			if (lastProgress != progress) {
				lastProgress = progress;

				if (updateProgress != null) {
					updateProgress.updateProgress(progress);
				}
			}
			if (((one << i) & dead) == 0)
				for (int j = i + 1; j < 52; j++)
					if (((one << j) & dead) == 0)
						for (int k = j + 1; k < 52; k++)
							if (((one << k) & dead) == 0) {
								c[4] = i;
								c[5] = j;
								c[6] = k;
								Hand h = new Hand(c);
								res[Valuation.value(h.evaluate())]++;
							}
		}
		return res;
	}

	public static int[] runFiveCard(int[] c, long dead) {
		int res[] = new int[Valuation.NUM_RANKINGS];
		for (int i = 0; i < 52; i++)
			if (((one << i) & dead) == 0)
				for (int j = i + 1; j < 52; j++)
					if (((one << j) & dead) == 0) {
						c[5] = i;
						c[6] = j;
						Hand h = new Hand(c);
						res[Valuation.value(h.evaluate())]++;
					}
		return res;
	}

	public static int[] runSixCard(int[] c, long dead) {
		int res[] = new int[Valuation.NUM_RANKINGS];
		for (int i = 0; i < 52; i++)
			if (((one << i) & dead) == 0) {
				c[6] = i;
				Hand h = new Hand(c);
				res[Valuation.value(h.evaluate())]++;
			}
		return res;
	}

	public static int[] runSevenCard(int[] c, long dead) {
		int res[] = new int[Valuation.NUM_RANKINGS];
		{
			Hand h = new Hand(c);
			res[Valuation.value(h.evaluate())]++;
		}
		return res;
	}

}
