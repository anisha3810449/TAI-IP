package com.example.flashlight_app

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    lateinit var torch : ImageButton
    private var state = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        torch = findViewById(R.id.torch)

        Dexter.withContext(this).withPermission(android.Manifest.permission.CAMERA).withListener(object:PermissionListener{
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                runFlashLight()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(applicationContext, "Camera permission required", Toast.LENGTH_LONG).show()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                TODO("Not yet implemented")
            }

        }).check()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun runFlashLight() {
        torch.setOnClickListener {
            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                val cameraId = cameraManager.cameraIdList[0]
                if (!state) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, true)
                    }
                    state = true
                    torch.setImageResource(R.drawable.on)
                    Toast.makeText(applicationContext, "Flashlight on", Toast.LENGTH_SHORT).show()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, false)
                    }
                    state = false
                    torch.setImageResource(R.drawable.off)
                    Toast.makeText(applicationContext, "Flashlight off", Toast.LENGTH_SHORT).show()

                }
            } catch (e: CameraAccessException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Cannot access camera", Toast.LENGTH_SHORT).show()
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Camera not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}