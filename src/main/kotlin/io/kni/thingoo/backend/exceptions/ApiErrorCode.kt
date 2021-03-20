package io.kni.thingoo.backend.exceptions

import io.kni.thingoo.backend.devices.exceptions.DeviceNotFoundException
import io.kni.thingoo.backend.devices.exceptions.ExistingDeviceKeyException
import io.kni.thingoo.backend.devices.exceptions.ExistingMACAddressException
import io.kni.thingoo.backend.devices.exceptions.InvalidDevicePatchEntryValueException
import io.kni.thingoo.backend.devices.exceptions.InvalidMACAddressException
import io.kni.thingoo.backend.entities.exceptions.EntityNotFoundException
import io.kni.thingoo.backend.entities.exceptions.ExistingEntityKeyException
import io.kni.thingoo.backend.entities.exceptions.InvalidEntityPatchEntryValueException
import io.kni.thingoo.backend.readings.exceptions.NoReadingsException
import io.kni.thingoo.backend.readings.exceptions.ReadingUnitTypeMismatchException
import org.springframework.http.HttpStatus

enum class ApiErrorCode(private val exception: RestException) : ErrorCode {
    API_000(RestException("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR)),
    AUTH_000(RestException("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR)),

    DEVICES_001(DeviceNotFoundException("Device with given id not found")),
    DEVICES_002(InvalidMACAddressException("Invalid device mac address")),
    DEVICES_003(ExistingMACAddressException("There is already a device registered with this macAddress")),
    DEVICES_004(ExistingDeviceKeyException("There is already a device registered with this key")),
    DEVICES_005(DeviceNotFoundException("Device with given key doesn't exist")),
    DEVICES_006(InvalidDevicePatchEntryValueException("Invalid patch object provided. Check types of provided fields")),

    ENTITIES_001(ExistingEntityKeyException("Duplicated Entity key value")),
    ENTITIES_002(EntityNotFoundException("Entity with given key and deviceKey doesn't exist")),
    ENTITIES_003(EntityNotFoundException("Entity with given id not found")),
    ENTITIES_004(InvalidEntityPatchEntryValueException("Invalid patch object provided. Check types of provided fields")),

    READINGS_001(ReadingUnitTypeMismatchException("Reading value is not correct value of entity's unit type")),
    READINGS_002(NoReadingsException("Given entity has no readings. Cannot return latest reading."))
    ;

    init {
        exception.code = this
    }

    override fun id(): String = this.name

    override fun throwException(): Nothing {
        throw this.exception
    }
}
