package com.hhd.multisocket514.business;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import com.hhd.multisocket514.bean.DeviceBuff;
import com.hhd.multisocket514.utils.LogUtil;
import com.hhd.multisocket514.utils.SeparateMusicUtil;

import android.R.integer;
import android.content.Context;
import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

public class DecodePlayer {

	private final String TAG = "DecodeToPCMSample";
	// 左声道
	private final int LEFTCHANNAL = 0;
	// 右声道
	private final int RIGHTCHANNAL = 1;
	/** 用来解码 */
	private MediaCodec mMediaCodec;
	/** 用来读取音频文件 */
	private MediaExtractor extractor;
	private MediaFormat format;
	private String mime = null;
	private int sampleRate = 0, channels = 0, bitrate = 0;
	private long presentationTimeUs = 0, duration = 0;

	/** 音乐数据的URL */
	private String musicUrl;

	/** 剩余音乐的缓冲区 */
	private byte[] oddBuff = new byte[0];
	private Context context;
	private String IP;
	private int Port;
	private boolean isDecode = true;

	/**
	 * 发送音乐的声道，左声道音乐或右声道音乐
	 */
	private int channel;
	private Socket socket;

	/**
	 * @param context
	 * @param IP
	 *            设备的IP
	 * @param Port
	 *            端口：左声道为8900，右声道为8901；
	 * @param MusicData
	 *            发送的音乐
	 * @param channel
	 *            发送音乐的声道
	 */
	public DecodePlayer(Context context, String IP, int Port
			) {
		try {
			socket = new Socket(IP, Port);

			this.context = context;
			this.IP = IP;
			this.Port = Port;

			// BufferedOutputStream bufferedOutputStream = new
			// BufferedOutputStream(
			// socket.getOutputStream());
			// bufferedOutputStream.write(MusicData);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMusic(byte[] groudbyte) {

		BufferedOutputStream bufferedOutputStream;
		try {
			bufferedOutputStream = new BufferedOutputStream(
					socket.getOutputStream());
			bufferedOutputStream.write(groudbyte);
			bufferedOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.i("发送音乐失败！");
		}

	}

	public int getChannelBuff(String IP, int Port) {

		int buff = 0;

		QueryDeviceBuf2 queryDeviceBuf2 = new QueryDeviceBuf2(context);
		queryDeviceBuf2.initQueryDeviceBuf();

		DeviceBuff deviceBuff = queryDeviceBuf2.getDeviceBuff(IP);

		if (Port == 8900) {
			buff = deviceBuff.getLeftChannelBuff();
		} else if (Port == 8901) {
			buff = deviceBuff.getRightChannelBuff();
		}

		return buff;

	}

	public void initMediaExtractor(String musicUrl) {
		extractor = new MediaExtractor();
		// 根据路径获取源文件
		try {
			extractor.setDataSource(musicUrl);
		} catch (Exception e) {
			LogUtil.e(" 设置文件路径错误" + e.getMessage());
		}

		try {

			int trackCount = extractor.getTrackCount();
			LogUtil.i("音频轨道数：" + trackCount);
			// 音频文件信息
			format = extractor.getTrackFormat(0);
			mime = format.getString(MediaFormat.KEY_MIME);

			sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

			// 声道个数：单声道或双声道
			channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
			// if duration is 0, we are probably playing a live stream
			duration = format.getLong(MediaFormat.KEY_DURATION);
			// System.out.println("歌曲总时间秒:"+duration/1000000);
			bitrate = format.getInteger(MediaFormat.KEY_BIT_RATE);

		} catch (Exception e) {
			// LogUtil.e("音频文件信息读取出错：" + e.getMessage());
			// 不要退出，下面进行判断
		}
		LogUtil.d("Track info: mime:" + mime + "\n" + "采样率sampleRate:"
				+ sampleRate + "\n" + "channels:" + channels + "\n"
				+ "bitrate:" + bitrate + "\n" + "duration:" + duration + "\n"
				+ "musicUrl:" + musicUrl);
		// 检查是否为音频文件
		if (format == null || !mime.startsWith("audio/")) {
			LogUtil.e("不是音频文件 end !");
			return;
		}
	}

	public void initMediaCodec() {
		// 实例化一个指定类型的解码器,提供数据输出
		// Instantiate an encoder supporting output data of the given mime type
		mMediaCodec = MediaCodec.createDecoderByType(mime);

		if (mMediaCodec == null) {
			LogUtil.e("创建解码器失败！");
			return;
		}
		mMediaCodec.configure(format, null, null, 0);

		mMediaCodec.start();
	}

	public void decode(String musicUrl) {

		initMediaExtractor(musicUrl);

		initMediaCodec();

		// 用来存放目标文件的数据
		ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
		// 解码后的数据
		ByteBuffer[] outputBuffers = mMediaCodec.getOutputBuffers();
		// 设置声道类型:AudioFormat.CHANNEL_OUT_MONO单声道，AudioFormat.CHANNEL_OUT_STEREO双声道
		int channelConfiguration = channels == 1 ? AudioFormat.CHANNEL_OUT_MONO
				: AudioFormat.CHANNEL_OUT_STEREO;
		LogUtil.i("channelConfiguration=" + channelConfiguration);

		extractor.selectTrack(0);

		// ==========开始解码=============
		boolean IsInputEnd = false;
		boolean IsOutputEnd = false;
		final long kTimeOutUs = 10;
		// 解码器元缓冲区资料
		MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
		while (!IsOutputEnd) {
			try {
				if (!IsInputEnd) {
					// 获取缓冲区的索引
					int inputBufIndex = mMediaCodec
							.dequeueInputBuffer(kTimeOutUs);
					if (inputBufIndex >= 0) {
						ByteBuffer dstBuf = inputBuffers[inputBufIndex];
						// 将编码好的音频样本放进dstbuf，返回样本的数据大小
						int sampleSize = extractor.readSampleData(dstBuf, 0);
						if (sampleSize < 0) {
							LogUtil.d("输入缓冲区满了");
							IsInputEnd = true;
							sampleSize = 0;
						} else {
							// 获取音频样本的显现时间
							presentationTimeUs = extractor.getSampleTime();
						}
						// 将输入的数据排队
						mMediaCodec
								.queueInputBuffer(
										inputBufIndex,
										0,
										sampleSize,
										presentationTimeUs,
										IsInputEnd ? MediaCodec.BUFFER_FLAG_END_OF_STREAM
												: 0);

						if (!IsInputEnd) {
							extractor.advance();
						}

					} else {
						LogUtil.e("inputBufIndex " + inputBufIndex);
					}
				} // !IsInputEnd

				// decode to PCM and push it to the AudioTrack player
				// 获取输出缓冲的索引
				int outputBufIndex = mMediaCodec.dequeueOutputBuffer(info,
						kTimeOutUs);
				// LogUtil.i("获取输出缓冲的索引"+res);

				if (outputBufIndex >= 0) {
					ByteBuffer buf = outputBuffers[outputBufIndex];
					final byte[] chunk = new byte[info.size];
					buf.get(chunk);
					buf.clear();
					if (chunk.length > 0) {

						// chunk解码后的音频流
						// TODO:处理...
						ArrayList<byte[]> fullMusic = SeparateMusicUtil
								.separate16bitMusic(chunk);
						// 一个声道的音乐
						byte[] oneChannelMc = fullMusic.get(channel);
						// 当剩余音乐缓冲区为0时，将不够1200字节剩余的音乐放进缓冲区，

						if (oddBuff.length == 0) {

							int packagenum = oneChannelMc.length / 1200;// 数据报的数量

							oddBuff = new byte[oneChannelMc.length - packagenum
									* 1200];
							// 字节少于1200的数据，放进缓冲区
							System.arraycopy(oneChannelMc, packagenum * 1200,
									oddBuff, 0, oddBuff.length);
							for (int i = 0; i <= packagenum && isDecode == true; i++) {
								byte[] groudbyte = Arrays
										.copyOfRange(oneChannelMc, i * 1200,
												i * 1200 + 1200);
								sendMusic(groudbyte);

							}

						} else {
							byte[] laterBuff = new byte[oneChannelMc.length
									+ oddBuff.length];
							int packagenum = laterBuff.length / 1200;// 数据报的数量

							System.arraycopy(oddBuff, 0, laterBuff, 0,
									oddBuff.length);
							
							System.arraycopy(oneChannelMc, 0, laterBuff, oddBuff.length,
									oneChannelMc.length);

							oddBuff = new byte[laterBuff.length - packagenum
									* 1200];
							// 字节少于1200的数据，放进缓冲区
							System.arraycopy(laterBuff, packagenum * 1200,
									oddBuff, 0, oddBuff.length);
							for (int i = 0; i <= packagenum && isDecode == true; i++) {
								byte[] groudbyte = Arrays
										.copyOfRange(laterBuff, i * 1200,
												i * 1200 + 1200);
								sendMusic(groudbyte);

							}

						}

					}
					// 释放输出缓冲
					mMediaCodec.releaseOutputBuffer(outputBufIndex, false);
					if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
						LogUtil.d("saw output EOS.");
						IsOutputEnd = true;
					}

				} else if (outputBufIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
					outputBuffers = mMediaCodec.getOutputBuffers();
					LogUtil.w("[AudioDecoder]output buffers have changed.");
				} else if (outputBufIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
					MediaFormat oformat = mMediaCodec.getOutputFormat();
					LogUtil.w("[AudioDecoder]output format has changed to "
							+ oformat);
				} else {
					LogUtil.w("[AudioDecoder] dequeueOutputBuffer returned "
							+ outputBufIndex);
				}

			} catch (RuntimeException e) {
				LogUtil.e("[decodeMP3] error:" + e.getMessage());
			}
		}
		// =================================================================================
		if (mMediaCodec != null) {
			mMediaCodec.stop();
			mMediaCodec.release();
			mMediaCodec = null;
		}
		if (extractor != null) {
			extractor.release();
			extractor = null;
		}
		// clear source and the other globals
		duration = 0;
		mime = null;
		sampleRate = 0;
		channels = 0;
		bitrate = 0;
		presentationTimeUs = 0;
		duration = 0;
	}

}
