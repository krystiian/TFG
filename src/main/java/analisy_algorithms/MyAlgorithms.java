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

import com.google.common.base.Optional;
import crawler.HtmlParseData;
import crawler.Page;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javax.ws.rs.client.Entity.html;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author chris
 */
public class MyAlgorithms {

    private final static Pattern pattern = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}");
    
    public List fetchEmails(String s)
    {
        List emailList = new ArrayList();
        String[] b = s.split("\\s+");
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
    
    public String fetchLang(String s)
    {
        /*
        HtmlParseData pd = (HtmlParseData) p.getParseData();
        String s = pd.getHtml();
        String[] b = s.replaceAll("\\s+,", "").replaceAll("\""," \" ").split("\\s+|-");
        for(int i = 0; i < b.length; ++i) if(b[i].equals("lang=")) return b[i+2];
        */
        Document document = Jsoup.parse(s);
        return document.getElementsByAttribute("lang").attr("lang"); 
    }
    
    
    public void printAllEmails(List l)
    {
        if(l.size() > 0)
            for(int i = 0; i < l.size(); ++i) System.out.print(l.get(i).toString() + " ");
    }
        
    public void printLang(String s){System.out.println(s);}
    
  }
