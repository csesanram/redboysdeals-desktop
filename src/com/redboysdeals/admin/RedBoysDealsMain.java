/**
 * 
 */
package com.redboysdeals.admin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author User
 *
 */
public class RedBoysDealsMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//Index deals
		String cellPhoneURL = "http://api.epn.ebay.com/deals/v1/country/us/feed/json?campid=5337825386&categoryid=15032&customid=2001&count=44&offset=1&feedtype_id=2&toolid=100034&feedType=json";
		FetchTodayDeals fetchTodayDeals = new FetchTodayDeals();
		//fetchTodayDeals.getCellPhonesAccessories(cellPhoneURL);
		
		//Hot Deals
		String clothsURL = "http://api.epn.ebay.com/deals/v1/country/us/feed/json?campid=5337825386&categoryid=11450&customid=3001&count=40&offset=1&feedtype_id=2&toolid=100034&feedType=json";
		HotDeals hotDeals = new HotDeals();
		hotDeals.getClothing(clothsURL);
		
		//LessThan$5 Deals
		LessThan5Dollars lessThan5Dollars = new LessThan5Dollars();
		lessThan5Dollars.collectData(getCategoryList());
		
		//Travel Deals
		String travelURL = "http://api.epn.ebay.com/deals/v1/country/us/feed/json?campid=5337825386&categoryid=3252&customid=3001&count=40&offset=1&feedtype_id=2&toolid=100034&feedType=json";
		TravelDeals travelDeals = new TravelDeals();
		travelDeals.getTravel(travelURL);
		
		//Health And Beauty Deals
		String healthAndBeautyURL = "http://api.epn.ebay.com/deals/v1/country/us/feed/json?campid=5337825386&categoryid=26395&customid=3001&count=40&offset=1&feedtype_id=2&toolid=100034&feedType=json";
		HealthAndBeautyDeals healthAndBeautyDeals = new HealthAndBeautyDeals();
		healthAndBeautyDeals.getHealthAndBeauty(healthAndBeautyURL);
		
	}
	
	public static Map getCategoryList() {
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
		return map;
	}

}
