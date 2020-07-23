

package com.zanime.link.database.history

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
interface HistoryDao {
    @Query("SELECT * from history3 order by Date(date) ASC")
    fun getAllHistorys(): LiveData<List<History>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(history : History)

    @Query("DELETE FROM history3")
    fun deleteAll()
    @Query("DELETE FROM history3 WHERE source =:s and name = :n ")
    fun remove(s:String , n:String)

}
