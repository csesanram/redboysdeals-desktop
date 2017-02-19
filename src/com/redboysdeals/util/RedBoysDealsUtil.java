/**
 * 
 */
package com.redboysdeals.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONObject;

import com.redboysdeals.admin.EbayDataField;

/**
 * @author User
 *
 */
public class RedBoysDealsUtil {
	
	public static ArrayList CollectData(String categID, String urlToRead) throws IOException {
		ArrayList arrayList = new ArrayList();
		//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		//String urlToRead = "http://api.epn.ebay.com/deals/v1/country/us/feed/json?campid=5337825386&categoryid="+entry.getKey()+"&customid=3001&count=20&offset=1&feedtype_id=2&toolid=100034&feedType=json";
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
				String title = item.getString("title");
				String dealURL = item.getString("DealURL");
				String imageURL = item.getString("Image300URL");
				String salePrice = item.getString("SalePrice");
				String listPrice = item.getString("ListPrice");
				
				String imageURL2 = item.getString("ImageURL");
				String imageURL3 = item.getString("Image140URL");
				String dealEndTime = item.getString("EndTime");
				String quantity = item.getString("Quantity");
				String quantitySold = item.getString("QuantitySold");
				String savingsRate = item.getString("SavingsRate");
				String itemSummary = item.getString("ItemSummary");
				
				ebayDataField.setTitle(title);
				ebayDataField.setDealURL(dealURL);
				ebayDataField.setImageURL(imageURL);
				ebayDataField.setSalePrice(salePrice);
				ebayDataField.setListPrice(listPrice);
				
				ebayDataField.setImageURL2(imageURL2);
				ebayDataField.setImageURL3(imageURL3);
				ebayDataField.setDealEndTime(dealEndTime);
				ebayDataField.setQuantity(quantity);
				ebayDataField.setQuantitySold(quantitySold);
				ebayDataField.setSavingsRate(savingsRate);
				ebayDataField.setItemSummary(itemSummary);
				
				arrayList.add(ebayDataField);
			}
		}
		return arrayList;
	}

	public static String viewProductPageCreator(EbayDataField ebayDataField, String templateName, String distPathLocation, String webPathLocation) throws Exception {
		
		VelocityEngine ve = new VelocityEngine();
        ve.init();
              
        VelocityContext context = new VelocityContext();
        
        context.put("title", ebayDataField.getTitle());
        context.put("finalPrice", "$"+ebayDataField.getSalePrice());
        context.put("saving", ebayDataField.getSavingsRate());
        context.put("listPrice", "$"+ebayDataField.getListPrice());
        context.put("imgsrc", ebayDataField.getImageURL());
        context.put("dealURL", ebayDataField.getDealURL());
        
        Template t = ve.getTemplate(templateName);

        StringWriter sw = new StringWriter();
        String fileName = distPathLocation+"/"+fileNameGenerator(ebayDataField.getTitle())+".html";
        String webPathName = webPathLocation+"/"+fileNameGenerator(ebayDataField.getTitle())+".html";;
        context.put("webPathName", "http://www.redboysdeals.com/"+webPathName);
        
        t.merge( context, sw );
        
        FileWriter fw = new FileWriter(fileName);
        fw.write(sw.toString());
        fw.close();
        System.out.println( "View Product File has been created"+ fileName);
        
		return webPathName;
	}
	
	public static String fileNameGenerator(String titleName) {
		String viewProductFileName = "";
		viewProductFileName = titleName.replaceAll("[^a-zA-Z0-9 ]", "");
		return addFileNameWithHyphen(viewProductFileName);
	}
	
	public static String addFileNameWithHyphen (String name) {
		String addWithHyphen = "";
		String[] splited = name.split("\\s+");
		for (int i=0; i<splited.length; i++) {
			if (i == splited.length ) {
				addWithHyphen = splited[i];
			} else if (i == 0) {
				addWithHyphen = splited[i];
			} else {
				addWithHyphen = addWithHyphen + "-" + splited[i];
			}
		}
		return addWithHyphen;
	}
}
