// Copyright 2012 Square, Inc.
package com.count.android.cache.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


/**
 * Use GSON to serialize classes to a bytes.
 * <p/>
 * Note: This will only work when concrete classes are specified for {@code T}.
 * If you want to specify an interface for {@code T} then you need to also
 * include the concrete class name in the serialized byte array so that you can
 * deserialize to the appropriate type.
 */
public class StringConverter implements TwoLevelLruCache.Converter<String> {

	private static final int RECORD_CONNECTION_SIZE = 8 * 1024;
	public StringConverter() {
	    }

	@Override
    public String from(byte[] bytes) {    	
		String key = null;
		BufferedInputStream in = new BufferedInputStream(
				new ByteArrayInputStream(bytes));

		final ByteArrayOutputStream responseData = new ByteArrayOutputStream(
				RECORD_CONNECTION_SIZE); // big enough to handle success
											// response without reallocating
		int c;
		try {
			while ((c = in.read()) != -1) {
				responseData.write(c);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			key = responseData.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
    }

    @Override
    public void toStream(String str, OutputStream bytes) throws IOException {
    BufferedOutputStream bufferedOutput = null;
    bufferedOutput = new BufferedOutputStream(bytes, RECORD_CONNECTION_SIZE);
    //Start writing to the output stream
    bufferedOutput.write(str.getBytes());
    bufferedOutput.close();
    }
}