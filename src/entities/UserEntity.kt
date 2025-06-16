package entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import tables.UserStatTable
import tables.UserTable

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    var firstName by UserTable.firstName
    var lastName by UserTable.lastName
    var userName by UserTable.userName
    var referenceId by UserTable.referenceId

    val stat by UserStatEntity backReferencedOn UserStatTable.user
}