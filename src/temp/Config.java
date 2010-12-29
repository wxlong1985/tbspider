package temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Properties;

import com.jimenghu.Sys;

public class Config {
	
	public final static String ClientMission = "ClientMission.ini";
	
	public final static String lastxmlCheck = "lastxmlcheck";
	
	public final static String xmlDistance = "5";
	
	public final static String lastMissionCheck = "lastMissionCheck";
	
	public final static String lastSaleinfoShopCheck = "lastSaleinfoShopCheck";
	
	public final static String missionDistance = "5";
	
	public final static String saleinfoMissionDistance = "5";
	
	
	public final static String lastShopMissionCheck = "lastShopMissionCheck";
	
	public final static String shopMissionCheckDistance = "5";
	
	public final static String lastDateUpdate = "lastDateUpdate";
	
	public final static String lastDateUpdateDistance = "1";
	
	public final static String username = "username";
	
	/**
	 *  shopcategory from page index
	 */
	public final static String fromIndex = "fromIndex";

	/**
	 * shopcategory end page index
	 */
	public final static String endIndex = "endIndex";

	public final static String urlHead = "urlHead";

	/**
	 * mission current index
	 */
	public final static String currendIndex = "currendIndex";
	
	/**
	 * shopcategory current page index
	 */
	public final static String currendshopIndex = "currendshopIndex";
	
	public final static synchronized String readProperty(String key,String filestr) {

		try {
			File file = new File(filestr);
			if(!file.exists()){
				Sys.out(new NullPointerException("配置文件   "+filestr+"   不存在"), "配置文件   "+filestr+"   不存在");
			}
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			while(true){
				FileLock fl = raf.getChannel().tryLock();
				if(fl != null && fl.isValid()){
					Properties pro = new Properties();
					FileDescriptor fd = raf.getFD();
					FileInputStream fips = new FileInputStream(fd);
					pro.load(fips);
					fl.release();
					fips.close();					
					raf.close();
					String value = pro.getProperty(key);
					return value;
				}else{
					Thread.sleep(1);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public final static String readProperty(String key) {

		try {
			File file = new File(ClientMission);
			if(!file.exists()){
				Sys.out(new NullPointerException("配置文件   ClientMission   不存在"), "配置文件   ClientMission   不存在");
			}
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			while(true){
				FileLock fl = raf.getChannel().tryLock();
				if(fl != null && fl.isValid()){
					Properties pro = new Properties();
					FileDescriptor fd = raf.getFD();
					FileInputStream fips = new FileInputStream(fd);
					pro.load(fips);
					fl.release();
					fips.close();
					raf.close();
					String value = pro.getProperty(key);
					return value;
				}else{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*public final static Properties getProperty() {

		try {
			File file = new File(ClientMission);
			FileInputStream fips = new FileInputStream(file);
			Properties pro = new Properties();
			pro.load(fips);
			fips.close();
			return pro;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/
	/*public final static void storeProperty(Properties pro) {

		try {
			if(pro != null){
				File file = new File(ClientMission);
				FileOutputStream fops = new FileOutputStream(file);
				pro.store(fops, "comments");
				fops.close();
			}			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	public final static void storeProperty(String key,String value) {
		Properties pro = new Properties();
		try {			
			File file = new File(ClientMission);
			if(!file.exists()){
				Sys.out(new NullPointerException("配置文件   ClientMission   不存在"), "配置文件   ClientMission   不存在");
			}
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			while(true){
				FileLock fl = raf.getChannel().tryLock();
				if(fl != null && fl.isValid()){
					FileDescriptor fd = raf.getFD();
					FileInputStream fips = new FileInputStream(fd);
					pro.load(fips);	
					fl.release();					
					fips.close();
					raf.close();
					break;
				}else{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Sys.out(e, e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Sys.out(e, e.getMessage());
		}
		
		try {
			File file = new File(ClientMission);
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			while(true){
				FileLock fl = raf.getChannel().tryLock();
				if(fl != null && fl.isValid()){				
					FileDescriptor fd = raf.getFD();
					FileOutputStream fops = new FileOutputStream(fd);
					pro.put(key, value);
					pro.store(fops, "comments");
					fl.release();
					fops.close();					
					raf.close();
					break;
				}else{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final static void storeProperty(String key,String value,String filestr) {
		Properties pro = new Properties();
		try {			
			File file = new File(filestr);
			if(!file.exists()){
				Sys.out(new NullPointerException("配置文件   "+filestr+"   不存在"), "配置文件   "+filestr+"   不存在");
			}
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			while(true){
				FileLock fl = raf.getChannel().tryLock();
				if(fl != null && fl.isValid()){
					FileDescriptor fd = raf.getFD();
					FileInputStream fips = new FileInputStream(fd);
					pro.load(fips);	
					fl.release();					
					fips.close();
					raf.close();
					break;
				}else{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Sys.out(e, e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Sys.out(e, e.getMessage());
		}
		
		try {
			File file = new File(filestr);
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			while(true){
				FileLock fl = raf.getChannel().tryLock();
				if(fl != null && fl.isValid()){				
					FileDescriptor fd = raf.getFD();
					FileOutputStream fops = new FileOutputStream(fd);
					pro.put(key, value);
					pro.store(fops, "comments");
					fl.release();
					fops.close();					
					raf.close();
					break;
				}else{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		/*Config.storeProperty("aa","bb");
		Config.storeProperty("bb","cc");*/
		System.out.println(Config.readProperty(Config.username));
		/*File file = new File("F:/svn/taobao/taobao/bin/Debug/ClientMission.ini");
		RandomAccessFile raf = new RandomAccessFile(file,"rw");
		while(true){
			FileLock fl = raf.getChannel().tryLock();
			if(fl != null && fl.isValid()){
				String a= "dd;";
			}
		}*/
		
		/*BufferedReader in = new BufferedReader(new InputStreamReader(System.in));//创建读入流
		  String a;
		  while((a= in.readLine()).length()!=0)
		  System.out.println(a);*/

	}
}
