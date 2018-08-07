package org.ministryofhealth.newimci.tests

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import org.ministryofhealth.newimci.R

import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class AfterTestActivity : AppCompatActivity() {
    var attempt_id = null
    val db = null
    val attempt = null
    val responseList = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_test)

        val viewKonfetti = findViewById<KonfettiView>(R.id.viewKonfetti)
        attempt_id = intent.getIntExtra("attempt_id", 0)



        viewKonfetti.build()
                .addColors(Color.parseColor("#6200EA"), Color.parseColor("#FFAB00"), Color.parseColor("#D50000"))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(Size(12), Size(16, 6f))
                .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)
    }


}
