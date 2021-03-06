package io.github.mohamedisoliman.androiddev.data.local

import androidx.room.Room
import android.content.Context
import android.content.SharedPreferences
import io.github.mohamedisoliman.androiddev.data.model.RedditFilter
import io.github.mohamedisoliman.androiddev.data.model.RedditPost
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Created by Mohamed Ibrahim on 8/5/18.
 */
class RedditLocalStore(app: Context) {

  private val sharedPreferences: SharedPreferences
  private val database: PostsDatabase

  private companion object {
    private const val KEY_FILTER = "FILTER"
    private const val FILE_NAME = "Androiddev_pref"
  }

  init {
    sharedPreferences = app.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    database = Room.databaseBuilder(app, PostsDatabase::class.java, "androiddev.db")
        .fallbackToDestructiveMigration()
        .build()
  }

  var filter: String
    get() = sharedPreferences.getString(KEY_FILTER, RedditFilter.NEW.filterValue)!!
    set(value) {
      sharedPreferences.edit().putString(KEY_FILTER, value).apply()
    }

  val posts: Observable<List<RedditPost>>
    get() = database.getTopPosts()

  fun insertPosts(posts: List<RedditPost>): Completable {
    return database.insertPosts(posts)
  }

  fun deletePosts(): Completable {
    return database.deletePosts()
  }
}
