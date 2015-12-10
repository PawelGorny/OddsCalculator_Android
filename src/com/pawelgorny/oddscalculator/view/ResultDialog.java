package com.pawelgorny.oddscalculator.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pawelgorny.oddscalculator.R;

public class ResultDialog extends DialogFragment {

	private int[] counts;
	private int sum;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dismiss();
			}
		}).setView(createView());
		final Dialog result = builder.create();

		return result;
	}

	private View createView() {
		final Context context = getActivity();
		final StringBuilder builder = new StringBuilder();

		final LinearLayout view = new LinearLayout(context);
		view.setOrientation(LinearLayout.VERTICAL);
		view.setBackgroundColor(Color.WHITE);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		view.setPadding(10, 10, 10, 10);

		for (int i = 0; i < counts.length; i++) {
			if (counts[i] == 0)
				continue;
			float procf = ((float) 100 * counts[i]) / sum;
			int proc = Math.round(procf);

			builder.setLength(0);
			builder.append(getName(i));
			builder.append(' ');

			if (proc == 0) {
				builder.append("~0");
			} else if (procf < 1) {
				builder.append("<1");
			} else {
				builder.append(proc);
			}
			builder.append("%");

			final TextView textView = new TextView(context, null, android.R.attr.textAppearanceMedium);
			textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			textView.setText(builder.toString());

			final ProgressBar progressView = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
			progressView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			progressView.setMax(sum);
			progressView.setProgress(counts[i]);

			view.addView(textView);
			view.addView(progressView);
		}

		return view;
	}

	public void setContextData(int[] counts, int sum) {
		this.counts = counts;
		this.sum = sum;
	}

	private String getName(int i) {
		final Resources res = getActivity().getResources();
		final String result;

		switch (i) {
			case 0:
				result = res.getString(R.string.result_nopair);
				break;
			case 1:
				result = res.getString(R.string.result_pair);
				break;
			case 2:
				result = res.getString(R.string.result_2pairs);
				break;
			case 3:
				result = res.getString(R.string.result_three);
				break;
			case 4:
				result = res.getString(R.string.result_straight);
				break;
			case 5:
				result = res.getString(R.string.result_flush);
				break;
			case 6:
				result = res.getString(R.string.result_full);
				break;
			case 7:
				result = res.getString(R.string.result_four);
				break;
			case 8:
				result = res.getString(R.string.result_straightflush);
				break;
			default:
				result = "";
				break;
		}

		return result;
	}
}
