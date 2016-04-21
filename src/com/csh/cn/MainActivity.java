package com.csh.cn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.csh.cn.calculator.CalculatorC;
import com.csh.cn.sqlite.MyCursor;
import com.csh.cn.sqlite.MyData;
import com.csh.cn.sqlite.MyDatabaseHelper;
import com.csh.cn.sqlite.MySQLiteOperator;
import com.csh.cn.surface.StartView;

public class MainActivity extends Activity {

	private StartView startView;
	private Button startCalculator;

	// 最大的保存数据的个数
	private final static int MAX_SAVE = 20;

	// 与C交换数据
	private CalculatorC calculatorC;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_layout);

		ActionBar actionBar = super.getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.top_background));

		// 判断是否第一次使用软件
		SharedPreferences shared = super.getSharedPreferences("sudocalculator",
				Context.MODE_PRIVATE);
		int number = shared.getInt("number", 0);
		if (number == 0) {
			SharedPreferences.Editor editor = shared.edit();
			editor.putInt("number", 1);
			editor.commit();

			new MyDatabaseHelper(this, 0);
		}

		this.startView = (StartView) super.findViewById(R.id.startView1);
		this.startCalculator = (Button) super.findViewById(R.id.button1);

		this.startCalculator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startView.setIsCalculator(true);
				int[][] temp = calculatorC.getCalcuator(startView.getData());
				startView.setData(temp);
				startView.setIsCalculator(false);
			}
		});

		calculatorC=new CalculatorC();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		int[][] data = this.startView.getData();
		String message = "";
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				message += data[j][i];
			}
		}
		SharedPreferences shared = super.getSharedPreferences("sudocalculator",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString("data", message);
		editor.putBoolean("isdata", true);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_help:
			this.showHelp();
			break;
		case R.id.action_clear:
			this.startView.clearData();
			break;
		case R.id.action_jieping:
			int type = this.jiePing();
			if (type == -1) {
				Toast.makeText(this, R.string.activity_jieping_failed,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, R.string.activity_jieping_success,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.action_save:
			this.saveData();
			Toast.makeText(this, R.string.activity_save_info,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_lishi:
			this.showDialogLiShi();
			break;
		case R.id.action_random:
			int[][] data = new int[9][9];
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					data[i][j] = 0;
				}
			}
			startView.setIsCalculator(true);
			int[][] temp = calculatorC.getCalcuator(data);
			startView.setData(temp);
			startView.setIsCalculator(false);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	// 弹出一个帮助界面
	private void showHelp() {
		View view = View.inflate(this, R.layout.help_info, null);
		Dialog dialog = new AlertDialog.Builder(this)
				.setTitle(R.string.activity_dialog_hempinfo)
				.setView(view)
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						}).create();
		dialog.show();
	}

	// 弹出一个历史对话框可以供选择
	private void showDialogLiShi() {
		MyDatabaseHelper helper = new MyDatabaseHelper(this, 2);
		MyCursor cursor = new MyCursor(helper.getReadableDatabase());
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<MyData> myData = cursor.getData();
		for (int i = 0; i < myData.size(); i++) {
			MyData data = myData.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", this.buLing(i + 1));
			map.put("shijian", data.getShijian());
			map.put("message", data.getMessage());
			list.add(map);
			Log.i("csh", "---" + list);
		}

		if (list.size() == 0) {
			List<String> temp = new ArrayList<String>();
			temp.add(getString(R.string.activity_tishi_info));
			ListView listView = new ListView(this);
			listView.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, temp));
			final Dialog dialog = new AlertDialog.Builder(this)
					.setTitle(R.string.activity_dialog_lishililu)
					.setView(listView)
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							}).create();
			dialog.show();
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		} else {
			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.listview_layout, new String[] { "id", "shijian" },
					new int[] { R.id.textView1, R.id.textView2 });
			ListView listView = new ListView(this);
			listView.setAdapter(adapter);
			final Dialog dialog = new AlertDialog.Builder(this)
					.setTitle(R.string.activity_dialog_lishililu)
					.setView(listView)
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							}).create();
			dialog.show();

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {
					// TODO Auto-generated method stub
					// 取消对话框
					// dialog.dismiss();
					// 弹出popup菜单
					PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
					popupMenu.getMenuInflater().inflate(R.menu.popup,
							popupMenu.getMenu());
					popupMenu.show();
					popupMenu
							.setOnMenuItemClickListener(new OnMenuItemClickListener() {

								@Override
								public boolean onMenuItemClick(MenuItem item) {
									// TODO Auto-generated method stub
									switch (item.getItemId()) {
									case R.id.lookup:
										dialog.dismiss();
										// 更新数据
										int[][] data = MainActivity.this
												.stringToArray(list
														.get(position)
														.get("message")
														.toString());
										MainActivity.this.startView
												.setData(data);
										break;
									case R.id.delete:
										dialog.dismiss();
										MyDatabaseHelper helper = new MyDatabaseHelper(
												MainActivity.this, 2);
										MySQLiteOperator operator = new MySQLiteOperator(
												helper.getWritableDatabase());
										operator.delete(list.get(position)
												.get("shijian").toString());

										Toast.makeText(
												MainActivity.this,
												R.string.activity_delete_message,
												Toast.LENGTH_SHORT).show();
										break;
									}
									return false;
								}
							});
				}
			});
		}

	}

	// 补零操作
	private String buLing(int id) {
		if (id < 10) {
			return "0" + String.valueOf(id);
		} else {
			return String.valueOf(id);
		}
	}

	// 保存数据
	private void saveData() {
		MyDatabaseHelper helper = new MyDatabaseHelper(this, 2);
		MyCursor myCursor = new MyCursor(helper.getReadableDatabase());
		int count = myCursor.getCount();
		if (count > MAX_SAVE) {
			Toast.makeText(this, R.string.activity_save_max, Toast.LENGTH_SHORT)
					.show();
		} else {
			String message = this.arrayToString(this.startView.getData());
			MySQLiteOperator operator = new MySQLiteOperator(
					helper.getWritableDatabase());
			operator.insert(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.format(new Date()), message);

			Toast.makeText(MainActivity.this, R.string.activity_save_message,
					Toast.LENGTH_SHORT).show();
		}
	}

	// 把数组转化成字符串
	private String arrayToString(int[][] data) {
		String s = "";
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				s += data[i][j];
			}
		}

		return s;
	}

	// 把字符串转化成数组
	private int[][] stringToArray(String s) {
		if (s.length() != 81) {
			return null;
		} else {
			int[][] data = new int[9][9];
			for (int i = 0; i < s.length(); i++) {
				data[i / 9][i % 9] = Integer.parseInt(s.substring(i, i + 1));
			}

			return data;
		}
	}

	// 截屏
	private int jiePing() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS",
				Locale.CHINA);

		String fname = File.separator + "SudoCalculator" + File.separator
				+ format.format(new Date()) + ".png";
		File file = null;
		// 判断SDka
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return -1;
		} else {
			file = new File(Environment.getExternalStorageDirectory()
					.toString() + File.separator + fname);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
		}

		View view = getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		FileOutputStream fileOutputStream = null;
		if (bitmap != null) {
			try {
				fileOutputStream = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100,
						fileOutputStream);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			} finally {
				if (fileOutputStream != null) {
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			return -1;
		}

		return 1;
	}
}
