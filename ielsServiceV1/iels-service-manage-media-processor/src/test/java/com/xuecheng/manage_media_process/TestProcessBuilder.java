package com.xuecheng.manage_media_process;

import com.iels.framework.utils.Mp4VideoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @param
 * @description: 在java程序中调用第三方程序 [如本机的.exe可执行程序]
 * @author: snypxk
 * @return:
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProcessBuilder {

    /*
     * ffmpeg是一个可行的视频处理程序，可以通过Java调用ffmpeg.exe完成视频处理
     * 在java中可以使用Runtime类和Process Builder类两种方式来执行外部程序，工作中至少掌握一种
     * 本项目使用Process Builder的方式来调用ffmpeg完成视频处理
     */

    /*
     * @description: 使用 Process Builder类来调用第三方应用程序
     * @author: snypxk
     * @param
     * @return: void
     **/
    @Test
    public void testProcessBuilder() throws IOException {
        //1.创建ProcessBuilder对象
        ProcessBuilder processBuilder = new ProcessBuilder();
        //设置执行的第三方程序(命令)
//        processBuilder.command("ping","127.0.0.1");
        processBuilder.command("ipconfig");
//        processBuilder.command("java","-jar","f:/xc-service-manage-course.jar");
        //将标准输入流和错误输入流合并，通过标准输入流读取信息 就可以 拿到第三方程序输出的错误信息、正常信息
        processBuilder.redirectErrorStream(true);

        //启动一个进程
        Process process = processBuilder.start();
        //由于前边将错误和正常信息合并在输入流，只读取输入流
        InputStream inputStream = process.getInputStream();
        //将字节流转成字符流
        InputStreamReader reader = new InputStreamReader(inputStream, "gbk");
        //字符缓冲区
        char[] chars = new char[1024];
        int len = -1;
        while ((len = reader.read(chars)) != -1) {
            String string = new String(chars, 0, len);
            System.out.println(string);
        }
        inputStream.close();
        reader.close();
    }


    @Test
    public void testFFmpeg() throws IOException {
        //1.创建ProcessBuilder对象
        ProcessBuilder processBuilder = new ProcessBuilder();
        //设置执行的第三方程序(命令)
        //ffmpeg.exe -i lucene.avi -c:v libx264 -s 1280x720 -pix_fmt yuv420p -b:a 63k -b:v 753k -r 18 .\lucene.mp4
        List<String> command = new ArrayList<>();
        command.add("D:\\IDEA_workspace\\xuecheng\\tools\\ffmpeg-20180227-fa0c9d6-win64-static\\bin\\ffmpeg.exe");
        command.add("-i");
        command.add("D:\\IDEA_workspace\\xuecheng\\ffmpeg\\solr.avi");
        command.add("-y");//覆盖输出文件
        command.add("-c:v");
        command.add("libx264");
        command.add("-s");
        command.add("1280x720");
        command.add("-pix_fmt");
        command.add("yuv420p");
        command.add("-b:a");
        command.add("63k");
        command.add("-b:v");
        command.add("753k");
        command.add("-r");
        command.add("18");
        command.add("D:\\IDEA_workspace\\xuecheng\\ffmpeg\\solr-afterchange.mp4");

        //将标准输入流和错误输入流合并，通过标准输入流读取信息 就可以 拿到第三方程序输出的错误信息、正常信息
        processBuilder.redirectErrorStream(true);

        try {
            //启动一个进程
            Process process = processBuilder.start();
            //由于前边将错误和正常信息合并在输入流，只读取输入流
            InputStream inputStream = process.getInputStream();
            //将字节流转成字符流
            InputStreamReader reader = new InputStreamReader(inputStream, "gbk");
            //字符缓冲区
            char[] chars = new char[1024];
            StringBuffer outputString = new StringBuffer();
            int len = -1;
            while ((len = reader.read(chars)) != -1) {
                String s = new String(chars, 0, len);
                outputString.append(s);
                System.out.println(s);
            }
            inputStream.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //测试使用工具类 - 将avi转成mp4
    @Test
    public void testProcessMp4() {
        //ffmpeg程序所在的路径
        String ffmpeg_path = "D:\\IDEA_workspace\\xuecheng\\tools\\ffmpeg-20180227-fa0c9d6-win64-static\\bin\\ffmpeg.exe";
        //video_path视频地址
        String video_path = "D:\\IDEA_workspace\\xuecheng\\ffmpeg\\solr.avi";
        //mp4_name mp4文件名称
        String mp4_name = "solr-afterchange.mp4";
        //mp4folder_path mp4文件目录路径
        String mp4folder_path = "D:\\IDEA_workspace\\xuecheng\\ffmpeg\\";
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4folder_path);
        //开始编码,如果成功返回success，否则返回输出的日志
        String result = mp4VideoUtil.generateMp4();
        System.out.println(result);
    }

}
