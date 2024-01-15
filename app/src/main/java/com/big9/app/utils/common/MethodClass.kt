package com.big9.app.utils.common

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.big9.app.R
import com.big9.app.ui.activity.RegActivity
import com.big9.app.utils.helpers.SharedPreff
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONObject
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Enumeration
import java.util.Locale


object MethodClass {
    fun custom_loader(ctx: Context?, msg: String?): Dialog? {
        var dialog: Dialog? = null
        ctx?.let {
            dialog = Dialog(it)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog?.setCancelable(false)
            dialog?.setContentView(R.layout.custom_loader)

            val text = dialog?.findViewById<View>(R.id.text_dialog) as TextView
            text.text = msg
        }
        return dialog



    }
    fun appVersion(context:Context): String {


        val packageManager = context.packageManager
        val packageName = context.packageName

        var myVersionName: String = "0.0.0" // initialize String


        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return myVersionName
    }
 fun get_error_method(err: ResponseBody): String {
         val jsonErr = JSONObject(err.string())
         val gson = Gson()
         val type = object : TypeToken<String>() {}.type
         val errorResponse: String? = gson.fromJson(jsonErr.toString(), type)
         return "".toString()
     }
    fun check_networkconnection(ctx: Context?): Boolean {
        var isConnected = false
        ctx?.let {
            val connectivityManager: ConnectivityManager? =
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetwork = connectivityManager?.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            isConnected = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        return isConnected

    }

    fun checkNetworkConnection(context: Context?): Boolean {
        var isConnected = false

        context?.let {
            val connectivityManager =
                it.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager?.activeNetwork
                val networkCapabilities = connectivityManager?.getNetworkCapabilities(network)

                isConnected = networkCapabilities?.run {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                } ?: false
            } else {
                val activeNetworkInfo = connectivityManager?.activeNetworkInfo
                isConnected = activeNetworkInfo?.isConnectedOrConnecting == true
            }
        }

        return isConnected
    }
    fun Context.userLogout() {
        SharedPreff(this).clearUserData()
        val intent = Intent(this, RegActivity::class.java)
        intent.putExtra("isAlreadyLogin",true)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        this.startActivity(intent)
        // Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show()
    }

    fun Context.appUpdate() {
        val appPackageName = "com.big9.app"
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }

    }



    @SuppressLint("HardwareIds")
    fun deviceUid(context: Context): String {

        //String device_uid = tManager.getDeviceId() != null ? tManager.getDeviceId() : "123";
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getLocalIPAddress(): String {
        try {
            val interfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface: NetworkInterface = interfaces.nextElement()
                val addresses: Enumeration<InetAddress> = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address: InetAddress = addresses.nextElement()
                    if (!address.isLoopbackAddress) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "Unable to retrieve IP address"
    }


    fun getCurrentTimestamp(): String {
        val calendar = Calendar.getInstance()

        // Format for the date and time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Extracting components
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Month is zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        // Creating a formatted date and time string
        val formattedDateTime = dateFormat.format(calendar.time)

        // Example: Print the current date and time components
        println("Year: $year")
        println("Month: $month")
        println("Day: $day")
        println("Hour: $hour")
        println("Minute: $minute")
        println("Second: $second")

        // Example: Print the formatted date and time
        println("Formatted Date and Time: $formattedDateTime")

        return formattedDateTime
    }

    fun getCurrentDateTime(): String {
        val dateFormat = "yyyy-MM-dd HH:mm:ss"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        return formatter.format(Date())
    }
}



