package com.solersoft.taskergb.tasker.getip

import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable
import com.solersoft.taskergb.R

@TaskerOutputObject
class GetIPOutput(
        @get:TaskerOutputVariable(VAR_IP, R.string.ip, R.string.ip_description) var publicIp: String?
){
    companion object {
        const val VAR_IP = "ip"
        const val VAR_SPLIT_IP = "split_ip"
    }
}