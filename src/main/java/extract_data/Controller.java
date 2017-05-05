package extract_data;

import crawler.CrawlConfig;
import crawler.CrawlController;
import crawler.PageFetcher;
import crawler.RobotstxtConfig;
import crawler.RobotstxtServer;
import static crawler.UserAgentDirectives.logger;

public class Controller {
    public static void main(String[] args) throws Exception {
        long startedAt = System.currentTimeMillis();
        if (args.length != 2) {
            logger.info("Needed parameters: ");
            logger.info("\t rootFolder (it will contain intermediate crawl data)");
            logger.info("\t numberOfCralwers (number of concurrent threads)");
            return;
        }
        String crawlStorageFolder = args[0];
        int numberOfCrawlers = 1;   
        
        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(2);
        config.setMaxPagesToFetch(200);
        config.setResumableCrawling(false);
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(25);      

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://www.tecnonews.info/");
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
        System.out.println("Total Time Crawling: " + (System.currentTimeMillis() - startedAt)/1000 + " Seconds");
    }
}