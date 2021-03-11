package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.devices.dto.DeviceDto
import io.kni.thingoo.backend.devices.dto.SetupDeviceDto
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

    @Column(name = "key", nullable = false, unique = true)
    var key: String,

    @Column(name = "mac_address", nullable = false, unique = true)
    var macAddress: String,

    @Column(name = "display_name", nullable = true)
    var displayName: String?,

    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER, orphanRemoval = true)
    var entities: MutableList<Entity> = mutableListOf()
) : Serializable {
    fun toSetupDeviceDto(): SetupDeviceDto {
        return SetupDeviceDto(
            key, macAddress, entities.map { it.toSetupEntityDto() }
        )
    }

    fun toDto(): DeviceDto {
        return DeviceDto(
            id, key, macAddress, displayName, entities.map { it.toDto() }
        )
    }

    fun addEntity(entity: Entity) {
        entity.device = this
        this.entities.add(entity)
    }

    fun deleteEntity(entity: Entity) {
        this.entities.remove(entity)
        entity.device = null
    }

    override fun toString(): String {
        return "Device(id=$id, key='$key', macAddress='$macAddress', displayName='$displayName')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Device

        if (id != other.id) return false
        if (key != other.key) return false
        if (macAddress != other.macAddress) return false
        if (displayName != other.displayName) return false
        if (entities != other.entities) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + key.hashCode()
        result = 31 * result + macAddress.hashCode()
        result = 31 * result + (displayName?.hashCode() ?: 0)
        result = 31 * result + entities.hashCode()
        return result
    }
}
