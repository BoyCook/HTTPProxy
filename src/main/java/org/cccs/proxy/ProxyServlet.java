package org.cccs.proxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static java.lang.String.format;

/**
 * User: boycook
 * Date: 18/04/2012
 * Time: 12:55
 */
public class ProxyServlet extends HttpServlet {

    private static final String DESTINATION = "http://www.google.co.uk";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ProxyServlet called...");
        Set<String> allowed = new HashSet<String>();
        allowed.add("/search");
        Proxy proxy = new Proxy(DESTINATION, request, response, allowed);
        proxy.doProxy();
        super.service(request, response);
    }
}
