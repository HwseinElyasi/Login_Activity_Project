package hwsein.developer.example.loginapp.MVP.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import hwsein.developer.example.loginapp.Fragment.HomeFragment
import hwsein.developer.example.loginapp.databinding.ActivityMainBinding
import hwsein.developer.example.loginapp.ext.Utils
import hwsein.developer.example.loginapp.remote.model.ApiModel
import hwsein.developer.example.loginapp.remote.ext.ErrorUtils
import hwsein.developer.example.loginapp.remote.retrofitService.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error

class ViewMainActivity(
    private val context: Context,
    private val utils: Utils
) {

    var checkTimer = false
    private lateinit var countDownTimer: CountDownTimer

    val binding = ActivityMainBinding.inflate(LayoutInflater.from(context))

    init {

        if (getEncryptShared()){

            binding.fragmentContainer.visibility = View.VISIBLE
            utils.setFragment(HomeFragment())

        }

    }

    fun initEmail() {

        binding.btnSend.setOnClickListener {

            val email = binding.edtInputEmail.text.toString()

            checkedEmail(email)


        }

        binding.txtWrong.setOnClickListener {

            clickInWrongEmail()

        }

    }

    fun checkCode(androidId: String) {

        binding.btnConfirm.setOnClickListener {

            val email = binding.edtInputEmail.text.toString()
            val code = binding.edtCode.text.toString()

            val retrofit = RetrofitService.apiService

            CoroutineScope(Dispatchers.IO).launch {

                val response = retrofit.verifyCode(androidId, code, email)

                if (response.isSuccessful) {

                    saveDataSecurity(email, code, androidId)

                    launch(Dispatchers.Main) {

                        val data = response.body() as ApiModel
                        showToast("Welcome")
                        binding.fragmentContainer.visibility = View.VISIBLE
                        utils.setFragment(HomeFragment())

                    }
                } else
                    launch(Dispatchers.Main) {

                        showToast(ErrorUtils.getError(response))

                    }

            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun onClickHandler(email: String) {


        binding.btnSend.visibility = View.INVISIBLE
        binding.textInputEmail.visibility = View.INVISIBLE
        binding.edtInputEmail.visibility = View.INVISIBLE

        binding.txtSendEmail.visibility = View.VISIBLE
        binding.txtSendEmail.text = "send Email in $email"
        binding.edtCode.visibility = View.VISIBLE
        binding.textInputCode.visibility = View.VISIBLE
        binding.btnConfirm.visibility = View.VISIBLE
        binding.txtWrong.visibility = View.VISIBLE


    }

    private fun clickInWrongEmail() {

        binding.btnSend.visibility = View.VISIBLE
        binding.textInputEmail.visibility = View.VISIBLE
        binding.edtInputEmail.visibility = View.VISIBLE

        binding.txtSendEmail.visibility = View.INVISIBLE
        binding.edtCode.visibility = View.INVISIBLE
        binding.textInputCode.visibility = View.INVISIBLE
        binding.btnConfirm.visibility = View.INVISIBLE
        binding.txtWrong.visibility = View.INVISIBLE


    }

    private fun checkedEmail(email: String) {

        if (email.isEmpty()) {
            binding.textInputEmail.error = "Email Empty"

            if (email.length < 6)
                binding.textInputEmail.error = "Email < 6"
            else
                binding.textInputEmail.error = null

        } else {

            binding.textInputEmail.error = null

            onClickHandler(email)
            sendEmailInServer(email)

            if (!checkTimer)
                timer(120)

        }

    }

    private fun sendEmailInServer(email: String) {

        val retrofitService = RetrofitService.apiService

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val response = retrofitService.sendEmail(email)

                if (response.isSuccessful) {

                    launch(Dispatchers.Main) {

                        val data = response.body() as ApiModel

                    }

                } else

                    launch(Dispatchers.Main) {

                        showToast(ErrorUtils.getError(response))

                    }

            } catch (error: Exception) {

                Log.i("ERROR_SERVER", error.message.toString())

            }
        }

    }

    private fun timer(seconds: Int) {
        checkTimer = true
        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000L) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {

                val minutes = (millisUntilFinished / 1000) / 60
                val second = (millisUntilFinished / 1000) % 60
                binding.txtTimer.text = "%02d:%02d".format(minutes, second)
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {

                binding.txtTimer.text = "00:00"
                binding.edtInputEmail.isEnabled = true
                checkTimer = false

            }


        }

        countDownTimer.start()

    }

    fun cancelInTimer() {

        countDownTimer.cancel()

    }

    private fun saveDataSecurity(email: String, code: String, androidId: String) {

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val shared = EncryptedSharedPreferences.create(
            "sharedPreferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val edit = shared.edit()
        edit.putString("email", email)
        edit.putString("code", code)
        edit.putString("androidId", androidId)
        edit.putBoolean("state", true)
        edit.apply()

    }

    fun getEncryptShared(): Boolean {

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val shared = EncryptedSharedPreferences.create(
            "sharedPreferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )


        return shared.getBoolean("state" , false)
    }

    private fun showToast(text: String) = Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

}