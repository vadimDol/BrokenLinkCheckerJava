package  net.BrokenLinkChecker;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection.Response;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BrokenLinkChecker {

    private static Integer getStatusCode(String url) throws IOException{
        Response response = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).followRedirects(false).execute();
        return response.statusCode();
    }


    private static Elements getLinks(String url) throws IOException{
        Connection connection = Jsoup.connect(url)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                .ignoreHttpErrors(true);
        Document document = connection.get();
        return document.select("a");
    }

    private static String getDomain(String url, Pattern pattern) {
        Matcher matcherUrl = pattern.matcher(url);
        return matcherUrl.find() ? matcherUrl.group(2) : null;
    }

    private static boolean isImage(String url)
    {
        int last = url.length()-1;
        int extStart = url.length()-3;
        String ext = url.substring(extStart);
        return (ext.equalsIgnoreCase("jpg")
                ||ext.equalsIgnoreCase("png")
                ||ext.equalsIgnoreCase("gif")
                ||ext.equalsIgnoreCase("tif"));


    }
    public static void main (String[] args)
    {
        String inUrl = "https://liberta.ru.com";
        //Pattern pattern = Pattern.compile("(http://www.|https://www.|http://|https://|www.):?([^/ :]+):?([^/ ]*)(/?[^ #?]*)\\\\x3f?([^ #]*)#?([^ ]*)");
       // String mainDomain = getDomain(inUrl, pattern);
        try
        {
            String mainDomain = ParseURL.getDomain(inUrl);
            System.out.println(mainDomain);
            FileWriter fileAllLinks = new FileWriter("AllLinks.txt", false);
            FileWriter fileBrokenLinks = new FileWriter("BrokenLinks.txt", false);
            Stack stack = new Stack() ;
            stack.push(inUrl);
            HashMap<String, Integer> linkMap = new HashMap<String, Integer>();
            while (!stack.isEmpty()) {
                String url = stack.pop();
                if(linkMap.get(url) == null ) {
                    Integer statusCode = getStatusCode(url);
                    if(statusCode == 200) {
                        Elements links = getLinks(url);
                        linkMap.put(url, statusCode);
                        fileAllLinks.write(statusCode + " " + url + "\n");
                        for (Element link : links) {
                            String linkAttr = link.attr("abs:href");
                            String domain = ParseURL.getDomain(linkAttr);
                            if( (!stack.isExists(linkAttr)) && (linkMap.get(linkAttr) == null)
                                    && (mainDomain.equals(domain))) {
                                stack.push(linkAttr);

                            }

                        }
                    }
                    else {
                        linkMap.put(url, statusCode);
                        fileAllLinks.write(statusCode + " " + url + "\n");
                        fileBrokenLinks.write(statusCode + " " + url + "\n");
                    }

                }
            }
            fileAllLinks.flush();
            fileBrokenLinks.flush();

        }
        catch (MalformedURLException urlEx) {
            System.out.println(urlEx.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
