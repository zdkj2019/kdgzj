package com.zj.activity.w;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zj.R;
import com.zj.activity.FrameActivity;
import com.zj.activity.main.MainActivity;
import com.zj.cache.DataCache;
import com.zj.cache.ServiceReportCache;
import com.zj.utils.Config;

public class CjwtShowActivity extends FrameActivity {

	private String flag, cs;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data;
	private String[] from;
	private int[] to;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_cjwtshow);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("query");
			}
		});

	}

	@Override
	protected void initVariable() {

		listView = (ListView) findViewById(R.id.listView);
		data = new ArrayList<Map<String, Object>>();
		from = new String[] { "tv_ask", "tv_answer" };
		to = new int[] { R.id.tv_ask, R.id.tv_answer };

	}

	@Override
	protected void initView() {
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		String name = "";
		if (type == 1) {
			name = "快递柜常见问题";
		} else if (type == 2) {
			name = "自助及POS机设备常见问题";
		} else if (type == 3) {
			name = "零部件常见问题";
		} else if (type == 4) {
			name = "结算常见问题";
		} else if (type == 5) {
			name = "APP使用";
		} else if (type == 6) {
			name = "其他";
		}
		title.setText(name);
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;

				}
			}
		};

		topBack.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {
				String sqlid = "_APP_APPXX_WTCX";
				cs = type + "";
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("tv_ask", temp.getString("cjwtnr"));
						item.put("tv_answer",temp.getString("cjwtdan"));
						data.add(item);
					}
					Message msg = new Message();
					msg.what = SUCCESSFUL;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;
			case SUCCESSFUL:
				adapter = new SimpleAdapter(CjwtShowActivity.this, data,
						R.layout.listview_cjwt_item, from, to);
				listView.setAdapter(adapter);
				break;
			case FAIL:
				dialogShowMessage_P("没有数据",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}
						});
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

	};

}
