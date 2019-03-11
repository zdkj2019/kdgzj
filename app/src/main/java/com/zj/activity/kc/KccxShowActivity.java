package com.zj.activity.kc;

import java.util.Map;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.zj.R;
import com.zj.activity.FrameActivity;
import com.zj.cache.DataCache;
import com.zj.cache.ServiceReportCache;
import com.zj.common.Constant;

/**
 * 库存查询展示
 * 
 * @author hs
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class KccxShowActivity extends FrameActivity {

	private Button confirm, cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kcxxshow);
		initVariable();
		initView();
		initListeners();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void initVariable() {
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");
		
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());
		((TextView) findViewById(R.id.tv_1)).setText(itemmap.get("jgmc").toString());
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("jgmc_1").toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("hpmc").toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("jldw").toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("dqkc").toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("sqjc").toString());
	}

	@Override
	protected void initListeners() {
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
					onBackPressed();
					break;
				}
				
			}
		};
		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
	}

	@Override
	protected void getWebService(String s) {
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;

			case Constant.SUCCESS:
			
				break;

			case Constant.FAIL:
				dialogShowMessage_P("你查询的时间段内，没有数据",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}

						});
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
