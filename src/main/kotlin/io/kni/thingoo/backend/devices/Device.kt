package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.entities.Entity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@javax.persistence.Entity
@Table(name = "devices")
class Device(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(name = "device_id", nullable = false, unique = true)
    var deviceID: String,

    @Column(name = "mac_address", nullable = false, unique = true)
    var macAddress: String,

    @Column(name = "display_name", nullable = true)
    var displayName: String?,

    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER, orphanRemoval = true)
    var entities: List<Entity> = mutableListOf()
) : Serializable {
    fun toRegisterDeviceDto(): RegisterDeviceDto {
        return RegisterDeviceDto(
            deviceID, macAddress, displayName, entities.map { it.toRegisterEntityDto() }
        )
    }

    override fun toString(): String {
        return "Device(id=$id, deviceID='$deviceID', macAddress='$macAddress', displayName='$displayName'," +
            " entities=$entities)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Device

        if (id != other.id) return false
        if (deviceID != other.deviceID) return false
        if (macAddress != other.macAddress) return false
        if (displayName != other.displayName) return false
        if (entities != other.entities) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + deviceID.hashCode()
        result = 31 * result + macAddress.hashCode()
        result = 31 * result + (displayName?.hashCode() ?: 0)
        result = 31 * result + entities.hashCode()
        return result
    }
}
