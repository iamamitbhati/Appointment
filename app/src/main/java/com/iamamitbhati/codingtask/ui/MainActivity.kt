package com.iamamitbhati.codingtask.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iamamitbhati.codingtask.R
import com.iamamitbhati.codingtask.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val appointmentFrag = FragmentAppointment()
        val fragmentTrasaction = supportFragmentManager.beginTransaction()
        fragmentTrasaction.replace(R.id.fragment_container_view, appointmentFrag)
        fragmentTrasaction.commit()
    }
}