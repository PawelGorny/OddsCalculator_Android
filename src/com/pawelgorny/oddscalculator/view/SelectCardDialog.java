package com.pawelgorny.oddscalculator.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.pawelgorny.oddscalculator.R;
import com.pawelgorny.oddscalculator.view.TableActivity.ContextData;

public class SelectCardDialog extends DialogFragment {

	public interface OnSelectedCard {
		public void selectedCard(final int value);
	}

	private static final int VALUE_ID = R.id.value;

	private OnSelectedCard selectCardListener;
	private ContextData contextData;
	private Button clear;
	private CardAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.dialog_select_card, container, false);
		final GridView grid = (GridView) view.findViewById(R.id.gridView);
		clear = (Button) view.findViewById(R.id.clear);

		// clear always have 0!
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectCardListener != null) {
					selectCardListener.selectedCard(0);
				}
				dismiss();
			}
		});

		adapter = new CardAdapter(getActivity());
		adapter.exclude(contextData.selectedCards);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (selectCardListener != null) {
					final int value = (Integer) arg1.getTag(VALUE_ID);
					selectCardListener.selectedCard(value);
				}
				dismiss();
			}
		});

		return view;
	}

	public void setSelectCardListener(OnSelectedCard selectCardListener) {
		this.selectCardListener = selectCardListener;
	}

	public void setContextData(ContextData contextData) {
		this.contextData = contextData;
		if (adapter != null) {
			adapter.exclude(contextData.selectedCards);
		}
	}

	public ContextData getContextData() {
		return contextData;
	}
}
