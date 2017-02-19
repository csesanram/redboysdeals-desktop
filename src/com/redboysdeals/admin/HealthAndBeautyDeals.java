/**
 * 
 */
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

/**
 * @author User
 * 
 */
/**
 * @author User
 *
 */
public class HealthAndBeautyDeals {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		getHealthAndBeauty("http://api.epn.ebay.com/deals/v1/country/us/feed/json?campid=5337825386&categoryid=26395&customid=3001&count=40&offset=1&feedtype_id=2&toolid=100034&feedType=json");
	}

	public static void getHealthAndBeauty(String urlToRead) throws Exception {
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
		System.out.println("Total Health And Beauty Deals Count:" +arr.length());
		for (int i = 0; i < arr.length(); i++) {
			JSONObject item = arr.getJSONObject(i);
			String title = item.getString("title").replace("'", "");
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
			/*JSONObject contentObj = item.getJSONObject("content");
			String tableValue = contentObj.getString("value");
			//sb.append("<p>"+title+"</p> \n").append(tableValue).append("\n <br/>");
			 */			
		}
		if (!footerDivFalg && !headerDivFlag) {
			sb.append("</div> \n");
		}
		generateOutput(sb.toString());
		//System.out.println(sb.toString());
	}
	
	public static void generateOutput(String dealsStr) throws Exception {
		
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        
      
        VelocityContext context = new VelocityContext();
        context.put("hotDeals", dealsStr);

        Template t = ve.getTemplate( "./src/com/redboysdeals/admin/healthandbeauty_html.vm" );

        StringWriter sw = new StringWriter();

        t.merge( context, sw );

        /*
         *  use the output in the body of your emails
         */
        
        FileWriter fw = new FileWriter("./src/com/redboysdeals/admin/healthandbeauty/healthandbeautydeals/healthandbeauty.html");
        fw.write(sw.toString());
        fw.close();
        System.out.println( "healthandbeauty.html file has been created!!!!.." );
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
