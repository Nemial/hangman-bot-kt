package table

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

const val MAX_LENGTH_VARCHAR = 255

object UserTable : IntIdTable() {
    val firstName = varchar("first_name", MAX_LENGTH_VARCHAR).nullable()
    val lastName = varchar("last_name", MAX_LENGTH_VARCHAR).nullable()
    val userName = varchar("username", MAX_LENGTH_VARCHAR)
    val referenceId = integer("reference_id").nullable()

    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}