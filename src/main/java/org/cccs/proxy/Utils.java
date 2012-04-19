package org.cccs.proxy;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * User: boycook
 * Date: 18/04/2012
 * Time: 16:51
 */
public final class Utils {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    public static int copyStream(InputStream sourceStream, OutputStream destinationStream) throws IOException {
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

    public static String listStrings(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(string);
        }
        return sb.toString();
    }

    public static String stripContext(HttpServletRequest request) {
        String context = request.getServletPath();
        String path = request.getRequestURI() + "?" + request.getQueryString();
        return stripContext(context, path);
    }

    public static String stripContext(String context, String path) {
        if (path.startsWith(context)) {
            return path.substring(context.length());
        }
        return path;
    }
}
