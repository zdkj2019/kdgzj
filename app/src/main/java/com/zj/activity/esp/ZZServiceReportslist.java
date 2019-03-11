package com.zj.activity.esp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zj.R;
import com.zj.activity.FrameActivity;
import com.zj.activity.main.MainActivity;
import com.zj.cache.DataCache;
import com.zj.cache.ServiceReportCache;
import com.zj.utils.Config;

/**
 * 纸质服务报告choose
 * 
 * @author
 */
@SuppressLint("HandlerLeak")
public class ZZServiceReportslist extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
	private String[] from;
	private int[] to;
	private String picPath1;
	private String fromActivity = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
		initVariable();
		initView();
		initListeners();
		if (!backboolean) {
			showProgressDialog();
		}
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
		data = new ArrayList<Map<String, String>>();

		from = new String[] { "textView1", "网点名称", "oddnumber", "timemy",
				"datemy" };
		to = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy,
				R.id.datemy };

		picPath1 = new String();

		Intent intent = getIntent();
		fromActivity = intent.getStringExtra("fromActivity");
	}

	@Override
	protected void initView() {
		title.setText(DataCache.getinition().getTitle());
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

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				// ServiceReportCache.setIndex(index);

				if (index >= 0) {
					// final int index = arg2;
					String selectId = "" + data.get(index).get("oddnumber");

					Intent in = new Intent(getApplicationContext(),
							BusinessAcceptanceSure.class);
					in.putExtra("picPath1", "");
					in.putExtra("zbh", selectId);
					in.putExtra("fromActivity", fromActivity);
					startActivity(in);
					finish();

				} else {
					// skipActivity(ServiceReportsAppraise.class);
				}
			}
		});
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_FWBG_CX2", "2000-01-01*3000-01-01*"
								+ DataCache.getinition().getUserId() + "*"
								+ DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");

				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("textView1", getListItemIcon(i)+"");
						String timeff = temp
								.getString("shgl_ywgl_fwbgszb_bzsj");
						timeff = timeff.substring(2);
						item.put("no", String.valueOf(i + 1));
						item.put("oddnumber",// 派工单号
								temp.getString("shgl_ywgl_fwbgszb_zbh"));
						item.put("faultuser",
								temp.getString("shgl_ywgl_fwbgszb_bzr"));
						item.put("网点名称", temp.getString("ccgl_wlsjb_bzr"));
						item.put("timemy",// 时间
								mdateformat(1, timeff));

						item.put("datemy",// 年月日
								mdateformat(0, timeff));

						data.add(item);
					}
					ServiceReportCache.setData(data);
					Message msg = new Message();
					msg.what = SUCCESSFUL;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = FAIL;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		} else {
			// 直接加载
			Message msg = new Message();
			msg.what = SUCCESSFUL;// 成功
			handler.sendMessage(msg);
		}

	}

	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
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
				adapter = new SimpleAdapter(ZZServiceReportslist.this,
						ServiceReportCache.getData(),
						R.layout.listview_dispatchinginformationreceiving_item,
						from, to);
				listView.setAdapter(adapter);
				break;

			case FAIL:
				dialogShowMessage_P("你查询的时间段内，没有派工单号",
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

}
