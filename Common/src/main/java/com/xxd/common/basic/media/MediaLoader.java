package com.xxd.common.basic.media;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;
import com.xxd.common.basic.media.bean.AudioBean;
import com.xxd.common.basic.media.bean.ImgBean;
import com.xxd.common.basic.media.bean.VideoBean;
import com.xxd.common.basic.utils.FileP2pUtil;
import com.xxd.common.basic.utils.SdkVerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public class MediaLoader {

    private static final String TAG = MediaLoader.class.getSimpleName();

    /**
     * 表列字段常量接口
     */
    public interface Column {
        String ID = MediaStore.MediaColumns._ID;
        String DATA = MediaStore.MediaColumns.DATA;
        String DISPLAY_NAME = MediaStore.MediaColumns.DISPLAY_NAME;
        String SIZE = MediaStore.MediaColumns.SIZE;
        String DATE_MODIFIED = MediaStore.MediaColumns.DATE_MODIFIED;
        String WIDTH = MediaStore.MediaColumns.WIDTH;
        String HEIGHT = MediaStore.MediaColumns.HEIGHT;
        String DURATION = MediaStore.MediaColumns.DURATION;
        String MIME_TYPE = MediaStore.MediaColumns.MIME_TYPE;
        String RELATIVE_PATH = MediaStore.MediaColumns.RELATIVE_PATH;
    }

    /**
     * 加载类型枚举常量类
     */
    public enum LoadType {
        IMG_VIDEO,
        IMG,
        VIDEO,
        AUDIO,
        DOC,
        ;
    }

    /**
     * 查询条件构建器
     */
    public static class SelectionBuilder {

        StringBuilder mStrBuilder;

        public SelectionBuilder() {
            mStrBuilder = new StringBuilder();
        }

        public SelectionBuilder and(String condition) {
            if (TextUtils.isEmpty(condition)) {
                return this;
            }

            if (!TextUtils.isEmpty(mStrBuilder.toString())) {
                mStrBuilder.append(" AND ");
            }
            mStrBuilder.append("(");
            mStrBuilder.append(condition);
            mStrBuilder.append(")");
            return this;
        }

        public SelectionBuilder or(String condition) {
            if (TextUtils.isEmpty(condition)) {
                return this;
            }

            if (!TextUtils.isEmpty(mStrBuilder.toString())) {
                mStrBuilder.append(" OR ");
            }
            mStrBuilder.append("(");
            mStrBuilder.append(condition);
            mStrBuilder.append(")");
            return this;
        }

        public String build() {
            return mStrBuilder.toString();
        }

    }

    /**
     * 是否为Android Q及以上的版本
     *
     * @return
     */
    private static boolean isHigherAndroidQ() {
        return SdkVerUtil.isAndroidQ();
    }

    private static String[] sProjections;

    /**
     * 获取要返回的表列的字段数组
     *
     * @return
     */
    private static String[] getProjections() {
        if (sProjections != null) {
            return sProjections;
        }

        List<String> projections = new ArrayList<>();
        projections.add(Column.ID);
        projections.add(Column.DATA);
        projections.add(Column.DISPLAY_NAME);
        projections.add(Column.SIZE);
        projections.add(Column.DATE_MODIFIED);
        projections.add(Column.WIDTH);
        projections.add(Column.HEIGHT);
        projections.add(Column.DURATION);
        projections.add(Column.MIME_TYPE);
        if (isHigherAndroidQ()) {
            projections.add(Column.RELATIVE_PATH);
        }

        sProjections = new String[projections.size()];
        sProjections = projections.toArray(sProjections);

        return sProjections;
    }

    /**
     * 查询文档条件语句
     */
    private static String sDocSelection = new SelectionBuilder()
            .and(MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_DOCUMENT)
            .or(MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_NONE)
            .and(MediaStore.Files.FileColumns.MIME_TYPE + " NOT LIKE " + "\'application/octet-stream\'")
            .and(MediaStore.Files.FileColumns.MIME_TYPE + " NOT LIKE " + "\'application/vnd.tcpdump.pcap\'")
            .and(MediaStore.Files.FileColumns.MIME_TYPE + " NOT LIKE " + "\'application/x-info\'")
            .and(MediaStore.Files.FileColumns.MIME_TYPE + " NOT LIKE " + "\'application/javascript\'")
            .and(MediaStore.Files.FileColumns.MIME_TYPE + " NOT LIKE " + "\'application/x-maker\'")
            .and(MediaStore.MediaColumns.SIZE + ">" + 0)
            .build();

    /**
     * 查询图片条件语句
     */
    private static String sImgSelection = new SelectionBuilder()
            .and(MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            .and(MediaStore.MediaColumns.SIZE + ">" + 0)
            .build();
    /**
     * 查询视频条件语句
     */
    private static String sVideoSelection = new SelectionBuilder()
            .and(MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
            .and(MediaStore.MediaColumns.SIZE + ">" + 0)
            .and(MediaStore.MediaColumns.DURATION + ">=" + 500)
            .build();
    /**
     * 查询音频条件语句
     */
    private static String sAudioSelection = new SelectionBuilder()
            .and(MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO)
            .and(MediaStore.MediaColumns.SIZE + ">" + 0)
            .and(MediaStore.MediaColumns.DURATION + ">=" + 500)
            .build();
    /**
     * 查询图片和视频条件语句
     */
    private static String sImgVideoSelection = new SelectionBuilder()
            .and(sImgSelection)
            .or(sVideoSelection)
            .build();

    /**
     * 根据加载类型来返回对应的查询条件语句
     *
     * @param loadType
     * @param extraCondition 额外的查询条件（该条件用于与即and）
     * @return
     */
    private static String getSelection(@Nullable LoadType loadType, @Nullable String extraCondition) {
        String selection = null;
        if (loadType != null) {
            switch (loadType) {
                case IMG:
                    selection = sImgSelection;
                    break;
                case VIDEO:
                    selection = sVideoSelection;
                    break;
                case AUDIO:
                    selection = sAudioSelection;
                    break;
                case IMG_VIDEO:
                    selection = sImgVideoSelection;
                    break;
                case DOC:
                    selection = sDocSelection;
                    break;
            }
        }

        if (!TextUtils.isEmpty(extraCondition)) {
            SelectionBuilder builder = new SelectionBuilder();
            builder.and(selection);
            builder.and(extraCondition);
            selection = builder.build();
        }
        return selection;
    }

    /**
     * 根据加载类型从媒体库中取出对应类型的媒体信息出来
     *
     * @param loadType       可为NULL，为NULL时表示加载所有条目
     * @param extraCondition 额外的查询条件
     * @return
     */
    @WorkerThread
    public static List<MediaInfo> load(@Nullable LoadType loadType, @Nullable String extraCondition) {
        try {
            ContentResolver resolver = Utils.getApp().getContentResolver();
            Uri uri = MediaStore.Files.getContentUri("external");
            String[] projections = getProjections();
            String selections = getSelection(loadType, extraCondition);
            String sortOrder = MediaStore.Files.FileColumns._ID + " DESC";
            Cursor cursor = resolver.query(uri, projections, selections, null, sortOrder);
            if (cursor == null) {
                Log.e(TAG, "load: load " + loadType.name() + " cursor is null.");
                return null;
            }

            List<MediaInfo> mediaInfoList = new ArrayList<>();
            MediaInfo mediaBean = null;

            int idIdx = cursor.getColumnIndex(Column.ID);
            int dataIdx = cursor.getColumnIndex(Column.DATA);
            int displayNameIdx = cursor.getColumnIndex(Column.DISPLAY_NAME);
            int sizeIdx = cursor.getColumnIndex(Column.SIZE);
            int dateModifiedIdx = cursor.getColumnIndex(Column.DATE_MODIFIED);
            int widthIdx = cursor.getColumnIndex(Column.WIDTH);
            int heightIdx = cursor.getColumnIndex(Column.HEIGHT);
            int durationIdx = cursor.getColumnIndex(Column.DURATION);
            int mimeTypeIdx = cursor.getColumnIndex(Column.MIME_TYPE);
            int relativePathIdx = -1;
            if (isHigherAndroidQ()) {
                relativePathIdx = cursor.getColumnIndex(Column.RELATIVE_PATH);
            }

            int id;
            String data;
            String displayName;
            long size;
            long dateModified;
            int width;
            int height;
            long duration;
            String mimeType;
            String uriStr = null;
            String relativePath;

            //是否是要单独获取图片、视频、音频的标志
            boolean isIva = false;
            if (loadType != null) {
                if (loadType == LoadType.IMG || loadType == LoadType.VIDEO || loadType == LoadType.AUDIO) {
                    isIva = true;
                }
            }
            while (cursor.moveToNext()) {
                mediaBean = null;

                id = cursor.getInt(idIdx);
                data = cursor.getString(dataIdx);
                displayName = cursor.getString(displayNameIdx);
                size = cursor.getLong(sizeIdx);
                dateModified = cursor.getLong(dateModifiedIdx);
                width = cursor.getInt(widthIdx);
                height = cursor.getInt(heightIdx);
                duration = cursor.getLong(durationIdx);
                mimeType = cursor.getString(mimeTypeIdx);
                if (relativePathIdx >= 0) {
                    relativePath = cursor.getString(relativePathIdx);
                }
                //Log.e(TAG, "mimeType = " + mimeType + ", displayName = " + displayName);

                if (mimeType != null) {
                    if (mimeType.startsWith("image")) {
                        uriStr = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id).toString();
                        ImgBean imgBean = new ImgBean(data, displayName, size, dateModified, uriStr, mimeType, width, height);
                        mediaBean = imgBean;

                    } else if (mimeType.startsWith("video")) {
                        uriStr = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id).toString();
                        VideoBean videoBean = new VideoBean(data, displayName, size, dateModified, uriStr, mimeType, width, height, duration);
                        mediaBean = videoBean;

                    } else if (mimeType.startsWith("audio")) {
                        uriStr = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString();
                        AudioBean audioBean = new AudioBean(data, displayName, size, dateModified, uriStr, mimeType, duration);
                        mediaBean = audioBean;

                    }
                }

                if (mediaBean == null && !isIva) {
                    uriStr = ContentUris.withAppendedId(uri, id).toString();
                    mediaBean = new MediaInfo(data, displayName, size, dateModified, uriStr, mimeType);
                }
                //Log.i(TAG, "mediaBean = " + mediaBean.toString());
                if (checkMediaInfoRealExist(mediaBean)) {
                    mediaInfoList.add(mediaBean);
                }
            }

            cursor.close();

            return mediaInfoList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载图片
     *
     * @return
     */
    @WorkerThread
    public static List<ImgBean> loadImage() {
        List<MediaInfo> mediaInfoList = load(LoadType.IMG, null);
        return convert(mediaInfoList);
    }

    /**
     * 加载图片(限定于只加载特定路径下的图片)
     *
     * @param addPackage
     * @return
     */
    @WorkerThread
    public static List<ImgBean> loadImageLimitByPath(boolean addPackage) {
        String extraCondition = createExtraConditionByPath(FileP2pUtil.getImgRelativeDir(addPackage));
        List<MediaInfo> mediaInfoList = load(LoadType.IMG, extraCondition);
        return convert(mediaInfoList);
    }

    /**
     * 加载视频
     *
     * @return
     */
    @WorkerThread
    public static List<VideoBean> loadVideo() {
        List<MediaInfo> mediaInfoList = load(LoadType.VIDEO, null);
        return convert(mediaInfoList);
    }

    /**
     * 加载视频(限定于只加载特定路径下的视频)
     *
     * @param addPackage
     * @return
     */
    @WorkerThread
    public static List<VideoBean> loadVideoLimitByPath(boolean addPackage) {
        String extraCondition = createExtraConditionByPath(FileP2pUtil.getVideoRelativeDir(addPackage));
        List<MediaInfo> mediaInfoList = load(LoadType.VIDEO, extraCondition);
        return convert(mediaInfoList);
    }

    /**
     * 加载图片视频(限定于只加载特定路径下的图片视频)
     *
     * @param addPackage
     * @return
     */
    @WorkerThread
    public static List<MediaInfo> loadImgVideoLimitByPath(boolean addPackage) {
        String imgPath = FileP2pUtil.getImgRelativeDir(addPackage);
        String videoPath = FileP2pUtil.getVideoRelativeDir(addPackage);
        String extraCondition = createExtraConditionByPath(imgPath, videoPath);
        List<MediaInfo> mediaInfoList = load(LoadType.IMG_VIDEO, extraCondition);
        return mediaInfoList;
    }

    /**
     * 加载音频
     *
     * @return
     */
    @WorkerThread
    public static List<AudioBean> loadAudio() {
        List<MediaInfo> mediaInfoList = load(LoadType.AUDIO, null);
        return convert(mediaInfoList);
    }

    /**
     * 加载音频(限定于只加载特定路径下的音频)
     *
     * @param addPackage
     * @return
     */
    @WorkerThread
    public static List<AudioBean> loadAudioLimitByPath(boolean addPackage) {
        String extraCondition = createExtraConditionByPath(FileP2pUtil.getAudioRelativeDir(addPackage));
        List<MediaInfo> mediaInfoList = load(LoadType.AUDIO, extraCondition);
        return convert(mediaInfoList);
    }

    /**
     * 加载图片跟视频
     *
     * @return
     */
    @WorkerThread
    public static List<MediaInfo> loadImgVideo() {
        return load(LoadType.IMG_VIDEO, null);
    }

    /**
     * 加载文档
     *
     * @return
     */
    @WorkerThread
    public static List<MediaInfo> loadDoc() {
        return load(LoadType.DOC, null);
    }

    /**
     * 加载指定mimeType类型的文档
     *
     * @param filterMimeTypeList 需要加载的文档的mimeType类型列表
     * @return
     */
    @WorkerThread
    public static List<MediaInfo> loadDoc(List<String> filterMimeTypeList) {
        SelectionBuilder builder = null;
        if (filterMimeTypeList != null && filterMimeTypeList.size() > 0) {
            builder = new SelectionBuilder();
            for (int i = 0; i < filterMimeTypeList.size(); i++) {
                if (i == 0) {
                    builder.and(MediaStore.Files.FileColumns.MIME_TYPE + " LIKE " + "\'" + filterMimeTypeList.get(i) + "\'");
                } else {
                    builder.or(MediaStore.Files.FileColumns.MIME_TYPE + " LIKE " + "\'" + filterMimeTypeList.get(i) + "\'");
                }
            }
        }
        String extraCondition = null;
        if (builder != null) {
            extraCondition = builder.build();
        }
        return load(LoadType.DOC, extraCondition);
    }

    private static String createExtraConditionByPath(@NonNull String path) {
        StringBuilder builder = new StringBuilder();
        if (isHigherAndroidQ()) {
            builder.append(MediaStore.MediaColumns.RELATIVE_PATH);
        } else {
            builder.append(MediaStore.MediaColumns.DATA);
        }
        builder.append(" like ");
        builder.append("'");
        builder.append(path + "%");
        builder.append("'");
        return builder.toString();
    }

    private static String createExtraConditionByPath(String... paths) {
        if (paths == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            if (isHigherAndroidQ()) {
                builder.append(MediaStore.MediaColumns.RELATIVE_PATH);
            } else {
                builder.append(MediaStore.MediaColumns.DATA);
            }
            builder.append(" like ");
            builder.append("'");
            builder.append(paths[i] + "%");
            builder.append("'");
            if (i < paths.length - 1) {
                builder.append(" or ");
            }
        }
        return builder.toString();
    }

    private static <T extends MediaInfo> List<T> convert(List<MediaInfo> mediaInfoList) {
        if (mediaInfoList == null || mediaInfoList.size() == 0) {
            return null;
        }

        List<T> retList = new ArrayList<>();
        for (MediaInfo mediaInfo : mediaInfoList) {
            retList.add((T) mediaInfo);
        }
        return retList;
    }

    /**
     * 检测MediaInfo所对应的文件是否真的存在（因为发现有些在数据库中虽然存在，但其对应的文件实际上却不存在）
     *
     * @param info
     * @return
     */
    private static boolean checkMediaInfoRealExist(MediaInfo info) {
        if (info == null) {
            return false;
        }
        String path = info.getPath();
        return FileUtils.isFileExists(path);
    }

}
