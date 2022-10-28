package com.rchyn.prosa.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rchyn.prosa.data.local.entity.RemoteKeys
import com.rchyn.prosa.data.local.entity.StoryEntity


@Database(entities = [StoryEntity::class, RemoteKeys::class], version = 2, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}