package com.jimenghu;

import java.io.File;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.businessalliance.hibernate.Buyinfo;
import com.businessalliance.hibernate.City;
import com.businessalliance.hibernate.Favproduct;
import com.businessalliance.hibernate.Favshop;
import com.businessalliance.hibernate.Firstcategory;
import com.businessalliance.hibernate.Mission;
import com.businessalliance.hibernate.Procatfirst;
import com.businessalliance.hibernate.Procatfour;
import com.businessalliance.hibernate.Procatsecond;
import com.businessalliance.hibernate.Procatthird;
import com.businessalliance.hibernate.Product;
import com.businessalliance.hibernate.Province;
import com.businessalliance.hibernate.SaleInfo;
import com.businessalliance.hibernate.Shops;
import com.businessalliance.hibernate.Userinfo;

public class DBDAO {

	private final static Log log = LogFactory.getLog(DBDAO.class);

	private Long productId = new Long(1);

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public final static long getMinShopMissionId() {
		String sql = "select min(id) as minid from shops";
		try {
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				return rs.getLong("minid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}

		return -1;
	}
	
	
	public final static void delAll(String sql ){
		try {
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			stm.executeUpdate(sql);		
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}

	}              
	
	
	public final static long getMaxShopMissionId() {
		String sql = "select max(id) as maxid from shops";
		try {
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				return rs.getLong("maxid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}

		return -1;
	}

	/**
	 * @return mission最小id值
	 */
	public final static long getMinMissionId() {
		String sql = "select min(id) as minid from mission";
		try {
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				return rs.getLong("minid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}

		return -1;
	}

	public final static long getMinUserinfoId() {
		String sql = "select min(id) as minid from userinfo";
		try {
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				return rs.getLong("minid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}

		return -1;
	}

	public final static long getMaxMissionId() {
		String sql = "select max(id) as maxid from mission";
		try {
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				return rs.getLong("maxid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}

		return -1;
	}

	public final synchronized static void saveImmediately(Userinfo userinfo) {
		StringBuffer sb = new StringBuffer();
		try {

			String columns = "";
			String values = "";
			if (userinfo.getProvince() != null
					&& userinfo.getProvince().length() > 0) {
				values += "'" + userinfo.getProvince() + "'";
			} else {
				values += "null";
			}

			if (userinfo.getCity() != null && userinfo.getCity().length() > 0) {
				values += ",'" + userinfo.getCity() + "'";
			} else {
				values += ",null";
			}
			if (userinfo.getTaobaohref() != null
					&& userinfo.getTaobaohref().length() > 0) {
				values += ",'" + userinfo.getTaobaohref() + "'";
			} else {
				values += ",null";
			}
			if (userinfo.getShopname() != null
					&& userinfo.getShopname().length() > 0) {
				values += ",'" + userinfo.getShopname() + "'";
			} else {
				values += ",null";
			}
			if (userinfo.getIscompaney() != null) {
				values += "," + userinfo.getIscompaney() + "";
			} else {
				values += ",false";
			}
			if (userinfo.getSalerate() != null
					&& userinfo.getSalerate().length() > 0) {
				values += ",'" + userinfo.getSalerate() + "'";
			} else {
				values += ",null";
			}
			if (userinfo.getBuyrate() != null
					&& userinfo.getBuyrate().length() > 0) {
				values += ",'" + userinfo.getBuyrate() + "'";
			} else {
				values += ",null";
			}
			if (userinfo.getUsername() != null
					&& userinfo.getUsername().length() > 0) {
				values += ",'" + userinfo.getUsername() + "'";
			} else {
				values += ",null";
			}
			if (userinfo.getCompaneyname() != null
					&& userinfo.getCompaneyname().length() > 0) {
				values += ",'" + userinfo.getCompaneyname() + "'";
			} else {
				values += ",null";
			}
			if (userinfo.getPersonspace() != null
					&& userinfo.getPersonspace().length() > 0) {
				values += ",'" + userinfo.getPersonspace() + "'";
			} else {
				values += ",null";
			}
			if (userinfo.getSalelevel() != null) {
				values += "," + userinfo.getSalelevel() + "";
			} else {
				values += ",0";
			}
			if (userinfo.getBuylevel() != null) {
				values += "," + userinfo.getBuylevel() + "";
			} else {
				values += ",0";
			}
			sb
					.append("insert into userinfo(province,city,taobaohref,shopname,iscompaney,salerate,buyrate,username,companeyname,personspace,salelevel,buylevel)");
			sb.append("values(" + values + ")");
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			if (stm.executeUpdate(sb.toString()) > 0) {
				con.commit();
				con.close();
				StringBuffer getidSql = new StringBuffer();
				getidSql.append("select max(id) as maxid from userinfo");
				con = getAccessConnect();
				ResultSet rs = con.createStatement().executeQuery(
						getidSql.toString());
				if (rs.next())
					userinfo.setId(rs.getLong("maxid"));
				con.close();
			} else {
				if (!con.isClosed()) {
					con.close();
				}
				throw new Exception("insert unsuccessful!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
			log.error("ERROR SQL " + sb.toString(), e);
			Sys.out(e, "ERROR SQL " + sb.toString());
		}

	}

	public final static void updateShopsLastcheckDate(Shops shop) {
		String sql = null;
		try {
			sql = "update shops set lastchecktime = '"
					+ shop.getLastchecktime() + "' where id = " + shop.getId();
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			stm.executeUpdate(sql);
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "updateMissionLastcheckDate method occor exception");
		}
	}

	public final static void updateMissionLastcheckDate(Mission mission) {
		String sql = "update mission set lastchecktime = '"
				+ mission.getLastchecktime() + "' where id = "
				+ mission.getId();

		try {
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			stm.executeUpdate(sql);
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error("ERROR SQL:" + sql, e);
			Sys.out(e, "SQL: " + sql);
		}

	}

	public final static void changeSaleinfoShopState(long min, long max) {
		String sql = null;
		try {
			sql = "update shops set mission = false where id > " + max
					+ " or id < " + min + " ";
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			if (stm.executeUpdate(sql) > 0) {
				con.commit();
				con.close();
			}
			sql = "update shops set mission = true where id between " + min
					+ " and  " + max + " ";
			con = getAccessConnect();
			stm = con.createStatement();
			if (stm.executeUpdate(sql) > 0) {
				con.commit();
				con.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "修改shops的状态");
		}
	}

	public final static void changeMissionState(long min, long max) {
		String sql = null;
		try {
			sql = "update mission set mission = false where id > " + max
					+ " or id < " + min + " ";
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			if (stm.executeUpdate(sql) > 0) {
				con.commit();
				con.close();
			}
			sql = "update mission set mission = true where id between " + min
					+ " and  " + max + " ";
			con = getAccessConnect();
			stm = con.createStatement();
			if (stm.executeUpdate(sql) > 0) {
				con.commit();
				con.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "修改mission的状态");
		}
	}

	public final synchronized static void saveShop(Shops shop) {
		StringBuffer sb = new StringBuffer();
		try {

			String columns = "";
			String values = "";
			if (shop.getProvince() != null && shop.getProvince().length() > 0) {
				values += "'" + shop.getProvince() + "'";
			} else {
				values += "null";
			}

			if (shop.getCity() != null && shop.getCity().length() > 0) {
				values += ",'" + shop.getCity() + "'";
			} else {
				values += ",null";
			}
			if (shop.getTaobaohref() != null
					&& shop.getTaobaohref().length() > 0) {
				values += ",'" + shop.getTaobaohref() + "'";
			} else {
				values += ",null";
			}
			if (shop.getShopname() != null && shop.getShopname().length() > 0) {
				values += ",'" + shop.getShopname() + "'";
			} else {
				values += ",null";
			}
			if (shop.getIscompaney() != null) {
				values += "," + shop.getIscompaney() + "";
			} else {
				values += ",false";
			}
			if (shop.getSalerate() != null && shop.getSalerate().length() > 0) {
				values += ",'" + shop.getSalerate() + "'";
			} else {
				values += ",null";
			}
			if (shop.getBuyrate() != null && shop.getBuyrate().length() > 0) {
				values += ",'" + shop.getBuyrate() + "'";
			} else {
				values += ",null";
			}
			if (shop.getUsername() != null && shop.getUsername().length() > 0) {
				values += ",'" + shop.getUsername() + "'";
			} else {
				values += ",null";
			}
			if (shop.getCompaneyname() != null
					&& shop.getCompaneyname().length() > 0) {
				values += ",'" + shop.getCompaneyname() + "'";
			} else {
				values += ",null";
			}
			if (shop.getPersonspace() != null
					&& shop.getPersonspace().length() > 0) {
				values += ",'" + shop.getPersonspace() + "'";
			} else {
				values += ",null";
			}
			if (shop.getSalelevel() != null) {
				values += "," + shop.getSalelevel() + "";
			} else {
				values += ",0";
			}
			if (shop.getBuylevel() != null) {
				values += "," + shop.getBuylevel() + "";
			} else {
				values += ",0";
			}
			if (shop.getMission() != null) {
				values += "," + shop.getMission() + "";
			} else {
				values += ",false";
			}
			if (shop.getId() != null) {
				values += "," + shop.getId() + "";
			} else {
				log.error("server send an error shops object:"
						+ JSONObject.fromObject(shop));
				return;
			}
			sb
					.append("insert into shops(province,city,taobaohref,shopname,iscompaney,salerate,buyrate,username,companeyname,personspace,salelevel,buylevel,mission,id)");
			sb.append(" values(" + values + ")");
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			if (stm.executeUpdate(sb.toString()) > 0) {
				con.commit();
				con.close();
				StringBuffer getidSql = new StringBuffer();
				getidSql.append("select max(id) as maxid from userinfo");
				con = getAccessConnect();
				ResultSet rs = con.createStatement().executeQuery(
						getidSql.toString());
				if (rs.next())
					shop.setId(rs.getLong("maxid"));
				con.close();
			} else {
				if (!con.isClosed()) {
					con.close();
				}
				throw new Exception("insert unsuccessful!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
			log.error("ERROR SQL: " + sb.toString(), e);
			Sys.out(e, "dbdao saveShop method occor exception");
		}

	}

	public final synchronized static void saveMission(Mission mission) {
		StringBuffer sb = new StringBuffer();
		try {

			String columns = "";
			String values = "";
			if (mission.getProvince() != null
					&& mission.getPersonspace().length() > 0) {
				values += "'" + mission.getProvince() + "'";
			} else {
				values += "null";
			}

			if (mission.getCity() != null
					&& mission.getPersonspace().length() > 0) {
				values += ",'" + mission.getCity() + "'";
			} else {
				values += ",null";
			}
			if (mission.getTaobaohref() != null
					&& mission.getTaobaohref().length() > 0) {
				values += ",'" + mission.getTaobaohref() + "'";
			} else {
				values += ",null";
			}
			if (mission.getShopname() != null
					&& mission.getShopname().length() > 0) {
				values += ",'" + mission.getShopname() + "'";
			} else {
				values += ",null";
			}
			if (mission.getIscompaney() != null) {
				values += "," + mission.getIscompaney() + "";
			} else {
				values += ",false";
			}
			if (mission.getSalerate() != null
					&& mission.getSalerate().length() > 0) {
				values += ",'" + mission.getSalerate() + "'";
			} else {
				values += ",null";
			}
			if (mission.getBuyrate() != null
					&& mission.getBuyrate().length() > 0) {
				values += ",'" + mission.getBuyrate() + "'";
			} else {
				values += ",null";
			}
			if (mission.getUsername() != null
					&& mission.getUsername().length() > 0) {
				values += ",'" + mission.getUsername() + "'";
			} else {
				values += ",null";
			}
			if (mission.getCompaneyname() != null
					&& mission.getCompaneyname().length() > 0) {
				values += ",'" + mission.getCompaneyname() + "'";
			} else {
				values += ",null";
			}
			if (mission.getPersonspace() != null
					&& mission.getPersonspace().length() > 0) {
				values += ",'" + mission.getPersonspace() + "'";
			} else {
				values += ",null";
			}
			if (mission.getSalelevel() != null) {
				values += "," + mission.getSalelevel() + "";
			} else {
				values += ",0";
			}
			if (mission.getBuylevel() != null) {
				values += "," + mission.getBuylevel() + "";
			} else {
				values += ",0";
			}
			if (mission.getMission() != null) {
				values += "," + mission.getMission() + "";
			} else {
				values += ",false";
			}
			if (mission.getId() != null) {
				values += "," + mission.getId() + "";
			} else {
				log.error("server send an error missio object:"
						+ JSONObject.fromObject(mission));
				return;
			}
			sb
					.append("insert into mission(province,city,taobaohref,shopname,iscompaney,salerate,buyrate,username,companeyname,personspace,salelevel,buylevel,mission,id)");
			sb.append(" values(" + values + ")");
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			if (stm.executeUpdate(sb.toString()) > 0) {
				con.commit();
				con.close();
				StringBuffer getidSql = new StringBuffer();
				getidSql.append("select max(id) as maxid from userinfo");
				con = getAccessConnect();
				ResultSet rs = con.createStatement().executeQuery(
						getidSql.toString());
				if (rs.next())
					mission.setId(rs.getLong("maxid"));
				con.close();
			} else {
				if (!con.isClosed()) {
					con.close();
				}
				throw new Exception("insert unsuccessful!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
			log.error(" ERROR SQL: " + sb.toString(), e);
			Sys.out(e, "dbdao saveImmediately method occor exception");
		}

	}

	public final static Userinfo findUser(String username) {
		try {
			Userinfo userinfo = null;
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			String sql = "select * from Userinfo where username = '" + username
					+ "' ";
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				userinfo = new Userinfo();
				userinfo.setBuylevel(rs.getInt("buylevel"));
				userinfo.setBuyrate(rs.getString("buyrate"));
				userinfo.setCity(rs.getString("city"));
				userinfo.setCompaneyname(rs.getString("companeyname"));
				userinfo.setIscompaney(rs.getBoolean("iscompaney"));
				userinfo.setPersonspace(rs.getString("personspace"));
				userinfo.setProvince(rs.getString("province"));
				userinfo.setSalelevel(rs.getInt("salelevel"));
				userinfo.setSalerate(rs.getString("salerate"));
				userinfo.setShopname(rs.getString("shopname"));
				userinfo.setTaobaohref(rs.getString("taobaohref"));
				userinfo.setUsername(rs.getString("username"));
				userinfo.setId(rs.getLong("id"));
			}
			return userinfo;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			// e.printStackTrace();
			Sys.out(e, e.getMessage());
			return null;
		}

	}

	public final static Userinfo findUser(Long id) {
		try {
			Userinfo userinfo = null;
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			String sql = "select * from Userinfo where id = " + id + "";
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				userinfo = new Userinfo();
				userinfo.setBuylevel(rs.getInt("buylevel"));
				userinfo.setBuyrate(rs.getString("buyrate"));
				userinfo.setCity(rs.getString("city"));
				userinfo.setCompaneyname(rs.getString("companeyname"));
				userinfo.setIscompaney(rs.getBoolean("iscompaney"));
				userinfo.setPersonspace(rs.getString("personspace"));
				userinfo.setProvince(rs.getString("province"));
				userinfo.setSalelevel(rs.getInt("salelevel"));
				userinfo.setSalerate(rs.getString("salerate"));
				userinfo.setShopname(rs.getString("shopname"));
				userinfo.setTaobaohref(rs.getString("taobaohref"));
				userinfo.setUsername(rs.getString("username"));
				userinfo.setId(rs.getLong("id"));
			}
			return userinfo;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			// e.printStackTrace();
			Sys.out(e, e.getMessage());
			return null;
		}

	}

	public final static Mission findMission(Long id) {
		try {
			Mission mission = null;
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			String sql = "select * from mission where id = " + id + "";
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				mission = new Mission();
				mission.setBuylevel(rs.getInt("buylevel"));
				mission.setBuyrate(rs.getString("buyrate"));
				mission.setCity(rs.getString("city"));
				mission.setCompaneyname(rs.getString("companeyname"));
				mission.setIscompaney(rs.getBoolean("iscompaney"));
				mission.setPersonspace(rs.getString("personspace"));
				mission.setProvince(rs.getString("province"));
				mission.setSalelevel(rs.getInt("salelevel"));
				mission.setSalerate(rs.getString("salerate"));
				mission.setShopname(rs.getString("shopname"));
				mission.setTaobaohref(rs.getString("taobaohref"));
				mission.setUsername(rs.getString("username"));
				mission.setId(rs.getLong("id"));
				mission.setLastchecktime(rs.getString("lastchecktime"));
			}
			return mission;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			// e.printStackTrace();
			Sys.out(e, e.getMessage());
			return null;
		}

	}

	public final static Shops findShop(Long id) {
		try {
			Shops shop = null;
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			String sql = "select * from shops where id = " + id + "";
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				shop = new Shops();
				shop.setBuylevel(rs.getInt("buylevel"));
				shop.setBuyrate(rs.getString("buyrate"));
				shop.setCity(rs.getString("city"));
				shop.setCompaneyname(rs.getString("companeyname"));
				shop.setIscompaney(rs.getBoolean("iscompaney"));
				shop.setPersonspace(rs.getString("personspace"));
				shop.setProvince(rs.getString("province"));
				shop.setSalelevel(rs.getInt("salelevel"));
				shop.setSalerate(rs.getString("salerate"));
				shop.setShopname(rs.getString("shopname"));
				shop.setTaobaohref(rs.getString("taobaohref"));
				shop.setUsername(rs.getString("username"));
				shop.setId(rs.getLong("id"));
				shop.setLastchecktime(rs.getString("lastchecktime"));
			}
			return shop;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			// e.printStackTrace();
			Sys.out(e, e.getMessage());
			return null;
		}

	}

	public final static List<Favproduct> findFavproducts(String sql) {
		try {
			Favproduct favproduct = null;
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			List<Favproduct> array = new ArrayList<Favproduct>();
			while (rs.next()) {
				favproduct = new Favproduct();
				favproduct.setId(rs.getLong("id"));
				favproduct.setAuctionid(rs.getString("auctionid"));
				favproduct.setFunfirst(rs.getInt("funfirst"));
				favproduct.setPrice(rs.getDouble("price"));
				favproduct.setFunfour(rs.getInt("funfour"));
				favproduct.setFunsecond(rs.getInt("funsecond"));
				favproduct.setFunthird(rs.getInt("funthird"));
				favproduct.setProducturl(rs.getString("producturl"));
				favproduct.setSellername(rs.getString("sellername"));
				favproduct.setTitle(rs.getString("title"));
				// favproduct.setUsername(rs.getString("username"));
				favproduct.setUserid(rs.getLong("userid"));
				array.add(favproduct);
			}
			return array;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			e.printStackTrace();
			Sys.out(e, e.getMessage());
			return null;
		}

	}

	public final static List<Favshop> findFavShops(String sql) {
		try {
			Favshop favshop = null;
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			List<Favshop> array = new ArrayList<Favshop>();
			while (rs.next()) {
				favshop = new Favshop();
				favshop.setId(rs.getLong("id"));
				// favshop.setSaveusername(rs.getString("saveusername"));
				favshop.setSaveuserId(rs.getLong("saveuserId"));
				favshop.setSellername(rs.getString("sellername"));
				array.add(favshop);
			}
			return array;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			// e.printStackTrace();
			Sys.out(e, e.getMessage());
			return null;
		}

	}

	public final static List<SaleInfo> findSaleinfos(String sql) {
		try {
			SaleInfo saleInfo = null;
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			/*
			 * for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
			 * System.out.println(rs.getMetaData().getColumnName(i+1)); }
			 */
			List<SaleInfo> array = new ArrayList<SaleInfo>();
			while (rs.next()) {
				saleInfo = new SaleInfo();

				saleInfo.setId(rs.getLong("id"));
				saleInfo.setUsername(rs.getString("username"));
				saleInfo.setPersonspace(rs.getString("personSpace"));
				saleInfo.setBuyday(rs.getString("buyDay"));
				saleInfo.setPrice(rs.getDouble("price"));
				saleInfo.setAuctionid(rs.getString("auctionid"));
				saleInfo.setProducturl(rs.getString("productUrl"));
				// saleInfo.setSellerid(rs.getLong("sellerId"));
				saleInfo.setSalerid(rs.getLong("salerid"));
				saleInfo.setTradeid(rs.getString("tradeId"));

				saleInfo.setTitle(rs.getString("title"));
				saleInfo.setFunfirst(rs.getInt("funfirst"));
				saleInfo.setFunsecond(rs.getInt("funsecond"));
				saleInfo.setFunthird(rs.getInt("funthird"));
				saleInfo.setFunfour(rs.getInt("funfour"));
				array.add(saleInfo);
			}
			return array;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			// e.printStackTrace();
			Sys.out(e, e.getMessage());
			return null;
		}

	}

	public final static List<Buyinfo> findBuyinfos(String sql) {
		try {
			Buyinfo buyinfo = null;
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			List<Buyinfo> array = new ArrayList<Buyinfo>();
			while (rs.next()) {
				buyinfo = new Buyinfo();
				buyinfo.setBuyday(rs.getString("buyday"));
				buyinfo.setId(rs.getLong("id"));
				buyinfo.setPersonspace(rs.getString("personspace"));
				buyinfo.setPrice(rs.getDouble("price"));
				buyinfo.setAuctionid(rs.getString("auctionid"));
				buyinfo.setProducturl(rs.getString("producturl"));
				buyinfo.setBuyerid(rs.getLong("buyerid"));
				buyinfo.setTradeid(rs.getString("tradeid"));
				buyinfo.setSalername(rs.getString("salername"));
				buyinfo.setTitle(rs.getString("title"));
				buyinfo.setFunfirst(rs.getInt("funfirst"));
				buyinfo.setFunsecond(rs.getInt("funsecond"));
				buyinfo.setFunthird(rs.getInt("funthird"));
				buyinfo.setFunfour(rs.getInt("funfour"));
				array.add(buyinfo);
			}
			return array;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			// e.printStackTrace();
			Sys.out(e, e.getMessage());
			return null;
		}

	}

	public final static List<Userinfo> findUsers(String sql) {
		try {
			Userinfo userinfo = null;
			Connection con = getAccessConnect();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			List<Userinfo> array = new ArrayList<Userinfo>();
			while (rs.next()) {
				userinfo = new Userinfo();
				userinfo.setBuylevel(rs.getInt("buylevel"));
				userinfo.setBuyrate(rs.getString("buyrate"));
				userinfo.setCity(rs.getString("city"));
				userinfo.setCompaneyname(rs.getString("companeyname"));
				userinfo.setIscompaney(rs.getBoolean("iscompaney"));
				userinfo.setPersonspace(rs.getString("personspace"));
				userinfo.setProvince(rs.getString("province"));
				userinfo.setSalelevel(rs.getInt("salelevel"));
				userinfo.setSalerate(rs.getString("salerate"));
				userinfo.setShopname(rs.getString("shopname"));
				userinfo.setTaobaohref(rs.getString("taobaohref"));
				userinfo.setUsername(rs.getString("username"));
				userinfo.setId(rs.getLong("id"));
				array.add(userinfo);
			}
			return array;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			e.printStackTrace();
			Sys.out(e, e.getMessage());
			return null;
		}

	}

	public final synchronized static void updateFavshopServerExist(
			Favshop favshop) {
		String sql = null;
		try {
			sql = "update favshop set serverexist = "
					+ favshop.getServerexist() + " where id ="
					+ favshop.getId();
			Connection con = getAccessConnect();
			if (con.createStatement().executeUpdate(sql) == 0) {
				throw new Exception("update failure!");
			}
			con.close();
		} catch (Exception e) {
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}
	}

	public final synchronized static void updateFavproductServerExist(
			Favproduct favproduct) {
		String sql = null;
		try {
			sql = "update favproduct set serverexist = "
					+ favproduct.getServerexist() + " where id ="
					+ favproduct.getId();
			Connection con = getAccessConnect();
			if (con.createStatement().executeUpdate(sql) == 0) {
				throw new Exception("update failure!");
			}
			con.close();
		} catch (Exception e) {
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}
	}

	public final synchronized static void updateProductServerExist(
			Product product) {
		String sql = null;
		try {
			sql = "update product set serverexist = "
					+ product.getServerexist() + " where id ="
					+ product.getId();
			Connection con = getAccessConnect();
			if (con.createStatement().executeUpdate(sql) == 0) {
				throw new Exception("update failure!");
			}
			con.close();
		} catch (Exception e) {
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}
	}

	public final synchronized static void updateSaleinfoServerExist(
			SaleInfo saleInfo) {
		String sql = null;
		try {
			sql = "update saleinfo set serverexist = "
					+ saleInfo.getServerexist() + " where id ="
					+ saleInfo.getId();
			Connection con = getAccessConnect();
			if (con.createStatement().executeUpdate(sql) == 0) {
				throw new Exception("update failure!");
			}
			con.close();
		} catch (Exception e) {
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}
	}

	public final synchronized static void updateBuyinfoServerExist(
			Buyinfo buyinfo) {
		String sql = null;
		try {
			sql = "update buyinfo set serverexist = "
					+ buyinfo.getServerexist() + " where id ="
					+ buyinfo.getId();
			Connection con = getAccessConnect();
			if (con.createStatement().executeUpdate(sql) == 0) {
				throw new Exception("update failure!");
			}
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			log.error("ERROR SQL: " + sql, e);
			Sys.out(e, "ERROR SQL: " + sql);
		}
	}

	public final synchronized static void updateUser(Userinfo userinfo) {
		String updateSql = "";
		try {

			updateSql += "update userinfo set ";
			if (userinfo.getProvince() != null
					&& userinfo.getProvince().length() > 0) {
				updateSql += "province = '" + userinfo.getProvince() + "'";
			} else {
				updateSql += "province = null";
			}
			if (userinfo.getCity() != null && userinfo.getCity().length() > 0) {
				updateSql += ",city = '" + userinfo.getCity() + "'";
			} else {
				updateSql += ",city = null";
			}
			if (userinfo.getTaobaohref() != null
					&& userinfo.getTaobaohref().length() > 0) {
				updateSql += ",taobaoHref = '" + userinfo.getTaobaohref() + "'";
			} else {
				updateSql += ",taobaoHref = null";
			}
			if (userinfo.getShopname() != null
					&& userinfo.getShopname().length() > 0) {
				updateSql += ",shopname = '" + userinfo.getShopname() + "'";
			} else {
				updateSql += ",shopname = null";
			}
			if (userinfo.getIscompaney() != null) {
				updateSql += ",iscompaney = " + userinfo.getIscompaney() + "";
			} else {
				updateSql += ",iscompaney = false";
			}
			if (userinfo.getSalerate() != null
					&& userinfo.getSalerate().length() > 0) {
				updateSql += ",salerate = '" + userinfo.getSalerate() + "'";
			} else {
				updateSql += ",salerate = null";
			}
			if (userinfo.getBuyrate() != null
					&& userinfo.getBuyrate().length() > 0) {
				updateSql += ",buyrate = '" + userinfo.getBuyrate() + "'";
			} else {
				updateSql += ",buyrate = null";
			}
			if (userinfo.getUsername() != null
					&& userinfo.getUsername().length() > 0) {
				updateSql += ",username = '" + userinfo.getUsername() + "'";
			} else {
				updateSql += ",username = null";
			}
			if (userinfo.getCompaneyname() != null
					&& userinfo.getCompaneyname().length() > 0) {
				updateSql += ",companeyname = '" + userinfo.getCompaneyname()
						+ "'";
			} else {
				updateSql += ",companeyname = null";
			}
			if (userinfo.getPersonspace() != null
					&& userinfo.getPersonspace().length() > 0) {
				updateSql += ",personspace = '" + userinfo.getPersonspace()
						+ "'";
			} else {
				updateSql += ",personspace = null";
			}
			if (userinfo.getSalelevel() != null) {
				updateSql += ",salelevel = " + userinfo.getSalelevel() + "";
			} else {
				updateSql += ",salelevel = 0";
			}
			if (userinfo.getBuylevel() != null) {
				updateSql += ",buylevel = " + userinfo.getBuylevel() + "";
			} else {
				updateSql += ",buylevel = 0";
			}
			if (userinfo.getServerexist() != null) {
				updateSql += ",serverexist = " + userinfo.getServerexist() + "";
			} else {
				updateSql += ",serverexist = false";
			}
			updateSql += " where id=" + userinfo.getId();
			Connection con = getAccessConnect();
			if (con.createStatement().executeUpdate(updateSql) == 0) {
				throw new Exception("update failure!");
			}
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			// log.error("SQL: " + updateSql + " " + e);
			log.error("ERROR SQL: " + updateSql, e);
			Sys.out(e, "ERROR SQL: " + updateSql);
		}
	}

	public final synchronized static void saveFavProduct(Favproduct product,
			String username) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("insert into  favproduct(title,price,funfirst,funsecond,funthird,funfour,sellername,userid,producturl,auctionid) values(");
		if (product.getTitle() != null) {
			sb.append("'" + product.getTitle() + "'");
		} else {
			sb.append("null");
		}
		if (product.getPrice() != null) {
			sb.append("," + product.getPrice() + "");
		} else {
			sb.append(",null");
		}
		if (product.getFunfirst() != null) {
			sb.append("," + product.getFunfirst() + "");
		} else {
			sb.append(",-1");
		}
		if (product.getFunsecond() != null) {
			sb.append("," + product.getFunsecond() + "");
		} else {
			sb.append(",0");
		}
		if (product.getFunthird() != null) {
			sb.append("," + product.getFunthird() + "");
		} else {
			sb.append(",0");
		}
		if (product.getFunfour() != null) {
			sb.append("," + product.getFunfour() + "");
		} else {
			sb.append(",0");
		}
		if (product.getSellername() != null
				&& product.getSellername().length() > 0) {
			sb.append(",'" + product.getSellername() + "'");
		} else {
			sb.append(",null");
		}
		if (product.getUserid() != null) {
			sb.append("," + product.getUserid() + "");
		} else {
			sb.append(",0");
		}
		if (product.getProducturl() != null
				&& product.getProducturl().length() > 0) {
			sb.append(",'" + product.getProducturl() + "'");
		} else {
			sb.append(",null");
		}
		if (product.getAuctionid() != null
				&& product.getAuctionid().length() > 0) {
			sb.append(",'" + product.getAuctionid() + "'");
		} else {
			sb.append(",null");
		}
		sb.append(")");

		try {
			Connection con = getAccessConnect();
			con.createStatement().executeUpdate(sb.toString());
			con.commit();
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + sb.toString(), e);
			Sys.out(e, "ERROR SQL: " + sb);
		}

		/*
		 * Userinfo userinfo = DBDAO.findUser(product.getSellername());
		 * 
		 * if(userinfo != null){ sb = new StringBuffer(); sb.append("insert into
		 * befavproduct(title,price,funfirst,funsecond,funthird,funfour,username,salerid,producturl,auctionid)
		 * values("); if(product.getTitle() != null){
		 * sb.append("'"+product.getTitle()+"'"); }else{ sb.append("null"); }
		 * if(product.getPrice() != null){ sb.append(","+product.getPrice()+"");
		 * }else{ sb.append(",null"); } if(product.getFunfirst() != null){
		 * sb.append(","+product.getFunfirst()+""); }else{ sb.append(",0"); }
		 * if(product.getFunsecond() != null){
		 * sb.append(","+product.getFunsecond()+""); }else{ sb.append(",0"); }
		 * if(product.getFunthird() != null){
		 * sb.append(","+product.getFunthird()+""); }else{ sb.append(",0"); }
		 * if(product.getFunfour() != null){
		 * sb.append(","+product.getFunfour()+""); }else{ sb.append(",0"); }
		 * if(username != null){ sb.append(",'"+username+"'"); }else{
		 * sb.append(",null"); } if(product.getUserid() != null){
		 * sb.append(","+userinfo.getId()+""); }else{ sb.append(",0"); }
		 * if(product.getProducturl() != null){
		 * sb.append(",'"+product.getProducturl()+"'"); }else{
		 * sb.append(",null"); }if(product.getAuctionid() != null){
		 * sb.append(",'"+product.getAuctionid()+"'"); }else{
		 * sb.append(",null"); } sb.append(")");
		 * 
		 * 
		 * try { Connection con = getAccessConnect();
		 * con.createStatement().executeUpdate(sb.toString()); con.commit();
		 * con.close(); } catch (HibernateException e) { log.error("SQL : " +
		 * sb.toString(), e); Sys.out(e, e.getMessage()); } catch (SQLException
		 * e) { log.error("SQL : " + sb.toString(), e); Sys.out(e,
		 * e.getMessage()); } }
		 */

	}

	public final synchronized static void saveFavShops(Favshop favshop) {
		String insertProduct = "insert into  favshop(sellername,saveuserId) values("
				+ "'"
				+ favshop.getSellername()
				+ "',"
				+ favshop.getSaveuserId() + ")";

		try {
			Connection con = getAccessConnect();
			con.createStatement().executeUpdate(insertProduct);
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + insertProduct, e);
			Sys.out(e, "ERROR SQL: " + insertProduct);
		}
	}

	public final static boolean checkExistSaleinfo(String tradeId) {

		String insertBuyinfo = "select * from saleinfo where tradeid = '"
				+ tradeId + "'";

		try {
			Connection con = getAccessConnect();
			ResultSet rs = con.createStatement().executeQuery(insertBuyinfo);
			if (rs.next()) {
				return true;
			}
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + insertBuyinfo, e);
			Sys.out(e, "ERROR SQL: " + insertBuyinfo);
		}

		return false;

	}

	public final static boolean checkExistBuyinfo(String tradeId) {

		String insertBuyinfo = "select * from buyinfo where tradeid = '"
				+ tradeId + "'";

		try {
			Connection con = getAccessConnect();
			ResultSet rs = con.createStatement().executeQuery(insertBuyinfo);
			if (rs.next()) {
				return true;
			}
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + insertBuyinfo, e);
			Sys.out(e, "ERROR SQL: " + insertBuyinfo);
		}

		return false;

	}

	public final static void delFavProducts(Long id) {

		String insertBuyinfo = "delete from favproduct where userid = " + id;
		try {
			Connection con = getAccessConnect();
			con.createStatement().executeUpdate(insertBuyinfo);
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + insertBuyinfo, e);
			Sys.out(e, "ERROR SQL: " + insertBuyinfo);
		}
	}

	public final static void delBuyinfo(Long id) {

		String insertBuyinfo = "delete from buyinfo where id = " + id;
		try {
			Connection con = getAccessConnect();
			con.createStatement().executeUpdate(insertBuyinfo);
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + insertBuyinfo, e);
			Sys.out(e, "ERROR SQL: " + insertBuyinfo);
		}
	}

	public final static void delSaleinfo(Long id) {

		String insertBuyinfo = "delete from saleinfo where id = " + id;
		try {
			Connection con = getAccessConnect();
			con.createStatement().executeUpdate(insertBuyinfo);
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + insertBuyinfo, e);
			Sys.out(e, "ERROR SQL: " + insertBuyinfo);
		}
	}

	public final static void delFavShops(Long id) {

		String insertBuyinfo = "delete from favshop where saveuserId = " + id;

		try {
			Connection con = getAccessConnect();
			con.createStatement().executeUpdate(insertBuyinfo);
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + insertBuyinfo, e);
			Sys.out(e, "ERROR SQL: " + insertBuyinfo);
		}
	}

	public final static boolean checkFavProductExist(String auctionId) {

		String insertBuyinfo = "select * from favproduct where auctionid = '"
				+ auctionId + "'";

		try {
			Connection con = getAccessConnect();
			ResultSet rs = con.createStatement().executeQuery(insertBuyinfo);
			if (rs.next()) {
				con.close();
				return true;
			}
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + insertBuyinfo, e);
			Sys.out(e, "ERROR SQL: " + insertBuyinfo);
		}
		return false;

	}

	public final static boolean checkFavShopExist(String auctionId) {

		String insertBuyinfo = "select * from favproduct where auctionid = '"
				+ auctionId + "'";

		try {
			Connection con = getAccessConnect();
			ResultSet rs = con.createStatement().executeQuery(insertBuyinfo);
			if (rs.next()) {
				con.close();
				return true;
			}
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + insertBuyinfo, e);
			Sys.out(e, "ERROR SQL: " + insertBuyinfo);
		}
		return false;

	}

