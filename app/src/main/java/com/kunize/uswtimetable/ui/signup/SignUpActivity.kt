package com.kunize.uswtimetable.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.kunize.uswtimetable.databinding.ActivitySignupBinding
import com.kunize.uswtimetable.ui.common.EventObserver
import com.kunize.uswtimetable.ui.common.ViewModelFactory
import com.kunize.uswtimetable.util.BackKeyManager
import com.kunize.uswtimetable.util.Constants.KEY_EMAIL
import com.kunize.uswtimetable.util.Constants.TAG

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    val viewModel: SignUpViewModel by viewModels { ViewModelFactory() }
    private var toast: Toast? = null
    lateinit var viewPager: ViewPager2
    private val backKeyManager = BackKeyManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        observeData()
    }

    private fun observeData() {
        viewModel.errorMessage.observe(this, EventObserver { message ->
            if (message.isBlank()) return@EventObserver
            makeToast(message)
        })

        viewModel.toastMessage.observe(this, EventObserver { message ->
            if (message.isBlank()) return@EventObserver
            makeToast(message)
        })

        viewModel.currentPage.observe(this) { page ->
            toast?.cancel()
            viewPager.currentItem = page
        }

        viewModel.loading.observe(this) {
            viewModel.setNextButtonEnable()
        }

        viewModel.success.observe(this, EventObserver { success ->
            if (success) {
                val intent = Intent(this, WelcomeSignUpActivity::class.java).apply {
                    Log.d(TAG, "SignUpActivity - observeData() called: ${viewModel.email.value}")
                    putExtra(KEY_EMAIL, viewModel.email.value)
                }
                startActivity(intent)
                this@SignUpActivity.finish()
            }
        })
    }

    private fun initViews() {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        viewPager = binding.signupViewPager
        viewPager.adapter = SignUpPageAdapter(this@SignUpActivity)
        viewPager.isUserInputEnabled = false
        binding.toolbar.setNavigationOnClickListener {
            when(viewModel.currentPage.value) {
                1 -> viewModel.moveToPreviousPage()
                else -> onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        backKeyManager.onBackPressed(
            "?????? ?????? ?????? ????????? ???????????????. \n" +
                    "??????????????? 2??? ?????? ????????????"
        )
    }

    private fun makeToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    override fun onStop() {
        super.onStop()
        toast?.cancel()
    }
}