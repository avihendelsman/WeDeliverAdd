package app.wedeliveradd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PickPackageReciver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val isPackagePicked = intent?.getBooleanExtra("state",false) ?: return
        if(isPackagePicked){

        }
    }
}