package com.hayapenguin.ft_header

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class HelloAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        Messages.showMessageDialog(e.project, "H", "MyIdeaDemo", Messages.getInformationIcon());
    }

}