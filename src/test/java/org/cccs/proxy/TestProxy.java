package org.cccs.proxy;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * User: boycook
 * Date: 19/04/2012
 * Time: 09:16
 */
public class TestProxy extends AbstractJMockTestSupport {

    private static final String DESTINATION = "http://www.google.co.uk";
    private Proxy proxy;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setup() throws MalformedURLException {
        Set<String> allowed = new HashSet<String>();
        allowed.add("/search");
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        one(request).getParameter(Proxy.DOMAIN);
        one(request).getServletPath();
        one(request).getRequestURI();
        one(request).getQueryString();
        confirmExpectations();
        proxy = new Proxy(DESTINATION, request, response, allowed);
    }

    @Test
    public void isUrlAllowedShouldBeTrueForPathInTheList() throws MalformedURLException {
        one(request).getParameter(Proxy.DOMAIN);
        confirmExpectations();
        assertTrue(proxy.isUrlAllowed(new URL("http://www.google.com/search?btnG=1&pws=0&q=foo+bar")));
    }

    @Test
    public void isUrlAllowedShouldBeTrueForPathIsNotInTheList() throws MalformedURLException {
        one(request).getParameter(Proxy.DOMAIN);
        confirmExpectations();
        assertFalse(proxy.isUrlAllowed(new URL("http://www.google.com/create?btnG=1&pws=0&q=foo+bar")));
    }
}
