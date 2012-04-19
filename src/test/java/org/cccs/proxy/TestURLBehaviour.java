package org.cccs.proxy;

import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: boycook
 * Date: 19/04/2012
 * Time: 09:02
 */
@RunWith(JMock.class)
public class TestURLBehaviour {

    @Test
    public void getUrlPath1ShouldWork() throws MalformedURLException {
        URL url = new URL("http://www.google.com/search?btnG=1&pws=0&q=foo+bar");
        assertThat(url.getPath(), is(equalTo("/search")));
    }

    @Test
    public void getUrlPathShouldWork() throws MalformedURLException {
        URL url = new URL("http://www.google.com/search/another?btnG=1&pws=0&q=foo+bar");
        assertThat(url.getPath(), is(equalTo("/search/another")));
    }
}
