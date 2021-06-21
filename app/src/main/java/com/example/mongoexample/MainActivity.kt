package com.example.mongoexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mongoexample.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.realm.mongodb.Credentials

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var client: GoogleSignInClient
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestServerAuthCode(BuildConfig.GOOGLE_CREDENTIAL)
            .build()

        client = GoogleSignIn.getClient(this, googleSignInOptions)
        binding.googleSignIn.setOnClickListener {
            signIn()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            handleSignInResult(account!!)
        }
    }

    private fun handleSignInResult(account: GoogleSignInAccount?){
        try{
            Log.d("MainActivity","${account?.serverAuthCode}")
            val idToken = account?.serverAuthCode
            val googleCredentials = Credentials.google(idToken)
            app.loginAsync(googleCredentials){ result ->
                if(result.isSuccess){
                    Log.d("MainActivity", "Successfully authenticated using Google OAuth")
                    startActivity(Intent(this, HomeActivity::class.java))
                }else{
                    Log.d("MainActivity", "Failed to Log in to MongoDB Realm: ${result.error.errorMessage}")
                }
            }
        }catch (e : ApiException){
            Log.d("MainActivity",  e.printStackTrace().toString())
        }
    }

    private fun signIn(){
        val signIntent = client.signInIntent
        startActivityForResult(signIntent, RC_SIGN_IN)
    }
}