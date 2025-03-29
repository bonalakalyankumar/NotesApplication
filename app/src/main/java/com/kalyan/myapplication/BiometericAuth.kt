package com.kalyan.noteapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.app.KeyguardManager
import android.content.Context
import android.util.Log
import android.widget.Button
import java.util.concurrent.Executor
import com.kalyan.myapplication.MainActivity
import com.kalyan.myapplication.R
import com.kalyan.myapplication.SplashActivity

class BiometericAuth : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_biometric)

        var unlockButton:Button = findViewById(R.id.unlockButton)

        unlockButton.setOnClickListener(){
            if (isBiometricAvailable()) {
                authenticateUser()
            } else {
                promptPatternPinPassword()
            }
        }


            if (isBiometricAvailable()) {
                authenticateUser()
            } else {
                promptPatternPinPassword()
            }
    }

    private fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // No biometrics enrolled, ask the user to enroll
                false
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                // No biometric hardware available on the device
                false
            }

            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Biometric authentication is available
                true
            }

            else -> false
        }
    }

    private fun authenticateUser() {
        val executor: Executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    proceedToMainActivity()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)

                    Log.d("BiometricAuth", "Authentication error: $errorCode")

                    if(errString.toString() == "Use Pattern"){
                        promptPatternPinPassword()
                    }
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in to your account")
            .setNegativeButtonText("Use Pattern")
            .build()

        biometricPrompt.authenticate(promptInfo)

    }


    private fun promptPatternPinPassword() {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val intent = keyguardManager.createConfirmDeviceCredentialIntent(
            "Authentication Required",
            "Please authenticate with your pattern or PIN."
        )

        if (intent != null) {
            startActivityForResult(intent, 1)
        } else {
            Toast.makeText(this, "Pattern, PIN or Password not set up", Toast.LENGTH_SHORT)
                .show()
            proceedToMainActivity() // Proceed to MainActivity if no PIN/pattern is set
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            proceedToMainActivity()
        } else {
            Toast.makeText(this, "Authentication failed or canceled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun proceedToMainActivity() {
            val intent = Intent(this, SplashActivity::class.java)
            overridePendingTransition(0, android.R.anim.fade_out)
            startActivity(intent)
            finish()
    }
}
