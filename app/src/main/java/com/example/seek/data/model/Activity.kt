package com.example.seek.data.model

import androidx.room.*
import io.reactivex.Observable

@Entity(tableName = "saved_activity_table", indices = [Index(value = ["activity_key", "activity", "type", "participants"], unique = true)])
data class Activity @Ignore constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "activity_key")
    val key: String? = null,

    @ColumnInfo(name = "activity")
    val activity: String? = null,

    @ColumnInfo(name = "type")
    val type: String? = null,

    @ColumnInfo(name = "participants")
    val participants: Int? = null,

    @Ignore
    var category: CategoryItem? = null
) {
    constructor(id: Int?, key: String?, activity: String?, type: String?, participants: Int?) : this(id, key, activity, type, participants, null)
}

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: Activity): Long

    @Query("DELETE FROM saved_activity_table WHERE activity_key = :key")
    suspend fun delete(key: String)

    @Query("SELECT * FROM saved_activity_table ORDER BY id ASC")
    fun getAllActivities() : Observable<List<Activity>>

    @Query("SELECT * FROM saved_activity_table WHERE activity_key = :key")
    fun getActivityByKey(key: String) : Observable<List<Activity>>

}
