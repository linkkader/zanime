
package com.zanime.link.database.library

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "library_table7")
data class Library(val name : String ="",val img : String="",
                   val source : String = ""
                   , @PrimaryKey val id: String = "" ,val date:String,val url: String) {
}
