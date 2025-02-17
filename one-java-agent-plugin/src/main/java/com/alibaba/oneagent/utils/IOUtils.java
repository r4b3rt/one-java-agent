package com.alibaba.oneagent.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author hengyunabc 2018-11-06
 *
 */
public class IOUtils {

	public static String toString(InputStream inputStream) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		return result.toString("UTF-8");
	}

	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
	}

    /**
     * @return a byte[] containing the information contained in the specified
     *         InputStream.
     * @throws java.io.IOException
     */
    public static byte[] getBytes(InputStream input) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        copy(input, result);
        result.close();
        return result.toByteArray();
    }

	public static IOException close(InputStream input) {
		return close((Closeable) input);
	}

	public static IOException close(OutputStream output) {
		return close((Closeable) output);
	}

	public static IOException close(final Reader input) {
		return close((Closeable) input);
	}

	public static IOException close(final Writer output) {
		return close((Closeable) output);
	}

	public static IOException close(final Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (final IOException ioe) {
			return ioe;
		}
		return null;
	}

	// support jdk6
	public static IOException close(final ZipFile zip) {
		try {
			if (zip != null) {
				zip.close();
			}
		} catch (final IOException ioe) {
			return ioe;
		}
		return null;
	}

	public static IOException close(URLClassLoader urlClassLoader) {
		try {
			if (urlClassLoader instanceof Closeable) {
				urlClassLoader.close();
			}
		} catch (IOException e) {
			return e;
		}
		return null;
	}

}
