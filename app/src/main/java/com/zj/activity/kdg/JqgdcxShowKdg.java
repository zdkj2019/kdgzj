package com.zj.activity.kdg;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zj.R;
import com.zj.activity.FrameActivity;
import com.zj.activity.main.MainActivity;
import com.zj.cache.DataCache;
import com.zj.cache.ServiceReportCache;
/**
 * 快递柜-近期工单查询-数据展示
 * @author zdkj
 *
 */
public class JqgdcxShowKdg extends FrameActivity {

	private Button confirm,cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_jqgdcxshow);
		initVariable();
		initView();
		initListeners();
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

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

		String zbh = itemmap.get("zbh").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(zbh);
		((TextView) findViewById(R.id.tv_axdh)).setText(itemmap.get("sx").toString());
		((TextView) findViewById(R.id.tv_xqmc)).setText(itemmap.get("qy").toString());
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("xqmc").toString());
		((TextView) findViewById(R.id.tv_ywlx)).setText(itemmap.get("xxdz").toString());
		((TextView) findViewById(R.id.tv_sblx)).setText(itemmap.get("gzxx").toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("ywlx").toString());
		((TextView) findViewById(R.id.tv_ds)).setText(itemmap.get("bz").toString());

	}

	@Override
	protected void initListeners() {
		//
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
				default:
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
//
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		finish();
//	}

}
