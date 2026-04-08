package crossnative

import androidx.compose.runtime.Composable

// notre manager va jouer de la musique
interface SoundManager {
    fun playSound()
}

// La promesse "expect" : On promet au compilateur qu'on va fournir
// un moyen de créer ce Manager sur chaque plateforme.
@Composable
expect fun rememberSoundManager(): SoundManager