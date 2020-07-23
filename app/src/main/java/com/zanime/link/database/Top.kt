
package com.zanime.link.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "top1")
data class Top(val name: String, val source : String, val img : String, @PrimaryKey val id: String = name+source,val url : String )
