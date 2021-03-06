/*
 * Copyright 2017 chris.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package analisy_algorithms;

import crawler.HtmlParseData;
import crawler.Page;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import main.mainMenu;
import org.apache.tika.language.LanguageIdentifier;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author chris
 */
public class MyAlgorithms {

    private final static Pattern pattern = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}");
    
    public List detectEmails(Page p)
    {
        String s = ((HtmlParseData) p.getParseData()).getText();
        List emailList = new ArrayList();
        String[] b = s.replaceAll(","," ").split("\\s+");
        for(int i = 0; i < b.length; ++i)
        {   
            if(b[i].contains("@"))
            {
                while (b[i].endsWith(".")) b[i] = b[i].substring(0, b[i].length() - 1);
                while (b[i].startsWith(".")) b[i] = b[i].substring(1, b[i].length());
                Matcher matcher = pattern.matcher(b[i].toUpperCase());
                if (matcher.matches()) emailList.add(b[i]);   
            }
        }
        return emailList;
    } 
    
    public String detectLanguage(Page p)
    {
        HtmlParseData htmlParseData = (HtmlParseData) p.getParseData();
        String s = htmlParseData.getHtml();
        Document document = Jsoup.parse(s);
        if(document.getElementsByAttribute("xml:lang").attr("xml:lang").length() > 0)
        {
            //System.out.print("xml:lang ");
            return codeToLang(document.getElementsByAttribute("xml:lang").attr("xml:lang").substring(0, 2));
        }
        else if(document.getElementsByAttribute("lang").attr("lang").length() > 0 ) 
        {
            //System.out.print("lang ");
            return codeToLang(document.getElementsByAttribute("lang").attr("lang").toLowerCase().substring(0, 2));
        }
        else if(document.getElementsByTag("p").size() > 0)
        {
            //System.out.print("p ");
            LanguageIdentifier languageIdentifier = new LanguageIdentifier(document.getElementsByTag("p").text().replaceAll("[^\\p{L}\\p{Nd}]+|[0-9]|\\s+", " "));
            return codeToLang(languageIdentifier.getLanguage());
        }
        //System.out.print("title ");
        LanguageIdentifier languageIdentifier = new LanguageIdentifier(htmlParseData.getTitle().replaceAll("\\s+", " "));
        return codeToLang(languageIdentifier.getLanguage());
    }
    
    public String getAllEmails(List l, mainMenu m)
    {
        String s = "";
        if(l.size() > 0)
        {
            m.emailsFetched += l.size();
            for(int i = 0; i < l.size(); ++i)
            {
                s += l.get(i).toString();
                if(i < l.size()-1) s += (",");   
            }
        }
        else s = "null";
        return s;
    }
    
    public String codeToLang(String code)
    {
        Locale loc = new Locale(code);
        return loc.getDisplayLanguage();
    }
    
    public boolean isValidURL(String url)
    {  
        URL u = null;
        try {  
            u = new URL(url);  
        } catch (MalformedURLException e) {  
            return false;  
        }

        try {  
            u.toURI();  
        } catch (URISyntaxException e) {  
            return false;  
        }  
        return true;  
    }
    
    public boolean pageContainsContent(Page p,String[] s, boolean all, boolean atLeast , boolean none)
    {
        if(!(p.getParseData() instanceof HtmlParseData)) return false;
        if(!(all || atLeast || none)) return true;
        HtmlParseData htmlp = (HtmlParseData) p.getParseData();
        String content = htmlp.getText().replaceAll("\\s+"," ").toLowerCase();
        int found = 0;
        for(int i = 0; i < s.length; ++i)
        {
            if(content.contains(s[i]))
            {
                if(none) return false;
                else if(atLeast) return true;
                ++found;
            }
        }
        if(all)
        {
            if(found != s.length) return false;
            else return true;
        }
        else if(atLeast) return false;
        return true;
    }
  }
