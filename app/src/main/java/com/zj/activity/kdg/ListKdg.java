package com.zj.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.zj.R;
import com.zj.activity.FrameActivity;
import com.zj.activity.kc.ThcxShowActivity;
import com.zj.activity.login.LoginActivity;
import com.zj.activity.main.MainActivity;
import com.zj.activity.w.Pqzgjk;
import com.zj.activity.w.PqzgjslShow;
import com.zj.cache.DataCache;
import com.zj.cache.ServiceReportCache;
import com.zj.common.Constant;
import com.zj.utils.Config;

/**
 * 快递柜-通用List
 * 
 * @author zdkj
 *
 */
public class ListKdg extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> datalist;
	private String[] from;
	private int[] to;
	private int queryType = DataCache.getinition().getQueryType();
	private String cs, status = "1";
	private SharedPreferences spf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
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
		datalist = new ArrayList<Map<String, Object>>();
		from = new String[] { "textView1", "faultuser", "zbh", "timemy",
				"datemy", "ztzt" };
		to = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy,
				R.id.datemy, R.id.ztzt };
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
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
					finish();
					break;

				}
			}
		};

		topBack.setOnClickListener(onClickListener);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ServiceReportCache.setIndex(position);
				if (position >= 0) {
					Intent intent = null;
					if (queryType == 201) {
						intent = new Intent(ListKdg.this, JdxyKdg.class);
					} else if (queryType == 203) {
						intent = new Intent(ListKdg.this, SmdwKdg.class);
					} else if (queryType == 204) {
						intent = new Intent(ListKdg.this, FwbgKdg.class);
					} else if (queryType == 2041) {
						intent = new Intent(ListKdg.this, FwbgXgKdg.class);
					} else if (queryType == 205) {
						intent = new Intent(ListKdg.this, JqgdcxShowKdg.class);
					} else if (queryType == 2301) {
						intent = new Intent(ListKdg.this, JdxyXj.class);
					} else if (queryType == 2302) {
						intent = new Intent(ListKdg.this, SmdwXj.class);
					} else if (queryType == 2303) {
						intent = new Intent(ListKdg.this, FwbgXj.class);
					} else if (queryType == 2304) {
						intent = new Intent(ListKdg.this, FwbgXgXj.class);
					} else if (queryType == 2305) {
						intent = new Intent(ListKdg.this, SmdwXj.class);
					} else if (queryType == 2101) {
						intent = new Intent(ListKdg.this, DhjcKdg.class);
					} else if (queryType == 2201) {
						intent = new Intent(ListKdg.this, JdxyKdgWx.class);
					} else if (queryType == 2202) {
						intent = new Intent(ListKdg.this, SmdwKdgWx.class);
					} else if (queryType == 2203) {
						intent = new Intent(ListKdg.this, FwbgKdgWx.class);
					} else if (queryType == 2204) {
						intent = new Intent(ListKdg.this, ZzzpKdgWx.class);
					} else if (queryType == 2205) {
						intent = new Intent(ListKdg.this, FwbgXgKdgWx.class);
					} else if (queryType == 2501) {
						intent = new Intent(ListKdg.this, Pqzgjk.class);
					} else if (queryType == 2801) {
						intent = new Intent(ListKdg.this,
								ThcxShowActivity.class);
					}
					startActivityForResult(intent, 1);
				}

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			int position = ServiceReportCache.getIndex();
			datalist.remove(position);
			adapter.notifyDataSetChanged();
			if (datalist.size() == 0) {
				Message msg = new Message();
				msg.what = FAIL;// 失败
				handler.sendMessage(msg);
			}
		}
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {
				String sqlid = "";
				int queryType = DataCache.getinition().getQueryType();
				String userid = spf.getString("userId", "");
				cs = userid;
				if (queryType == 201) {
					sqlid = "_PAD_SHGL_KDG_AZ_JDXY";
				} else if (queryType == 202) {
					sqlid = "";
				} else if (queryType == 203) {
					sqlid = "_PAD_SHGL_KDG_AZ_SMDW";
				} else if (queryType == 204) {
					sqlid = "_PAD_SHGL_KDG_AZ_FWBG";
				} else if (queryType == 2041) {
					sqlid = "_PAD_KDGAZ_FWBG_XG";
				} else if (queryType == 205) {
					status = getIntent().getStringExtra("status");
					if ("1".equals(status)) { // 已完工
						sqlid = "_PAD_SHGL_KDG_GDALL";
						cs = userid + "*" + userid;
					} else {// 未完工
						sqlid = "_PAD_SHGL_KDG_GDALL_A";
						cs = userid + "*" + userid;
					}
				} else if (queryType == 2301) {
					sqlid = "_PAD_KDG_XJ_JDXY";
				} else if (queryType == 2302) {
					sqlid = "_PAD_KDG_XJ_SMDW";
				} else if (queryType == 2303) {
					sqlid = "_PAD_KDG_XJ_FWBG";
				} else if (queryType == 2304) {
					sqlid = "_PAD_KDG_XJ_FWBG_XG";
				} else if (queryType == 2305) {
					sqlid = "_PAD_KDG_XJ_SMDW_ZD";
					cs = getIntent().getStringExtra("status");
				} else if (queryType == 2101) {
					sqlid = "_PAD_SHGL_KDG_AZ_DHJC";
				} else if (queryType == 2201) {
					sqlid = "_PAD_SHGL_KDG_WX_JDXY";
				} else if (queryType == 2202) {
					sqlid = "_PAD_SHGL_KDG_WX_SMDW";
				} else if (queryType == 2203) {
					sqlid = "_PAD_SHGL_KDG_WX_FWBG";
				} else if (queryType == 2204) {
					cs = userid + "*" + userid;
					sqlid = "_PAD_SHGL_KDG_ZZZP";
				} else if (queryType == 2205) {
					sqlid = "_PAD_KDGAZ_FWBG_XG2";
				} else if (queryType == 2501) {

					int status = getIntent().getIntExtra("status", 1);
					int sj = getIntent().getIntExtra("sj", 1);
					if (status == 1) {// 已完工
						sqlid = "_PAD_KDG_ZZPQGD_YWG";
						if (sj == 1) {// 本月
							cs = userid + "*" + userid + "*" + userid + "*"
									+ userid + "*0*0";
						} else {// 上月
							cs = userid + "*" + userid + "*" + userid + "*"
									+ userid + "*-1*-1";
						}

					} else {// 未完工
						sqlid = "_PAD_KDG_ZZPQGD_WWG";
						cs = userid + "*" + userid + "*" + userid + "*"
								+ userid;
					}

				} else if (queryType == 2801) {
					sqlid = "_PAD_DBCK_THCX1";
					cs = userid + "*" + userid;
				}

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						String timeff = "";
						item.put("textView1", getListItemIcon(i));
						if (queryType == 201 || queryType == 203
								|| queryType == 204 || queryType == 205) {// 接单相应
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
						}
						if (queryType == 204) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sfecgd", temp.getString("sfecgd"));
							item.put("sbewm", temp.getString("sbewm"));
							item.put("dygdh", temp.getString("dygdh"));
							item.put("fwnr", temp.getString("fwnr"));
						}
						if (queryType == 2041) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sbewm", temp.getString("sbewm"));
							item.put("fwnr", temp.getString("fwnr"));
						}
						if (queryType == 2203) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("fwnr", temp.getString("fwnr"));
							item.put("sfecgd", temp.getString("sfecgd"));
							item.put("sfecsm", temp.getString("sfecsm"));
							item.put("ecsmyy", temp.getString("ecsmyy"));
							item.put("kzzf7", temp.getString("kzzf7"));
							item.put("kzzf3_bm", temp.getString("kzzf3_bm"));
							item.put("kzzf4_bm", temp.getString("kzzf4_bm"));
							item.put("kzzf5_bm", temp.getString("kzzf5_bm"));
						}
						if (queryType == 2204) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("ds", temp.getString("ds"));
							item.put("fwnr", temp.getString("fwnr"));
						}
						if (queryType == 2202) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sbewm", temp.getString("sbewm"));
						}
						if (queryType == 2501) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("jbf", temp.getString("jbf"));
							item.put("jbfdh", temp.getString("jbfdh"));
							item.put("sfcs", temp.getString("sfcs"));
							item.put("csyylx", temp.getString("csyylx"));
							item.put("csnr", temp.getString("csnr"));
							item.put("scsj", temp.getString("scsj"));
							item.put("ddsj", temp.getString("ddsj"));
							item.put("sfwzzm", temp.getString("sfwzzm"));
						}
						if (queryType == 2041 || queryType == 2205) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sfecsm", temp.getString("sfecsm"));
							item.put("ecsmyy", temp.getString("ecsmyy"));
						}

						if (queryType == 2205) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("kzzf3", temp.getString("kzzf3"));
							item.put("kzzf4", temp.getString("kzzf4"));
							item.put("kzzf5", temp.getString("kzzf5"));
							item.put("kzzf3_bm", temp.getString("kzzf3_bm"));
							item.put("kzzf4_bm", temp.getString("kzzf4_bm"));
							item.put("kzzf5_bm", temp.getString("kzzf5_bm"));
							item.put("kzzf7", temp.getString("kzzf7"));
							item.put("fwnr", temp.getString("fwnr"));
							item.put("sfecgd", temp.getString("sfecgd"));

						}

						if (queryType == 2801) {
							item.put("textView1", getListItemIcon(i));
							timeff = temp.getString("zdrq");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("sgdh"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sgdh", temp.getString("sgdh"));
							item.put("bz", temp.getString("bz"));
						}

						timeff = timeff.substring(2);
						item.put("timemy", mdateformat(1, timeff));// 时间
						item.put("datemy", mdateformat(0, timeff));// 年月日

						datalist.add(item);
					}
					ServiceReportCache.setObjectdata(datalist);
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
		} else {
			Message msg = new Message();
			msg.what = FAIL;
			handler.sendMessage(msg);
		}

	}

	// @Override
	// public void onBackPressed() {
	// Intent intent = new Intent(this, MainActivity.class);
	// startActivity(intent);
	// finish();
	// }

	private class CurrAdapter extends SimpleAdapter {

		public CurrAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);

		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			final View view = super.getView(position, convertView, parent);
			try {
				Map<String, Object> item = datalist.get(position);
				String sfcs = (String) item.get("sfcs");
				if ("是".equals(sfcs)) {
					view.setBackgroundResource(R.color.red);
					// view.setBackgroundResource(R.color.yellow);
				} else if ("即将超时".equals(sfcs)) {
					view.setBackgroundResource(R.color.yellow);
				} else {
					view.setBackgroundResource(R.color.white);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
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
				adapter = new CurrAdapter(ListKdg.this,
						ServiceReportCache.getObjectdata(),
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
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

	};

}
