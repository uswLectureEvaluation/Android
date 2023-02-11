package com.kunize.uswtimetable.ui.class_info.interceptionFilter.addTime.filter.elearning

import com.kunize.uswtimetable.util.interceptingFilter.Filter
import com.kunize.uswtimetable.util.interceptingFilter.FilterRequest
import com.kunize.uswtimetable.util.interceptingFilter.FilterState
import com.kunize.uswtimetable.util.interceptingFilter.UnknownFilterFailState

class ELearningValidationFilter : Filter {

    override fun invoke(request: FilterRequest): FilterState {
        return if (request is ELearningValidationFilterRequest) checkELearningValidation(request)
        else UnknownFilterFailState
    }

    private fun checkELearningValidation(
        request: ELearningValidationFilterRequest
    ): FilterState {
        with(request) {
            for (newTime in timeDataTobeAdded) {
                if ((newTime.day.contains("토") || newTime.location.contains("이러닝") || newTime.location.isEmpty()) &&
                    currentTimeTable.count { it.location == "이러닝" || it.day == "토" } > 2
                ) {
                    return ELearningNotValidateState
                }
            }
            return FilterState.Validate
        }
    }
}