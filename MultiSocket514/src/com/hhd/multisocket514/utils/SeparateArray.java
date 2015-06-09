package com.hhd.multisocket514.utils;

import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SeparateArray {
	
//	private static boolean isGroupbyte = true;
//	
//	/**
//	 * @说明 拆包解析方法
//	 */
//	public static void splitByte(OutputStream ops) {
//		try {
//			ByteBuffer bbuf = ByteBuffer.allocate(2048);
//			int p = bbuf.position();
//			int l = bbuf.limit();
//			// 回绕缓冲区 一是将 curPointer 移到 0, 二是将 endPointer 移到有效数据结尾
//			bbuf.flip();
//			byte[] byten = new byte[bbuf.limit()]; // 可用的字节数量
//			bbuf.get(byten, bbuf.position(), bbuf.limit()); // 得到目前为止缓冲区所有的数据
//			// 进行基本检查，保证已经包含了一组数据
//			if (isGroupbyte) {
//				byte[] len = new byte[4];
//				// 数组源，数组源拷贝的开始位子，目标，目标填写的开始位子，拷贝的长度
//				System.arraycopy(byten, 0, len, 0, 4);
//				int length = 1200; // 每个字节流的最开始肯定是定义本条数据的长度
//				byte[] deco = new byte[length]; // deco 就是这条数据体
//				System.arraycopy(byten, 0, deco, 0, length);
//				// 判断消息类型，这个应该是从 deco 中解析了，但是下面具体的解析内容不再啰嗦
//				int type = 0;
//				// 判断类型分类操作
//				if (type == 1) {
//					
//				} else if (type == 2) {
//					
//				} else if (type == 3) {
//					
//				} else {
//					System.out.println("未知的消息类型，解析结束！");
//					// 清空缓存
//					bbuf.clear();
//				}
//				// 如果字节流是多余一组数据则递归
//				if (byten.length > length) {
//					byte[] temp = new byte[bbuf.limit() - length];
//					// 数组源，数组源拷贝的开始位子，目标，目标填写的开始位子，拷贝的长度
//					System.arraycopy(byten, length, temp, 0, bbuf.limit() - length);
//					// 情况缓存
//					bbuf.clear();
//					// 重新定义缓存
//					bbuf.put(temp);
//					// 递归回调
//					splitByte(ops);
//				}else if(byten.length == length){ // 如果只有一条数据，则直接重置缓冲就可以了
//					// 清空缓存
//					bbuf.clear();
//				}
//			} else {
//				// 如果没有符合格式包含数据，则还原缓冲变量属性
//				bbuf.position(p);
//				bbuf.limit(l);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
