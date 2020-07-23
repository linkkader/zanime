
package com.zanime.link.database.history

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history3")
data class History(val name : String ="",val img : String="",
                   val source : String = ""
                   , @PrimaryKey val id: String = "" ,val date:String,val url: String )
