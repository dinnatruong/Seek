package com.example.seek.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "saved_activity_table", indices = [Index(value = ["activity_key"], unique = true)])
data class Activity @Ignore constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "activity_key")
    val key: String? = null,

    @Ignore
    val activity: String? = null,

    @Ignore
    val type: String? = null,

    @Ignore
    val participants: Int? = null,

    @Ignore
    val category: Category? = null
) {
    constructor(id: Int?, key: String?) : this(id, key, null, null, null, null)
}

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: Activity): Long

    @Delete
    fun delete(activity: Activity)

    @Query("SELECT * FROM saved_activity_table ORDER BY id ASC")
    fun getAllActivities() : LiveData<List<Activity>>

    @Query("SELECT * FROM saved_activity_table WHERE activity_key MATCH :key")
    fun getActivityByKey(key: String) : LiveData<List<Activity>>

}
