package com.suwiki.core.model.exception

class TimetableCellOverlapException(
  val name: String,
  val startPeriod: Int,
  val endPeriod: Int,
  override val message: String = "겹치는 시간이 있어요. $name ($startPeriod - $endPeriod)",
) : RuntimeException()
