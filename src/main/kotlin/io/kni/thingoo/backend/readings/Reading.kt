package io.kni.thingoo.backend.readings

import io.kni.thingoo.backend.entities.Entity
import io.kni.thingoo.backend.readings.dto.ReadingDto
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@javax.persistence.Entity
@Table(name = "readings")
class Reading(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(name = "value", nullable = false)
    var value: String,

    @Column(name = "timestamp", nullable = false, updatable = false)
    @CreationTimestamp
    var timestamp: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "entity_id")
    var entity: Entity? = null,
) {
    fun toDto(): ReadingDto {
        return ReadingDto(
            id, value, timestamp!!
        )
    }

    override fun toString(): String {
        return "Reading(id=$id, value='$value', timestamp=$timestamp, entity=$entity)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reading

        if (id != other.id) return false
        if (value != other.value) return false
        if (timestamp != other.timestamp) return false
        if (entity != other.entity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + value.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + (entity?.hashCode() ?: 0)
        return result
    }
}
