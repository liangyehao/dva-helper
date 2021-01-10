package com.liang;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.SystemIndependent;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2021/1/9 13:58
 * @content 查看umi项目dva的model
 */
public class DvaScanner extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        if (project!=null&&project.getBasePath()!=null) {
            String basePath = uniteSeparator(project.getBasePath());
            java.util.List<File> dvaModels = getDvaModels(basePath);
            java.util.List<String> fileNames = getFileNames(dvaModels,basePath);
            showDvaModels(fileNames);
        }
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
