package com.fastcode.demo.sanitization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class CachedBodyServletInputStream extends ServletInputStream {

	@Autowired
	JsonSanitizition sanitizer;

	private InputStream cachedBodyInputStream;

	public CachedBodyServletInputStream(byte[] cachedBody) throws IOException {

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBody);
		System.out.println(">>>>>>CachedBodyServletInputStream<<<<<" + cachedBodyInputStream);

		String requestStr = IOUtils.toString(byteArrayInputStream);

		String jsonSanitize = sanitizer.jsonSanitize(requestStr);

		System.err.println(">>>>>>"+jsonSanitize);
		
		byte[] StringByte = IOUtils.toByteArray(jsonSanitize);

		InputStream bais = new ByteArrayInputStream(StringByte);
		
		System.err.println("sanitized input sytram"+bais);
		this.cachedBodyInputStream = bais;
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		try {
			return cachedBodyInputStream.available() == 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setReadListener(ReadListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return cachedBodyInputStream.read();
	}
}