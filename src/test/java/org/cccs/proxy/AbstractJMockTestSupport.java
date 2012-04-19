package org.cccs.proxy;

/**
 * User: boycook
 * Date: 19/04/2012
 * Time: 09:52
 */

import org.hamcrest.Description;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import java.util.ArrayList;
import java.util.List;

public class AbstractJMockTestSupport extends org.jmock.Expectations {
    private final Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    protected void confirmExpectations() {
        context.checking(this);
    }

    protected void justIgnore(final Object... mocks) {
        for (final Object mock : mocks) {
            ignoring(mock);
        }
        confirmExpectations();
    }

    public <T> T mock(final Class<T> classToMock) {
        return context.mock(classToMock);
    }

    public <T> T mock(final Class<T> classToMock, final String mockName) {
        return context.mock(classToMock, mockName);
    }

    public <T> T dummy(final Class<T> classToMock) {
        return ignoring(mock(classToMock));
    }

    public void applyMockAssertions() {
        context.assertIsSatisfied();
    }

    protected static Action returnSuccessiveValues(final Object... objects) {
        final String num = String.valueOf(objects.length);
        final List<Object> returnValues = new ArrayList<Object>();
        for (Object object : objects) {
            returnValues.add(object);
        }
        return new Action() {
            @Override
            public void describeTo(final Description description) {
            }

            @Override
            public Object invoke(final Invocation invocation) throws Throwable {
                if (returnValues.isEmpty()) {
                    throw new IllegalStateException("invocation count exceeded the " + num + " object(s) passed in");
                }
                final Object retval = returnValues.remove(0);
                if (retval != null) {
                    invocation.checkReturnTypeCompatibility(retval);
                }
                return retval;
            }
        };
    }
}
