package com.example.mypermissionapplication

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

//https://github.com/android/platform-samples/blob/main/samples/storage/src/main/java/com/example/platform/storage/mediastore/SelectedPhotosAccess.kt#L62
class MainActivity : AppCompatActivity() {
    private var showRationale = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Register ActivityResult handler
        val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            // Handle permission requests results
            // See the permission example in the Android platform samples: https://github.com/android/platform-samples
            println(results)
            if (results[READ_MEDIA_IMAGES] == true){
                println("READ_MEDIA_IMAGES == true")
            }else{

            }
        }

        getPermission(requestPermissions)


    }

    private fun getPermission(requestPermissions: ActivityResultLauncher<Array<String>>) {
        if(checkSelfPermission(READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "READ_MEDIA_IMAGES is granted", Toast.LENGTH_SHORT).show()
        }else if(shouldShowRequestPermissionRationale(READ_MEDIA_IMAGES)) {
            println("SHOULD")
//            if(showRationale){
//                Toast.makeText(this, "иди в настройки", Toast.LENGTH_SHORT).show()
//            }
//            else {
            AlertDialog.Builder(this)
                .setMessage("дай разрешение")
                .setPositiveButton("OK") { dialog, _ ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        requestPermissions.launch(
                            arrayOf(CAMERA,
                                READ_MEDIA_IMAGES,
                                READ_MEDIA_VIDEO,
                             //   READ_MEDIA_VISUAL_USER_SELECTED
                            )
                        )
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
                    } else {
                        requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
                    }
                    showRationale = true
                    dialog.dismiss()
                }
                .setNegativeButton("cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
//            }
        }else if(showRationale){
            Toast.makeText(this, "иди в настройки", Toast.LENGTH_SHORT).show()
        }else{
            println("ELSE")
// Permission request logic
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                requestPermissions.launch(arrayOf(CAMERA, READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))//, READ_MEDIA_VISUAL_USER_SELECTED))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
            } else {
                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
            }
        }
    }
}