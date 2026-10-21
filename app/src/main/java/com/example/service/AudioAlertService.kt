package com.example.service

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioAlertService {
    private var toneGenerator: ToneGenerator? = null
    var isEnabled: Boolean = true

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
        } catch (_: Exception) {
            toneGenerator = null
        }
    }

    fun playBeep() {
        if (!isEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
        } catch (_: Exception) {}
    }

    fun playWarning() {
        if (!isEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 200)
        } catch (_: Exception) {}
    }

    fun playSiren(scope: CoroutineScope) {
        if (!isEnabled) return
        scope.launch(Dispatchers.Default) {
            try {
                for (i in 0 until 3) {
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 220)
                    delay(280)
                }
            } catch (_: Exception) {}
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
        } catch (_: Exception) {}
        toneGenerator = null
    }
}
