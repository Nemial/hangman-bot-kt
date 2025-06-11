package entity

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import table.WordTable

class WordEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WordEntity>(WordTable)

    var word by WordTable.word

    var createdAt by WordTable.createdAt
    var updatedAt by WordTable.updatedAt
}