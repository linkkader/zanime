/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zanime.link.database.library

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
interface LibraryDao {
    @Query("SELECT * from library_table7 order by Date(date) DESC ")
    fun getAllLibraly(): LiveData<List<Library>>

    @Query("SELECT * from library_table7 WHERE source =:s and name = :n")
    fun getLibraly(s : String, n : String): LiveData<List<Library>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(library : Library)

    @Query("DELETE FROM LIBRARY_TABLE7 WHERE source =:s and name = :n ")
    fun delete(s : String, n : String)
}
