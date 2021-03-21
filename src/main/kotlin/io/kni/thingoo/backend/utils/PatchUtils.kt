package io.kni.thingoo.backend.utils

import io.kni.thingoo.backend.utils.exception.EntityPatchException
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

class PatchUtils {

    companion object {
        fun patchEntity(patch: Map<String, Any>, patchDtoClass: KClass<*>, entity: Any) {
            val patchDtoFields = patchDtoClass.declaredMemberProperties

            patchDtoFields.forEach { field ->
                val fieldName = field.name
                val fieldType = field.returnType

                if (patch.keys.contains(fieldName)) {
                    val patchValue = patch[fieldName]!!

                    when {
                        ReflectionUtils.isExactTypeOf(patchValue, fieldType) -> {
                            ReflectionUtils.setInstanceProperty(entity, fieldName, patchValue)
                        }
                        ReflectionUtils.isEnumTypeOf(patchValue, fieldType) -> {
                            val enumValue = ReflectionUtils.createEnumFromString(fieldType, patchValue.toString())
                            ReflectionUtils.setInstanceProperty(entity, fieldName, enumValue)
                        }
                        else -> {
                            throw EntityPatchException()
                        }
                    }
                }
            }
        }
    }
}
