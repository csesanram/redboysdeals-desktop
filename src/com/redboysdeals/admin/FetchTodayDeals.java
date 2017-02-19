package com.redboysdeals.admin;


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONObject;

import com.redboysdeals.util.RedBoysDealsUtil;

public class FetchTodayDeals {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		getCellPhonesAccessories("http://api.epn.ebay.com/deals/v1/country/us/feed/json?campid=5337825386&categoryid=15032&customid=2001&count=44&offset=1&feedtype_id=2&toolid=100034&feedType=json");
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
		System.out.println("Total Fetch Deals Count:" + arr.length());
		
		String templateName = "./src/com/redboysdeals/admin/todaydeals/today_deals_html.vm";
		String distPathLocation = "./src/com/redboysdeals/admin/todaydeals";
		String webPathLocation = "todaydeals"; 
		
		String itemElements = "";
		String createDate = RSSGenerator.getSystemDate();
		for (int i = 0; i < arr.length(); i++) {
			JSONObject item = arr.getJSONObject(i);
			String title = item.getString("title").replace("'", "");
			String dealURL = item.getString("DealURL");
			String imageURL = item.getString("Image300URL");
			String salePrice = item.getString("SalePrice");
			String listPrice = item.getString("ListPrice");
			
			EbayDataField ebayDataField = new EbayDataField();
			ebayDataField.setTitle(title);
			ebayDataField.setSalePrice(salePrice);
			ebayDataField.setListPrice(listPrice);
			ebayDataField.setImageURL(item.getString("ImageURL"));
			ebayDataField.setDealURL(dealURL);
			ebayDataField.setSavingsRate(item.getString("SavingsRate"));
			
			String viewFileNameWithPathLocation = RedBoysDealsUtil.viewProductPageCreator(ebayDataField, templateName, distPathLocation, webPathLocation);
			
			itemElements = RSSGenerator.RSSgenerator(ebayDataField, itemElements, "http://www.redboysdeals.com/"+viewFileNameWithPathLocation, createDate);
			
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
						sb.append("\t \t \t \t <p><a href='"+viewFileNameWithPathLocation+"' class='btn btn-primary'>View Deal</a> </p> \n");
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
		if (!footerDivFalg && !headerDivFlag) {
			sb.append("</div> \n");
		}
		
		headerDivFlag = true;
		footerDivFalg = false;
		incrementCount = 0;
				
		StringBuffer first4DealsSB = new StringBuffer();
		first4DealsSB.append("<div id='top-deal-text'>Top 10 Deals</div> \n");
		first4DealsSB.append("<div class='row text-center'> \n");
		for (int i = 40; i < 44; i++) {
			JSONObject item = arr.getJSONObject(i);
			String title = item.getString("title").replace("'", "");
			String dealURL = item.getString("DealURL");
			String imageURL = item.getString("Image300URL");
			String salePrice = item.getString("SalePrice");
			String listPrice = item.getString("ListPrice");
			
			EbayDataField ebayDataField = new EbayDataField();
			ebayDataField.setTitle(title);
			ebayDataField.setSalePrice(salePrice);
			ebayDataField.setListPrice(listPrice);
			ebayDataField.setImageURL(item.getString("ImageURL"));
			ebayDataField.setDealURL(dealURL);
			ebayDataField.setSavingsRate(item.getString("SavingsRate"));
			
			String viewFileNameWithPathLocation = RedBoysDealsUtil.viewProductPageCreator(ebayDataField, templateName, distPathLocation, webPathLocation);
			
			
			first4DealsSB.append("\t <div class='col-md-3 col-sm-6 hero-feature'> \n");
			first4DealsSB.append("\t \t <div class='thumbnail'> \n");
			first4DealsSB.append("\t \t \t <div class='toptext'>"+title+"</div>");
			first4DealsSB.append("\t \t \t <img src='"+imageURL+"' alt='"+title+"' height='190'> \n");
			first4DealsSB.append("\t \t \t <div class='caption'> \n");
			first4DealsSB.append("\t \t \t \t <p><span class='offer-price'>$"+salePrice+"</span> <span class='actual-price'>$<strike>"+listPrice+"</strike></span></p> \n");
			first4DealsSB.append("\t \t \t \t <p><a href='"+viewFileNameWithPathLocation+"' class='btn btn-primary'>View Deal</a> </p> \n");
			first4DealsSB.append("\t \t \t </div> \n");
			first4DealsSB.append("\t \t </div> \n");
			first4DealsSB.append("\t </div> \n");
			
		}
		
		first4DealsSB.append("</div> \n");
		generateOutput(sb.toString(),first4DealsSB.toString());
		
		RSSGenerator.generateOutput(itemElements, createDate);
		//System.out.println(sb.toString());
	}
	
	
	
	public static void generateOutput(String dealsStr, String first4Deals) throws Exception {
		
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        
      
        VelocityContext context = new VelocityContext();
        context.put("todayDeals", dealsStr);
        context.put("first4Deals", first4Deals);

        Template t = ve.getTemplate( "./src/com/redboysdeals/admin/index_html.vm" );

        StringWriter sw = new StringWriter();

        t.merge( context, sw );

        /*
         *  use the output in the body of your emails
         */
        
        FileWriter fw = new FileWriter("./src/com/redboysdeals/admin/index.html");
        fw.write(sw.toString());
        fw.close();
        System.out.println( "Index.html file has been created!!!!.." );
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
