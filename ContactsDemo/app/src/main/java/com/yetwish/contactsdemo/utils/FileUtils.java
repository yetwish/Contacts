package com.yetwish.contactsdemo.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.yetwish.contactsdemo.ApiCallback;
import com.yetwish.contactsdemo.database.DbContactsManager;
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

    private static FileFilter mFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(FILE_EXTENSION) || pathname.isDirectory();
        }
    };

    private static List<File> mContactsFiles;

    public static String getFilePath() {
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
    private static String getFileName() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return format.format(cal.getTime());
    }

    // TODO: 2016/9/9 搜索的范围
    public static void listContactsFile(@NonNull final ApiCallback<List<File>> callback) {
        new AsyncTask<Void, Void, List<File>>() {
            @Override
            protected List<File> doInBackground(Void... params) {
                if (mContactsFiles == null)
                    mContactsFiles = new ArrayList<>();
                mContactsFiles.clear();
                File rootFile = new File(getFilePath());
                searchContactsFile(rootFile);
                return mContactsFiles;
            }

            @Override
            protected void onPostExecute(List<File> result) {
                super.onPostExecute(result);
                if (result.size() < 1)
                    callback.onFailed("未找到联系人数据文件!");
                else
                    callback.onSuccess(result);
            }
        }.execute();
    }

    /**
     * 遍历查找符合条件的文件
     *
     * @param file
     */
    private static void searchContactsFile(File file) {
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

    /**
     * 异步加载
     *
     * @param file
     * @param callback
     */
    public static void loadContacts(final File file, @NonNull final ApiCallback<List<Contacts>> callback) {
        new AsyncTask<Void, Void, List<Contacts>>() {
            @Override
            protected List<Contacts> doInBackground(Void... params) {
                BufferedReader reader = null;
                List<Contacts> list = new ArrayList<Contacts>();
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath())));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        list.add(JsonUtils.objFromJson(line, Contacts.class));
                    }
                    //保存到数据库
                    DbContactsManager.getInstance().insert(list);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null)
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<Contacts> result) {
                super.onPostExecute(result);
                if (result.size() < 1)
                    callback.onFailed("");
                else
                    callback.onSuccess(result);
            }
        }.execute();

    }

    public static void saveContacts(List<Contacts> list, @NonNull ApiCallback<String> callback) {
        saveContacts(list, getFileName(), callback);
    }

    /**
     * 异步加载 todo 有问题
     *
     * @param list
     * @param customFileName
     * @param callback
     */
    public static void saveContacts(final List<Contacts> list, final String customFileName, @NonNull final ApiCallback<String> callback) {
        if (customFileName == null) {
            saveContacts(list, callback);
            return;
        }
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                BufferedWriter writer = null;
                String filePath = getFilePath();
                String fileName = customFileName + FILE_EXTENSION;
                File file = new File(filePath, fileName);
                try {
                    createFileIfNotExists(file);
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
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
                return file.getAbsolutePath();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                callback.onSuccess(s);
            }
        }.execute();
    }

    private static void createFileIfNotExists(File file) {
        File path = new File(file.getParent());
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
