package net.BrokenLinkChecker;

import java.net.*;

public class ParseURL {

    public static String getDomain(String url) throws MalformedURLException {
        URL m_url = new URL(url);
        return m_url.getHost();
    }
}
