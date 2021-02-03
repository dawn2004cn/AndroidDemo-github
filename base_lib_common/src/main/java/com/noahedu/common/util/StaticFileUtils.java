package com.noahedu.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

/**
 * 文件操作工具
 *
 * @author LiQi
 */
public class StaticFileUtils {
    private static final String TAG = StaticFileUtils.class.getName();

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param deletePath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String deletePath) {
        File file = new File(deletePath);
        // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在返回 false
            return false;
        } else {
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(file);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(deletePath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param file 被删除文件的文件File对象
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteFile(File file) {
        boolean flag = false;
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param deletePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    private static boolean deleteDirectory(String deletePath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!deletePath.endsWith(File.separator)) {
            deletePath = deletePath + File.separator;
        }
        File dirFile = new File(deletePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i]);
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取保存路径
     *
     * @param context  上下文
     * @param filePath 路径名字
     * @return 保存路径
     */
    public static String getPath(Context context, String filePath) {
        String path;
        // 判断是否安装有SD卡
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory() + "/" + filePath;
        } else {
            path = context.getCacheDir() + "/" + filePath;
        }
        return path;
    }

    /**
     * 通过切割URL获取路径文件名，然后拼接完整的存储路径。（原理是切割URL最后的文件名作为保存在本地的文件名）
     *
     * @param url            URL
     * @param context        上下文
     * @param folderPathName 保存的文件夹路径。样例:xxx/xxx。
     *                       <p>
     *                       如果本地没有，本方法会为你创建一个
     *                       <p>
     *                       已经判断是否有SD卡。如果有就拼接SD卡路径，没有就拼接手机内存路径
     *                       </p>
     * @return 拼接完整的存储路径
     */
    public static File putFilePath(String url, Context context, String folderPathName) {
        File file;
        String[] split = url.split("/");
        String name = split[split.length - 1];
        // String name = "SampleVideo_1080x720_10mb~4.mp4";
        String path = getPath(context, folderPathName);
        // 先创建文件夹file
        file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        file = new File(path, name);
        return file;
    }

    /**
     * 切割URl最尾部的内容，按“/”切割
     *
     * @param url url
     * @return URl最尾部的内容
     */
    public static String httpGetName(String url) {
        if (null != url) {
            String[] split = url.split("/");
            String name = split[split.length - 1];
            return name;
        } else
            return null;
    }

    /**
     * 判断路径是否存在并且是一个文件
     *
     * @param file 文件
     * @return true是存在并是一个文件，false不是。
     */
    public static boolean isFilePath(File file) {
        boolean isFile;
        if (file.exists() && file.isFile()) {
            isFile = true;
        } else {
            isFile = false;
        }
        return isFile;

    }

    /**
     * 创建一个目录路径，并且返回目录路径
     * <p>
     * 没有创建，有直接返回。
     * </p
     * <p>
     * 已经判断是否有SD卡。如果有就拼接SD卡路径，没有就拼接手机内存路径
     * </p>
     *
     * @param context       上下文
     * @param cataloguePath 目录路径。样例:xxx/xxx
     * @return 目录路径
     */
    public static String foundFilePathString(Context context, String cataloguePath) {
        File file = new File(getPath(context, cataloguePath));
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 创建一个目录路径，并且返回目录路径File对象
     * <p>
     * 没有创建，有直接返回。
     * </p
     * <p>
     * 已经判断是否有SD卡。如果有就拼接SD卡路径，没有就拼接手机内存路径
     * </p>
     *
     * @param context       上下文
     * @param cataloguePath 目录路径。样例:xxx/xxx
     * @return 目录路径File对象
     */
    public static File foundFilePathFile(Context context, String cataloguePath) {
        File file = new File(getPath(context, cataloguePath));
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 根据传过来的文件目录路径和文件名来创建路径，并且返回路径File对象。
     * <p>
     * 没有创建，有直接返回。
     * </p
     * <p>
     * 已经判断是否有SD卡。如果有就拼接SD卡路径，没有就拼接手机内存路径
     * </p>
     *
     * @param context       上下文
     * @param cataloguePath 目录路径。样例:xxx/xxx
     * @param fileName      文件名称，记得带扩展名 -->如果没有传值，就只返回目录路径File
     * @return 目录路径File对象
     */
    public static File nonentityFoundFile(Context context, String cataloguePath, String fileName) {
        String path = getPath(context, cataloguePath);
        // 先创建文件夹file
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        if (null != fileName && !"".equals(fileName)) {
            file = new File(path, fileName);
        }
        return file;
    }

    /**
     * 根据传过来的文件目录路径和文件名来创建路径，并且返回路径
     * <p>
     * 没有创建，有直接返回。
     * </p
     * <p>
     * 已经判断是否有SD卡。如果有就拼接SD卡路径，没有就拼接手机内存路径
     * </p>
     *
     * @param context       上下文
     * @param cataloguePath 目录路径。样例:xxx/xxx
     * @param fileName      文件名称，记得带扩展名 -->如果没有传值，就只返回目录路径File
     * @return 目录路径
     */
    public static String nonentityFoundString(Context context, String cataloguePath, String fileName) {
        String path = getPath(context, cataloguePath);
        // 先创建文件夹file
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        if (null != fileName && !"".equals(fileName)) {
            file = new File(path, fileName);
        }
        return file.getAbsolutePath();
    }

    /**
     * 通过包名去判断程序是不是已经安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return true是已经安装，false没有。
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    /**
     * 把输入流写出
     *
     * @param inputContent 输入流
     * @param file         输出文件路径
     * @param byteCache    byte数组缓存字节大小设置
     * @return true是写入成功，false失败。
     */
    public static boolean putFileOutputStream(InputStream inputContent,
                                              File file, int byteCache) {
        boolean sign = false;
        if (null != inputContent) {
            FileOutputStream fileOutputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                bufferedOutputStream = new BufferedOutputStream(
                        fileOutputStream);
                byte buffer[] = new byte[byteCache];
                int length;
                while ((length = inputContent.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, length);
                    bufferedOutputStream.flush();
                }
                if (file.exists()) {
                    sign = true;
                } else {
                    sign = false;
                }
            } catch (Exception e) {
                sign = false;
                e.printStackTrace();
            } finally {
                try {
                    if (null != bufferedOutputStream) {
                        bufferedOutputStream.close();
                    }
                    if (null != fileOutputStream) {
                        fileOutputStream.close();
                    }
                    inputContent.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sign;
    }

    /**
     * 把文本内容用ASE加密写到本地
     * <p>
     * 加密方式采用：AndroidAESEncryptor.encrypt128(key,content),解密请使用：AndroidAESEncryptor.decrypt128(key, content)
     * </p>
     *
     * @param password 密码
     * @param content  内容
     * @param file     路径
     * @return true是写入成功，false失败。
     */
    public static boolean putFileOutputStreamContent(String password,
                                                     String content, File file) {

        boolean sign = false;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(AndroidAESEncryptor.encrypt128(password, content)
                    .getBytes());
            bufferedOutputStream.flush();
            if (file.exists())
                sign = true;
            else
                sign = false;
        } catch (Exception e) {
            sign = false;
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedOutputStream) {
                    bufferedOutputStream.close();
                }
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sign;
    }

    /**
     * 通过递归得到某一路径下所有的目录及其文件
     *
     * @param filePath 目录路径
     * @return 目录路径里面的所有文件集合
     */
    public static List<File> getFiles(String filePath) {
        List<File> fileList = new ArrayList<File>();
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                /*
                 * 递归调用
				 */
                getFiles(file.getAbsolutePath());
            }
            fileList.add(file);
        }
        return fileList;
    }


    /**
     * 获取指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取指定单位类型的值 {@link SizeTypeEnum}
     * @return 指定单位类型的double值的大小 值跟此参数有关{@link SizeTypeEnum}
     */
    public static double getFileOrFilesSize(String filePath, SizeTypeEnum sizeType) {
        File file = new File(filePath);
        long blockSize;

        if (file.isDirectory()) {
            //Logger.e(TAG, "==是文件夹==");
            blockSize = getFileSizes(file);
        } else {
            //Logger.e(TAG, "==是文件==");
            blockSize = getFileSize(file);
        }
        //Logger.e(TAG, "blockSize=" + blockSize);
        return formetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize;
        if (file.isDirectory()) {
            blockSize = getFileSizes(file);
        } else {
            blockSize = getFileSize(file);
        }
        return FileSizeFormattingUtil.formatFileSize(blockSize, false);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != fis) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //Logger.e(TAG, "size01=" + size);
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "no-exists!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     */
    private static long getFileSizes(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            //Logger.e(TAG, "file：" + flist[i]);
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
                //Logger.e(TAG, "==是文件夹== >>> size：" + size);
            } else {
                size = size + getFileSize(flist[i]);
                //Logger.e(TAG, "==是文件== >>> size：" + size);
            }
        }
        //Logger.e(TAG, "size02=" + size);
        return size;
    }


    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double formetFileSize(long fileS, SizeTypeEnum sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
        }
        //Logger.e(TAG, "fileSizeLong=" + fileSizeLong);
        return fileSizeLong;
    }

    public enum SizeTypeEnum {
        /**
         * 获取文件大小单位为B的double值
         */
        SIZETYPE_B,
        /**
         * 获取文件大小单位为KB的double值
         */
        SIZETYPE_KB,
        /**
         * 获取文件大小单位为MB的double值
         */
        SIZETYPE_MB,
        /**
         * 获取文件大小单位为GB的double值
         */
        SIZETYPE_GB
    }
}
