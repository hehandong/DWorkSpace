package com.hhd.multisocket514.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class TransformUtil {

	/**
	 * 将字节数组格式化成16进制字符
	 * 
	 * @param data
	 * @return
	 */
	public static String ArrayTo16String(byte[] data) {

		StringBuilder result = new StringBuilder();

		for (byte b : data) {

			result.append(String.format("%02X", b));

		}
		return result.toString();

	}

	/**
	 * 将字节数组格式化成16进制字符
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/**
	 * 字节格式化成10进制正整数
	 * 
	 * @param b
	 * @return
	 */
	public static byte byteTo10Positivebyte(byte b) {
		byte result = (byte) (b & 0xff);
		return result;

	}

	

	
	
	
	 /** 
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *  
     * @param src 
     *            byte数组 
     * @param offset 
     *            从数组的第offset位开始 
     * @return int数值 
     */  
	public static int bytesToInt(byte[] src, int offset) {
		int value;	
		value = (int) ((src[offset] & 0xFF) 
				| ((src[offset+1] & 0xFF)<<8) 
				| ((src[offset+2] & 0xFF)<<16) 
				| ((src[offset+3] & 0xFF)<<24));
		return value;
	}
	
	 /** 
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
	public static int bytesToInt2(byte[] src, int offset) {
		int value;	
		value = (int) ( ((src[offset] & 0xFF)<<24)
				|((src[offset+1] & 0xFF)<<16)
				|((src[offset+2] & 0xFF)<<8)
				|(src[offset+3] & 0xFF));
		return value;
	}

	
	 /** 
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     * @param value 
     *            要转换的int值
     * @return byte数组
     */  
	public static byte[] intToBytes( int value ) 
	{ 
		byte[] src = new byte[4];
		src[3] =  (byte) ((value>>24) & 0xFF);
		src[2] =  (byte) ((value>>16) & 0xFF);
		src[1] =  (byte) ((value>>8) & 0xFF);  
		src[0] =  (byte) (value & 0xFF);				
		return src; 
	}
	 /** 
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */  
	public static byte[] intToBytes2(int value) 
	{ 
		byte[] src = new byte[4];
		src[0] = (byte) ((value>>24) & 0xFF);
		src[1] = (byte) ((value>>16)& 0xFF);
		src[2] = (byte) ((value>>8)&0xFF);  
		src[3] = (byte) (value & 0xFF);		
		return src;
	}
	
	/**
	 * 十进制整数转换成4字节
	 * <p>
	 * 
	 * @param number
	 *            十进制序号
	 * @return 4字节
	 */
	public static byte[] intTo4bytes(int number)
	{
		byte[] bytes = new byte[4];
		// 十进制转成16进制
		String hexString = Integer.toHexString(number);
		StringBuffer sb = new StringBuffer();
		sb.append(hexString);
		// 高位补零
		while (true)
		{
			if (sb.length() == 8)
			{
				break;
			}
			sb.insert(0, "0");
		}

		// System.out.println(number+"="+sb.toString());
		for (int i = 0; i < 4; i++)
		{
			bytes[3 - i] = (byte) Integer.parseInt(sb.toString().substring(i * 2, i * 2 + 2), 16);
			// System.out.println("bytes["+(3-i)+"]=" + bytes[3-i]);
		}
		return bytes;
	}
	

}
