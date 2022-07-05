package com.datastructures.pairingtask.UI

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.datastructures.pairingtask.R
import com.datastructures.pairingtask.database.UsersDatabase
import com.datastructures.pairingtask.pojo.User
import kotlinx.coroutines.runBlocking


class LoginFragment : Fragment(R.layout.fragment_login) {

    //Declarations
    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var userDB: UsersDatabase
    private lateinit var login: Button
    private lateinit var progressBar : ProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initializations
        userDB = UsersDatabase.getInstance(context)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        login = view.findViewById(R.id.login_button)

        //Login handling
        login.setOnClickListener{

            //Validating fields
            if(checkCredentials(username,password)){

                //verifying login with main UI blocked by spinner
                showProgressBar()

                runBlocking{
                    //getting password of entered username
                    val realPassword = userDB.usersDAO().getPasswordByUsername(username.text.toString())

                    //case of matched credentials
                    if (realPassword == password.text.toString()){
                        progressBar.hide()
                        navigateToScan()
                    }
                    //wrong or new credentials
                    else{
                        //case of new user
                        try {
                            userDB.usersDAO().insertUser(
                                User(
                                    username.text.toString(),
                                    password.text.toString()
                                )
                            )
                            navigateToScan()
                        }
                        //case of wrong password
                        catch (e:Throwable){
                            password.error = "Wrong password"
                        }
                    }
                }
                //freeing UI
                progressBar.hide()
            }
        }

    }

    //Replace login fragment with scan
    private fun navigateToScan() {
        val fragment: Fragment = ScanFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragments, fragment)
        fragmentTransaction.commit()
    }


    //initializing progress bar
    private fun showProgressBar() {
        progressBar = ProgressDialog(context)
        progressBar.setMessage("Logging you in...")
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressBar.progress = 0
        progressBar.max = 100
        progressBar.show()
    }

    //Checking credentials with error setting
    private fun checkCredentials(et1:EditText , et2:EditText) : Boolean{
        return if (TextUtils.isEmpty(et1.text)){
            et1.error = "Username required"
            false
        }else{
            if (TextUtils.isEmpty(et2.text)){
                et2.error = "Password required"
                false
            }else{
                true
            }
        }
    }
}