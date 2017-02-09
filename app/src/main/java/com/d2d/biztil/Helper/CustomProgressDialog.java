package com.d2d.biztil.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import com.d2d.biztil.R;


public class CustomProgressDialog extends ProgressDialog {

	public static String GREY_COLOR = "#d3d3d3";
	public static String THEME_COLOR = "#0c6790";
	public static ProgressDialog ctor(Context context) {
		CustomProgressDialog dialog = new CustomProgressDialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setIndeterminate(false);
		dialog.setCancelable(false);
		// dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		// dialog.getWindow().setDimAmount(0);

		return dialog;
	}

	public CustomProgressDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_progress);

		ProgressWheel progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
		progressWheel.setRimColor(Color.parseColor(GREY_COLOR));
		progressWheel.setBarColor(Color.parseColor(THEME_COLOR));

	}

	@Override
	public void show() {
		if (super.isShowing()) {
			super.dismiss();
		} else {
			super.show();
		}
	}

	@Override
	public void dismiss() {

		if (super.isShowing()) {
			super.dismiss();
		}
	}
}
