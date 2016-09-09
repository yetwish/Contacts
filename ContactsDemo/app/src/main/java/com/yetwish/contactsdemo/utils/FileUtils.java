package com.yetwish.contactsdemo.utils;

import android.os.Environment;

import com.yetwish.contactsdemo.model.Contacts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * todo
 * 文件操作帮助类
 * Created by yetwish on 2016/9/9.
 */
public class FileUtils {

    private static final String SDCARD_DIR = "/contactsDemo/";
    private static final String DATA_DIR = "/data/data/com.yetwish.contactsdemo/files/";

    private static final String FILE_EXTENSION = ".contacts.json";

    private static FileUtils sFileUtils;

    private FileFilter mFilter;
    private List<File> mContactsFiles;

    public static final FileUtils getInstance() {
        if (sFileUtils == null) {
            synchronized (FileUtils.class) {
                if (sFileUtils == null)
                    sFileUtils = new FileUtils();
            }
        }
        return sFileUtils;
    }

    private FileUtils() {
        mFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(FILE_EXTENSION) || pathname.isDirectory())
                    return true;
                return false;
            }
        };
    }

    public String getFilePath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //已挂载sdcard则放回sdcard路径
            return Environment.getExternalStorageDirectory() + SDCARD_DIR;
        } else //没有则返回context.getFilesDir().getPath()
            return DATA_DIR;
    }

    /**
     * 根据日期生成fileName
     *
     * @return
     */
    private String getFileName() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return format.format(cal.getTime()) + FILE_EXTENSION;
    }

    // TODO: 2016/9/9 搜索的范围
    public List<File> listContactsFile() {
        if (mContactsFiles == null)
            mContactsFiles = new ArrayList<>();
        File rootFile = new File(getFilePath());
        searchContactsFile(rootFile);
        return mContactsFiles;
    }

    /**
     * 遍历查找符合条件的文件
     *
     * @param file
     */
    private void searchContactsFile(File file) {
        if (!file.exists())
            return;
        File files[] = file.listFiles(mFilter);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory())
                searchContactsFile(files[i]);
            else
                mContactsFiles.add(files[i]);
        }
    }

    public List<Contacts> loadContacts(String filePath, String fileName) {
        BufferedReader reader = null;
        List<Contacts> list = new ArrayList<Contacts>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(JsonUtils.objFromJson(line, Contacts.class));
            }
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void saveContacts(List<Contacts> list) {
        BufferedWriter writer = null;
        try {
            String fileName = getFileName();
            createFileIfNotExists(getFilePath(), fileName);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFilePath() + fileName, false)));
            for (Contacts item : list) {
                writer.write(JsonUtils.toJson(item));
                writer.newLine();
                writer.flush();//todo ??是否需要
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void createFileIfNotExists(String filePath, String fileName) {
        File path = new File(filePath);
        File file = new File(filePath + fileName);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
