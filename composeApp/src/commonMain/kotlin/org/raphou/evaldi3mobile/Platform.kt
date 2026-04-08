package org.raphou.evaldi3mobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform