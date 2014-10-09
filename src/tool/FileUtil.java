package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;

public class FileUtil {

	public static final String TAG = "FileUtil";
	
	private Context context;
	
	public FileUtil(Context context) {
		this.context = context;
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:��׿�洢
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-14 ����10:29:18
	 * @Return_type:void
	 * @Desc : ��ָ���ļ������ļ������� �� data/data/<package name>/files/ �� 
	 */
	public void save(String filename, String content) throws Exception {
//		˽�в���ģʽ�������������ļ�ֻ�ܱ���Ӧ�÷��ʣ�����Ӧ���޷����ʸ��ļ����������˽�в���ģʽ�������ļ���д���ļ��е����ݻḲ��ԭ�ļ�������
//		Context.MODE_APPEND
//		Context.MODE_WORLD_READABLE
//		Context.MODE_WORLD_WRITEABLE
//		Context.MODE_PRIVATE
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outStream.write(content.getBytes());
//		System.out.println(context.getFilesDir());
//		System.out.println(context.getCacheDir());
//		System.out.println(context.getPackageName());
//		System.out.println(context.getDatabasePath("1.txt"));
		outStream.close();
	}
	
	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:����ʶ��
	 * @Full_path:tool.FileUtil.java
	 * @Date:@2014 2014-8-4 ����3:43:22
	 * @Return_type:void
	 * @Desc :���ط��� -- ��ָ���ļ������ļ������� �� data/data/<package name>/files/ �� 
	 */
	public static void save(Context context, byte[] data,String filename) throws Exception {
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outStream.write(data);
		outStream.close();
	}
	
	public void save(String filename, byte[] data) throws Exception {
//		˽�в���ģʽ�������������ļ�ֻ�ܱ���Ӧ�÷��ʣ�����Ӧ���޷����ʸ��ļ����������˽�в���ģʽ�������ļ���д���ļ��е����ݻḲ��ԭ�ļ�������
//		Context.MODE_APPEND
//		Context.MODE_WORLD_READABLE
//		Context.MODE_WORLD_WRITEABLE
//		Context.MODE_PRIVATE
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outStream.write(data);
		outStream.close();
	}
	
	/**********************************************/
	
	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:��׿�洢
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-7 ����1:33:12
	 * @Return_type:void
	 * @Desc : ���ж�ȡָ��·�����ļ���
	 */
	public List<String> readFile(String fullPath) throws Exception{
//		String fullPath = Environment.getExternalStorageDirectory()+"/haom/save0.txt";
		List<String> list = new ArrayList<String>();
		File file = new File(fullPath);
		if(!file.exists()){
			return null;
		}
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s = br.readLine();
		while(s!=null){
			list.add(s);
//			System.out.println(s);
			s = br.readLine();
		}
		br.close();
		return list;
	}
	
	
	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:��׿�洢
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-7 ����10:32:08
	 * @Return_type:void
	 * @Desc : �����ݱ�����SDCard��ָ����Ŀ¼��
	 */
	public void saveToSDCard(String fileName, String content) throws Exception{
//		Log.i(TAG, Environment.getExternalStorageDirectory().toString()); // /mnt/sdcard
		String fullPath = Environment.getExternalStorageDirectory()+"/haom";
		File file = new File(fullPath);
		if(!file.exists()){
			file.mkdirs();
		}
		file = new File(fullPath,fileName);
		FileOutputStream out = new FileOutputStream(file);
		out.write(content.getBytes());
		out.close();
	}
	
	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:��׿�洢
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-7 ����11:35:14
	 * @Return_type:void
	 * @Desc :ɾ��������·���ļ���Ŀ¼
	 */
	public boolean delFile(String fullPath){
		File file = new File(fullPath);
//		file.setExecutable(true,false);
//		file.setExecutable(true);
		if(file.exists()){
			return file.delete();
		}
		return false;
	}
	
	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:��׿�洢
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-7 ����12:14:39
	 * @Return_type:void
	 * @Desc : ɾ��ָ��·���µ���������
	 */
	public void delAllByDir(String dirPath){
		File path = new File(dirPath);
		if(path.isDirectory()){
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				File temp = files[i];
				if(temp.isFile()){
					delFile(files[i].toString());
				}else{
					delAllByDir(files[i].toString());
				}
			}
			path.delete();
		}
	}
	
	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:��׿�洢
	 * @Full_path:com.example.service.FileUtil.java
	 * @Date:@2014 2014-7-7 ����12:10:25
	 * @Return_type:boolean
	 * @Desc :�ж�ȫ·���µ��ļ���Ŀ¼�Ƿ����
	 */
	public boolean existsFile(String fullPath){
		File file = new File(fullPath);
		return file.exists();
	}
	
	
	
}
