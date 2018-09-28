package com.example.we.jobscheduler_ex

import android.app.job.JobInfo
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Messenger
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val MESSENGER_INTENT_KEY = BuildConfig.APPLICATION_ID + ".MESSENGER_INTENT_KEY"
    }

    override fun onStart() {
        super.onStart()
        val startServiceIntent = Intent(this, MyJobService::class.java)
        val messengerIncoming = Messenger(mHandler)
        startServiceIntent.putExtra(MESSENGER_INTENT_KEY, messengerIncoming)
        startService(startServiceIntent)
    }

    override fun onStop() {
        stopService(Intent(this, MyJobService::class.java))
        super.onStop()
    }

    private lateinit var mHandler: IncomingMessageHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHandler = IncomingMessageHandler(this)
        btn_schedule_job.setOnClickListener {
            scheduleJob()
        }
    }

    private var mJobId = 0

    private val mServiceComponent by lazy {
        ComponentName(this, MyJobService::class.java)
    }

    private fun scheduleJob() {
        val builder = JobInfo.Builder(mJobId++, mServiceComponent)

    }

    private class IncomingMessageHandler(activity: MainActivity) : Handler() {

    }
}
