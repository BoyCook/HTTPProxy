package org.cccs.stubs.proxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * User: boycook
 * Date: 18/04/2012
 * Time: 12:55
 */
public class ProxyServlet extends HttpServlet {
    /*
        http://www.google.com/search?btnG=1&pws=0&q=alex+fishlock
        localhost:9001/proxy/search?btnG=1&pws=0&q=alex+fishlock
     */
    private static final String DESTINATION = "http://www.google.co.uk";
//    private static final String DESTINATION = "http://dilbert.com";
    private static final String HOST_HEADER = "Host";
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ProxyServlet called...");

        HttpURLConnection connection = getConnection(request);
        setRequestHeaders(connection, request);
        setResponseHeaders(connection, response);

        response.setStatus(connection.getResponseCode());
        copyStream(connection.getInputStream(), response.getOutputStream());
        super.service(request, response);
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
        return DESTINATION;
    }

    protected static int copyStream(InputStream sourceStream, OutputStream destinationStream) throws IOException {
        int bytesRead = 0;
        int totalBytes = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (bytesRead >= 0) {
            bytesRead = sourceStream.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                destinationStream.write(buffer, 0, bytesRead);
            }
            totalBytes += bytesRead;
        }
        destinationStream.flush();
        destinationStream.close();
        return totalBytes;
    }

    protected static String concatComma(List<String> strings) {
        Iterator<String> it = strings.iterator();
        StringBuilder sb = new StringBuilder(it.next());
        while (it.hasNext()) {
            sb = sb.append(",").append(it.next());
        }
        return sb.toString();
    }
}
