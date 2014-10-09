package tool;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetUtil {
	// private Context context;
	//
	// public NetUtil(Context context) {
	// this.context = context;
	// }
	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:newsmanage
	 * @Full_path:cn.itcast.utils.NetUtil.java
	 * @Date:@2014 2014-7-31 ����9:50:01
	 * @Return_type:boolean
	 * @Desc :�ж���wifi����3g����,�û����������������ˣ�wifi�Ϳ��Խ������ػ������߲��š�
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:newsmanage
	 * @Full_path:cn.itcast.utils.NetUtil.java
	 * @Date:@2014 2014-7-31 ����9:50:01
	 * @Return_type:boolean
	 * @Desc :�ж��Ƿ���3G����
	 */
	public static boolean is3rd(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:newsmanage
	 * @Full_path:cn.itcast.utils.NetUtil.java
	 * @Date:@2014 2014-7-31 ����9:50:01
	 * @Return_type:boolean
	 * @Desc :�ж�WIFI�Ƿ��
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:newsmanage
	 * @Full_path:cn.itcast.utils.NetUtil.java
	 * @Date:@2014 2014-7-31 ����9:50:01
	 * @Return_type:boolean
	 * @Desc :�ж�GPS�Ƿ��
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = lm.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:newsmanage
	 * @Full_path:cn.itcast.utils.NetUtil.java
	 * @Date:@2014 2014-7-31 ����9:50:01
	 * @Return_type:boolean
	 * @Desc :�ж����������Ƿ����
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			// ��������������ж���������
			// �����ʹ�� cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:ͼ������
	 * @Full_path:tool.NetUtil.java
	 * @Date:@2014 2014-9-5 ����4:00:07
	 * @Return_type:boolean
	 * @Desc :
	 */
	public static boolean isServerSetup(String url) {
//		url = "http://192.168.1.112:8088/picsearch/validate.txt";
		url = "http://" + StaticParameter.WEB_IP + ":" + StaticParameter.WEB_PORT + "/" + StaticParameter.APP_NAME +"/validate.txt";
		try {
			// �����ȡ�ļ����ݵ�URL
			URL myURL = new URL(url);
			// ��URL����
			URLConnection ucon = myURL.openConnection();
			// ʹ��InputStream����URLConnection��ȡ����
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			// ��ByteArrayBuffer����
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			// �����������ת��ΪString,��UTF-8����
			String s = EncodingUtils.getString(baf.toByteArray(), "UTF-8");
//			System.out.println("s : "+s);
			if ("".equals(s)) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
