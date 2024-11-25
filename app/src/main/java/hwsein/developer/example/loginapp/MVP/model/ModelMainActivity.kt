package hwsein.developer.example.loginapp.MVP.model

import android.content.Context
import hwsein.developer.example.loginapp.remote.ext.DeviceId

class ModelMainActivity(
    private val context : Context
) {

    fun getAndroidId() = DeviceId.getAndroidId(context)

}