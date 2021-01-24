package com.example.seek.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

data class Activity (
    val activity: String? = null,
    val type: String? = null,
    val participants: Int? = null,
    val key: String? = null,
    val category: Category? = null
)

@Entity(tableName = "activity_table", indices = [Index(value = ["activity_id"], unique = true)])
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "activity_id") val activityId: String? = null
)

@Dao
interface ActivityDao  {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activityEntity: ActivityEntity): Long

    @Delete
    fun delete(activityEntity: ActivityEntity)

    @Query("SELECT * FROM activity_table ORDER BY id ASC")
    fun getAllActivities() : LiveData<List<ActivityEntity>>
}
