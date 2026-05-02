package com.mysticbyte.counter

import java.io.File

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun getDataStoreFilePath(): String {
    val home = System.getProperty("user.home")
    val dir = File(home, ".counter/datastore")
    if (!dir.exists()) {
        dir.mkdirs()
    }
    return File(dir, "counter.preferences_pb").absolutePath
}
