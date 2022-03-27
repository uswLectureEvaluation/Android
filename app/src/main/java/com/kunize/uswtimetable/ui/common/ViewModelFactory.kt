package com.kunize.uswtimetable.ui.common

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kunize.uswtimetable.retrofit.IRetrofit
import com.kunize.uswtimetable.retrofit.ApiClient
import com.kunize.uswtimetable.retrofit.IRetrofit
import com.kunize.uswtimetable.ui.evaluation.EvaluationViewModel
import com.kunize.uswtimetable.ui.login.LoginViewModel
import com.kunize.uswtimetable.ui.mypage.MyExamInfoViewModel
import com.kunize.uswtimetable.ui.mypage.MyPageViewModel
import com.kunize.uswtimetable.ui.mypage.MyPostViewModel
import com.kunize.uswtimetable.ui.notice.NoticeDetailViewModel
import com.kunize.uswtimetable.ui.notice.NoticeViewModel
import com.kunize.uswtimetable.ui.repository.evaluation.EvaluationRemoteDataSource
import com.kunize.uswtimetable.ui.repository.evaluation.EvaluationRepository
import com.kunize.uswtimetable.ui.repository.login.LoginRemoteDataSource
import com.kunize.uswtimetable.ui.repository.login.LoginRepository
import com.kunize.uswtimetable.ui.repository.my_post.MyExamInfoAssetDataSource
import com.kunize.uswtimetable.ui.repository.my_post.MyExamInfoRepository
import com.kunize.uswtimetable.ui.repository.my_post.MyPostAssetDataSource
import com.kunize.uswtimetable.ui.repository.my_post.MyPostRepository
import com.kunize.uswtimetable.ui.repository.mypage.MyPageRemoteDataSource
import com.kunize.uswtimetable.ui.repository.mypage.MyPageRepository
import com.kunize.uswtimetable.ui.repository.notice.NoticeDetailRemoteDataSource
import com.kunize.uswtimetable.ui.repository.notice.NoticeDetailRepository
import com.kunize.uswtimetable.ui.repository.notice.NoticeRemoteDataSource
import com.kunize.uswtimetable.ui.repository.notice.NoticeRepository
import com.kunize.uswtimetable.ui.repository.search_result.SearchResultRemoteDataSource
import com.kunize.uswtimetable.ui.repository.search_result.SearchResultRepository
import com.kunize.uswtimetable.ui.repository.signup.SignUpRemoteDataSource
import com.kunize.uswtimetable.ui.repository.signup.SignUpRepository
import com.kunize.uswtimetable.ui.signup.SignUpPage1ViewModel
import com.kunize.uswtimetable.ui.signup.SignUpPage2ViewModel
import com.kunize.uswtimetable.ui.search_result.SearchResultViewModel
import com.kunize.uswtimetable.ui.signup.SignUpViewModel
import com.kunize.uswtimetable.util.AssetLoader

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MyPostViewModel::class.java) -> {
                val repository = MyPostRepository(MyPostAssetDataSource(AssetLoader(context)))
                MyPostViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel() as T
            }
            modelClass.isAssignableFrom(SignUpPage1ViewModel::class.java) -> {
                val apiService = IRetrofit.getInstanceWithNoToken()
                val repository = SignUpRepository(SignUpRemoteDataSource(apiService))
                SignUpPage1ViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpPage2ViewModel::class.java) -> {
                val apiService = IRetrofit.getInstanceWithNoToken()
                val repository = SignUpRepository(SignUpRemoteDataSource(apiService))
                SignUpPage2ViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                val apiService = IRetrofit.getInstance()
                val repository = LoginRepository(LoginRemoteDataSource(apiService))
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MyPageViewModel::class.java) -> {
                val repository = MyPageRepository(MyPageRemoteDataSource())
                MyPageViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MyExamInfoViewModel::class.java) -> {
                val repository = MyExamInfoRepository(MyExamInfoAssetDataSource(AssetLoader(context)))
                MyExamInfoViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NoticeViewModel::class.java) -> {
                val apiService = IRetrofit.getInstanceWithNoToken()
                val repository = NoticeRepository(NoticeRemoteDataSource(apiService))
                NoticeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NoticeDetailViewModel::class.java) -> {
                val repository = NoticeDetailRepository(NoticeDetailRemoteDataSource())
                NoticeDetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EvaluationViewModel::class.java) -> {
                val repository = EvaluationRepository(EvaluationRemoteDataSource())
                EvaluationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchResultViewModel::class.java) -> {
                val repository = SearchResultRepository(SearchResultRemoteDataSource())
                SearchResultViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
            }
        }
    }
}