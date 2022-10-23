package com.xxd.common.basic.utils;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.xxd.common.basic.media.MediaMetadataInfo;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:媒体相关工具类
 */
public class MediaUtil {


    private static final String TAG = MediaUtil.class.getSimpleName();

    /**
     * 查找视频轨道
     *
     * @param extractor
     * @return
     */
    public static int selectVideoTrack(@NonNull MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找音频轨道
     *
     * @param extractor
     * @return
     */
    public static int selectAudioTrack(@NonNull MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取filePath文件的时长
     *
     * @param filePath 音视频文件路径
     * @return 时长 单位：毫秒
     */
    public static long getDuration(String filePath) {
        try {
            MediaExtractor mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(filePath);
            int trackIdx = selectVideoTrack(mediaExtractor);
            if (trackIdx == -1) {
                trackIdx = selectAudioTrack(mediaExtractor);
                if (trackIdx == -1) {
                    return 0;
                }
            }
            MediaFormat mediaFormat = mediaExtractor.getTrackFormat(trackIdx);
            long duration = mediaFormat.containsKey(MediaFormat.KEY_DURATION) ? mediaFormat.getLong(MediaFormat.KEY_DURATION) : 0;
            duration = duration / 1000; //微妙转换为毫秒
            mediaExtractor.release();
            return duration;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取媒体元数据信息
     *
     * @param path
     * @return
     */
    public static MediaMetadataInfo getMediaMetadataInfo(String path) {
        MediaMetadataRetriever retriever = null;
        MediaMetadataInfo metadataInfo = null;
        try {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);

            metadataInfo = new MediaMetadataInfo();
            //bitrate
            String value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
            if (!TextUtils.isEmpty(value)) {
                metadataInfo.setBitrate((int) Str2NumUtil.parse(value));
            }
            //width
            value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            if (!TextUtils.isEmpty(value)) {
                Log.i(TAG, "getMediaMetadataInfo: video width = " + value);
                metadataInfo.setWidth((int) Str2NumUtil.parse(value));
            }
            value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_WIDTH);
            if (!TextUtils.isEmpty(value)) {
                Log.i(TAG, "getMediaMetadataInfo: image width = " + value);
                metadataInfo.setWidth((int) Str2NumUtil.parse(value));
            }
            //height
            value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            if (!TextUtils.isEmpty(value)) {
                metadataInfo.setHeight((int) Str2NumUtil.parse(value));
            }
            value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_HEIGHT);
            if (!TextUtils.isEmpty(value)) {
                metadataInfo.setHeight((int) Str2NumUtil.parse(value));
            }
            //duration
            value = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (!TextUtils.isEmpty(value)) {
                Log.i(TAG, "getMediaMetadataInfo: duration = " + value);
                metadataInfo.setDuration((int) Str2NumUtil.parse(value));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (retriever != null) {
                retriever.release();
            }

        }
        return metadataInfo;
    }

}
