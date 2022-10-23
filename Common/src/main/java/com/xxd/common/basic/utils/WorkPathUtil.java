package com.xxd.common.basic.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:工作路径工具类
 */
public class WorkPathUtil {


    /**
     * 工作根目录名称
     */
    public static final String WORK_DIR = "/work";
    /**
     * 存储视频文件的目录名称
     */
    public static final String WORK_VIDEO_DIR = WORK_DIR + "/video";
    /**
     * 存储音频文件的目录名称
     */
    public static final String WORK_AUDIO_DIR = WORK_DIR + "/audio";
    /**
     * 存储图片文件的目录名称
     */
    public static final String WORK_IMG_DIR = WORK_DIR + "/img";
    /**
     * 存储拷贝素材的目录名称
     */
    public static final String WORK_COPY_DIR = WORK_DIR + "/copy";
    /**
     * 存储PDF文件的目录名称
     */
    public static final String WORK_PDF_DIR = WORK_DIR + "/pdf";
    /**
     * 存储WORD文件的目录名称
     */
    public static final String WORK_WORD_DIR = WORK_DIR + "/word";

    /**
     * 根据输入的视频路径生成新的输出的视频路径
     *
     * @param srcFilePath 输入视频路径(可为空)
     * @return 输出视频路径
     */
    public static String generateVideoFilePath(@Nullable String srcFilePath) {
        return FileUtil.generateFilePath(WORK_VIDEO_DIR, ".mp4", srcFilePath);
    }

    /**
     * 生成指定文件名称的视频文件路径
     *
     * @param fileName
     * @return
     */
    public static String generateVideoFilePathByName(@NonNull String fileName) {
        return FileUtil.generateFilePathByName(WORK_VIDEO_DIR, fileName);
    }

    /**
     * 生成指定后缀名的音频文件路径
     *
     * @param suffix
     * @return
     */
    public static String generateAudioFilePath(String suffix) {
        return FileUtil.generateFilePath(WORK_AUDIO_DIR, suffix, null);
    }

    /**
     * 生成指定文件名称的音频文件路径
     *
     * @param fileName
     * @return
     */
    public static String generateAudioFilePathByName(String fileName) {
        return FileUtil.generateFilePathByName(WORK_AUDIO_DIR, fileName);
    }

    /**
     * 生成Gif图片文件路径
     *
     * @return
     */
    public static String generateGifFilePath() {
        return generateImgFilePath(".gif");
    }

    /**
     * 生成png图片文件路径
     *
     * @return
     */
    public static String generatePngFilePath() {
        return generateImgFilePath(".png");
    }

    /**
     * 生成jpg图片文件路径
     *
     * @return
     */
    public static String generateJpgFilePath() {
        return generateImgFilePath(".jpg");
    }

    /**
     * 生成图片相关文件路径
     *
     * @param suffix (形如: .png、.jpg、.gif等与图片相关的文件后缀名称)
     * @return
     */
    public static String generateImgFilePath(String suffix) {
        return FileUtil.generateFilePath(WORK_IMG_DIR, suffix, null);
    }

    /**
     * 在拷贝目录下生成指定文件名称的文件路径
     *
     * @param fileName
     * @param autoCreate 是否创建该文件路径的标志，true为创建该文件，false不创建该文件
     * @return
     */
    public static String generateFilePathByNameInCopyDir(@NonNull String fileName, boolean autoCreate) {
        return FileUtil.generateFilePathByName(WORK_COPY_DIR, fileName, autoCreate);
    }

    /**
     * 创建pdf文件并返回该文件的路径
     *
     * @return
     */
    public static String generatePdfFilePath() {
        return FileUtil.generateFilePath(WORK_PDF_DIR, ".pdf");
    }

    /**
     * 创建格式为docx的word文档文件并返回该文件的路径
     *
     * @return
     */
    public static String generateWordFilePath() {
        return FileUtil.generateFilePath(WORK_WORD_DIR, ".docx");
    }
}
