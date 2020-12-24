package com.iels.framework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 由转化后得到的MP4文件生成 m3u8 和 ts 文件
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
public class HlsVideoUtil extends VideoUtil {

    private String ffmpeg_path;
    private String video_path;
    private String m3u8_name;
    private String m3u8folder_path;

    /*
     * @description: 构造类
     * @author: snypxk
     * @param ffmpeg_path       - ffmpeg.exe的绝对路径
     * @param video_path        - 其他格式的视频转化后得到的MP4文件的绝对路径[带后缀]
     * @param m3u8_name         - m3u8文件的名称[不包含路径,带后缀]
     * @param m3u8folder_path   - m3u8文件所在的目录
     * @return:
     **/
    public HlsVideoUtil(String ffmpeg_path, String video_path, String m3u8_name, String m3u8folder_path) {
        super(ffmpeg_path);
        this.ffmpeg_path = ffmpeg_path;
        this.video_path = video_path;
        this.m3u8_name = m3u8_name;
        this.m3u8folder_path = m3u8folder_path;
    }

    /*
     * @description: 删除原来已经生成的m3u8及ts文件
     * @author: snypxk
     * @param m3u8_path
     * @return: void
     **/
    private void clear_m3u8(String m3u8_path) {
        File m3u8dir = new File(m3u8_path);
        if (!m3u8dir.exists()) {
            m3u8dir.mkdirs();
        }
    }

    /*
     * @description: 生成m3u8文件
     * @author: snypxk
     * @param
     * @return: java.lang.String - 成功则返回success，失败返回控制台日志
     **/
    public String generateM3u8() {
        //清理m3u8文件目录
        clear_m3u8(m3u8folder_path);
        //ffmpeg -i  lucene.mp4   -hls_time 10 -hls_list_size 0 -hls_segment_filename ./hls/lucene_%05d.ts ./hls/lucene.m3u8
        List<String> commend = new ArrayList<String>();
        commend.add(ffmpeg_path);
        commend.add("-i");
        commend.add(video_path);
        commend.add("-hls_time");
        commend.add("10");
        commend.add("-hls_list_size");
        commend.add("0");
        commend.add("-hls_segment_filename");
        commend.add(m3u8folder_path + m3u8_name.substring(0, m3u8_name.lastIndexOf(".")) + "_%05d.ts");
        commend.add(m3u8folder_path + m3u8_name);
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
        //通过查看视频时长判断是否成功
        Boolean check_video_time = check_video_time(video_path, m3u8folder_path + m3u8_name);
        if (!check_video_time) {
            return outstring;
        }
        //通过查看m3u8列表判断是否成功
        List<String> ts_list = get_ts_list();
        if (ts_list == null) {
            return outstring;
        }
        return "success";
    }

    /*
     * @description: 检查视频处理是否完成,获取ts文件列表
     * @author: snypxk
     * @param
     * @return: java.util.List<java.lang.String> - ts列表
     **/
    public List<String> get_ts_list() {
        List<String> fileList = new ArrayList<String>();
        List<String> tsList = new ArrayList<String>();
        String m3u8file_path = m3u8folder_path + m3u8_name;
        BufferedReader br = null;
        String str = null;
        String bottomline = "";
        try {
            br = new BufferedReader(new FileReader(m3u8file_path));
            while ((str = br.readLine()) != null) {
                bottomline = str;
                if (bottomline.endsWith(".ts")) {
                    tsList.add(bottomline);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (bottomline.contains("#EXT-X-ENDLIST")) {
            fileList.addAll(tsList);
            return fileList;
        }
        return null;
    }

//    public static void main(String[] args) throws IOException {
//        String ffmpeg_path = "D:\\Program Files\\ffmpeg-20180227-fa0c9d6-win64-static\\bin\\ffmpeg.exe";
//        String video_path = "E:\\ffmpeg_test\\1.mp4";
//        String m3u8_name = "1.m3u8";
//        String m3u8_path = "E:\\ffmpeg_test\\1\\";
//        HlsVideoUtil videoUtil = new HlsVideoUtil(ffmpeg_path, video_path, m3u8_name, m3u8_path);
//        String s = videoUtil.generateM3u8();
//        System.out.println(s);
//        System.out.println(videoUtil.get_ts_list());
//    }
}
