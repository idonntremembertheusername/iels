package com.iels.framework.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 使用 FFmpeg.exe 把 video.avi 转 video.mp4
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
public class Mp4VideoUtil extends VideoUtil {
    private String ffmpeg_path;
    private String video_path;
    private String mp4_name;
    private String mp4folder_path;

    /*
     * @description: 构造函数
     * @author: snypxk
     * @param ffmpeg_path       - FFmpeg.exe的路径位置
     * @param video_path        - 源视频(.avi)的绝对完全路径[带文件名和后缀]
     * @param mp4_name          - 转成后的MP4格式的文件名[带后缀.mp4]
     * @param mp4folder_path    - 转成后的MP4格式的文件的保存路径位置[不带文件名和后缀]
     * @return: null
     **/
    public Mp4VideoUtil(String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path) {
        super(ffmpeg_path);
        this.ffmpeg_path = ffmpeg_path;
        this.video_path = video_path;
        this.mp4_name = mp4_name;
        this.mp4folder_path = mp4folder_path;
    }

    //如果存在同名的MP4文件,则清除
    private void clear_mp4(String mp4_path) {
        //删除原来已经生成的m3u8及ts文件
        File mp4File = new File(mp4_path);
        if (mp4File.exists() && mp4File.isFile()) {
            mp4File.delete();
        }
    }

    /*
     * @description: 视频编码，生成mp4文件
     * @author: snypxk
     * @param
     * @return: java.lang.String - 成功返回success，失败返回控制台日志
     **/
    public String generateMp4() {
        //清除已生成的mp4
        this.clear_mp4(mp4folder_path + mp4_name);
        //ffmpeg.exe -i  lucene.avi -c:v libx264 -s 1280x720 -pix_fmt yuv420p -b:a 63k -b:v 753k -r 18 .\lucene.mp4
        List<String> commend = new ArrayList<String>();
        commend.add(ffmpeg_path);
        commend.add("-i");
        commend.add(video_path);
        commend.add("-c:v");
        commend.add("libx264");
        commend.add("-y");//覆盖输出文件
        commend.add("-s");
        commend.add("1280x720");
        commend.add("-pix_fmt");
        commend.add("yuv420p");
        commend.add("-b:a");
        commend.add("63k");
        commend.add("-b:v");
        commend.add("753k");
        commend.add("-r");
        commend.add("18");
        commend.add(mp4folder_path + mp4_name);
        String outstring = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            //将标准输入流和错误输入流合并，通过标准输入流程读取信息
            builder.redirectErrorStream(true);
            Process p = builder.start();
            outstring = waitFor(p);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Boolean check_video_time = this.check_video_time(video_path, mp4folder_path + mp4_name);
        if (!check_video_time) {
            return outstring;
        } else {
            return "success";
        }
    }
}
