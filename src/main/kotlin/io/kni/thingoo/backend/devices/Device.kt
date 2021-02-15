package io.kni.thingoo.backend.devices

import io.kni.thingoo.backend.entities.Entity
import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@javax.persistence.Entity
@Table(name = "devices")
class Device(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    var deviceID: String,
    var macAddress: String,
    var displayName: String?,
    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var entities: Set<Entity>
) : Serializable
