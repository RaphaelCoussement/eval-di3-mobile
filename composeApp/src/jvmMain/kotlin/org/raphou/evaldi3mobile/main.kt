package org.raphou.evaldi3mobile

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Evaldi3Mobile",
    ) {
        App()
    }
}