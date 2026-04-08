package crossnative

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.Toolkit

// L'implémentation dans java du Manager
class DesktopSoundManager : SoundManager {
    override fun playSound() {
        Toolkit.getDefaultToolkit().beep()
    }
}

// actual pour le Desktop
@Composable
actual fun rememberSoundManager(): SoundManager {
    return remember { DesktopSoundManager() }
}