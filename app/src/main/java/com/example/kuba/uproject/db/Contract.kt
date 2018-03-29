package com.example.kuba.uproject.db

import android.provider.BaseColumns

/**
 * Created by kuba on 28/03/2018.
 */

val DATABASE_NAME = "habittrainer.db"
val DATABASE_VERSION = 10

object HabitEntry : BaseColumns {
    val TABLE_NAME = "habit"

    val ID_COL = "id"
    val TITLE_COL = "title"
    val DESCR_COL = "description"
    val IMAGE_COL = "image"
}
