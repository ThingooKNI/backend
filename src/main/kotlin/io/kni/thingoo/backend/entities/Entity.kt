package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.devices.Device
import io.kni.thingoo.backend.entities.dto.EntityDto
import io.kni.thingoo.backend.entities.dto.SetupEntityDto
import io.kni.thingoo.backend.icons.MaterialIcon
import io.kni.thingoo.backend.readings.Reading
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
import javax.persistence.OneToMany
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
    @Column(name = "value_type", nullable = false)
    var valueType: ValueType,

    @Column(name = "unit_display_name", nullable = false)
    var unitDisplayName: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "icon", nullable = true)
    var icon: MaterialIcon?,

    @ManyToOne
    @JoinColumn(name = "device_id")
    var device: Device? = null,

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, orphanRemoval = true)
    var readings: MutableList<Reading> = mutableListOf()
) : Serializable {
    fun toSetupEntityDto(): SetupEntityDto {
        return SetupEntityDto(
            key, type, valueType, unitDisplayName
        )
    }

    fun toDto(): EntityDto {
        return EntityDto(
            id, key, displayName, type, valueType, unitDisplayName, icon
        )
    }

    override fun toString(): String {
        return "Entity(id=$id, key='$key', displayName=$displayName, type=$type, valueType=$valueType, unitDisplayName='$unitDisplayName', device=$device)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as io.kni.thingoo.backend.entities.Entity

        if (id != other.id) return false
        if (key != other.key) return false
        if (displayName != other.displayName) return false
        if (type != other.type) return false
        if (valueType != other.valueType) return false
        if (unitDisplayName != other.unitDisplayName) return false
        if (device != other.device) return false
        if (readings != other.readings) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + key.hashCode()
        result = 31 * result + (displayName?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + valueType.hashCode()
        result = 31 * result + unitDisplayName.hashCode()
        result = 31 * result + (device?.hashCode() ?: 0)
        result = 31 * result + readings.hashCode()
        return result
    }
}
