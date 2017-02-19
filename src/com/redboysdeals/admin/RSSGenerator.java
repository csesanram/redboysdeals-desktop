/**
 * 
 */
package com.redboysdeals.admin;

import java.io.FileWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author User
 * 
 */
public class RSSGenerator {

	public static String RSSgenerator(EbayDataField ebayDataField, String sb,
			String viewFileNameWithPathLocation, String systemDate) {
		StringBuffer tempSb = new StringBuffer();

		tempSb.append("\t \t <item>\n");
		tempSb.append("\t \t \t <guid isPermaLink='false'>"
				+ viewFileNameWithPathLocation + "</guid> \n");
		tempSb.append("\t \t \t <title><![CDATA[" + ebayDataField.getTitle()
				+ "]]></title> \n");
		tempSb.append("\t \t \t <link>" + viewFileNameWithPathLocation
				+ "</link> \n");
		tempSb.append("\t \t \t <description><![CDATA[<img src='"
				+ ebayDataField.getImageURL() + "' />"
				+ ebayDataField.getTitle() + "]]></description> \n");
		tempSb.append("\t \t \t <enclosure url='"+ebayDataField.getImageURL()+"' type='image/jpeg'/>");
		tempSb.append("\t \t \t <pubDate>" + systemDate + "</pubDate> \n");
		tempSb.append("\t \t \t <media:thumbnail height='51' width='90' url='"
				+ ebayDataField.getImageURL() + "'/> \n");
		tempSb.append("\t \t \t <media:content height='51' width='90' medium='image' type='image/jpeg' url='"
				+ ebayDataField.getImageURL() + "'/> \n");
		tempSb.append("\t \t </item> \n");

		return sb + tempSb.toString();
	}

	public static void generateOutput(String item, String createdate)
			throws Exception {

		VelocityEngine ve = new VelocityEngine();
		ve.init();

		VelocityContext context = new VelocityContext();
		context.put("createdate", createdate);
		context.put("item", item);

		Template t = ve
				.getTemplate("./src/com/redboysdeals/admin/rss.vm");

		StringWriter sw = new StringWriter();

		t.merge(context, sw);

		/*
		 * use the output in the body of your emails
		 */

		FileWriter fw = new FileWriter(
				"./src/com/redboysdeals/admin/rss.xml");
		fw.write(sw.toString());
		fw.close();
		System.out.println("rss.xml file has been created!!!!..");
	}

	public static String getSystemDate() {
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"E, y-M-d 'at' h:m:s a z");

		dateFormatter = new SimpleDateFormat("E, d MMMM yyyy h:m:s a z");
		return dateFormatter.format(now).toLowerCase();
	}
}
