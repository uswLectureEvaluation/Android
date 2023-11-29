package com.suwiki.data.timetable.datasource

import com.suwiki.core.model.timetable.OpenLecture
import kotlinx.coroutines.flow.Flow

interface LocalOpenLectureDatasource {
  suspend fun getOpenLectureList(): List<OpenLecture>
  suspend fun insertOpenLecture(data: OpenLecture)
  suspend fun deleteAllOpenLecture()
  suspend fun setOpenLectureListVersion(): Flow<Long>
  suspend fun getOpenLectureListVersion(version: Long)
}
