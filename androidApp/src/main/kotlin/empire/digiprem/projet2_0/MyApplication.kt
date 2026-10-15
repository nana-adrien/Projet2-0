package empire.digiprem.projet2_0

import android.app.Application
import empire.digiprem.projet2_0.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application(){

    override fun onCreate() {
        initKoin {
            androidContext(this@MyApplication)
        }
        super.onCreate()
    }
}