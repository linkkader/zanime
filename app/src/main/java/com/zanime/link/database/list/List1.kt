
package com.zanime.link.database.list

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "list_table")
data class List1(val name: String, val img : String, @PrimaryKey val id: Int)
