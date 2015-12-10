package com.pawelgorny.oddscalculator;

import jpoker.poker.Card;
import jpoker.poker.Operation;
import jpoker.poker.UpdateProgress;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;

import com.pawelgorny.oddscalculator.view.ResultDialog;

public class CalculateTask extends AsyncTask<Void, Integer, Result> implements UpdateProgress {

	private static final String RESULT_DIALOG = "result_dialog";
	private ProgressDialog dialog;
	private int[] cardsArr;
	private int[] cards;
	private FragmentManager fragmentManager;

	public CalculateTask(int[] cardsArr, Context context, FragmentManager fragmentManager) {
		this.cardsArr = cardsArr;

		this.dialog = new ProgressDialog(context);
		this.dialog.setMessage(context.getResources().getString(R.string.calculating));
		this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		this.dialog.setMax(100);
		this.dialog.setCancelable(false);
		this.dialog.setCanceledOnTouchOutside(false);

		this.fragmentManager = fragmentManager;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.show();
		dialog.setProgress(0);
	}

	@Override
	protected Result doInBackground(Void... arg0) {
		int counts[] = null;
		long dead = 0;
		Card card1 = null;
		Card card2 = null;
		cards = new int[7];

		for (int i = 0; i < cardsArr.length; i++) {
			Card card = new Card(cardsArr[i] - 1);
			cards[i] = card.value();
			dead |= Operation.one << cards[i];
			switch (i) {
				case 0:
					card1 = card;
					break;
				case 1:
					card2 = card;
					break;
			}
		}

		if (cardsArr.length == 2 && card1 != null && card2 != null) {
			if (card1.rank().equals(card2.rank())) {// pair
				counts = jpoker.poker.results.ResultPair.getResults(card1.rank());
			} else if (card1.suit().equals(card2.suit())) {
				counts = jpoker.poker.results.ResultSuited.getResults(card1.rank(), card2.rank());
			} else {
				counts = jpoker.poker.results.ResultOffsuit.getResults(card1.rank(), card2.rank());
			}
		}

		if (counts == null) {
			counts = Operation.calculate(cards, dead, cardsArr.length, this);
		}

		int sum = 0;
		for (int i = 0; i < counts.length; i++) {
			sum += counts[i];
		}

		final Result result = new Result(counts, sum);
		return result;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		final Integer value = values[0];
		dialog.setProgress(value);
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		dialog.setProgress(100);
		dialog.dismiss();

		final ResultDialog resultDialog = new ResultDialog();

		resultDialog.setContextData(result.counts, result.sum);
		resultDialog.show(fragmentManager, RESULT_DIALOG);
	}

	public void updateProgress(final int value) {
		publishProgress(new Integer[] { value });
	}
}
