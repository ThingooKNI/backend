package io.kni.thingoo.backend.utils

class StringUtils {
    companion object {
        fun isBoolean(str: String): Boolean {
            return str == "true" || str == "false"
        }

        fun isInteger(str: String): Boolean {
            return str.matches(Regex("-?\\d+"))
        }

        fun isDecimal(str: String): Boolean {
            return str.matches(Regex("-?\\d+.\\d+"))
        }

        fun getRandomAlphanumericString(length: Int): String {
            val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

            return List(length) { charset.random() }.joinToString("")
        }
    }
}
