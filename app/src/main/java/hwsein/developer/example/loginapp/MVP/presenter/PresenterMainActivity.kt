package hwsein.developer.example.loginapp.MVP.presenter

import hwsein.developer.example.loginapp.MVP.model.ModelMainActivity
import hwsein.developer.example.loginapp.MVP.view.ViewMainActivity
import hwsein.developer.example.loginapp.ext.Utils

class PresenterMainActivity(
    private val view : ViewMainActivity,
    private val model : ModelMainActivity
) : Utils {

    override fun onCreate2(){

        initAndSendEmail()
        confirmCode()

    }

    override fun onDestroy2() {

        view.cancelInTimer()

    }

    private fun initAndSendEmail(){

        view.initEmail()

    }

    fun confirmCode(){

        view.checkCode(model.getAndroidId())

    }



}