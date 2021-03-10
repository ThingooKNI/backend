package io.kni.thingoo.backend.icons

import com.fasterxml.jackson.annotation.JsonFormat

// keep it sorted
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class MaterialIcon(val iconKey: String, val displayName: String) {
    ALARM("alarm", "clock"),
    HOME("home", "home"),
    LIGHTBULB("lightbulb", "lightbulb"),
    LOCK("lock", "lock"),
    MIC("mic", "microphone"),
    OUTLET("outlet", "electric outlet"),
    PERSON("person", "person"),
    PLAY_CIRCLE("play_circle", "player"),
    POWER("power", "power plug"),
    POWER_SETTINGS_NEW("power_settings_new", "power"),
    PRINT("print", "printer"),
    SENSORS("sensors", "sensor"),
    SETTINGS_REMOTE("settings_remote", "remote"),
    THERMOSTAT("thermostat", "temperature"),
    TOGGLE_ON("toggle_on", "switch"),
    WIFI("wifi", "wifi");

    fun getName(): String {
        return this.name
    }
}
