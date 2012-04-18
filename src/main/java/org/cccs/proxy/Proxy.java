package org.cccs.proxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.cccs.proxy.Utils.concatComma;
import static org.cccs.proxy.Utils.copyStream;

/**
 * User: boycook
 * Date: 18/04/2012
 * Time: 16:49
 */
public class Proxy {

    private static final String HOST_HEADER = "Host";
    private String destination;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public Proxy(String destination, HttpServletRequest request, HttpServletResponse response) {
        this.destination = destination;
        this.request = request;
        this.response = response;
    }

    public void doProxy() throws IOException {
        HttpURLConnection connection = getConnection(request);
        setRequestHeaders(connection, request);
        setResponseHeaders(connection, response);

        response.setStatus(connection.getResponseCode());
        copyStream(connection.getInputStream(), response.getOutputStream());
    }

    private HttpURLConnection getConnection(HttpServletRequest request) throws IOException {
        URL url = new URL(getUrl(request));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(request.getMethod().toUpperCase());
        return connection;
    }

    private void setRequestHeaders(HttpURLConnection connection, HttpServletRequest request) {
        for (Enumeration enu = request.getHeaderNames(); enu.hasMoreElements(); ) {
            String headerName = (String) enu.nextElement();
            String headerValue = request.getHeader(headerName);
            if (headerName.equalsIgnoreCase(HOST_HEADER)) {
                headerValue = getDestination(request);
            }
            if (headerName != null) {
                System.out.println(format("RequestHeader: [%s] [%s]", headerName, headerValue));
                connection.setRequestProperty(headerName, request.getHeader(headerName));
            }
        }
    }

    private void setResponseHeaders(HttpURLConnection connection, HttpServletResponse response) {
        Map<String, List<String>> responseHeaders = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
            String headerName = entry.getKey();
            String headerValue = concatComma(entry.getValue());
            if (headerName != null) {
                System.out.println(format("ResponseHeader: [%s] [%s]", headerName, headerValue));
                response.setHeader(headerName, headerValue);
            }
        }
    }

    private String getUrl(HttpServletRequest request) {
        String context = request.getServletPath();
        String path = request.getRequestURI() + "?" + request.getQueryString();
        if (path.startsWith(context)) {
            path = path.substring(context.length());
        }
        String url = getDestination(request) + path;
        System.out.println("Got URL: " + url);
        return url;
    }

    private String getDestination(HttpServletRequest request) {
        String domain = request.getParameter("domain");
        if (domain != null && domain.length() > 0) {
            return domain;
        }
        return destination;
    }
}
