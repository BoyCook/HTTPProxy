package org.cccs.proxy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.cccs.proxy.Utils.listStrings;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: boycook
 * Date: 19/04/2012
 * Time: 09:22
 */
public class TestUtils {

    @Test
    public void listStringsShouldWork() {
        List<String> strings = new ArrayList<String>();
        strings.add("foo");
        strings.add("and");
        strings.add("bar");

        assertThat(listStrings(strings), is(equalTo("foo,and,bar")));
    }

    @Test
    public void getURLPathShouldWork() {
        assertThat(Utils.stripContext("/search", "/search/another"), is(equalTo("/another")));
    }
}
