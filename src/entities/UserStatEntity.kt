package entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import tables.UserStatTable

class UserStatEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserStatEntity>(UserStatTable)

    var user by UserEntity referencedOn UserStatTable.user

    var won by UserStatTable.won
    var lost by UserStatTable.lost
    var createdAt by UserStatTable.createdAt
    var updatedAt by UserStatTable.updatedAt
}