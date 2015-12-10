package com.pawelgorny.oddscalculator.view;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.pawelgorny.oddscalculator.Constants;
import com.pawelgorny.oddscalculator.R;
import com.pawelgorny.oddscalculator.model.CardColor;
import com.pawelgorny.oddscalculator.model.CardNumber;

public class CardAdapter extends BaseAdapter {

	private final int[] allCards = new int[Constants.NUMBER_OF_SUITS * Constants.NUMBER_OF_RANKS];
	private final static StringBuilder builder = new StringBuilder();
	private final static String DRAWABLE_NAME = "drawable";
	private static final int VALUE_ID = R.id.value;
	private static final String UNKNOWN = "unknown";

	private final Context context;
	private final Resources res;
	private final String packageName;
	private int[] dataSource = new int[0];

	public CardAdapter(Context context) {
		this.context = context;
		this.res = context.getResources();
		this.packageName = context.getPackageName();

		for (int c = 0; c < CardColor.length; c++) {
			for (int r = 1; r <= CardNumber.length; r++) {
				int nr = (c * CardNumber.length + r);
				allCards[nr - 1] = nr;
			}
		}
	}

	@Override
	public int getCount() {
		return dataSource.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return dataSource[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int card = (int) getItemId(position);

		if (convertView == null) {
			convertView = prepareView();
		}

		fillView((ImageView) convertView, card);

		return convertView;
	}

	private View prepareView() {
		final ImageView result = new ImageView(context);
		result.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		result.setScaleType(ImageView.ScaleType.CENTER_CROP);
		result.setPadding(10, 10, 10, 10);

		return result;
	}

	private void fillView(ImageView convertView, int card) {
		final int resId = findResId(card, res, packageName);
		convertView.setImageResource(resId);
		convertView.setTag(VALUE_ID, card);
	}

	public static int findResId(int card, final Resources res, final String packageName) {
		builder.setLength(0);

		if (card == 0) {
			builder.append(UNKNOWN);
		} else {
			builder.append(CardColor.getName(card));
			builder.append('_');
			builder.append(CardNumber.getName(card));
		}

		final int resId = res.getIdentifier(builder.toString(), DRAWABLE_NAME, packageName);
		return resId;
	}

	public void exclude(List<Integer> selectedCards) {
		dataSource = new int[allCards.length - selectedCards.size()];

		int index = 0;
		for (int i = 0; i < allCards.length; ++i) {
			final int value = allCards[i];
			if (!selectedCards.contains(value)) {
				dataSource[index] = value;
				++index;
			}
		}

		notifyDataSetChanged();
	}
}
