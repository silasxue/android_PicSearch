package tool;

public class SDCardUtil {
	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:newsmanage
	 * @Full_path:cn.itcast.utils.SDCardUtil.java
	 * @Date:@2014 2014-7-31 ����9:55:53
	 * @Return_type:void
	 * @Desc :�ж�sd���Ƿ����
	 */
	public static boolean sdCardExist() {
//		Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//		Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	
	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:newsmanage
	 * @Full_path:cn.itcast.utils.SDCardUtil.java
	 * @Date:@2014 2014-7-31 ����9:56:53
	 * @Return_type:String
	 * @Desc :��ȡ��ȡsd����Ŀ¼
	 */
	public static String sdDir() {
		return android.os.Environment.getExternalStorageDirectory().toString();
	}
}
