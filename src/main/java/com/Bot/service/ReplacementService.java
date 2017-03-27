package com.Bot.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ReplacementService {

	public String getReplacement(String group){
		
		Document doc = null;
		try {
			doc = Jsoup.connect("http://hpk.edu.ua/replacements").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Elements elementParse = doc.select(".news-body td");
		List<String> allReplacement = new ArrayList<>();
		
		List<String> replac = new ArrayList<String>();

		for(int i = 0; i < elementParse.size(); i++){
		if (elementParse.get(i).text().equals(group)){
			replac.add(elementParse.get(i+1).text());
			replac.add(elementParse.get(i+2).text());
			replac.add(elementParse.get(i+3).text());
			replac.add(elementParse.get(i+4).text());
			replac.add(elementParse.get(i+5).text());
			allReplacement.add(replac.toString());
			replac = new ArrayList<String>();
			}
		}
		String replacement = replac.toString();
		
		StringBuilder replacements = new StringBuilder();
		for (String replace : allReplacement) {
			replacements.append(replace + "\n");
		}
		
		
		return String.format("Заміни %s:%n %s.", group, replacements.toString()) ;
	}

}
