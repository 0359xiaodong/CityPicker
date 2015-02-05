package me.jp.citypicker;

import me.jp.citypicker.view.CityPicker;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private CityPicker mCityPicker;
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mCityPicker = (CityPicker) findViewById(R.id.citypicker);
		mTextView = (TextView) findViewById(R.id.textView);
	}

	public void onClick(View view) {
		if (!TextUtils.isEmpty(mCityPicker.getDetailArea()))
			mTextView.setText(mCityPicker.getDetailArea());
	}

}
