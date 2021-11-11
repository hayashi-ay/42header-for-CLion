package com.hayapenguin.ft_header

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import kotlinx.coroutines.Runnable

class UpdateHeader: BulkFileListener{
    override fun before(events: MutableList<out VFileEvent>) {
        super.before(events)
    }

    override fun after(events: MutableList<out VFileEvent>) {
        super.after(events)
        for (event in events) {
            if (event is VFileContentChangeEvent) {
                val runnable = Runnable {
                    FileDocumentManager.getInstance().getDocument(event.file)?.insertString(0, "10");
                }
                WriteCommandAction.runWriteCommandAction(ProjectManager.getInstance().defaultProject, runnable);
            }
        }
    }
}