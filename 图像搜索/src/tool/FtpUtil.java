package tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	private static FTPClient ftpClient = new FTPClient();

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:����ʶ��
	 * @Full_path:tool.FtpUtil.java
	 * @Date:@2014 2014-8-18 ����5:18:21
	 * @Return_type:void
	 * @Desc :����ftp�ķ���
	 */
	private static void connectFtp(String ip, String port, String username, String password) {
		try {
			if (!ftpClient.isConnected()) {
				ftpClient.connect(ip, Integer.parseInt(port));
				ftpClient.login(username, password);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:����ʶ��
	 * @Full_path:tool.FtpUtil.java
	 * @Date:@2014 2014-8-18 ����5:18:11
	 * @Return_type:void
	 * @Desc :�Ͽ�ftp�ķ���
	 */
	public static void disConnctFtp() {
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				ftpClient.logout();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Description: ��FTP�����������ļ�
	 * 
	 * @Version1.0
	 * @param ip
	 *            FTP������hostname
	 * @param port
	 *            FTP�������˿�
	 * @param username
	 *            FTP��¼�˺�
	 * @param password
	 *            FTP��¼����
	 * @param remotePath
	 *            FTP�������ϵ����·��
	 * @param fileName
	 *            Ҫ���ص��ļ���
	 * @param localPath
	 *            ���غ󱣴浽���ص�·��
	 * @return
	 */
	public static byte[] getFileByte(String ip, String port, String username, String password, String remotePath, String fileName, String localPath) {
		byte[] b = null;
		try {
			int reply;
			FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
			conf.setServerLanguageCode("zh");
			// ftpClient.connect(ip, Integer.parseInt(port));
			// �������Ĭ�϶˿ڣ�����ʹ��ftp.connect(ip)�ķ�ʽֱ������FTP������
			// ftpClient.login(username, password);// ��¼
			connectFtp(ip, port, username, password);
			// �����ļ���������Ϊ������
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// ��ȡftp��¼Ӧ�����
			reply = ftpClient.getReplyCode();
			// ��֤�Ƿ��½�ɹ�
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refused connection.");
				return null;
			}
			// ת�Ƶ�FTP������Ŀ¼��ָ����Ŀ¼��
			// ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), "iso-8859-1"));
			ftpClient.changeWorkingDirectory(remotePath);
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			// ��ȡ�ļ��б�
			FTPFile[] fs = ftpClient.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					// File localFile = new File(localPath + "/" + ff.getName());
					// OutputStream is = new FileOutputStream(localFile);
					// ftpClient.retrieveFile(ff.getName(), is);
					// is.close();
					InputStream ins = ftpClient.retrieveFileStream(ff.getName());
					b = toByteArray(ins);
					break;
				}
			}
			// ftpClient.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return b;
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:����ʶ��
	 * @Full_path:tool.FtpUtil.java
	 * @Date:@2014 2014-8-18 ����2:51:49
	 * @Return_type:byte[]
	 * @Desc :inputstreamתbyte����
	 */
	private static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}

	/**
	 * ͨ��ftp�ϴ��ļ�
	 * 
	 * @param url
	 *            ftp��������ַ �磺 192.168.1.112
	 * @param port
	 *            �˿��� �� 21
	 * @param username
	 *            ��¼��
	 * @param password
	 *            ����
	 * @param remotePath
	 *            �ϵ�ftp�������Ĵ���·��
	 * @param fileNamePath
	 *            Ҫ�ϴ����ļ�·��
	 * @param fileName
	 *            Ҫ�ϴ����ļ���
	 * @return
	 */
	public static String ftpUpload(String url, String port, String username, String password, String remotePath, String fileNamePath, String fileName) {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		String returnMessage = "0";
		try {
			ftpClient.connect(url, Integer.parseInt(port));
			boolean loginResult = ftpClient.login(username, password);
			int returnCode = ftpClient.getReplyCode();
			if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// �����¼�ɹ�
				ftpClient.makeDirectory(remotePath);
				// �����ϴ�Ŀ¼
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				fis = new FileInputStream(fileNamePath + fileName);
				ftpClient.storeFile(fileName, fis);
				returnMessage = "1"; // �ϴ��ɹ�
			} else {// �����¼ʧ��
				returnMessage = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("FTP�ͻ��˳���", e);
		} finally {
			// IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("�ر�FTP���ӷ����쳣��", e);
			}
		}
		return returnMessage;
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:����ʶ��
	 * @Full_path:tool.FtpUtil.java
	 * @Date:@2014 2014-8-4 ����10:32:53
	 * @Return_type:String
	 * @Desc : ���صķ���:�� fileNamePath�� fileName �ϲ���һ������
	 */
	public static String ftpUpload(String url, String port, String username, String password, String remotePath, String fileFullPath) {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		String returnMessage = "0";
		try {
			ftpClient.connect(url, Integer.parseInt(port));
			boolean loginResult = ftpClient.login(username, password);
			int returnCode = ftpClient.getReplyCode();
			if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// �����¼�ɹ�
				ftpClient.makeDirectory(remotePath);
				// �����ϴ�Ŀ¼
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				fis = new FileInputStream(fileFullPath);
				ftpClient.storeFile(new File(fileFullPath).getName(), fis);
				returnMessage = "1"; // �ϴ��ɹ�
			} else {// �����¼ʧ��
				returnMessage = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("FTP�ͻ��˳���", e);
		} finally {
			// IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("�ر�FTP���ӷ����쳣��", e);
			}
		}
		return returnMessage;
	}

	public static void main(String[] args) {
		// ftpUpload("192.168.1.112", "21", "weixin", "weixin", "FileFromAndroid", "D:/html5/", "android.jpg");
		// System.out.println(123);
	}
}
