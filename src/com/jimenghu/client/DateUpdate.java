package com.jimenghu.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.businessalliance.hibernate.Buyinfo;
import com.businessalliance.hibernate.Favproduct;
import com.businessalliance.hibernate.Favshop;
import com.businessalliance.hibernate.Product;
import com.businessalliance.hibernate.SaleInfo;
import com.businessalliance.hibernate.Userinfo;
import com.jimenghu.DBDAO;

public class DateUpdate implements Runnable {

	private long wait = 1000 * 60;

	private boolean run = true;

	private Socket socket;

	private static final Log log = LogFactory.getLog(DateUpdate.class);

	private String ip;

	private int port;

	
	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public DateUpdate(String ip, int port) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.port = port;
	}

	public DateUpdate(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	public DateUpdate(Socket socket, BufferedReader br, PrintWriter pw) {
		this.socket = socket;
		this.br = br;
		this.pw = pw;
	}

	private Socket createSocket() {
		while (true) {
			try {
				return new Socket(ip, port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			} catch (ConnectException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				try {
					Thread.sleep(1000 * 120);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
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

	private PrintWriter pw;

	private BufferedReader br;

	public void run() {
		// TODO Auto-generated method stub
		log.info("上传数据");
		try {

			if (br == null) {
				if (socket == null) {
					socket = createSocket();
				}
				br = new BufferedReader(new InputStreamReader(socket
						.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream());
			}
			
			List<Userinfo> userinfos = DBDAO
					.findUsers("select * from userinfo where serverexist = false");
			int i = 0;
			JSONArray array = new JSONArray();
			for (Iterator iter = userinfos.iterator(); iter.hasNext();) {
				Userinfo userinfo = (Userinfo) iter.next();
				array.add(userinfo);
				i++;
				if(i % 100 == 0){
					JSONObject o = new JSONObject();
					o.put(MessageType.Head, MessageType.clientUpdate);
					o.put(MessageType.shopUpdate, array);
					pw.println(o.toString());
					pw.flush();
					array.clear();
				}
			}
			if(array.size() > 0){
				JSONObject o = new JSONObject();
				o.put(MessageType.Head, MessageType.clientUpdate);
				o.put(MessageType.shopUpdate, array);
				pw.println(o.toString());
				pw.flush();
				array.clear();
			}
			List<Buyinfo> buyinfos = DBDAO
			.findBuyinfos("select * from buyinfo");
			for (Iterator iter = buyinfos.iterator(); iter.hasNext();) {
				Buyinfo buyinfo = (Buyinfo) iter.next();
				array.add(buyinfo);
				i++;
				if(i % 100 == 0){
					JSONObject o = new JSONObject();
					o.put(MessageType.Head, MessageType.clientUpdate);
					o.put(MessageType.buyinfoUpdate, array);
					pw.println(o.toString());
					pw.flush();
					array.clear();
				}
			}
			if(array.size() > 0){
				JSONObject o = new JSONObject();
				o.put(MessageType.Head, MessageType.clientUpdate);
				o.put(MessageType.buyinfoUpdate, array);
				pw.println(o.toString());
				pw.flush();
				array.clear();
			}			
	        DBDAO.delAll("delete from buyinfo");
			List<SaleInfo> saleInfos = DBDAO
			.findSaleinfos("select * from saleinfo");
			for (Iterator iter = saleInfos.iterator(); iter.hasNext();) {
				SaleInfo saleinfo = (SaleInfo) iter.next();
				array.add(saleinfo);
				i++;
				if(i % 100 == 0){
					JSONObject o = new JSONObject();
					o.put(MessageType.Head, MessageType.clientUpdate);
					o.put(MessageType.saleinfoUpdate, array);
					pw.println(o.toString());
					pw.flush();
					array.clear();
				}
			}
			if(array.size() > 0){
				JSONObject o = new JSONObject();
				o.put(MessageType.Head, MessageType.clientUpdate);
				o.put(MessageType.saleinfoUpdate, array);
				pw.println(o.toString());
				pw.flush();
				array.clear();
			}
			DBDAO.delAll("delete from saleinfo");
			List<Favproduct> favproducts = DBDAO
					.findFavproducts("select * from favproduct");
			for (Iterator iter = favproducts.iterator(); iter.hasNext();) {
				Favproduct favproduct = (Favproduct) iter.next();
				array.add(favproduct);
				i++;
				if(i % 100 == 0){
					JSONObject o = new JSONObject();
					o.put(MessageType.Head, MessageType.clientUpdate);
					o.put(MessageType.favproductUpdate, array);
					pw.println(o.toString());
					pw.flush();
					array.clear();
				}
			}
			if(array.size() > 0){
				JSONObject o = new JSONObject();
				o.put(MessageType.Head, MessageType.clientUpdate);
				o.put(MessageType.favproductUpdate, array);
				pw.println(o.toString());
				pw.flush();
				array.clear();
			}
			DBDAO.delAll("delete from favproduct");
			List<Favshop> favshops = DBDAO
					.findFavShops("select * from favshop");
			for (Iterator iter = favshops.iterator(); iter.hasNext();) {
				Favshop favshop = (Favshop) iter.next();
				array.add(favshop);
				i++;
				if(i % 100 == 0){
					JSONObject o = new JSONObject();
					o.put(MessageType.Head, MessageType.clientUpdate);
					o.put(MessageType.favshopUpdate, array);
					pw.println(o.toString());
					pw.flush();
					array.clear();
				}
			}
			if(array.size() > 0){
				JSONObject o = new JSONObject();
				o.put(MessageType.Head, MessageType.clientUpdate);
				o.put(MessageType.favshopUpdate, array);
				pw.println(o.toString());
				pw.flush();
				array.clear();
			}
			
			DBDAO.delAll("delete from favshop");
			
			JSONObject o = new JSONObject();
			o.put(MessageType.Head, MessageType.clientUpdateOver);
			pw.println(o.toString());
			pw.flush();
			try {
				String line = br.readLine();
				log.info("BACK:      "+line);
				JSONObject msg = JSONObject.fromObject(line);
				int head = Integer.parseInt(msg.getString(MessageType.Head));
				switch (head) {
				case MessageType.DateUpdateSuccess:
					log.info("数据上传完成");
					br.close();
					br = null;
					pw.close();
					pw = null;
					socket.close();
					socket = null;
					for (Iterator iter = userinfos.iterator(); iter
								.hasNext();) {
							Userinfo userinfo = (Userinfo) iter.next();
							userinfo.setServerexist(true);
							DBDAO.updateUser(userinfo);
					}
					/*if (o.containsKey(MessageType.buyinfoUpdate)) {
						List<Buyinfo> buyinfos = o
								.getJSONArray(MessageType.buyinfoUpdate);
						for (Iterator iter = buyinfos.iterator(); iter
								.hasNext();) {
							JSONObject json = JSONObject
									.fromObject(iter.next());
							// Buyinfo buyinfo = new Buyinfo();
							// buyinfo.setId(json.getLong("id"));
							DBDAO.delBuyinfo(json.getLong("id"));
							// buyinfo.setServerexist(true);
							// DBDAO.updateBuyinfoServerExist(buyinfo);
						}
					}
					if (o.containsKey(MessageType.saleinfoUpdate)) {
						List<SaleInfo> saleInfos = o
								.getJSONArray(MessageType.saleinfoUpdate);
						for (Iterator iter = saleInfos.iterator(); iter
								.hasNext();) {
							JSONObject json = JSONObject
									.fromObject(iter.next());
							
							 * SaleInfo saleinfo = new SaleInfo();
							 * saleinfo.setId(json.getLong("id"));
							 * saleinfo.setServerexist(true);
							 * DBDAO.updateSaleinfoServerExist(saleinfo);
							 
							DBDAO.delSaleinfo(json.getLong("id"));
						}
					}*/
					/*if (o.containsKey(MessageType.favproductUpdate)) {
						List<Favproduct> favproducts = o
								.getJSONArray(MessageType.favproductUpdate);
						for (Iterator iter = favproducts.iterator(); iter
								.hasNext();) {
							JSONObject element = JSONObject.fromObject(iter
									.next());
							Favproduct favproduct = (Favproduct) JSONObject
									.toBean(element, Favproduct.class);							
							DBDAO.delFavProducts(favproduct.getId());
						}
					}*/
					/*if (o.containsKey(MessageType.productUpdate)) {
						List<Favproduct> favproducts = o
								.getJSONArray(MessageType.productUpdate);
						for (Iterator iter = favproducts.iterator(); iter
								.hasNext();) {
							JSONObject element = JSONObject.fromObject(iter
									.next());
							Product product = (Product) JSONObject.toBean(
									element, Product.class);
							product.setServerexist(true);
							DBDAO.updateProductServerExist(product);
						}
					}*/
					/*if (o.containsKey(MessageType.favshopUpdate)) {
						List<Favshop> favshops = o
								.getJSONArray(MessageType.favshopUpdate);
						for (Iterator iter = favshops.iterator(); iter
								.hasNext();) {
							JSONObject element = JSONObject.fromObject(iter
									.next());
							Favshop favshop = (Favshop) JSONObject.toBean(
									element, Favshop.class);							
							DBDAO.delFavShops(favshop.getId());
						}
					}*/

					break;
				default:
					break;
				}
				//new Thread(new Collecter()).start();
				log.info("数据完成");
				if(br != null){
					br.close();
				}
				if(pw != null){
					pw.close();
				}
				if(socket != null){
					socket.close();
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error(e);
		}

	}

	private JSONObject getUpdate() {
		JSONObject o = new JSONObject();
		boolean flag = true;
		o.put(MessageType.Head, MessageType.clientUpdate);
		List<Userinfo> userinfos = DBDAO
				.findUsers("select * from userinfo where serverexist = false");
		List<Buyinfo> buyinfos = DBDAO
				.findBuyinfos("select * from buyinfo");
		DBDAO.delAll("delete from buyinfo");
		List<SaleInfo> saleInfos = DBDAO
				.findSaleinfos("select * from saleinfo");
		DBDAO.delAll("delete from saleinfo");
		List<Favproduct> favproducts = DBDAO
				.findFavproducts("select * from favproduct");
		DBDAO.delAll("delete from favproduct");
		List<Favshop> favshop = DBDAO
				.findFavShops("select * from favshop");
		DBDAO.delAll("delete from favshop");
		if (userinfos.size() > 0) {
			JSONArray array = new JSONArray();
			for (Iterator iter = userinfos.iterator(); iter.hasNext();) {
				Userinfo userinfo = (Userinfo) iter.next();
				JSONObject json = new JSONObject();
				json.put("buylevel", userinfo.getBuylevel());
				json.put("buyrate", userinfo.getBuyrate());
				json.put("city", userinfo.getCity());
				json.put("companeyname", userinfo.getCompaneyname());
				json.put("iscompaney", userinfo.getIscompaney());
				json.put("personspace", userinfo.getPersonspace());
				json.put("province", userinfo.getProvince());
				json.put("salelevel", userinfo.getSalelevel());
				json.put("salerate", userinfo.getSalerate());
				json.put("shopname", userinfo.getShopname());
				json.put("taobaohref", userinfo.getTaobaohref());
				json.put("username", userinfo.getUsername());
				array.add(json);
			}
			o.put(MessageType.shopUpdate, userinfos);
			flag = false;
		}

		if (buyinfos.size() > 0) {
			JSONArray array = new JSONArray();
			for (Iterator iter = buyinfos.iterator(); iter.hasNext();) {
				Buyinfo buyinfo = (Buyinfo) iter.next();
				JSONObject json = new JSONObject();
				//json.put("id", buyinfo.getId());
				json.put("salername", buyinfo.getSalername());
				json.put("personspace", buyinfo.getPersonspace());
				json.put("buyday", buyinfo.getBuyday());
				json.put("tradeid", buyinfo.getTradeid());
				json.put("producturl", buyinfo.getProducturl());
				json.put("price", buyinfo.getPrice());
				json.put("buyerid", buyinfo.getBuyerid());
				json.put("auctionid", buyinfo.getAuctionid());
				json.put("title", buyinfo.getTitle());
				json.put("funfirst", buyinfo.getFunfirst());
				json.put("funsecond", buyinfo.getFunsecond());
				json.put("funthird", buyinfo.getFunthird());
				json.put("funfour", buyinfo.getFunfour());
				array.add(json);
			}
			o.put(MessageType.buyinfoUpdate, array);
			flag = false;
		}
		if (saleInfos.size() > 0) {
			JSONArray array = new JSONArray();
			for (Iterator iter = saleInfos.iterator(); iter.hasNext();) {
				SaleInfo saleinfo = (SaleInfo) iter.next();
				JSONObject json = new JSONObject();
				//json.put("id", saleinfo.getId());
				json.put("username", saleinfo.getUsername());
				json.put("personspace", saleinfo.getPersonspace());
				json.put("producturl", saleinfo.getProducturl());
				json.put("buyday", saleinfo.getBuyday());
				json.put("tradeid", saleinfo.getTradeid());
				json.put("auctionid", saleinfo.getAuctionid());
				json.put("price", saleinfo.getPrice());
				json.put("salerid", saleinfo.getSalerid());
				json.put("title", saleinfo.getTitle());
				json.put("funfirst", saleinfo.getFunfirst());
				json.put("funsecond", saleinfo.getFunsecond());
				json.put("funthird", saleinfo.getFunthird());
				json.put("funfour", saleinfo.getFunfour());
				array.add(json);
			}
			o.put(MessageType.saleinfoUpdate, array);
			flag = false;
		}
		if (favproducts.size() > 0) {
			JSONArray array = new JSONArray();
			for (Iterator iter = favproducts.iterator(); iter.hasNext();) {
				Favproduct element = (Favproduct) iter.next();
				JSONObject json = new JSONObject();
				json.put("auctionid", element.getAuctionid());
				json.put("funfirst", element.getFunfirst());
				json.put("funfour", element.getFunfour());
				json.put("funsecond", element.getFunsecond());
				json.put("funthird", element.getFunthird());
				json.put("price", element.getPrice());
				json.put("producturl", element.getProducturl());
				json.put("sellername", element.getSellername());
				json.put("title", element.getTitle());
				json.put("userid", element.getUserid());
				array.add(json);
			}
			o.put(MessageType.favproductUpdate, array);
			flag = false;
		}
		if (favshop.size() > 0) {
			JSONArray array = new JSONArray();
			for (Iterator iter = favshop.iterator(); iter.hasNext();) {
				Favshop element = (Favshop) iter.next();
				JSONObject json = new JSONObject();
				json.put("saveuserId", element.getSaveuserId());
				json.put("sellername", element.getSellername());
				array.add(json);
			}
			o.put(MessageType.favshopUpdate, array);
			flag = false;
		}
		if (flag) {
			return null;
		} else {
			return o;
		}

	}

	public static void main(String[] args) {
		/*
		 * ArrayList<Userinfo> list = new ArrayList<Userinfo>(); Userinfo
		 * userinfo = new Userinfo(); userinfo.setBuylevel(1);
		 * userinfo.setUsername("aaa"); list.add(userinfo); userinfo = new
		 * Userinfo(); userinfo.setBuylevel(2); userinfo.setUsername("bbb");
		 * list.add(userinfo); JSONObject o = new JSONObject(); o.put("d",
		 * list); System.out.println(o.toString());
		 */

		/*Thread dateUpdate = new Thread(new DateUpdate("127.0.0.1", 11111));
		dateUpdate.start();*/
		/*JSONArray array = new JSONArray();
		array.add("dd");
		array.add("bb");
		log.info(array);
		array.clear();
		log.info(array.size());*/
		List<Userinfo> userinfos = DBDAO
		.findUsers("select * from userinfo where serverexist = false");
		for (Iterator iter = userinfos.iterator(); iter
		.hasNext();) {
			Userinfo userinfo = (Userinfo) iter.next();
			userinfo.setServerexist(true);
			DBDAO.updateUser(userinfo);
		}
	}
}
