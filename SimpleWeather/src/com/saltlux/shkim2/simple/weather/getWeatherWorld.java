package com.saltlux.shkim2.simple.weather;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.jdom2.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class getWeatherWorld{
	static final String owmApiKey = "";
	static final String rssFeed = "http://api.openweathermap.org/data/2.5/%s?lat=%s&lon=%s&appid=" + owmApiKey; 
	
	private String toStringUnixTime(long unixTime) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		calendar.setTimeInMillis(unixTime*1000);
    	return dateFormat.format(calendar.getTime());
    }
	
	private URL getUrl(String time, String latitude, String longitude) throws MalformedURLException {
		/*
		 * OpenWeatherMap provides current weather and forecast weather
		 * 'time' parameter
		 *   - weather: to get the current weather information
		 *   - forecast: to get the forecast weather information
		 * 
		 */
		URL url = new URL(String.format(rssFeed, time, latitude, longitude));
		return url;
	}
	
	private String getWeatherInfoFromURL(URL url) throws Exception  {
		final int maxTry = 3; //Sometimes, OpenWeatherMap returns 500 server error
		for (int i = 0 ; i < maxTry ; i++) {
			try {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				Scanner scan = new Scanner(connection.getInputStream(), "UTF-8");
				StringBuilder str = new StringBuilder();
				while(scan.hasNext()) {
					str.append(scan.nextLine());
				}
				scan.close();
				connection.disconnect();
				return str.toString();
				
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (Exception te) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	private void currentWeatherParsing(URL url, String x, String y) throws Exception {
		JSONParser mainParser = new JSONParser();
		JSONObject parseredObject;
		String jsonData = getWeatherInfoFromURL(url);
		if (jsonData == null) {
			return;
		}
		parseredObject = (JSONObject) mainParser.parse(jsonData);
		String updateDt = toStringUnixTime((Long) parseredObject.get("dt"));
		
		JSONArray weatherInfo = (JSONArray) parseredObject.get("weather");
	    JSONObject mainInfo = (JSONObject) parseredObject.get("main");
	    JSONObject windInfo = (JSONObject) parseredObject.get("wind");
	    JSONObject cloudsInfo = (JSONObject) parseredObject.get("clouds");
	    JSONObject weatherObj = (JSONObject) weatherInfo.get(0);
	    
	    for(Object key : mainInfo.keySet()) {
	    	System.out.println("(current) main key: " + key + " / value: " + mainInfo.get(key));
	    }
	    for(Object key : windInfo.keySet()) {
	    	System.out.println("(current) wind key: " + key + " / value: " + windInfo.get(key));
	    }
	    for(Object key : weatherObj.keySet()) {
	    	System.out.println("(current) weather key: " + key + " / value: " + weatherObj.get(key));
	    }
	    for(Object key : cloudsInfo.keySet()) {
	    	System.out.println("(current) clouds key: " + key + " / value: " + cloudsInfo.get(key));
	    }
		
	}
	
	private void forecastWeatherParsing(URL url, String x, String y) throws Exception {
		JSONParser mainParser = new JSONParser();
		JSONObject parseredObject;
		String jsonData = getWeatherInfoFromURL(url);
		if (jsonData == null) {
			return;
		}
		parseredObject = (JSONObject) mainParser.parse(jsonData);
		JSONArray forecastArray = (JSONArray) parseredObject.get("list");
		
		for(int j = 0; j < forecastArray.size(); j++) {
			JSONObject forecastWeatherInfoArray = (JSONObject) forecastArray.get(j);
	    	String updateDt = toStringUnixTime((Long) forecastWeatherInfoArray.get("dt"));
	    	
	    	JSONArray weatherInfo = (JSONArray) forecastWeatherInfoArray.get("weather");
		    JSONObject mainInfo = (JSONObject) forecastWeatherInfoArray.get("main");
		    JSONObject windInfo = (JSONObject) forecastWeatherInfoArray.get("wind");
		    JSONObject cloudsInfo = (JSONObject) forecastWeatherInfoArray.get("clouds");
		    JSONObject weatherObj = (JSONObject) weatherInfo.get(0);
		    
		    for(Object key : mainInfo.keySet()) {
		    	System.out.println("(forecast) main key: " + key + " / value: " + mainInfo.get(key));
		    }
		    for(Object key : windInfo.keySet()) {
		    	System.out.println("(forecast) wind key: " + key + " / value: " + windInfo.get(key));
		    }
		    for(Object key : weatherObj.keySet()) {
		    	System.out.println("(forecast) weather key: " + key + " / value: " + weatherObj.get(key));
		    }
		    for(Object key : cloudsInfo.keySet()) {
		    	System.out.println("(forecast) clouds key: " + key + " / value: " + cloudsInfo.get(key));
		    }
		}
		
		
	}
	
	public static void main(String[] args) throws Exception {
		getWeatherWorld runner = new getWeatherWorld();
		URL url = null;
		
		String latitude = "37.33";
		String longitude = "126.7";
		
		url = runner.getUrl("weather", latitude, longitude);
		runner.currentWeatherParsing(url, latitude, longitude);
		
		url = runner.getUrl("forecast", latitude, longitude);
		runner.forecastWeatherParsing(url, latitude, longitude);
		
	}
}