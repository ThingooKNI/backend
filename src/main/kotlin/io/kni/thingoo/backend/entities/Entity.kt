package io.kni.thingoo.backend.entities

import io.kni.thingoo.backend.devices.Device
import java.io.Serializable
import javax.persistence.Entity
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    var key: String,
    var displayName: String?,
    var type: EntityType,
    var unitType: UnitType,
    var unitDisplayName: String,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    var device: Device

) : Serializable
