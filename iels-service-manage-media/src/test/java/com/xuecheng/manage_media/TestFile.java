package com.xuecheng.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * @Description: 测试文件分块上传与合并
 * @Author: snypxk
 * @Date: 2019/12/11 19
 * @Other:
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFile {
    /*
       上传流程如下：
        1、上传前先把文件分成块
        2、一块一块的上传,上传中断后重新上传,已上传的分块则不用再上传
        3、各分块上传完成最后合并文件
     */

    /*
     * @description: 测试文件分块
     * @author: snypxk
     * @param
     * @return: void
     *      RandomAccessFile 是java Io体系中功能最丰富的文件内容访问类。即可以读取文件内容，也可以向文件中写入内容。
     *  但是和其他输入/输入流不同的是，程序可以直接跳到文件的任意位置来读写数据。因为RandomAccessFile可以自由访问文
     *  件的任意位置，所以如果我们希望只访问文件的部分内容，那就可以使用RandomAccessFile类。
     *
     *      创建RandomAccessFile对象还需要指定mode参数: 该参数指定RandomAccessFile的访问模式，有以下4个值：
     *          “r” 以只读方式来打开指定文件夹。如果试图对该RandomAccessFile执行写入方法，都将抛出IOException异常。
     *          “rw” 以读，写方式打开指定文件。如果该文件尚不存在，则试图创建该文件。
     *          “rws” 以读，写方式打开指定文件。相对于”rw” 模式，还要求对文件内容或元数据的每个更新都同步写入到底层设备。
     *          “rwd” 以读，写方式打开指定文件。相对于”rw” 模式，还要求对文件内容每个更新都同步写入到底层设备。
     **/
    @Test
    public void testChunk() throws IOException {
        //源文件: 本次的测试文件是非mp4格式
        File sourceFile = new File("D:\\IDEA_workspace\\xuecheng\\ffmpeg\\lucene.avi");
        //块文件目录
        String chunkPath = "D:\\IDEA_workspace\\xuecheng\\ffmpeg\\chunks\\";
        File chunkFolder = new File(chunkPath);
        //如果块文件目录不存在,则创建目录文件夹
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }
        //8bit(位)=1Byte(字节) 1024Byte(字节)=1KB 1024KB=1MB 1024MB=1GB 1024GB=1TB
        //定义分块文件的大小: 1 MB [单位: 字节Byte]
        long chunkSize = 1024 * 1024;
        //分块数量 = 源文件大小 / 分块文件的大小 [File.length()方法: 返回这个文件的字节大小]
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        if (chunkNum <= 0) {
            chunkNum = 1;
        }
        //缓冲区大小
        byte[] b = new byte[1024];
        //使用RandomAccessFile访问文件
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");
        //分块
        for (int i = 0; i < chunkNum; i++) {
            //创建分块文件
            File file = new File(chunkPath + i);
            boolean newFile = file.createNewFile();
            if (newFile) {
                //向分块文件中写数据
                RandomAccessFile raf_write = new RandomAccessFile(file, "rw");
                int len = -1;
                //如果块文件的大小达到1MB,则开始把源文件数据写入新的下一块文件
                while ((len = raf_read.read(b)) != -1) {
                    raf_write.write(b, 0, len);
                    if (file.length() > chunkSize) {
                        break;
                    }
                }
                raf_write.close();
            }
        }
        raf_read.close();
    }

    /*
     * @description: 测试文件合并
     * @author: snypxk
     * @param
     * @return: void
     **/
    @Test
    public void testMergeFile() throws IOException {
        //块文件目录
        File chunkFolder = new File("D:\\IDEA_workspace\\xuecheng\\ffmpeg\\chunks\\");
        //设定合并后的文件名及其路径
        File mergeFile = new File("D:\\IDEA_workspace\\xuecheng\\ffmpeg\\lucene-merge.avi");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        //创建新的合并文件
        mergeFile.createNewFile();
        //用于写文件
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
        //指针指向文件顶端
        raf_write.seek(0);
        //缓冲区
        byte[] b = new byte[1024];
        //分块文件列表
        File[] fileArray = chunkFolder.listFiles();
        // 转成List集合，便于排序
        List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
        // 把块文件按名称从小到大排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                    return -1;
                }
                return 1;
            }
        });
        //合并文件
        for (File chunkFile : fileList) {
            //创建一个读块文件的对象
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
            }
            raf_read.close();
        }
        raf_write.close();
    }

}
