package id.allana.todoapp_learnfromudemy.activity.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.allana.todoapp_learnfromudemy.activity.MainActivity
import id.allana.todoapp_learnfromudemy.databinding.ActivitySplashScreenBinding


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val ivIcon = binding.ivIcon
        val tvTitleApp = binding.tvTitleApp

        val ivIconAnimator = ObjectAnimator.ofFloat(ivIcon, "alpha", 0f, 1f).also {
            it.duration = 1000
        }

        val tvTitleAppAnimator = ObjectAnimator.ofFloat(tvTitleApp, "alpha", 0f, 1f).also {
            it.duration = 1000
        }

        AnimatorSet().also {
            it.playSequentially(ivIconAnimator, tvTitleAppAnimator)
            it.start()

            it.addListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}

                override fun onAnimationEnd(p0: Animator) {
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                }

                override fun onAnimationCancel(p0: Animator) {}

                override fun onAnimationRepeat(p0: Animator) {}

            })
        }
    }
}