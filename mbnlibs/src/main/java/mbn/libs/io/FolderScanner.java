package mbn.libs.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FolderScanner {

    public static ArrayList<File> findFoldersContainingFileTypes(File baseFolder, String[] ignoreFolders, boolean ignoreHiddenFolders, String... fileTypes) {
        ArrayList<File> folders = new ArrayList<>();
        findFoldersContainingFileTypes(folders, baseFolder, ignoreFolders, ignoreHiddenFolders, Pattern.compile(getPatternString(fileTypes)));
        return folders;
    }

    public static ArrayList<File> findFileTypesInFolder(File baseFolder, String... fileTypes) {
        ArrayList<File> files = new ArrayList<>();
        findFileTypesInFolder(files, baseFolder, Pattern.compile(getPatternString(fileTypes)));
        return files;
    }


    public static void findFoldersContainingFileTypes(List<File> foundFolders, File baseFolder, String[] ignoreFolders, boolean ignoreHiddenFolders, Pattern pattern, String... fileTypes) {
        if (baseFolder.getName().startsWith(".")) {
            return;
        }
        if (ignoreFolders != null) {
            for (String path : ignoreFolders) {
                if (baseFolder.getPath().equals(path)) {
                    return;
                }
            }
        }
        if (pattern == null) {
            pattern = Pattern.compile(getPatternString(fileTypes));
        }
        boolean found = false;
        for (File f : baseFolder.listFiles()) {
            if (f.isDirectory()) {
                findFoldersContainingFileTypes(foundFolders, f, ignoreFolders, ignoreHiddenFolders, pattern, fileTypes);
            } else {
                if (!found && f.isFile()) {
                    String fileName = f.getName();
                    found = pattern.matcher(fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase()).matches();
                }
            }
        }
        if (found) {
            foundFolders.add(baseFolder);
        }
    }

    public static void findFileTypesInFolder(List<File> foundFiles, File baseFolder, Pattern pattern, String... fileTypes) {
        if (pattern == null) {
            pattern = Pattern.compile(getPatternString(fileTypes));
        }
        for (File f : baseFolder.listFiles()) {
            if (f.isFile()) {
                String fileName = f.getName();
                if (pattern.matcher(fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase()).matches()) {
                    foundFiles.add(f);
                }
            }
        }
    }


    public static String getPatternString(String... types) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length - 1; i++) {
            builder.append(types[i].toLowerCase());
            builder.append('|');
        }
        builder.append(types[types.length - 1].toLowerCase());
        return builder.toString();
    }
}
