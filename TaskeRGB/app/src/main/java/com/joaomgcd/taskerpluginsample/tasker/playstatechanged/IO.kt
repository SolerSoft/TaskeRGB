package com.joaomgcd.taskerpluginsample.tasker.playstatechanged

import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable
import com.joaomgcd.taskerpluginsample.R


@TaskerInputRoot
class PlayStateFilter @JvmOverloads constructor(
        @field:TaskerInputField(PLAY_MODE_KEY, labelResId = R.string.play_mode) val playMode: Int? = null
) {
    val isPlaying get() = playMode == PLAY_MODE_PLAYING
    val isStopped get() = playMode == PLAY_MODE_STOPPED

    companion object {

        const val PLAY_MODE_KEY = "playMode"
        const val PLAY_MODE_PLAYING = 0
        const val PLAY_MODE_STOPPED = 1
    }
}

@TaskerInputRoot
@TaskerOutputObject()
class PlayState @JvmOverloads constructor(
        @field:TaskerInputField("playing") @get:TaskerOutputVariable("playing", R.string.playing_label, R.string.playing_html_label) val playing: Boolean? = null,
        @field:TaskerInputField("artist") @get:TaskerOutputVariable("artist", R.string.artist_label, R.string.artist_html_label) val artist: String? = null,
        @field:TaskerInputField("songName") @get:TaskerOutputVariable("song", R.string.song_label, R.string.song_html_label) val songName: String? = null

)
