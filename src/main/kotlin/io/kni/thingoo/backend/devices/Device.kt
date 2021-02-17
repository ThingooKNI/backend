package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.entities.Entity
import java.io.Serializable
import javax.persistence.CascadeType
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

    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var entities: List<Entity> = emptyList()
) : Serializable {
    fun toRegisterDeviceDto(): RegisterDeviceDto {
        return RegisterDeviceDto(
            deviceID, macAddress, displayName, entities.map { it.toRegisterEntityDto() }
        )
    }

    override fun toString(): String {
        return "Device(id=$id, deviceID='$deviceID', macAddress='$macAddress', displayName=$displayName," +
            " entities=$entities)"
    }
}
