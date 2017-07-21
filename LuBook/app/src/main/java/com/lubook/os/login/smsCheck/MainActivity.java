package com.lubook.os.login.smsCheck;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lubook.os.R;
import com.lubook.os.login.smsCheck.dao.RestDao;
import com.lubook.os.login.smsCheck.tools.NetWorkTools;


public class MainActivity extends Activity {

	private EditText editText;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_check);
		initViews();
	}

	private void initViews() {
		editText = (EditText) findViewById(R.id.et_inputNum);
		button = (Button) findViewById(R.id.btn_ckeck);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (NetWorkTools.isNetWorkConnected(getApplication())) {
					Log.e("aaa", "aaa-===有！！！！！！WIFIXX");
					String num = editText.getText().toString().trim();
					checkNum(num);
				} else {
					Log.e("aaa", "aaa---无网络连接");
				}

			}
		});
	}

	protected void checkNum(final String num) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e("aaa", "num-->" + num);
				String checkNum = new RestDao().checkNum(num);
				Log.e("aaa", "checkNum==》" + checkNum);
				// checkNum==》{"resp":{"respCode":"000000","templateSMS":{"createDate":"20151223102143","smsId":"96544282fb642b3fa98163e77c0a3968"}}}

			}
		}).start();

	}
}
