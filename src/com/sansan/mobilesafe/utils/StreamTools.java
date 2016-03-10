package com.sansan.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
	/**
	 * @param is 输入流
	 * @return String 返回的字符串
	 * @throws IOException 
	 */
	public static String readFromStream(InputStream is) throws IOException{
//		InputStreamReader reader = new InputStreamReader(is);
//		BufferedReader br = new BufferedReader(reader);
//		StringBuilder builder = new StringBuilder();
//		String line = null;
//		while ((line=br.readLine()) != null) {
//			builder.append(line);
//			
//		}
//		is.close();
//		br.close();
//		return builder.toString();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = is.read(buffer))!=-1){
			baos.write(buffer, 0, len);
		}
		is.close();
		String result = baos.toString();
		baos.close();
		return result;
	}
}
