package org.cccs.proxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static org.cccs.proxy.Utils.listStrings;
import static org.cccs.proxy.Utils.copyStream;
import static org.cccs.proxy.Utils.stripContext;

/**
 * User: boycook
 * Date: 18/04/2012
 * Time: 16:49
 */
public class Proxy {

    public static final String HOST_HEADER = "Host";
    public static final String DOMAIN = "domain";
    private String destination;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Set<String> allowedPaths;
    private final URL url;

    public Proxy(String destination, HttpServletRequest request, HttpServletResponse response, Set<String> allowedPaths) throws MalformedURLException {
        this.destination = destination;
        this.request = request;
        this.response = response;
        this.allowedPaths = allowedPaths;
        this.url = new URL(getUrl(request));
    }

    public void doProxy() throws IOException {
        if (isUrlAllowed(url)) {
            HttpURLConnection connection = getConnection(request);
            setRequestHeaders(connection, request);
            setResponseHeaders(connection, response);
            response.setStatus(connection.getResponseCode());
            copyStream(connection.getInputStream(), response.getOutputStream());
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            throw new SecurityException(format("You've attempted to access a URL that's not allowed [%s]", url));
        }
    }

    private HttpURLConnection getConnection(HttpServletRequest request) throws IOException {
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
            String headerValue = listStrings(entry.getValue());
            if (headerName != null) {
                System.out.println(format("ResponseHeader: [%s] [%s]", headerName, headerValue));
                response.setHeader(headerName, headerValue);
            }
        }
    }

    protected boolean isUrlAllowed(URL url) {
        return allowedPaths != null && allowedPaths.size() > 0 && allowedPaths.contains(url.getPath());
    }

    protected String getUrl(HttpServletRequest request) {
        return getDestination(request) + stripContext(request);
    }

    protected String getDestination(HttpServletRequest request) {
        String domain = request.getParameter(DOMAIN);
        if (domain != null && domain.length() > 0) {
            return domain;
        }
        return destination;
    }
}
