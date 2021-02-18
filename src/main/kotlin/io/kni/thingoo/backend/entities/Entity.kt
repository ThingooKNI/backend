package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.entities.dto.EntityDto
import io.kni.thingoo.backend.entities.dto.RegisterEntityDto
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "entities")
class Entity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(name = "key", nullable = false)
    var key: String,

    @Column(name = "display_name", nullable = true)
    var displayName: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: EntityType,

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type", nullable = false)
    var unitType: UnitType,

    @Column(name = "unit_display_name", nullable = false)
    var unitDisplayName: String,

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    var device: Device? = null
) : Serializable {
    fun toRegisterEntityDto(): RegisterEntityDto {
        return RegisterEntityDto(
            key, displayName, type, unitType, unitDisplayName
        )
    }

    fun toDto(): EntityDto {
        return EntityDto(
            id, key, displayName, type, unitType, unitDisplayName
        )
    }

    override fun toString(): String {
        return "Entity(id=$id, key='$key', displayName='$displayName', type=$type, unitType=$unitType," +
            " unitDisplayName='$unitDisplayName')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as io.kni.thingoo.backend.entities.Entity

        if (id != other.id) return false
        if (key != other.key) return false
        if (displayName != other.displayName) return false
        if (type != other.type) return false
        if (unitType != other.unitType) return false
        if (unitDisplayName != other.unitDisplayName) return false
        if (device != other.device) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + key.hashCode()
        result = 31 * result + (displayName?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + unitType.hashCode()
        result = 31 * result + unitDisplayName.hashCode()
        result = 31 * result + (device?.hashCode() ?: 0)
        return result
    }
}
