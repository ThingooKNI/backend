package io.kni.thingoo.backend.utils

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

class PatchUtils {

    companion object {
        fun patchEntity(patch: Map<String, Any>, patchDtoClass: KClass<*>, entity: Any) {
            val patchDtoFields = patchDtoClass.declaredMemberProperties
            patchDtoFields.forEach {
                if (patch.keys.contains(it.name)) {
                    val patchValue = patch[it.name]!!
                    when {
                        ReflectionUtils.isExactTypeOf(patchValue, it.returnType) -> {
                            ReflectionUtils.setInstanceProperty(entity, it.name, patchValue)
                        }
                        ReflectionUtils.isEnumTypeOf(patchValue, it.returnType) -> {
                            val enumValue = ReflectionUtils.createEnumFromString(it.returnType, patchValue.toString())
                            ReflectionUtils.setInstanceProperty(entity, it.name, enumValue)
                        }
                        else -> {
                            throw Exception("Patch entry field has invalid type")
                        }
                    }
                }
            }
        }
    }
}
