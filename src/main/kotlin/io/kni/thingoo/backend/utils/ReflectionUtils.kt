package io.kni.thingoo.backend.utils

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.javaType

class ReflectionUtils {

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
            val property = instance::class.members
                .first { it.name == propertyName } as KProperty1<Any, *>
            return property.get(instance) as R
        }

        @Suppress("UNCHECKED_CAST")
        fun setInstanceProperty(instance: Any, propertyName: String, propertyValue: Any) {
            val property = instance::class.members.first { it.name == propertyName } as KMutableProperty1<Any, Any>
            property.set(instance, propertyValue)
        }

        fun isExactTypeOf(value: Any, type: KType): Boolean {
            return value::class.createType().withNullability(true) == type
        }

        fun isEnumTypeOf(value: Any, type: KType): Boolean {
            val isEnum = isEnum(type)
            if (isEnum) {
                val enumValues = getEnumValues(type)

                return enumValues.any { it.toString() == value.toString() }
            }

            return false
        }

        @Suppress("UNCHECKED_CAST")
        fun createEnumFromString(type: KType, value: String): Any {
            val enumValues = getEnumValues(type)
            return enumValues.first { it.name == value }
        }

        @Suppress("UNCHECKED_CAST")
        fun getEnumValues(type: KType): Array<Enum<*>> {
            return (type.javaType as Class<*>).enumConstants as Array<Enum<*>>
        }

        fun isEnum(type: KType): Boolean {
            return (type.javaType as Class<*>).isEnum
        }
    }
}
