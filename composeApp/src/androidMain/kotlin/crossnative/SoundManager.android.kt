package crossnative

import android.content.Context
import android.media.RingtoneManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Fonction d'extension qui utilise le système natif (context) pour faire un bip.
 */
fun Context.playNotificationSound() {
    try {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(this, uri)
        ringtone?.play()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// L'implémentation Android de notre Manager
class AndroidSoundManager(private val context: Context) : SoundManager {
    override fun playSound() {
        context.playNotificationSound()
    }
}

// la fonction composable qui retourne notre manager
@Composable
actual fun rememberSoundManager(): SoundManager {
    val context = LocalContext.current
    return remember { AndroidSoundManager(context) }
}