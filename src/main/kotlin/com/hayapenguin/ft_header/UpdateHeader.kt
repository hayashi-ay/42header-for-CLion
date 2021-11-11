package com.hayapenguin.ft_header

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import kotlinx.coroutines.Runnable
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

                var filename = event.file.name;
                var user = "ahayashi;";
                var email = "ahayashi@student.42tokyo.jp";

                val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                val date = LocalDateTime.now();

                var header:String = "/* ************************************************************************** */\n"
                    .plus("/*                                                                            */\n")
                    .plus("/*                                                        :::      ::::::::   */\n")
                    .plus("/*   ")
                    .plus(filename.padEnd(51))
                    .plus(":+:      :+:    :+:   */\n")
                    .plus("/*                                                    +:+ +:+         +:+     */\n")
                    .plus("/*   By: ")
                    .plus(("$user $email").padEnd(43))
                    .plus("+#+  +:+       +#+        */\n")
                    .plus("/*                                                +#+#+#+#+#+   +#+           */\n")
                    .plus("/*   Created: " + date.format(formatter) + " by ")
                    .plus(user.padEnd(18))
                    .plus("#+#    #+#             */\n")
                    .plus("/*   Updated: " + date.format(formatter) + " by ")
                    .plus(user.padEnd(17))
                    .plus("###   ########.fr       */\n")
                    .plus("/*                                                                            */\n")
                    .plus("/* ************************************************************************** */\n");

                val runnable = Runnable {
                    FileDocumentManager.getInstance().getDocument(event.file)?.insertString(0, header);
                }
                WriteCommandAction.runWriteCommandAction(ProjectManager.getInstance().defaultProject, runnable);
            }
        }
    }
}