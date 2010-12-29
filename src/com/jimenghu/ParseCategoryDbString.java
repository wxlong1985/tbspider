package com.jimenghu;

import java.io.StringReader;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseCategoryDbString {

	/**
	 * @param args
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws DocumentException {
		// TODO Auto-generated method stub
		String xml = "<root><option value='13'>手机</option><option value='1043'>笔记本电脑</option><option value='11'>电脑硬件/台式机/网络设备</option><option value='1103'>IP卡/网络电话/在线影音充值</option><option value='12'>MP3/MP4/iPod/录音笔</option><option value='17'>数码相机/摄像机/图形冲印</option><option value='1048'>3C数码配件市场</option><option value='1046'>家用电器/hifi音响/耳机</option><option value='1052'>网络服务/电脑软件</option><option value='1041'>移动联通充值中心/IP长途</option><option value='1105'>闪存卡/U盘/移动存储</option><option value='37'>男装</option><option value='1106'>运动鞋</option><option value='1104'>个人护理/保健/按摩器材</option><option value='1102'>腾讯QQ专区</option><option value='14'>女装/流行女装</option><option value='1056'>女鞋</option><option value='1055'>女士内衣/男士内衣/家居服</option><option value='15'>彩妆/香水/护肤/美体</option><option value='23'>珠宝/钻石/翡翠/黄金</option><option value='31'>箱包皮具/热销女包/男包</option><option value='1044'>品牌手表/流行手表</option><option value='1054'>饰品/流行首饰/时尚饰品</option><option value='18'>运动/瑜伽/健身/球迷用品</option><option value='1082'>流行男鞋/皮鞋</option><option value='1045'>户外/军品/旅游/机票</option><option value='1040'>ZIPPO/瑞士军刀/饰品/眼镜</option><option value='22'>汽车/配件/改装/摩托/自行车</option><option value='24'>居家日用/厨房餐饮/卫浴洗浴</option><option value='1122'>时尚家饰/工艺品/十字绣</option><option value='1050'>家具/家具定制/宜家代购</option><option value='1049'>床上用品/靠垫/窗帘/布艺</option><option value='21'>办公设备/文具/耗材</option><option value='36'>网络游戏装备/游戏币/帐号/代练</option><option value='26'>装潢/灯具/五金/安防/卫浴</option><option value='1051'>保健品/滋补品</option><option value='29'>食品/茶叶/零食/特产</option><option value='1020'>母婴用品/奶粉/孕妇装</option><option value='16'>电玩/配件/游戏/攻略</option><option value='30'>玩具/动漫/模型/卡通</option><option value='35'>网络游戏点卡</option><option value='34'>书籍/杂志/报纸</option><option value='33'>音乐/影视/明星/乐器</option><option value='20'>古董/邮币/字画/收藏</option><option value='32'>宠物/宠物食品及用品</option><option value='27'>成人用品/避孕用品/情趣内衣</option><option value='1042'>网店装修/物流快递/图片存储</option><option value='1053'>演出/旅游/吃喝玩乐折扣券</option><option value='1047'>鲜花速递/蛋糕配送/园艺花艺</option><option value='1062'>童装/婴儿服/鞋帽</option><option value='1153'>运动服</option><option value='1154'>服饰配件/皮带/帽子/围巾</option></root>";
		
		String id = "34";
		
		
		StringReader sr = new StringReader(xml);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(sr);
		for (Iterator iter = doc.selectNodes("//option").iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			System.out.println("insert into shopcategory(categoryname) values('"+element.getTextTrim().replace(" ->", "")+"');");
		}

	}

}
