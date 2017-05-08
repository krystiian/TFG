package extract_data;

import analisy_algorithms.MyAlgorithms;
import crawler.HtmlParseData;
import crawler.Page;
import crawler.WebCrawler;
import crawler.WebURL;
import java.util.Set;
import java.util.regex.Pattern;
import main.mainMenu;



public class MyCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(
        ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
        "|rm|smil|wmv|swf|wma|zip|rar|gz|bmp|gif|jpe?g|png|tiff?))$");
    MyAlgorithms algorithms = new MyAlgorithms();

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
     @Override
     public boolean shouldVisit(Page referringPage, WebURL url) {
         String href = url.getURL().toLowerCase();
         mainMenu menu = this.getMyController().menu;
         if(FILTERS.matcher(href).matches()) return false;
         menu.enlacesTotales += 1;
         menu.setTextStats(menu.enlacesTotales + " ENLACES   |   " + menu.enlacesValidos + " VALIDOS   |   " + menu.enlacesAnalizados + " ANALIZADOS  |  " + menu.enlacesErroneos + " ERROR  |  " + menu.emailsFetched + " EMAILS");
         if(isUnderCondition(menu, url) && algorithms.pageContainsContent(referringPage, menu.contains, menu.isAll, menu.isAtLeast, menu.isNone)){
             menu.enlacesValidos += 1;
             if(href.contains("?")) url.setPriority((byte)1);
         }
         else url.setPriority((byte)2);
         return true;
         /*for(int i = 0; i < menu.semilla.length; ++i)
         {
             String semilla = removePrefix(menu.semilla[i]);
             String urlr = removePrefix(href);
             if(semilla.equals(urlr) || urlr.equals(semilla+"/"))
             {
                 menu.enlacesValidos += 1;
                 return true;
             }
         }
        if(!FILTERS.matcher(href).matches() && isUnderCondition(menu, url))
        {
            menu.enlacesValidos += 1;
            return true;
        }
        return false;
         */
     }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
     @Override
     public void visit(Page page) {
         String url = page.getWebURL().getURL();
         mainMenu menu = this.getMyController().menu;
         if (page.getParseData() instanceof HtmlParseData && isUnderCondition(menu, page.getWebURL()) && algorithms.pageContainsContent(page, menu.contains, menu.isAll, menu.isAtLeast, menu.isNone)) {
             //menu.updateBar((int)Math.floor(menu.enlacesAnalizados/(double)menu.links*100));
             //menu.enlacesValidos += 1;
             //menu.setTextStats(menu.enlacesTotales + " ENLACES   |   " + menu.enlacesValidos + " VALIDOS   |   " + menu.enlacesAnalizados + " ANALIZADOS  |  " + menu.enlacesErroneos + " ERROR  |  " + menu.emailsFetched + " EMAILS");
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();             
             Set<WebURL> links = htmlParseData.getOutgoingUrls();
             
             menu.writeConsole(tiempoEjecucion(page,menu));
             menu.enlacesAnalizados += 1;
             menu.setTextStats(menu.enlacesTotales + " ENLACES   |   " + menu.enlacesValidos + " VALIDOS   |   " + menu.enlacesAnalizados + " ANALIZADOS  |  " + menu.enlacesErroneos + " ERROR  |  " + menu.emailsFetched + " EMAILS");
         }
    }
     
     public String removePrefix(String url)
     {
        if(url.startsWith("http://")) url = url.replace("http://","");
        else if(url.startsWith("http:\\\\"))url = url.replace("http:\\\\","");
        else if(url.startsWith("https://")) url = url.replace("https://","");
        else if(url.startsWith("https:\\\\"))url = url.replace("https:\\\\","");
        return url;
     }
     public Boolean isUnderCondition(mainMenu menu, WebURL href)
     {
         String s = href.getURL().toLowerCase();
         if(menu.isContiene)
         {
             boolean foundContiene = false;
             String[] contiene = menu.contiene;
             for(int i=0; i < contiene.length; ++i)
             {
                 if(s.contains(contiene[i])) foundContiene = true;
             }
             if(!foundContiene) return false;
         }
         
         if(menu.isNoContiene)
         {
             String[] noContiene = menu.noContiene;
             for(int i=0; i < noContiene.length; ++i)
             {
                 if(s.contains(noContiene[i])) return false;
             }
         }
                  
         if(menu.isRegex)
         {
             Pattern regex = Pattern.compile(menu.regex);
             if(!regex.matcher(s).matches()) return false;
         }
         return true;
     }
     
     public String tiempoEjecucion(Page p, mainMenu menu)
     {
         String s = "";
         String idioma = "null";
         String email = "null";
         String url = p.getWebURL().getURL().toLowerCase();
         String status = ""+p.getStatusCode();
         if(!menu.isIdioma && !menu.isEmails)
         {
             menu.data.put(menu.enlacesAnalizados+1+"", new Object[]{url,status,idioma,email});
             return "";
         }
         if(menu.isIdioma) idioma = algorithms.detectLanguage(p);
         if(menu.isEmails) email = algorithms.getAllEmails(algorithms.detectEmails(p),menu) + "\n";
         menu.data.put(menu.enlacesAnalizados+1+"", new Object[]{url,status,idioma,email});
         return ("URL: " + p.getWebURL().getURL().toLowerCase() + "\nSTATUS: " + status + "\nIDIOMA: " + idioma +"\nEMAILS: " +email +"\n");
     }
}