package extract_data;

import analisy_algorithms.MyAlgorithms;
import crawler.HtmlParseData;
import crawler.Page;
import crawler.WebCrawler;
import crawler.WebURL;
import java.util.Set;
import java.util.regex.Pattern;



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
         return !FILTERS.matcher(href).matches()
                && (href.startsWith("http://www.upf.edu/") || href.startsWith("https://www.upf.edu"));
     }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
     @Override
     public void visit(Page page) {
         String url = page.getWebURL().getURL();
         if (page.getParseData() instanceof HtmlParseData) {
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             //this.getMyController().getJF().getJ3().setText(Integer.toString(htmlParseData.getOutgoingUrls().size()));
             String text = htmlParseData.getText();
             String html = htmlParseData.getHtml();  
             String title = htmlParseData.getTitle();
             Set<WebURL> links = htmlParseData.getOutgoingUrls();
             
             
             System.out.println(url);
             System.out.println(page.getWebURL().getPath());
             System.out.println(htmlParseData.getOutgoingUrls().size());
             System.out.println("STATUS CODE: " + page.getStatusCode());
             System.out.println("LANG: " + algorithms.detectLanguage(page));
             System.out.print("EMAILS: ");
             algorithms.printAllEmails(algorithms.detectEmails(page));

             
             /*System.out.println("Html length: " + html.length());
             System.out.println("Number of outgoing links: " + links.size());*/
         }
    }
}