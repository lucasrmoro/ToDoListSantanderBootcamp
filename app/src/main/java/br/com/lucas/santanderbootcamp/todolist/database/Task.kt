package br.com.lucas.santanderbootcamp.todolist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo
    var taskTitle: String,
    @ColumnInfo
    val taskDescription: String,
    @ColumnInfo
    val taskDate: String,
    @ColumnInfo
    val taskHour: String
) : Serializable
