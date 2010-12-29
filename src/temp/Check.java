package temp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.crypto.Data;

/**
 * @author Administrator
 *对于该类的所有Boolean类型返回值，如果返回true，则表示验证通过，否则验证未通过
 */
public class Check {
	
	/**
	 * 验证str是否以http开头
	 */
	public static boolean isUrl(String str){
		if(str != null){
			if(str.indexOf("http://") != -1){
				return true;
			}else{
				return false;
			}
		}else
		return false;
	}
	
	/**
	 * 验证str是否为float类型
	 */
	
	public static boolean isFloat(String str){
		
		try{
			if((str == null)||str.equals(""))
				return false;
			Float.parseFloat(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 验证str是否为int类型
	 */
	public static boolean isInt(String str){
		
		try{
			if((str == null)||str.equals(""))
				return false;
			Float.parseFloat(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 验证str是否为时间类型，分类说明，以下只讨论了网页中可能出现的几种情况
	 * @param checkResult
	 */
	public static boolean isDate(String str) {
		try {
			if ((str==null)||str.equals(""))
				return false;
			//适用于str的值为"[2009.11.06 00:08:07]"类型
			else if(str.indexOf("[")!=-1&&str.indexOf(".")!=-1&&str.indexOf("]")!=-1){
				String s=str.substring(1, 20);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
				Date d=sdf.parse(s);
				return true;
			}
			//适用于str的值为"[2009-11-06 00:08:07]"类型
			else if(str.indexOf("[")!=-1&&str.indexOf("-")!=-1&&str.indexOf("]")!=-1){
				String s=str.substring(1, 20);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d=sdf.parse(s);
				return true;
			}
			//适用于str的值为"[2009/11/06 00:08:07]"类型
			else if(str.indexOf("[")!=-1&&str.indexOf("/")!=-1&&str.indexOf("]")!=-1){
				String s=str.substring(1, 20);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date d=sdf.parse(s);
				return true;
			}
			//适用于str的值为"2009.11.06 00:08:07"类型
			else if(str.indexOf("[")==-1&&str.indexOf(".")!=-1&&str.indexOf("]")==-1){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
				Date d=sdf.parse(str);
				return true;
			}
			//适用于str的值为"2009-11-06 00:08:07"类型
			else if(str.indexOf("[")==-1&&str.indexOf("-")!=-1&&str.indexOf("]")==-1){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d=sdf.parse(str);
				return true;
			}
			//适用于str的值为"2009/11/06 00:08:07"类型
			else if(str.indexOf("[")==-1&&str.indexOf("/")!=-1&&str.indexOf("]")==-1){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date d=sdf.parse(str);
				return true;
			}
			else
				return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
	public static void printRes(Boolean checkResult){
		if(checkResult==true){
			System.out.println("*****************网页结构已经发生改变、或者系统出错，请及时修改文件错误！***************");
			try {
				//发现错误，停顿10秒，以提醒用户
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else 
			System.out.println("*****************网页结构正常***************");
	}
	
	/*public static void main(String[] args) {
		System.out.println(isDate("[2009-11-06 00:08:07]"));
		System.out.println(isDate("[2009.11.06 00:08:07]"));
		System.out.println(isDate("2009-11-06 00:08:07"));
		System.out.println(isDate("2009.11.06 00:08:07"));
	}*/
}
