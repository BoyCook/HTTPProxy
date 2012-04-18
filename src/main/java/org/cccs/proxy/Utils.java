package org.cccs.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
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

    public static String concatComma(List<String> strings) {
        Iterator<String> it = strings.iterator();
        StringBuilder sb = new StringBuilder(it.next());
        while (it.hasNext()) {
            sb = sb.append(",").append(it.next());
        }
        return sb.toString();
    }

}
