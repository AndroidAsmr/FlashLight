package com.hadirahimi.flashlight

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.hadirahimi.flashlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    //binding
    private lateinit var binding : ActivityMainBinding
    
    //Other
    private lateinit var cameraManager : CameraManager
    
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        //setup Camera Service
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        
        //App Title Design
        setupAppName()
        
        //initViews
        binding.apply{
            // Check if the device has a flashlight
            val hasFlashLight = applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            
            if (! hasFlashLight)
            {
                // Show an error message if the device does not have a flashlight
                light.isEnabled = false
                Toast.makeText(this@MainActivity , "this device does not have a flashLight" , Toast.LENGTH_SHORT).show()
            }
            //animation listener
            layoutMain.setTransitionListener(object : MotionLayout.TransitionListener{
                override fun onTransitionStarted(motionLayout : MotionLayout? , startId : Int , endId : Int) {}
                
                override fun onTransitionChange(motionLayout : MotionLayout? , startId : Int , endId : Int , progress : Float) {}
    
                override fun onTransitionCompleted(motionLayout : MotionLayout? , currentId : Int)
                {
                    if (currentId == motionLayout!!.endState)
                    {
                        try
                        {
                            val cameraId = cameraManager.cameraIdList[0]
                            cameraManager.setTorchMode(cameraId,true)
                        }catch (e : CameraAccessException)
                        {
                            e.printStackTrace()
                        }catch (e:ArrayIndexOutOfBoundsException)
                        {
                            e.printStackTrace()
                        }
                    }
                    else{
                        try
                        {
                            val cameraId = cameraManager.cameraIdList[0]
                            cameraManager.setTorchMode(cameraId,false )
                        }catch (e : CameraAccessException)
                        { e.printStackTrace() }
                        catch (e:ArrayIndexOutOfBoundsException)
                        { e.printStackTrace() }
                    }
                }
    
                override fun onTransitionTrigger(motionLayout : MotionLayout? , triggerId : Int , positive : Boolean , progress : Float) {}
    
            })
            
            
        }
    }
    private fun setupAppName()
    {
     val spannableString = SpannableString(getString(R.string.flashlight))
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this,R.color.clr_green))
        spannableString.setSpan(colorSpan,5,10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.title.text = spannableString
    }
    
    
    
    
    
    
    
    
    
    
    
}