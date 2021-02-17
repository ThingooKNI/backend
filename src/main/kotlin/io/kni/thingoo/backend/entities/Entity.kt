package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.devices.Device
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
    // @JsonIgnore
    var device: Device? = null
) : Serializable {
    fun toRegisterEntityDto(): RegisterEntityDto {
        return RegisterEntityDto(
            key, displayName, type, unitType, unitDisplayName
        )
    }

    override fun toString(): String {
        return "Entity(id=$id, key='$key', displayName=$displayName, type=$type, unitType=$unitType," +
            " unitDisplayName='$unitDisplayName')"
    }
}
