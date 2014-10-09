package com.hl.picsearch;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import tool.FileUtil;
import tool.FtpUtil;
import tool.NetUtil;
import tool.SDCardUtil;
import tool.StaticParameter;
import tool.UtilsSendStr;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class TakePicActivity extends Activity {
	private Camera camera;
	FileUtil fileUtil;
	private static String commFilePath = "";
	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//ǿ��Ϊ����
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//����
		setContentView(R.layout.layout_takepic);
		pb = (ProgressBar) this.findViewById(R.id.wait_bar);
		pb.setVisibility(View.GONE);
		SurfaceView surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
		surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.getHolder().setFixedSize(176, 144);
		surfaceView.getHolder().setKeepScreenOn(true);
		surfaceView.getHolder().addCallback(new SurfaceCallback());
		fileUtil = new FileUtil(this.getBaseContext());
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// System.out.println("onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// System.out.println("onRestart");
		pb = (ProgressBar) this.findViewById(R.id.wait_bar);
		pb.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @author �ص�������
	 * 
	 */
	private final class SurfaceCallback implements Callback {
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				camera = Camera.open();// ������ͷ
				Camera.Parameters parameters = camera.getParameters();
				// System.out.println(parameters.flatten());//���г����������
				parameters.setPictureFormat(PixelFormat.JPEG);// ������Ƭ�ĸ�ʽ
				parameters.setPreviewSize(800, 480);
				parameters.setPreviewFrameRate(5);
				parameters.setPictureSize(640, 480);
//				parameters.setPictureSize(1024, 768);
				parameters.setJpegQuality(80);
				// camera.setDisplayOrientation(270);
				camera.setParameters(parameters);// ��������ͷ�Ĳ���.����ǰ���������Ч
				camera.setPreviewDisplay(holder);
				camera.startPreview();// ��ʼԤ��
				camera.autoFocus(null);// ��ʼ���Խ�
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// android:screenOrientation="sensor"
			// System.out.println("surfaceChanged turn le ");
			if (camera != null) {
				// System.out.println(getWindowManager().getDefaultDisplay().getRotation());
				// System.out.println(Surface.ROTATION_270);
				// int rotation = getWindowManager().getDefaultDisplay().getRotation();
				// int degrees = 0;
				// switch (rotation) {
				// case Surface.ROTATION_0:
				// degrees = 0;
				// break;
				// case Surface.ROTATION_90:
				// degrees = 90;
				// break;
				// case Surface.ROTATION_180:
				// degrees = 180;
				// break;
				// case Surface.ROTATION_270:
				// degrees = 270;
				// break;
				// }
				//
				// camera.setDisplayOrientation(degrees);
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if (camera != null) {
				camera.release();
				camera = null;
			}
		}
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:picsearch
	 * @Full_path:com.hl.picsearch.EntranceActivity.java
	 * @Date:@2014 2014-7-28 ����11:15:48
	 * @Return_type:void
	 * @Desc : ������صİ�ť�¼�
	 */
	public void takepicture(View view) {
		switch (view.getId()) {
		case R.id.take_pic:
			camera.takePicture(null, null, new MyPictureCallback());
			break;
		case R.id.cancle_pic:
			// 1.ɾ���ļ�
			System.out.println("delFile : " + fileUtil.delFile(commFilePath));
			commFilePath = "";
			// 2.ʹ����ɼ�������
			camera.startPreview();
			break;
		case R.id.send_pic:
			if(!NetUtil.isServerSetup("")){
				pb.setVisibility(View.GONE);
				Toast.makeText(TakePicActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
				return;
			}
			
			if (NetUtil.isNetworkAvailable(getApplicationContext())) {// ����ͨ��
				pb.setVisibility(View.VISIBLE);
				
				// 1.�����ļ���������
				FtpUtil.ftpUpload(StaticParameter.FTP_IP, StaticParameter.FTP_PORT, StaticParameter.FTP_USERName, StaticParameter.FTP_USERPWD, StaticParameter.FTP_PIC_PATH, commFilePath);
				// 2. ���������ִ������ʶ����������շ��ؽ����
				String returnStr = "";
				try {
					returnStr = UtilsSendStr.httpPost("http://" + StaticParameter.WEB_IP + ":" + StaticParameter.WEB_PORT + "/" + StaticParameter.APP_NAME + "/PicSearchServlet", "UTF-8", null, new File(commFilePath).getName());
					System.out.println("returnStr : "+returnStr);
				} catch (Exception e) {
				}
				// 3.ɾ���ļ�
				fileUtil.delFile(commFilePath);
				commFilePath = "";
				// 4.��ʾ�������
				if ("null".equals(returnStr)) {
					Toast.makeText(TakePicActivity.this, R.string.no_return_result, Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				Intent intent = new Intent();
				intent.putExtra("returnStr", returnStr);
				intent.putExtra("ftp_dest_path", "pic_search/1/");
				intent.setClass(TakePicActivity.this, ResultActivity.class);
				startActivity(intent);
				// pb.setVisibility(View.GONE);
			} else {
				Toast.makeText(TakePicActivity.this, R.string.net_fail, Toast.LENGTH_LONG).show();
				return;
			}
			break;
		default:
			break;
		}
	}

	@SuppressLint("SdCardPath")
	private final class MyPictureCallback implements PictureCallback {
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				// System.out.println("path : " + Environment.getExternalStorageDirectory().toString());
				if (SDCardUtil.sdCardExist()) {// ��sd�������
					// File jpgFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
					File jpgFile = new File(Environment.getExternalStorageDirectory(), UUID.randomUUID() + ".jpg");
					FileOutputStream outStream = new FileOutputStream(jpgFile);
					outStream.write(data);
					outStream.close();
					commFilePath = jpgFile.getPath();
				} else {
					String fileName = UUID.randomUUID() + ".jpg";
					FileUtil.save(getApplicationContext(), data, fileName);
					commFilePath = "/data/data/com.hl.picsearch/files/" + fileName;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:picsearch
	 * @Full_path:com.hl.picsearch.EntranceActivity.java
	 * @Date:@2014 2014-7-28 ����11:15:48
	 * @Return_type:void
	 * @Desc : ������Ļ�ɶԽ�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			/*
			 * System.out.println("����������Ļ");
			 * 
			 * if (SDCardUtil.sdCardExist()) { System.out.println("����sd������Ŀ¼�ǣ� " + SDCardUtil.sdDir()); }else{ System.out.println("no ����sd�� "); }
			 * 
			 * if (NetUtil.isNetworkAvailable(getApplicationContext())) { System.out.println("�������� ok"); }
			 * 
			 * if (NetUtil.isWifi(getApplicationContext())) { System.out.println("�ж���wifi"); } else { System.out.println("�ж���3g"); } if (NetUtil.isWifiEnabled(getApplicationContext())) {
			 * System.out.println("WIFI had ��"); }
			 * 
			 * if (NetUtil.is3rd(getApplicationContext())) { System.out.println("has 3G����"); }else{ System.out.println("no 3G����"); } if (NetUtil.isGpsEnabled(getApplicationContext())) {
			 * System.out.println("GPS had ��"); }
			 */
			camera.autoFocus(null);
			return true;
		}
		return super.onTouchEvent(event);
	}
}
