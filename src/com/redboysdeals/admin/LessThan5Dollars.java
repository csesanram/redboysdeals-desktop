package com.redboysdeals.admin;

/**
 * @author User
 *
 */


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONObject;

public class LessThan5Dollars {

	public static void main(String[] args) throws Exception {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(20081, "Antiques");
		map.put(550, "Art");
		map.put(2984, "Baby");
		map.put(267, "Books");
		map.put(12576, "Business");
		map.put(625, "Cameras");
		map.put(15032, "CellPhonesAccessories");
		map.put(11450, "ClothingShoesAccessories");
		map.put(11116, "Coins");
		map.put(1, "Collectibles");
		map.put(58058, "Computers");
		map.put(293, "ConsumerElectronics");
		map.put(14339, "Crafts");
		map.put(237, "Dolls");
		
		map.put(11232, "DVDs");
		
		map.put(6000, "eBayMotors");
		map.put(45100, "EntertainmentMemorabilia");
		map.put(99, "EverythingElse");
		map.put(172008, "GiftCards");
		map.put(26395, "HealthBeauty");
		map.put(11700, "HomeGarden");
		map.put(281, "JewelryWatches");
		map.put(11233, "Music");
		map.put(619, "MusicalInstruments");
		map.put(1281, "PetSupplies");
		map.put(870, "PotteryGlass");
		map.put(10542, "RealEstate");
		map.put(316, "SpecialtyServices");
		map.put(888, "SportingGoods");
		map.put(64482, "SportsMem");
		
		map.put(260, "Stamps");
		map.put(1305, "TicketsExperiences");
		map.put(220, "ToysHobbies");
		map.put(3252, "Travel");
		map.put(1249, "VideoGames");
		map.put(11232, "DVDs");
		
		collectData(map);
		
		//getCellPhonesAccessories("http://api.epn.ebay.com/deals/v1/country/us/feed/json?campid=5337825386&categoryid=11450&customid=3001&count=20&offset=1&feedtype_id=2&toolid=100034&feedType=json");
	}

	public static void collectData(Map<Integer, String> categMap) throws Exception {
		ArrayList arrayList = new ArrayList();
		for (Map.Entry<Integer, String> entry : categMap.entrySet()) {
			//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			String urlToRead = "http://api.epn.ebay.com/deals/v1/country/us/feed/json?campid=5337825386&categoryid="+entry.getKey()+"&customid=3001&count=20&offset=1&feedtype_id=2&toolid=100034&feedType=json";
			StringBuilder result = new StringBuilder();
			
			URL url = new URL(urlToRead);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			
			JSONObject obj = new JSONObject(result.toString());
			if (!obj.isNull("entry")) {
				JSONArray arr = obj.getJSONArray("entry");
				
				for (int i = 0; i < arr.length(); i++) {
					EbayDataField ebayDataField = new EbayDataField();
					JSONObject item = arr.getJSONObject(i);
					String title = item.getString("title").replace("'", "");
					String dealURL = item.getString("DealURL");
					String imageURL = item.getString("Image300URL");
					String salePrice = item.getString("SalePrice");
					String listPrice = item.getString("ListPrice");
					
					if (salePrice !=null && !"".equals(salePrice)) {
						int salePriceAmt = Integer.parseInt(salePrice.split("\\.")[0]);
						if (salePriceAmt< 6) {
							ebayDataField.setTitle(title);
							ebayDataField.setDealURL(dealURL);
							ebayDataField.setImageURL(imageURL);
							ebayDataField.setSalePrice(salePrice);
							ebayDataField.setListPrice(listPrice);
							arrayList.add(ebayDataField);
						}
					}
					
				}
			}
		}
		System.out.println("Total Deals Collected::"+ arrayList.size());
		processCollectedDeals(arrayList);
	}
	
