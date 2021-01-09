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
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2021/1/9 13:58
 * @content
 */
public class DvaScanner extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        String projectBasePath = getProjectBasePath(anActionEvent);
        String res = Utils.exec("umi dva list model",projectBasePath);
        showModels(anActionEvent,res);
        System.out.println();
    }

    private void showModels(AnActionEvent anActionEvent,String res) {
//        showPopupBalloon(anActionEvent.getData(PlatformDataKeys.EDITOR),res);
        showMessage(res);
    }

    private void showPopupBalloon(final Editor editor, final String result) {
        ApplicationManager.getApplication().invokeLater(() -> {
            JBPopupFactory factory = JBPopupFactory.getInstance();
            factory.createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                    .setFadeoutTime(5000)
                    .createBalloon()
                    .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
        });
    }

    private void showMessage(String msg){
        Messages.showInfoMessage(msg,"Dva Helper");
    }

    /**
     * 得到项目的基本路径
     *
     * @param anActionEvent 一个行动的事件
     * @return {@link String}
     */
    private String getProjectBasePath(@NotNull AnActionEvent anActionEvent) {
        Project data = anActionEvent.getData(PlatformDataKeys.PROJECT);
        if (data!=null) {
            return data.getBasePath();
        }
        return "";
    }
}
