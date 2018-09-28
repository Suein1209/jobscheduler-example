package com.example.we.jobscheduler_ex

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import com.example.we.jobscheduler_ex.MainActivity.Companion.MESSENGER_INTENT_KEY

class MyJobService : JobService() {

    private val TAG = MainActivity::class.java.simpleName

    private var mMessenger: Messenger? = null

    /**
     * START_STICKY : Service가 강제 종료되었을 경우 시스템이 다시 Service를 재시작 시켜 주지만 intent 값을 null로
     * START_NOT_STICKY : 강제로 종료 된 Service가 재시작 하지 않는다.
     * START_REDELIVER_INTENT : START_STICKY와 마찬가지로 Service가 종료 되었을 경우 시스템이 다시 Service를 재시작 시켜 주지만 intent 값을 그대로 유지
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mMessenger = intent?.getParcelableExtra(MESSENGER_INTENT_KEY)
        return Service.START_NOT_STICKY
    }

    /**
     * 스케줄러 시작시 호출
     * Job이 종료되기 이전에 취소될 필요가 있을 경우 시스템에 의해 호출
     */
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.e(TAG, "= onStartJob =")
        //thread와 같이 메소드 종료후 작업이 지속되면 return true
        //지속되는 작업이 없을 경우 return false

        // Uses a handler to delay the execution of jobFinished().

        params?.let {
            val handler = Handler()
            handler.postDelayed({
                sendMessage(params.jobId)
                jobFinished(params, false)
            }, 3000)
        }
        return true
    }

    /**
     * 스케줄러 종료시 호출
     */
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.e(TAG, "= onStartJob =")
        //갑작스러운 중지로 현재 실행하던 Job을 다시 스케쥴러에 등록하여 다음 기회에 실행할 필요가 있다면 return true
        // 이 작업이 스케쥴링되는 것을 방지할려면 return false
        return false
    }

    private fun sendMessage(params: Any?) {
        mMessenger?.let {
            val m = Message.obtain()
            m.obj = params
            try {
                it.send(m)
            } catch (e: RemoteException) {
                Log.e(TAG, "Error passing service object back to activity.")
            }
        }
    }
}