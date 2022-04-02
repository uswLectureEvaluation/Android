package com.kunize.uswtimetable


import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.kakao.adfit.ads.AdListener
import com.kunize.uswtimetable.databinding.ActivityMainBinding
import com.kunize.uswtimetable.dataclass.TimeData
import com.kunize.uswtimetable.util.KeepStateNavigator
import org.json.JSONArray
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navigationFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navigationFragment.navController

        val keepNavigator = KeepStateNavigator(this, navigationFragment.childFragmentManager, R.id.fragment_container)
        navController.navigatorProvider.addNavigator(keepNavigator)

        navController.setGraph(R.navigation.nav_graph)

        binding.bottomNav.setupWithNavController(navController)

        binding.bannerAdView.setClientId(getString(R.string.kakaoAdfitID))  // 할당 받은 광고단위 ID 설정
        binding.bannerAdView.setAdListener(object : AdListener {  // optional :: 광고 수신 리스너 설정

            override fun onAdLoaded() {
                // 배너 광고 노출 완료 시 호출
            }

            override fun onAdFailed(errorCode: Int) {
                // 배너 광고 노출 실패 시 호출
            }

            override fun onAdClicked() {
                // 배너 광고 클릭 시 호출
            }

        })

        binding.bannerAdView.loadAd()
    }

    override fun onPause() {
        super.onPause()
        binding.bannerAdView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.bannerAdView.destroy()
    }


    override fun onResume() {
        super.onResume()
        binding.bannerAdView.resume()
    }

    companion object {
        val Int.dp: Int
            get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

        val Float.dp: Int
            get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

        fun jsonToArray(jsonStr: String?): MutableList<TimeData> {
            val returnTimeData = mutableListOf<TimeData>()
            if (jsonStr != "") {
                val jsonArray = JSONArray(jsonStr)
                for (idx in 0 until jsonArray.length()) {
                    val jsonObj = jsonArray.getJSONObject(idx)
                    val className = jsonObj.getString("name")
                    val professor = jsonObj.getString("professor")
                    val location = jsonObj.getString("location")
                    val day = jsonObj.getString("day")
                    val startTime = jsonObj.getString("startTime")
                    val endTime = jsonObj.getString("endTime")
                    val color = jsonObj.getInt("color")

                    returnTimeData.add(
                        TimeData(
                            className,
                            professor,
                            location,
                            day,
                            startTime,
                            endTime,
                            color
                        )
                    )
                }
            }
            return returnTimeData
        }

        fun bitmapToString(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val byte = baos.toByteArray()
            return Base64.encodeToString(byte, Base64.DEFAULT)
        }

        fun stringToBitmap(encodedString: String): Bitmap? {
            return try {
                val byte = Base64.decode(encodedString, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(byte, 0, byte.size)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}