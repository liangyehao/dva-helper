package com.liang;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.SystemIndependent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * dva助手
 *
 * @author liangyehao
 * @date 2021/01/11
 */
public class DvaHelper extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        if (project!=null&&project.getBasePath()!=null) {
            String basePath = uniteSeparator(project.getBasePath());
            java.util.List<File> dvaModels = getDvaModels(basePath);
            java.util.List<String> fileNames = getFileNames(dvaModels,basePath);
            showDvaModels(filterModels(fileNames,basePath));
        }
    }

    /**
     * 通过正则表达式过滤出真正的model
     *
     * @param fileNames 文件名
     * @param basePath  基本路径
     * @return {@link List<String>}
     */
    private List<String> filterModels(List<String> fileNames, String basePath) {
        List<String> list = new ArrayList<>();
        String isModel = ".+namespace(\\s+)?:(\\s+)?(['\"]).+['\"](\\s+)?,.+";
        for (String fileName : fileNames) {
            try {
                String s = FileUtils.readFileToString(new File(basePath+File.separator+fileName), "UTF-8")
                        .replaceAll("\\n","");
                if (Pattern.matches(isModel, s)) {
                    list.add(fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private void showDvaModels(java.util.List<String> fileNames) {
        String br = "\n";
        String tab = "\t";
        StringBuilder msg = new StringBuilder("Total models: "+fileNames.size()+br);
        for (String fileName : fileNames) {
            msg.append(tab).append(fileName).append(br);
        }
        Messages.showInfoMessage(msg.toString(),"Dva Helper Created By Lyh");
    }

    private String uniteSeparator(String basePath) {
        return basePath.replaceAll("\\\\", Matcher.quoteReplacement(File.separator))
                .replaceAll("\\/", Matcher.quoteReplacement(File.separator))
                .replaceAll("\\\\\\\\", Matcher.quoteReplacement(File.separator))
                .replaceAll("\\/\\/", Matcher.quoteReplacement(File.separator));
    }

    private java.util.List<String> getFileNames(java.util.List<File> dvaModels, @SystemIndependent String basePath) {
        java.util.List<String> fileNames = new ArrayList<>();
        dvaModels.forEach(file -> fileNames.add(file.getPath().replace(basePath+File.separator,"")));
        return fileNames;
    }

    /**
     * 获取Dva的model文件
     *
     * @param basePath 基本路径
     * @return {@link java.util.List <File>}
     */
    private List<File> getDvaModels(String basePath) {

        Collection<File> files = FileUtils.listFiles(new File(basePath+File.separator+"src"), new String[]{"js", "ts"}, true);
        return files.stream()
                .filter(file -> file.getPath().contains(File.separator + "models") && !file.getPath().contains(".umi")).collect(Collectors.toList());
    }
}

