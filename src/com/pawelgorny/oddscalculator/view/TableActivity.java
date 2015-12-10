package com.pawelgorny.oddscalculator.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pawelgorny.oddscalculator.CalculateTask;
import com.pawelgorny.oddscalculator.Constants;
import com.pawelgorny.oddscalculator.HandCardsNotSelectedException;
import com.pawelgorny.oddscalculator.R;
import com.pawelgorny.oddscalculator.view.SelectCardDialog.OnSelectedCard;

public class TableActivity extends FragmentActivity {
	public class ContextData {
		public final int cardTypeId;
		public final int cardId;
		public final List<Integer> selectedCards;

		public ContextData(int cardTypeId, int cardId, List<Integer> selectedCards) {
			this.cardTypeId = cardTypeId;
			this.cardId = cardId;
			this.selectedCards = selectedCards;
		}
	}

	private static final int handCardCount = 2;
	private static final int tableCardCount = 4;
	private static final int HAND_CARD = 0;
	private static final int TABLE_CARD = 1;
	private static final String SELECT_CARD_DIALOG = "select_card_dialog";
	private static final int NUMBER_ID = R.id.number;
	private static final int VALUE_ID = R.id.value;

	private ImageView[] handCard = new ImageView[handCardCount];
	private ImageView[] tableCard = new ImageView[tableCardCount];

	private SelectCardDialog selectDialog;

	private OnClickListener selectHandCardListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final int cardId = (Integer) v.getTag(NUMBER_ID);
			showSelectCardDialog(HAND_CARD, cardId);
		}
	};

	private OnClickListener selectTableCardListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final int cardId = (Integer) v.getTag(NUMBER_ID);
			showSelectCardDialog(TABLE_CARD, cardId);
		}
	};

	private OnSelectedCard selectCardListener = new OnSelectedCard() {

		@Override
		public void selectedCard(int value) {
			final ContextData contextData = selectDialog.getContextData();
			updateSelectedCard(value, contextData);
		}
	};

	private OnClickListener calculateListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			calculateResult();
		}
	};

	// FIXME: add banner with adverts?
	// private Banner advBanner = new Banner(Constants.ZONE_ID, null);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);

		final LinearLayout handCardLayout = (LinearLayout) findViewById(R.id.handCardLayout);
		for (int i = 0; i < handCardCount; ++i) {
			final ImageView button = prepareImageButton(i, selectHandCardListener);

			handCardLayout.addView(button);
			handCard[i] = button;
		}

		final LinearLayout tableCardLayout = (LinearLayout) findViewById(R.id.tableCardLayout);
		for (int i = 0; i < tableCardCount; ++i) {
			final ImageView button = prepareImageButton(i, selectTableCardListener);

			tableCardLayout.addView(button);
			tableCard[i] = button;
		}

		final Button calculateButton = (Button) findViewById(R.id.calculateButton);
		calculateButton.setOnClickListener(calculateListener);
	}

	protected void calculateResult() {
		int cardsArr[];
		try {
			cardsArr = getCardsValues();

			new CalculateTask(cardsArr, this, getSupportFragmentManager()).execute();
		} catch (HandCardsNotSelectedException e) {
			showErrorDialog();
		}
	}

	private void showErrorDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(R.string.info_cards_not_selected);
		builder.setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		final Dialog dialog = builder.create();
		dialog.show();
	}

	private int[] getCardsValues() throws HandCardsNotSelectedException {
		int index = 0;
		int[] cards = new int[7];

		for (int i = 0; i < handCard.length; i++) {
			final Integer value = (Integer) handCard[i].getTag(VALUE_ID);
			if (handCard[i] != null) {
				if (value.equals(Constants.CARD_CLEAR)) {
					throw new HandCardsNotSelectedException();
				} else {
					cards[index] = value;
					++index;
				}
			}
		}

		for (int i = 0; i < tableCard.length; i++) {
			final Integer value = (Integer) tableCard[i].getTag(VALUE_ID);
			if (value != 0) {
				cards[index] = value;
				++index;
			}
		}

		final int[] result = new int[index];
		System.arraycopy(cards, 0, result, 0, index);

		return result;
	}

	protected void updateSelectedCard(int card, ContextData contextData) {
		final ImageView view;

		switch (contextData.cardTypeId) {
			case HAND_CARD:
				view = handCard[contextData.cardId];
				break;
			case TABLE_CARD:
				view = tableCard[contextData.cardId];
				break;
			default:
				view = null;
				break;
		}

		if (view != null) {
			final int resId = CardAdapter.findResId(card, getResources(), getPackageName());
			view.setImageResource(resId);
			view.setTag(VALUE_ID, card);
		}
	}

	protected void showSelectCardDialog(int cardTypeId, int cardId) {
		if (selectDialog == null) {
			selectDialog = new SelectCardDialog();
			selectDialog.setSelectCardListener(selectCardListener);
		}

		final List<Integer> selectedCards = new ArrayList<Integer>();
		for (int i = 0; i < handCard.length; ++i) {
			final Integer value = (Integer) handCard[i].getTag(VALUE_ID);
			if (value != 0) {
				selectedCards.add(value);
			}
		}
		for (int i = 0; i < tableCard.length; ++i) {
			final Integer value = (Integer) tableCard[i].getTag(VALUE_ID);
			if (value != 0) {
				selectedCards.add(value);
			}
		}

		selectDialog.setContextData(new ContextData(cardTypeId, cardId, selectedCards));
		selectDialog.show(getSupportFragmentManager(), SELECT_CARD_DIALOG);
	}

	private ImageView prepareImageButton(int i, OnClickListener listener) {
		final ImageView button = new ImageView(this);
		button.setImageResource(R.drawable.unknown);
		button.setClickable(true);
		button.setOnClickListener(listener);
		button.setTag(NUMBER_ID, i);
		button.setTag(VALUE_ID, 0);
		button.setPadding(10, 10, 10, 10);

		return button;
	}
}
