package com.impeccthreads.cgfamilyrecipebook.activity.useraccount

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.activity.HomeActivity
import com.impeccthreads.cgfamilyrecipebook.application.AppCacheManager
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.application.Constants
import com.impeccthreads.cgfamilyrecipebook.application.Family
import com.impeccthreads.cgfamilyrecipebook.dto.User
import com.impeccthreads.cgfamilyrecipebook.utility.PreferenceHelper
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class LoginActivity : BaseActivity() {

    companion object {
        private const val TAG = "PhoneAuthActivity"
        private const val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"
        private const val STATE_INITIALIZED = 1
        private const val STATE_VERIFY_FAILED = 3
        private const val STATE_VERIFY_SUCCESS = 4
        private const val STATE_CODE_SENT = 2
        private const val STATE_SIGNIN_FAILED = 5
        private const val STATE_SIGNIN_SUCCESS = 6
    }

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var verificationInProgress = false
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

       PreferenceHelper.isUserRegistered().let {
            if (it!!)
            {
                AppCacheManager.currentUser = PreferenceHelper.getCurrentUserDetails()
                startActivity(Intent(this, HomeActivity::class.java))
                return
            }
        }

        // [START initialize_auth]
        // Initialize Firebase Auth
//        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                // [START_EXCLUDE silent]
                verificationInProgress = false
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential)
                // [END_EXCLUDE]

//                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                // [START_EXCLUDE silent]
                verificationInProgress = false
                // [END_EXCLUDE]

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    editTextMobile.error = "Invalid phone number."
                    // [END_EXCLUDE]
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    showToast("Quota exceeded.")
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED)
                // [END_EXCLUDE]
            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId!!)

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT)
                // [END_EXCLUDE]
            }
        }
        // [END phone_auth_callbacks]
    }

    fun showProgressBar() {
        loadingpanelmask_layout.visibility = View.VISIBLE
        loadingPanel.visibility = View.VISIBLE
        /* Ion.with(blowingflame_img)
                .error(R.drawable.progressbar_custom)
                .animateGif(AnimateGifMode.ANIMATE)
                .load("file:///android_asset/fire1.gif"); */
    }

    fun hideProgressBar() {
        loadingpanelmask_layout.visibility = View.GONE
        loadingPanel.visibility = View.GONE
    }

    fun isRegisterValidationSuccess(): Boolean {

        if (editTextName.text.isEmpty())
        {
            this.showToast(Constants.AlertMessages.enterName)
            return false
        }

        if (editTextMobile.text.isEmpty())
        {
            this.showToast(Constants.AlertMessages.enterMobile)
            return false
        }
        else if (editTextMobile.text.count() != 10)
        {
            this.showToast(Constants.AlertMessages.enterValidMobile)
            return false
        }


        return true
    }

    fun isVerifyValidationSuccess(): Boolean {

        if (editTextVerificationCode.text.isEmpty())
        {
            this.showToast(Constants.AlertMessages.enterName)
        }

        return true
    }

    fun generateUserDetails(): User {

        val user = User()
        user.username = editTextName.text.toString()
        user.mobileno = editTextMobile.text.toString()
        user.family = Family.cgfamily.toString()

        return user
    }


    fun registerOnClickLogin(view: View) {

        if (isRegisterValidationSuccess())
        {
            val allowedUser = Constants.getAllowedUser()

            val registeredUserList = allowedUser.filter { it.mobileno == editTextMobile.text.toString() }

            if (registeredUserList.count() > 0)
            {
                PreferenceHelper.setIsUserRegistered(true)

                val user = generateUserDetails()
                PreferenceHelper.setCurrentUserDetails(user)
                AppCacheManager.currentUser = user

                startActivity(Intent(this, HomeActivity::class.java))
                showToast("User logged in successfully")
            }
            else
            {
                showToast("User not registered")
            }

//            editTextVerificationCode.hint = "Enter OTP send to " + editTextMobile.text.toString()
//            showProgressBar()
//            startPhoneNumberVerification(editTextMobile.text.toString())
        }
    }

    fun resendCodeOnClick(view: View) {

        editTextVerificationCode.setText("")

        resendVerificationCode(editTextMobile.text.toString(), resendToken)
    }

    fun verifyCodeOnClick(view: View) {

        if (isVerifyValidationSuccess())
        {
            showProgressBar()
            verifyPhoneNumberWithCode(storedVerificationId, editTextVerificationCode.text.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, verificationInProgress)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        verificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS)
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,      // Phone number to verify
                60,               // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this,             // Activity (for callback binding)
                callbacks) // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        verificationInProgress = true
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    // [START resend_verification]
    private fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this, // Activity (for callback binding)
                callbacks, // OnVerificationStateChangedCallbacks
                token) // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")

                        val user = task.result!!.user
                        // [START_EXCLUDE]
                        updateUI(STATE_SIGNIN_SUCCESS, user)
                        // [END_EXCLUDE]
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            // [START_EXCLUDE silent]
                            editTextVerificationCode.error = "Invalid code."
                            // [END_EXCLUDE]
                        }
                        // [START_EXCLUDE silent]
                        // Update UI
                        updateUI(STATE_SIGNIN_FAILED)
                        // [END_EXCLUDE]
                    }
                }
    }
    // [END sign_in_with_phone]

    private fun signOut() {
        auth.signOut()
        updateUI(STATE_INITIALIZED)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null)
        {
            updateUI(STATE_SIGNIN_SUCCESS, user)
        }
        else
        {
            updateUI(STATE_INITIALIZED)
        }
    }

    private fun updateUI(uiState: Int, cred: PhoneAuthCredential) {
        updateUI(uiState, null, cred)
    }

    private fun updateUI(uiState: Int, user: FirebaseUser? = auth.currentUser, cred: PhoneAuthCredential? = null) {

        hideProgressBar()

        when (uiState) {
            STATE_INITIALIZED -> {
                // Initialized state, show only the phone number field and start button

            }
            STATE_CODE_SENT -> {
                // Code sent state, show the verification field, the
                constraintLayoutRegister.visibility = View.GONE
                constraintLayoutOTPVerification.visibility = View.VISIBLE
            }
            STATE_VERIFY_FAILED -> {
                // Verification has failed, show all options

            }
            STATE_VERIFY_SUCCESS -> {
                // Verification has succeeded, proceed to firebase sign in

                // Set the verification text based on the credential
                if (cred != null)
                {
                    if (cred.smsCode != null)
                    {
                        editTextVerificationCode.setText(cred.smsCode)
                    }

                }
            }
            STATE_SIGNIN_FAILED -> {
                // No-op, handled by sign-in check

            }
            STATE_SIGNIN_SUCCESS -> {

                PreferenceHelper.setIsUserRegistered(true)
//                PreferenceHelper.setUserMobileNo(editTextMobile.text.toString())

//                val user = User()
//                user.name = editTextName.text.toString()
//                user.mobile = editTextMobile.text.toString()
//
//                PreferenceHelper.setCurrentUserDetails(user)

                startActivity(Intent(this, HomeActivity::class.java))
            }
        } // Np-op, handled by sign-in check
    }

}


