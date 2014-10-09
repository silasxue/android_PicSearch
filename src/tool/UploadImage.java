package tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class UploadImage {

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:takepicture
	 * @Full_path:tool.UploadImage.java
	 * @Date:@2014 2014-7-28 ����3:34:03
	 * @Return_type:void
	 * @Desc : ��������ϴ��ļ�
	 */
	public static boolean upload(File uploadFile,String servletURL) {
//		String path = "http://192.168.1.112:8088/html5/UploadFileServlet";
		String path = servletURL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", "");
		params.put("timelength", "");
		FormFile formFile = new FormFile(uploadFile, "videofile", "image/jpeg");
		try {
			return SocketHttpRequester.post(path, params, formFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