	public static void processCollectedDeals (ArrayList arryList) throws Exception {
		StringBuffer sb = new StringBuffer();
		boolean headerDivFlag = true;
		boolean footerDivFalg = false;
		int incrementCount = 0;
		for (int i = 0; i < arryList.size(); i++) {
			EbayDataField ebayDataField = (EbayDataField)arryList.get(i);
			String title = ebayDataField.getTitle();
			String dealURL = ebayDataField.getDealURL();
			String imageURL = ebayDataField.getImageURL();
			String salePrice = ebayDataField.getSalePrice();
			String listPrice = ebayDataField.getListPrice();
			
			if (headerDivFlag) {
				sb.append("<div class='row text-center'> \n");
			}
				headerDivFlag = false;
				sb.append("\t <div class='col-md-3 col-sm-6 hero-feature'> \n");
					sb.append("\t \t <div class='thumbnail'> \n");
						sb.append("\t \t \t <img src='"+imageURL+"' alt='"+title+"' height='190'> \n");
						sb.append("\t \t \t <div class='caption'> \n");
						sb.append("\t \t \t \t <h3 class='prod-head'>"+title+"</h3> \n");
						sb.append("\t \t \t \t <p><span class='offer-price'>$"+salePrice+"</span> <span class='actual-price'>$<strike>"+listPrice+"</strike></span></p> \n");
						sb.append("\t \t \t \t <p><a href='"+dealURL+"' class='btn btn-primary'>View Deal</a> </p> \n");
						sb.append("\t \t \t </div> \n");
					sb.append("\t \t </div> \n");
				sb.append("\t </div> \n");
				incrementCount++;
				if (incrementCount == 4) {
					footerDivFalg = true;
				}
			if (footerDivFalg) {
				sb.append("</div> \n");
				footerDivFalg = false;
				incrementCount = 0;
				headerDivFlag = true;
			}
		}
		if (!footerDivFalg && !headerDivFlag) {
			sb.append("</div> \n");
		}
		generateOutput(sb.toString());
	}
	public static void getCellPhonesAccessories(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();

		JSONObject obj = new JSONObject(result.toString());
		//String pageName = obj.getJSONObject("entry").getString("title");
		StringBuffer sb = new StringBuffer();
		JSONArray arr = obj.getJSONArray("entry");
		boolean headerDivFlag = true;
		boolean footerDivFalg = false;
		int incrementCount = 0;
		for (int i = 0; i < arr.length(); i++) {
			JSONObject item = arr.getJSONObject(i);
			String title = item.getString("title");
			String dealURL = item.getString("DealURL");
			String imageURL = item.getString("Image300URL");
			String salePrice = item.getString("SalePrice");
			String listPrice = item.getString("ListPrice");
			
			if (headerDivFlag) {
				sb.append("<div class='row text-center'> \n");
			}
				headerDivFlag = false;
				sb.append("\t <div class='col-md-3 col-sm-6 hero-feature'> \n");
					sb.append("\t \t <div class='thumbnail'> \n");
						sb.append("\t \t \t <img src='"+imageURL+"' alt='"+title+"' border='0' height='190px'> \n");
						sb.append("\t \t \t <div class='caption'> \n");
						sb.append("\t \t \t \t <h3 class='prod-head'>"+title+"</h3> \n");
						sb.append("\t \t \t \t <p><span class='offer-price'>$"+salePrice+"</span> <span class='actual-price'>$<strike>"+listPrice+"</strike></span></p> \n");
						sb.append("\t \t \t \t <p><a href='"+dealURL+"' class='btn btn-primary'>View Deal</a> </p> \n");
						sb.append("\t \t \t </div> \n");
					sb.append("\t \t </div> \n");
				sb.append("\t </div> \n");
				incrementCount++;
				if (incrementCount == 4) {
					footerDivFalg = true;
				}
			if (footerDivFalg) {
				sb.append("</div> \n");
				footerDivFalg = false;
				incrementCount = 0;
				headerDivFlag = true;
			}
			/*JSONObject contentObj = item.getJSONObject("content");
			String tableValue = contentObj.getString("value");
			//sb.append("<p>"+title+"</p> \n").append(tableValue).append("\n <br/>");
			 */			
		}
		generateOutput(sb.toString());
		//System.out.println(sb.toString());
	}
	
	public static void generateOutput(String dealsStr) throws Exception {
		
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        
      
        VelocityContext context = new VelocityContext();
        context.put("hotDeals", dealsStr);

        Template t = ve.getTemplate( "./src/com/redboysdeals/admin/lessthan5dollars_html.vm" );

        StringWriter sw = new StringWriter();

        t.merge( context, sw );

        /*
         *  use the output in the body of your emails
         */
        
        FileWriter fw = new FileWriter("./src/com/redboysdeals/admin/lessthan5dollars.html");
        fw.write(sw.toString());
        fw.close();
        System.out.println( "lessthan5dollars.html file has been created!!!!.." );
	}
	
	public static void getComputersTablets(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();

		JSONObject obj = new JSONObject(result.toString());
		//String pageName = obj.getJSONObject("entry").getString("title");
		StringBuffer sb = new StringBuffer();
		JSONArray arr = obj.getJSONArray("entry");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject item = arr.getJSONObject(i);
			String title = item.getString("title");
			JSONObject contentObj = item.getJSONObject("content");
			
			String tableValue = contentObj.getString("value");
			sb.append("<p>"+title+"</p> \n").append(tableValue).append("\n <br/>");
			
		}
		System.out.println(sb.toString());
	}
}
