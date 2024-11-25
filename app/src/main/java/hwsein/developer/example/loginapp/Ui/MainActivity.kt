package hwsein.developer.example.loginapp.Ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hwsein.developer.example.loginapp.MVP.model.ModelMainActivity
import hwsein.developer.example.loginapp.MVP.presenter.PresenterMainActivity
import hwsein.developer.example.loginapp.MVP.view.ViewMainActivity
import hwsein.developer.example.loginapp.R
import hwsein.developer.example.loginapp.ext.Utils

class MainActivity : AppCompatActivity() , Utils {
    private lateinit var presenter : PresenterMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ViewMainActivity(this , this)
        setContentView(view.binding.root)

        presenter = PresenterMainActivity(view , ModelMainActivity(this))
        presenter.onCreate2()

    }

    override fun onDestroy2() {
        super.onDestroy()
        presenter.onDestroy2()
    }

    override fun setFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container , fragment)
            .commit()

    }

}