	public final static boolean checkUserNameExist(String userName) {

		String insertBuyinfo = "select * from userinfo where username = '"
				+ userName + "'";
		try {
			Connection con = getAccessConnect();
			ResultSet rs = con.createStatement().executeQuery(insertBuyinfo);
			if (rs.next()) {
				con.close();
				return true;
			}
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + insertBuyinfo, e);
			Sys.out(e, "ERROR SQL: " + insertBuyinfo);
		}
		return false;

	}

	public final synchronized static void saveSaleInfo(SaleInfo saleinfo) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("insert into saleinfo(username,personspace,buyday,tradeid,producturl,price,salerid,auctionid,title,funfirst,funsecond,funthird,funfour) values(");

		if (saleinfo.getUsername() != null
				&& saleinfo.getUsername().length() > 0) {
			sb.append("'" + saleinfo.getUsername() + "'");
		} else
			sb.append("null");
		if (saleinfo.getPersonspace() != null
				&& saleinfo.getPersonspace().length() > 0) {
			sb.append(",'" + saleinfo.getPersonspace() + "'");
		} else
			sb.append(",null");
		if (saleinfo.getBuyday() != null) {
			sb.append(",'" + saleinfo.getBuyday() + "'");
		} else
			sb.append(",null");
		if (saleinfo.getTradeid() != null && saleinfo.getTradeid().length() > 0) {
			sb.append(",'" + saleinfo.getTradeid() + "'");
		} else
			sb.append(",null");
		if (saleinfo.getProducturl() != null
				&& saleinfo.getProducturl().length() > 0) {
			sb.append(",'" + saleinfo.getProducturl() + "'");
		} else
			sb.append(",null");
		if (saleinfo.getPrice() != null) {
			sb.append("," + saleinfo.getPrice() + "");
		} else
			sb.append(",0");
		if (saleinfo.getSalerid() != null) {
			sb.append("," + saleinfo.getSalerid() + "");
		} else
			sb.append(",null");
		if (saleinfo.getAuctionid() != null
				&& saleinfo.getAuctionid().length() > 0) {
			sb.append(",'" + saleinfo.getAuctionid() + "'");
		} else
			sb.append(",null");
		if (saleinfo.getTitle() != null && saleinfo.getTitle().length() > 0) {
			sb.append(",'" + saleinfo.getTitle() + "'");
		} else
			sb.append(",null");
		if (saleinfo.getFunfirst() != null) {
			sb.append("," + saleinfo.getFunfirst() + "");
		} else
			sb.append(",-1");
		if (saleinfo.getFunsecond() != null) {
			sb.append("," + saleinfo.getFunsecond() + "");
		} else
			sb.append(",0");
		if (saleinfo.getFunthird() != null) {
			sb.append("," + saleinfo.getFunthird() + "");
		} else
			sb.append(",0");
		if (saleinfo.getFunfour() != null) {
			sb.append("," + saleinfo.getFunfour() + "");
		} else
			sb.append(",0");

		sb.append(")");

		try {
			Connection con = getAccessConnect();
			con.createStatement().executeUpdate(sb.toString());
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + sb.toString(), e);
			Sys.out(e, "ERROR SQL: " + sb);
		}
	}

	public final synchronized static void saveBuyInfo(Buyinfo buyinfo) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("insert into buyinfo(salername,personspace,buyday,tradeid,producturl,price,buyerid,auctionid,title,funfirst,funsecond,funthird,funfour) values(");
		if (buyinfo.getSalername() != null
				&& buyinfo.getSalername().length() > 0) {
			sb.append("'" + buyinfo.getSalername() + "'");
		} else
			sb.append("null");
		if (buyinfo.getPersonspace() != null
				&& buyinfo.getPersonspace().length() > 0) {
			sb.append(",'" + buyinfo.getPersonspace() + "'");
		} else
			sb.append(",null");
		if (buyinfo.getBuyday() != null && buyinfo.getBuyday().length() > 0) {
			sb.append(",'" + buyinfo.getBuyday() + "'");
		} else
			sb.append(",null");
		if (buyinfo.getTradeid() != null && buyinfo.getTradeid().length() > 0) {
			sb.append(",'" + buyinfo.getTradeid() + "'");
		} else
			sb.append(",null");
		if (buyinfo.getProducturl() != null
				&& buyinfo.getProducturl().length() > 0) {
			sb.append(",'" + buyinfo.getProducturl() + "'");
		} else
			sb.append(",null");
		if (buyinfo.getPrice() != null) {
			sb.append("," + buyinfo.getPrice() + "");
		} else
			sb.append(",0");
		if (buyinfo.getBuyerid() != null) {
			sb.append("," + buyinfo.getBuyerid() + "");
		} else
			sb.append(",null");
		if (buyinfo.getAuctionid() != null
				&& buyinfo.getAuctionid().length() > 0) {
			sb.append(",'" + buyinfo.getAuctionid() + "'");
		} else
			sb.append(",null");
		if (buyinfo.getTitle() != null && buyinfo.getTitle().length() > 0) {
			sb.append(",'" + buyinfo.getTitle() + "'");
		} else
			sb.append(",null");
		if (buyinfo.getFunfirst() != null) {
			sb.append("," + buyinfo.getFunfirst() + "");
		} else
			sb.append(",-1");
		if (buyinfo.getFunsecond() != null) {
			sb.append("," + buyinfo.getFunsecond() + "");
		} else
			sb.append(",0");
		if (buyinfo.getFunthird() != null) {
			sb.append("," + buyinfo.getFunthird() + "");
		} else
			sb.append(",0");
		if (buyinfo.getFunfour() != null) {
			sb.append("," + buyinfo.getFunfour() + "");
		} else
			sb.append(",0");

		sb.append(")");

		try {
			Connection con = getAccessConnect();
			con.createStatement().executeUpdate(sb.toString());
			con.close();
		} catch (SQLException e) {
			log.error("SQL : " + sb.toString(), e);
			Sys.out(e, "ERROR SQL: " + sb);
		}
	}

	private final static String accessDriver = "sun.jdbc.odbc.JdbcOdbcDriver";

	private final static String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=db.mdb";

	public static HashMap<String, Integer> provinces;

	public static HashMap<String, Integer> citys;

	public static HashMap<String, Integer> funfirsts;

	public static HashMap<String, Integer> funseconds;

	public static HashMap<String, Integer> funthirds;

	public static HashMap<String, Integer> funfours;

	public final static int addProvince(String province) throws SQLException {
		Connection con = getAccessConnect();
		con.createStatement().executeUpdate(
				"insert into province(province) values('" + province + "');");
		con.close();
		con = getAccessConnect();
		ResultSet rs = con.createStatement().executeQuery(
				"select * from province");
		provinces.clear();
		while (rs.next()) {
			provinces.put(rs.getString("province").replace("省", ""), rs
					.getInt("id"));
		}
		con.close();
		con = getAccessConnect();
		rs = con.createStatement().executeQuery(
				"select max(id) as max from province");
		if (rs.next()) {
			int i = rs.getInt("max");
			con.close();
			return i;
		} else {
			con.close();
			return -1;
		}
	}

	public final static int addCity(String province, String city)
			throws SQLException {
		Long id = -1L;
		Connection con = getAccessConnect();
		ResultSet rs = con.createStatement().executeQuery(
				"select id from province where province = '" + province + "';");
		if (rs.next()) {
			id = rs.getLong("id");
		}
		con.close();
		con = getAccessConnect();
		con.createStatement().executeUpdate(
				"insert into city(upid,city) values(" + id + ",'" + city
						+ "');");
		con.close();
		con = getAccessConnect();
		rs = con.createStatement().executeQuery("select * from city");
		citys.clear();
		while (rs.next()) {
			citys.put(rs.getString("city").replace("市", ""), rs.getInt("id"));
		}
		con.close();
		con = getAccessConnect();
		rs = con.createStatement().executeQuery(
				"select max(id) as max from city");
		if (rs.next()) {
			int i = rs.getInt("max");
			con.close();
			return i;
		} else {
			con.close();
			return -1;
		}

	}
	
	private static DataSource pooled = null;

	static {
		try {
			Class.forName(accessDriver);			
			provinces = new HashMap<String, Integer>();
			citys = new HashMap<String, Integer>();
			funfirsts = new HashMap<String, Integer>();
			funseconds = new HashMap<String, Integer>();
			funthirds = new HashMap<String, Integer>();
			funfours = new HashMap<String, Integer>();
			Connection con = getAccessConnect();
			ResultSet rs = con.createStatement().executeQuery(
					"select * from province");
			while (rs.next()) {
				provinces.put(rs.getString("province").replace("省", ""), rs
						.getInt("id"));
			}
			con.close();
			con = getAccessConnect();
			rs = con.createStatement().executeQuery("select * from city");
			while (rs.next()) {
				citys.put(rs.getString("city").replace("市", ""), rs
						.getInt("id"));
			}
			con.close();
			con = getAccessConnect();
			rs = con.createStatement()
					.executeQuery("select * from procatfirst");
			while (rs.next()) {
				funfirsts.put(rs.getString("categoryname"), rs.getInt("id"));
			}
			con.close();
			con = getAccessConnect();
			rs = con.createStatement().executeQuery(
					"select * from procatsecond");
			while (rs.next()) {
				funseconds.put(rs.getString("categoryname"), rs.getInt("id"));
			}
			con.close();
			con = getAccessConnect();
			rs = con.createStatement()
					.executeQuery("select * from procatthird");
			while (rs.next()) {
				funthirds.put(rs.getString("categoryname"), rs.getInt("id"));
			}
			con.close();
			con = getAccessConnect();
			rs = con.createStatement().executeQuery("select * from procatfour");
			while (rs.next()) {
				funfours.put(rs.getString("categoryname"), rs.getInt("id"));
			}
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final static synchronized Connection getAccessConnect() {

		Connection con = null;
		while (true) {
			try {
				con = DriverManager.getConnection(url, "admin", "jimenghu");
				//con = pooled.getConnection();
				if (con != null) {
					return con;
				} else {
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
			} 
			
		}
	}

	public static void main(String[] args) throws SQLException {

		try {
			throw new NullPointerException("dd");
		} catch (Exception e) {
			// TODO: handle exception
			StackTraceElement[] elements = e.getStackTrace();
			for (int i = 0; i < elements.length; i++) {
				log.info(elements[i].getClassName() + " "
						+ elements[i].getMethodName() + " "
						+ elements[i].getLineNumber());
			}
		}

	}
}