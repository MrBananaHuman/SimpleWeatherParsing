package com.saltlux.shkim2.simple.weather;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class getWeatherKorea{
	private static final String rssFeed = "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=%s&gridy=%s";

	private String getUrl(int x, int y) {
		String url = String.format(rssFeed, x, y);
		return url;
	}

	private void parsing(org.jdom2.Document mainParser) throws JDOMException, IOException {
		Element root = mainParser.getRootElement();
		Element header = root.getChild("header");
		Element body = root.getChild("body");
		List<Element> data_list = body.getChildren("data");
		
		String updatedDate = header.getChild("tm").getValue();
		
    	for (Element el : data_list) {
    		for(Element dataChild : el.getChildren()){
    			String key = dataChild.getName();
    			String value = dataChild.getTextTrim();
    			System.out.println("key: " + key + " / value: " + value);
    			
    		}
 
    	}
	}
	
	public static void main(String[] args) throws Exception {
		getWeatherKorea runner = new getWeatherKorea();
		org.jdom2.Document document;
		SAXBuilder parser = new SAXBuilder();
		int x = 60;	//x-coordinate of location
		int y = 127;	//y-coordinate of location
		
		document = parser.build(runner.getUrl(x, y));
		runner.parsing(document);	
	}
}