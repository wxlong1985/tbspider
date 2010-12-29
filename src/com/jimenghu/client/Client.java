package com.jimenghu.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.businessalliance.hibernate.Mission;
import com.businessalliance.hibernate.Shops;
import com.jimenghu.DBDAO;
import com.jimenghu.ParseSecondCategoryShops;
import com.jimenghu.Sys;
import com.taobao.collectbuyinfo.CollectBuyinfoThread;
import com.taobao.collectfavinfo.CollectFavInfoThread;
import com.taobao.collectsaleinfo.CollectSaleinfoThread;

public class Client implements Runnable {

	private final static int port = 11111;

	private final static String ip = "www.wakehu.com";

	private Socket socket;

	private PrintWriter pw;

	private BufferedReader br;

	private boolean run = true;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final Log log = LogFactory.getLog(Client.class);
	
	private Thread parseSecondCategory;
	
	private boolean reStart = false;
	
	public final static ThreadGroup threadGroup = new ThreadGroup("ClientThreadGroup");
	
	private static Thread parseShopThread;
	
	private static Thread collectbuyinfoThread;
	
	private static Thread collectfavThread;
	
	private static Thread collectsaleinfoThread;
	
	private Socket createSocket(){
		while(true){
			try {
				//new DateUpdate(ip,port);
				return new Socket(ip, port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}catch (ConnectException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				try {
					Thread.sleep(1000 * 120);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					log.error(e1);
					System.exit(0);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
		}
		
	}
	
	private JSONObject over(){
		JSONObject o = new JSONObject();
		o.put(MessageType.Head, MessageType.over);
		return o;
	}
	
	private DateUpdate dateUpdate;
	
	public Client() {
		// TODO Auto-generated constructor stub
		/*parseSecondCategory = new Thread(new ParseSecondCategoryShops(),"ParseSecondCategoryShops");
		parseSecondCategory.start();*/
		log.info("start");
		Client.parseShopThread = new Thread(new ParseSecondCategoryShops(),ParseSecondCategoryShops.class.getName());
		Client.collectbuyinfoThread = new Thread(new CollectBuyinfoThread(),CollectBuyinfoThread.class.getName());
		Client.collectfavThread = new Thread(new CollectFavInfoThread(),CollectFavInfoThread.class.getName());
		Client.collectsaleinfoThread = new Thread(new CollectSaleinfoThread(),CollectSaleinfoThread.class.getName());
		Client.parseShopThread.setPriority(Thread.MIN_PRIORITY);
		//Collecter.parseShopThread.setDaemon(true);
		Client.collectbuyinfoThread.setPriority(Thread.MIN_PRIORITY);
		//Collecter.collectbuyinfoThread.setDaemon(true);
		Client.collectfavThread.setPriority(Thread.MIN_PRIORITY);
		//Collecter.collectfavThread.setDaemon(true);
		Client.collectsaleinfoThread.setPriority(Thread.MIN_PRIORITY);
		Client.parseShopThread.start();
		Client.collectbuyinfoThread.start();
		Client.collectfavThread.start();
		Client.collectsaleinfoThread.start();
		
		
		try {
			File file = new File(Config.ClientMission);
			if (file.exists()) {
				String lastxmlCheck = Config.readProperty(Config.lastxmlCheck);
				String lastMissionCheck = Config.readProperty(Config.lastMissionCheck);
				String lastSaleinfoShopCheck = Config.readProperty(Config.lastSaleinfoShopCheck);
				String lastShopMissionCheck = Config.readProperty(Config.lastShopMissionCheck);
				if(lastSaleinfoShopCheck == null || lastxmlCheck == null || lastMissionCheck == null || lastShopMissionCheck == null){//config 文件存在或不存在都重新发送xml文件信息和任务信息
					String userName = Config.readProperty(Config.username);
					if(userName == null || userName.trim().length() == 0){
						log.error("配置文件用户名不能为空");
						this.run = false;
						//Sys.out(new NullPointerException("配置文件用户名不能为空"), "配置文件用户名不能为空");
						System.exit(0);
						return;
					}
					socket = createSocket();					
					pw = new PrintWriter(socket.getOutputStream());
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					Thread thread = new Thread(threadGroup,this,"client");
					thread.start();
					
					//xml的检查
					JSONObject checkxmlfile = getCheckXmlJSONObject();
					pw.println(checkxmlfile.toString());
					pw.flush();	
					//用户任务的检查
					JSONObject mission = new JSONObject();
					mission.put(MessageType.Head, MessageType.mission);
					long min = DBDAO.getMinMissionId();
					long max = DBDAO.getMaxMissionId();
					mission.put("max", max);
					mission.put("min", min);
					mission.put(Config.username, userName);
					pw.println(mission.toString());
					pw.flush();
					//销售信息，也就是卖家信息的检查
					JSONObject saleinfoMission = new JSONObject();
					saleinfoMission.put(MessageType.Head, MessageType.saleinfoMission);
					saleinfoMission.put("min", DBDAO.getMinShopMissionId());
					saleinfoMission.put("max", DBDAO.getMaxShopMissionId());
					saleinfoMission.put(Config.username, userName);
					pw.println(saleinfoMission.toString());
					pw.flush();
					//所要解析的页面的地址起始终结信息
					JSONObject shopMission = new JSONObject();
					shopMission.put(MessageType.Head, MessageType.shopMissionClient);
					Object fromindex = Config.readProperty(Config.fromIndex);
					Object endindex = Config.readProperty(Config.endIndex);
					Object urlHead = Config.readProperty(Config.urlHead);
					shopMission.put(Config.fromIndex, fromindex);
					shopMission.put(Config.endIndex, endindex);
					shopMission.put(Config.urlHead, null);
					shopMission.put(Config.username, userName);
					pw.println(shopMission.toString());
					pw.flush();
					//用户结束信息
					pw.println(over().toString());
					pw.flush();
				}else{
					String userName = Config.readProperty(Config.username);
					if(userName == null || userName.trim().length() == 0){
						//log.error("配置文件用户名不能为空");						
						this.run = false;
						System.exit(0);
						//Sys.out(new NullPointerException("配置文件用户名不能为空"), "配置文件用户名不能为空");
						return;
					}
					socket = createSocket();					
					pw = new PrintWriter(socket.getOutputStream());
					br = new BufferedReader(new InputStreamReader(socket
							.getInputStream()));
					Thread thread = new Thread(threadGroup,this,"client");
					thread.start();
					Calendar lastxmlCheckTime = Calendar.getInstance();
					lastxmlCheckTime.setTime(sdf.parse(lastxmlCheck));
					Calendar now = Calendar.getInstance();
					Calendar result = Calendar.getInstance();
					result.setTimeInMillis(now.getTimeInMillis()
							- lastxmlCheckTime.getTimeInMillis());
					if (result.get(Calendar.DAY_OF_YEAR) > Integer.parseInt(Config.readProperty(Config.xmlDistance))) {						
						JSONObject checkxmlfile = getCheckXmlJSONObject();
						pw.println(checkxmlfile.toString());
						pw.flush();
						
					}					
					Calendar lastmissionCheckTime = Calendar.getInstance();
					lastmissionCheckTime.setTime(sdf.parse(lastMissionCheck));
					result.setTimeInMillis(now.getTimeInMillis() - lastmissionCheckTime.getTimeInMillis());
					if (result.get(Calendar.DAY_OF_YEAR) > Integer.parseInt(Config.readProperty(Config.missionDistance))) {
						
						JSONObject mission = new JSONObject();
						mission.put(MessageType.Head, MessageType.mission);
						long min = DBDAO.getMinMissionId();
						if(min == -1){
							mission.put("min", min);
							mission.put(Config.username, userName);
						}else{
							long max = DBDAO.getMaxMissionId();
							mission.put("max", max);
							mission.put("min", min);	
							mission.put(Config.username, userName);
						}
						pw.println(mission.toString());
						pw.flush();
					}
					
					Calendar lastsaleinfoMissionCheckTime = Calendar.getInstance();
					lastsaleinfoMissionCheckTime.setTime(sdf.parse(lastSaleinfoShopCheck));
					result.setTimeInMillis(now.getTimeInMillis() - lastsaleinfoMissionCheckTime.getTimeInMillis());
					if (result.get(Calendar.DAY_OF_YEAR) > Integer.parseInt(Config.readProperty(Config.saleinfoMissionDistance))) {
						
						JSONObject mission = new JSONObject();
						mission.put(MessageType.Head, MessageType.saleinfoMission);
						long min = DBDAO.getMinShopMissionId();
						if(min == -1){
							mission.put("min", min);	
							mission.put(Config.username, userName);
						}else{
							long max = DBDAO.getMaxShopMissionId();
							mission.put("max", max);
							mission.put("min", min);	
							mission.put(Config.username, userName);
						}
						pw.println(mission.toString());
						pw.flush();
					}
					
					Calendar lastshopMissionCheckTime = Calendar.getInstance();
					lastshopMissionCheckTime.setTime(sdf.parse(lastShopMissionCheck));
					result.setTimeInMillis(now.getTimeInMillis() - lastshopMissionCheckTime.getTimeInMillis());
					if (result.get(Calendar.DAY_OF_YEAR) > Integer.parseInt(Config.readProperty(Config.shopMissionCheckDistance))) {
						
						JSONObject shopMission = new JSONObject();
						shopMission.put(MessageType.Head, MessageType.shopMissionClient);
						Object fromindex = Config.readProperty(Config.fromIndex);
						Object endindex = Config.readProperty(Config.endIndex);
						Object urlHead = Config.readProperty(Config.urlHead);
						shopMission.put(Config.fromIndex, fromindex);
						shopMission.put(Config.endIndex, endindex);
						shopMission.put(Config.urlHead, urlHead);
						shopMission.put(Config.username, userName);
						pw.println(shopMission.toString());
						pw.flush();
						
					}
					pw.println(over().toString());
					pw.flush();
				}
				
			} else {
				log.error("ClientMission.ini文件缺失");
				System.exit(0);
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pw.close();
			try {
				br.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pw.close();
			try {
				br.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pw.close();
			try {
				br.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pw.close();
			try {
				br.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pw.close();
			try {
				br.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @return 核对Client端的XML文件
	 */
	private JSONObject getCheckXmlJSONObject() {
		JSONObject checkXmlJson = new JSONObject();
		checkXmlJson.put("head", MessageType.CheckXmlFile);
		File file = new File(CheckXml.ParseSecondCategoryShops);
		if (file.exists()) {
			checkXmlJson.put(CheckXml.ParseSecondCategoryShops, file
					.lastModified());
		} else {
			checkXmlJson.put(CheckXml.ParseSecondCategoryShops, -1L);
		}
		file = new File(CheckXml.JudgeShopType);
		if (file.exists()) {
			checkXmlJson.put(CheckXml.JudgeShopType, file.lastModified());
		} else {
			checkXmlJson.put(CheckXml.JudgeShopType, -1L);
		}
		file = new File(CheckXml.parseShopWithHref);
		if (file.exists()) {
			checkXmlJson.put(CheckXml.parseShopWithHref, file.lastModified());
		} else {
			checkXmlJson.put(CheckXml.parseShopWithHref, -1L);
		}
		file = new File(CheckXml.parseShopWithoutHref);
		if (file.exists()) {
			checkXmlJson
					.put(CheckXml.parseShopWithoutHref, file.lastModified());
		} else {
			checkXmlJson.put(CheckXml.parseShopWithoutHref, -1L);
		}
		file = new File(CheckXml.parseSaleRate);
		if (file.exists()) {
			checkXmlJson.put(CheckXml.parseSaleRate, file.lastModified());
		} else {
			checkXmlJson.put(CheckXml.parseSaleRate, -1L);
		}
		file = new File(CheckXml.parseSaleInfo);
		if (file.exists()) {
			checkXmlJson.put(CheckXml.parseSaleInfo, file.lastModified());
		} else {
			checkXmlJson.put(CheckXml.parseSaleInfo, -1L);
		}
		file = new File(CheckXml.parseProductDetail);
		if (file.exists()) {
			checkXmlJson.put(CheckXml.parseProductDetail, file.lastModified());
		} else {
			checkXmlJson.put(CheckXml.parseProductDetail, -1L);
		}
		file = new File(CheckXml.parseFavProductCategory);
		if (file.exists()) {
			checkXmlJson.put(CheckXml.parseFavProductCategory, file.lastModified());
		} else {
			checkXmlJson.put(CheckXml.parseFavProductCategory, -1L);
		}
		file = new File(CheckXml.ParseUserFavoriteProduct);
		if (file.exists()) {
			checkXmlJson.put(CheckXml.ParseUserFavoriteProduct, file.lastModified());
		} else {
			checkXmlJson.put(CheckXml.ParseUserFavoriteProduct, -1L);
		}
		file = new File(CheckXml.ParseUserFavoriteShop);
		if (file.exists()) {
			checkXmlJson.put(CheckXml.ParseUserFavoriteShop, file.lastModified());
		} else {
			checkXmlJson.put(CheckXml.ParseUserFavoriteShop, -1L);
		}
		file = new File(CheckXml.GetBuyUrl);
		if (file.exists()) {
			checkXmlJson.put(CheckXml.GetBuyUrl, file.lastModified());
		} else {
			checkXmlJson.put(CheckXml.GetBuyUrl, -1L);
		}
		return checkXmlJson;
	}

	public void run() {
		// TODO Auto-generated method stub
		while (run) {
			String line = null;
			try {
				log.info("ready read a line");
				
				line = br.readLine();
				if(line == null){
					log.info("read null line out");
					//Thread.sleep(10000);
					continue;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e);
				this.run = false;
				if (!socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();

					}
				}
				e.printStackTrace();
				break;
			}
			if (line != null && line.trim().length() != 0) {
				try {
					JSONObject msg = JSONObject.fromObject(line);
					int head = Integer
							.parseInt(msg.getString(MessageType.Head));
					switch (head) {
					case MessageType.startCollect:
						log.info("开始数据更新");
						/*Collecter collecter = new Collecter();
						new Thread(collecter).start();*/
						new Thread(threadGroup,new DateUpdate(socket,br,pw),DateUpdate.class.getName()).start();
						this.run = false;
						break;
					case MessageType.CheckXmlFileServerBack:
						log.info("检查xml: " + msg);
						//this.run = false;
						if (!MessageType.checkPass.equals(msg
								.getString(CheckXml.JudgeShopType))) {
							File file = new File(CheckXml.JudgeShopType);
							FileWriter fw = new FileWriter(file);
							fw.write(msg.getString(CheckXml.JudgeShopType));
							fw.close();
							file.setLastModified(msg
									.getLong(CheckXml.JudgeShopTypeModify));
						}
						if (!MessageType.checkPass.equals(msg
								.getString(CheckXml.parseProductDetail))) {
							File file = new File(CheckXml.parseProductDetail);
							FileWriter fw = new FileWriter(file);
							fw
									.write(msg
											.getString(CheckXml.parseProductDetail));
							fw.close();
							file
									.setLastModified(msg
											.getLong(CheckXml.parseProductDetailModify));
						}
						if (!MessageType.checkPass.equals(msg
								.getString(CheckXml.parseSaleInfo))) {
							File file = new File(CheckXml.parseSaleInfo);
							FileWriter fw = new FileWriter(file);
							fw.write(msg.getString(CheckXml.parseSaleInfo));
							fw.close();
							file.setLastModified(msg
									.getLong(CheckXml.parseSaleInfoModify));
						}
						if (!MessageType.checkPass.equals(msg
								.getString(CheckXml.parseSaleRate))) {
							File file = new File(CheckXml.parseSaleRate);
							FileWriter fw = new FileWriter(file);
							fw.write(msg.getString(CheckXml.parseSaleRate));
							fw.close();
							file.setLastModified(msg
									.getLong(CheckXml.parseSaleRateModify));
						}
						if (!MessageType.checkPass.equals(msg
								.getString(CheckXml.ParseSecondCategoryShops))) {
							File file = new File(
									CheckXml.ParseSecondCategoryShops);
							FileWriter fw = new FileWriter(file);
							fw
									.write(msg
											.getString(CheckXml.ParseSecondCategoryShops));
							fw.close();
							file
									.setLastModified(msg
											.getLong(CheckXml.ParseSecondCategoryShopsModify));
						}
						if (!MessageType.checkPass.equals(msg
								.getString(CheckXml.parseShopWithHref))) {
							File file = new File(CheckXml.parseShopWithHref);
							FileWriter fw = new FileWriter(file);
							fw.write(msg.getString(CheckXml.parseShopWithHref));
							fw.close();
							file.setLastModified(msg
									.getLong(CheckXml.parseShopWithHrefModify));
						}
						if (!MessageType.checkPass.equals(msg
								.getString(CheckXml.parseShopWithoutHref))) {
							File file = new File(CheckXml.parseShopWithoutHref);
							FileWriter fw = new FileWriter(file);
							fw.write(msg
									.getString(CheckXml.parseShopWithoutHref));
							fw.close();
							file
									.setLastModified(msg
											.getLong(CheckXml.parseShopWithoutHrefModify));
						}
						if (!MessageType.checkPass.equals(msg
								.getString(CheckXml.parseFavProductCategory))) {
							File file = new File(CheckXml.parseFavProductCategory);
							FileWriter fw = new FileWriter(file);
							fw.write(msg
									.getString(CheckXml.parseFavProductCategory));
							fw.close();
							file
									.setLastModified(msg
											.getLong(CheckXml.parseFavProductCategoryModify));
						}
						if (!MessageType.checkPass.equals(msg
								.getString(CheckXml.ParseUserFavoriteProduct))) {
							File file = new File(CheckXml.ParseUserFavoriteProduct);
							FileWriter fw = new FileWriter(file);
							fw.write(msg
									.getString(CheckXml.ParseUserFavoriteProduct));
							fw.close();
							file
									.setLastModified(msg
											.getLong(CheckXml.ParseUserFavoriteProductModify));
						}
						if (!MessageType.checkPass.equals(msg
								.getString(CheckXml.ParseUserFavoriteShop))) {
							File file = new File(CheckXml.ParseUserFavoriteShop);
							FileWriter fw = new FileWriter(file);
							fw.write(msg
									.getString(CheckXml.ParseUserFavoriteShop));
							fw.close();
							file
									.setLastModified(msg
											.getLong(CheckXml.ParseUserFavoriteShopModify));
						}
						this.modifyLastXmlCheckTime();
						break;
					/*case MessageType.firstXmlFileInfo:
						
						File file = new File(CheckXml.JudgeShopType);
						FileWriter fw = new FileWriter(file);
						fw.write(msg.getString(CheckXml.JudgeShopType));
						fw.close();
						file.setLastModified(msg
								.getLong(CheckXml.JudgeShopTypeModify));
						file = new File(CheckXml.parseProductDetail);
						fw = new FileWriter(file);
						fw.write(msg.getString(CheckXml.parseProductDetail));
						fw.close();
						file.setLastModified(msg
								.getLong(CheckXml.parseProductDetailModify));
						file = new File(CheckXml.parseSaleInfo);
						fw = new FileWriter(file);
						fw.write(msg.getString(CheckXml.parseSaleInfo));
						fw.close();
						file.setLastModified(msg
								.getLong(CheckXml.parseSaleInfoModify));
						file = new File(CheckXml.parseSaleRate);
						fw = new FileWriter(file);
						fw.write(msg.getString(CheckXml.parseSaleRate));
						fw.close();
						file.setLastModified(msg
								.getLong(CheckXml.parseSaleRateModify));
						file = new File(CheckXml.ParseSecondCategoryShops);
						fw = new FileWriter(file);
						fw.write(msg
								.getString(CheckXml.ParseSecondCategoryShops));
						fw.close();
						file
								.setLastModified(msg
										.getLong(CheckXml.ParseSecondCategoryShopsModify));
						file = new File(CheckXml.parseShopWithHref);
						fw = new FileWriter(file);
						fw.write(msg.getString(CheckXml.parseShopWithHref));
						fw.close();
						file.setLastModified(msg
								.getLong(CheckXml.parseShopWithHrefModify));
						file = new File(CheckXml.parseShopWithoutHref);
						fw = new FileWriter(file);
						fw.write(msg.getString(CheckXml.parseShopWithoutHref));
						fw.close();
						file.setLastModified(msg
								.getLong(CheckXml.parseShopWithoutHrefModify));
						this.modifyLastXmlCheckTime();
						break;*/
					case MessageType.missionChange:
						//log.info(msg);
						
						long min = msg.getLong("min");
						long max = msg.getLong("max");
						log.info("店铺任务改变： min: "+ min + "  max: "+ max);
						/*min = min + 3;
						max = max - 13;*/
						DBDAO.changeMissionState(min, max);
						if(msg.containsKey("mission")){
							JSONArray array = msg.getJSONArray("mission");
							for (Iterator iter = array.iterator(); iter.hasNext();) {
								JSONObject o = JSONObject.fromObject(iter.next());
								//log.info(o);
								Mission mission = new Mission();
								mission.setBuylevel(o.getInt("buylevel"));
								mission.setBuyrate(o.getString("buyrate"));
								mission.setCity(o.getString("city"));
								mission.setCompaneyname(o.getString("companeyname"));
								mission.setId(o.getLong("id"));
								mission.setIscompaney(o.getBoolean("iscompaney"));
								mission.setPersonspace(o.getString("personspace"));
								mission.setProvince(o.getString("province"));
								mission.setSalelevel(o.getInt("salelevel"));
								mission.setSalerate(o.getString("salerate"));
								mission.setShopname(o.getString("shopname"));
								mission.setTaobaohref(o.getString("taobaohref"));
								mission.setUsername(o.getString("username"));
								mission.setMission(true);
								DBDAO.saveMission(mission);
							}
						}						
						break;
					case MessageType.saleinfoMissionChange:
						//log.info(msg);
						
						long minIndex = msg.getLong("min");
						long maxIndex = msg.getLong("max");
						log.info("收集线程任务改变： min: "+ minIndex + "  max: "+ maxIndex);
						/*min = min + 3;
						max = max - 13;*/
						DBDAO.changeSaleinfoShopState(minIndex, maxIndex);
						if(msg.containsKey("mission")){
							JSONArray shops = msg.getJSONArray("mission");
							for (Iterator iter = shops.iterator(); iter.hasNext();) {
								JSONObject o = JSONObject.fromObject(iter.next());
								//log.info(o);
								Shops shop = new Shops();
								shop.setBuylevel(o.getInt("buylevel"));
								shop.setBuyrate(o.getString("buyrate"));
								shop.setCity(o.getString("city"));
								shop.setCompaneyname(o.getString("companeyname"));
								shop.setId(o.getLong("id"));
								shop.setIscompaney(o.getBoolean("iscompaney"));
								shop.setPersonspace(o.getString("personspace"));
								shop.setProvince(o.getString("province"));
								shop.setSalelevel(o.getInt("salelevel"));
								shop.setSalerate(o.getString("salerate"));
								shop.setShopname(o.getString("shopname"));
								shop.setTaobaohref(o.getString("taobaohref"));
								shop.setUsername(o.getString("username"));
								shop.setMission(true);
								DBDAO.saveShop(shop);
							}
						}						
						break;
					case MessageType.shopMissionServer:
						log.info("店铺任务改变： "+ msg);
						String fromIndex = msg.getString(MessageType.shopmissionfromindex);
						String endIndex = msg.getString(MessageType.shopmissionendindex);
						String urlHead = msg.getString(Config.urlHead);
						Config.storeProperty(Config.fromIndex, fromIndex);
						Config.storeProperty(Config.endIndex, endIndex);
						Config.storeProperty(Config.urlHead, urlHead);
						Config.storeProperty(Config.lastShopMissionCheck, sdf.format(new Date()));
						//this.run = false;
						break;
					default:
						break;
					}

				} catch (Exception e) {
					// TODO: handle exception					
					log.error(e);
					e.printStackTrace();
					Sys.out(e, e.getMessage());
					//
				}

			}

		}
	}

	private void modifyLastMissionCheckTime(){
		
		try {
			File file = new File(Config.ClientMission);
			Properties pro = new Properties();
			FileInputStream fips = new FileInputStream(file);
			pro.load(fips);
			fips.close();
			FileOutputStream fops = new FileOutputStream(file);
			Calendar lastCheck = Calendar.getInstance();
			pro.setProperty(Config.lastMissionCheck,sdf.format(lastCheck.getTime()));
			pro.store(fops, "lastCheck");
			fops.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
		}
		
	}

	private void modifyLastXmlCheckTime(){
		
		try {
			File file = new File(Config.ClientMission);
			Properties pro = new Properties();
			FileInputStream fips = new FileInputStream(file);
			pro.load(fips);
			fips.close();
			FileOutputStream fops = new FileOutputStream(file);
			Calendar lastCheck = Calendar.getInstance();
			pro.setProperty(Config.lastxmlCheck,sdf.format(lastCheck.getTime()));
			pro.store(fops, "lastCheck");
			fops.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
		}
		
	}
	
	public static void main(String[] args) {
		
		Thread thread = new Thread(threadGroup,new Client(),"client");
		thread.start();
		/*while(true){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info(thread.isAlive());
		}*/
		//JSONObject o = new JSONObject();
	}
}
