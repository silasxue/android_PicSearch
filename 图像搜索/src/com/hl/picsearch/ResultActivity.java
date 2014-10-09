package com.hl.picsearch;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import tool.FileUtil;
import tool.FtpUtil;
import tool.StaticParameter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {
	private ImageView result_img = null;
	private TextView result_productName = null; // ��Ʒ��
	private TextView result_brandName = null;// Ʒ������
	private TextView result_companyName = null;// ��˾��
	private TextView result_originAddr = null;// ������
	private TextView result_originTime = null;// ����ʱ��
	String ftp_dest_path = "";
	String localFullPath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_result);
		result_img = (ImageView) findViewById(R.id.result_img);
		result_productName = (TextView) findViewById(R.id.result_productName);
		result_brandName = (TextView) findViewById(R.id.result_brandName);
		result_companyName = (TextView) findViewById(R.id.result_companyName);
		result_originAddr = (TextView) findViewById(R.id.result_originAddr);
		result_originTime = (TextView) findViewById(R.id.result_originTime);
		Intent intent = getIntent();
		String returnStr = intent.getStringExtra("returnStr");
		ftp_dest_path = intent.getStringExtra("ftp_dest_path");
		show(returnStr);// ͨ�������������xml�ļ���url��ý��
	}

	private void show(String returnStr) {
		if ("null".equals(returnStr)) {
			return;
		}
		String[] arra = returnStr.split("~");
		if (arra.length == 6) {
			result_productName.setText(arra[1]);
			result_brandName.setText(arra[2]);
			result_companyName.setText(arra[3]);
			result_originAddr.setText(arra[4]);
			result_originTime.setText(arra[5]);
			// ����picUrl��ͼƬͨ��ftp���ص��ͻ��ˣ�����ʾҳ��ʹ�á�
			try {
				String fileName = UUID.randomUUID() + ".jpg";
				byte[] data = FtpUtil.getFileByte(StaticParameter.FTP_IP, StaticParameter.FTP_PORT, StaticParameter.FTP_USERName, StaticParameter.FTP_USERPWD, ftp_dest_path, arra[0], null);
				FileUtil.save(getApplicationContext(), data, fileName);
				localFullPath = "/data/data/com.hl.picsearch/files/" + fileName;
				Bitmap bitmap = BitmapFactory.decodeFile(localFullPath);
				result_img.setImageBitmap(bitmap);
			} catch (Exception e) {
			}
			// Bitmap bitmap = BitmapFactory.decodeFile("http://api.androidhive.info/music/images/rihanna.png");
			// result_img.setImageBitmap(returnBitMap("http://192.168.1.112:8088/picsearch/10.jpg"));// ��ʾͼƬ
		}
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:ͼ������
	 * @Full_path:com.hl.picsearch.ResultActivity.java
	 * @Date:@2014 2014-9-5 ����2:22:21
	 * @Return_type:Bitmap
	 * @Desc :����http �� url���Bitmap
	 */
	public Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ɾ����ftp���ص�ͼƬ
		FileUtil fileUtil = new FileUtil(this.getBaseContext());
		fileUtil.delFile(localFullPath);
		FtpUtil.disConnctFtp();// �ر�FTP
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:ͼ������
	 * @Full_path:com.hl.picsearch.ResultActivity.java
	 * @Date:@2014 2014-9-5 ����2:22:04
	 * @Return_type:void
	 * @Desc : ������һ��acticity
	 */
	public void returnPhoto(View view) {
		Intent intent = new Intent();
		intent.setClass(ResultActivity.this, TakePicActivity.class);
		startActivity(intent);
		new FileUtil(this.getBaseContext()).delFile(localFullPath);
		FtpUtil.disConnctFtp();// �ر�FTP����
	}
}
