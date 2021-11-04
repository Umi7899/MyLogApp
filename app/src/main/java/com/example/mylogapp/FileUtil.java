package com.example.mylogapp;

import java.io.File;

public class FileUtil {
    public static String[] getLogsName(String folderPath) {
        File file01 = new File(folderPath);
        String[] files01 = file01.list();
        int LogNums = 0;
        for (int i = 0; i < files01.length; i++) {
            if(files01[i].length()==11) {
                File file02 = new File(folderPath + "/" + files01[i]);
                if (!file02.isDirectory()) {
                    LogNums++;
                }
            }
        }

        String[] files02 = new String[LogNums];
        int j = 0;
        for (int i = 0; i < files01.length; i++) {
            if(files01[i].length()==11) {
                File file02 = new File(folderPath + "/" + files01[i]);
                if (!file02.isDirectory()) {
                    files02[j] = file02.getName();
                    j++;
                }
            }
        }
        return files02;
    }
}
