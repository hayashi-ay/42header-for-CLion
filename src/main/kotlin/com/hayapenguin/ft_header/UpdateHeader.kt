package com.hayapenguin.ft_header

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import kotlinx.coroutines.Runnable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateHeader: BulkFileListener{
    override fun before(events: MutableList<out VFileEvent>) {
        super.before(events)
    }

    override fun after(events: MutableList<out VFileEvent>) {
        super.after(events)
        for (event in events) {
            if (event is VFileContentChangeEvent) {
                var file = event.file;
                if (file.extension != "c" && file.extension != "h")
                    continue;
                var user = System.getenv("FT_NAME") ?: "nop";
                var email = System.getenv("FT_EMAIL") ?: "nop@42.jp";

                val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                val date = LocalDateTime.now();
                if (!event.file.isInLocalFileSystem)
                    continue;
                var existHeader = false;
                var textLen = FileDocumentManager.getInstance().getDocument(event.file)?.textLength ?: 0;
                if (textLen > 5) {
                    var firstLine = FileDocumentManager.getInstance().getDocument(event.file)?.getText(TextRange(0, 5));
                    existHeader = firstLine.equals("/* **");
                }
                var needsUpdate = false;
                if (existHeader)
                {
                    var dateStr = FileDocumentManager.getInstance().getDocument(event.file)?.getText(TextRange(662, 672));
                    println(dateStr)
                    var f = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    var updateTime = LocalDate.parse(dateStr, f).atStartOfDay();
                    if (updateTime.isBefore(LocalDateTime.now().minusDays(1)))
                    {
                        needsUpdate = true
                    }
                }
                if (existHeader && needsUpdate) {
                    var header = "/*   Updated: " + date.format(formatter) + " by "
                        .plus(user.padEnd(17))
                        .plus("###   ########.jp       */\n");
                    val runnable = Runnable {
                        FileDocumentManager.getInstance().getDocument(event.file)?.replaceString(648, 648 + header.length, header);
                    }
                    WriteCommandAction.runWriteCommandAction(ProjectManager.getInstance().defaultProject, runnable);
                } else if (!existHeader) {
                    var header:String = "/* ************************************************************************** */\n"
                    .plus("/*                                                                            */\n")
                    .plus("/*                                                        :::      ::::::::   */\n")
                    .plus("/*   ")
                    .plus(file.name.padEnd(51))
                    .plus(":+:      :+:    :+:   */\n")
                    .plus("/*                                                    +:+ +:+         +:+     */\n")
                    .plus("/*   By: ")
                    .plus(("$user <$email>").padEnd(43))
                    .plus("+#+  +:+       +#+        */\n")
                    .plus("/*                                                +#+#+#+#+#+   +#+           */\n")
                    .plus("/*   Created: " + date.format(formatter) + " by ")
                    .plus(user.padEnd(18))
                    .plus("#+#    #+#             */\n")
                    .plus("/*   Updated: " + date.format(formatter) + " by ")
                    .plus(user.padEnd(17))
                    .plus("###   ########.jp       */\n")
                    .plus("/*                                                                            */\n")
                    .plus("/* ************************************************************************** */\n")
                    .plus("\n");

                    val runnable = Runnable {
                        event.file.refresh(false, false);
                        FileDocumentManager.getInstance().getDocument(event.file)?.insertString(0, header);
                    }
                    WriteCommandAction.runWriteCommandAction(ProjectManager.getInstance().defaultProject, runnable);
                }
            }
        }
    }
}