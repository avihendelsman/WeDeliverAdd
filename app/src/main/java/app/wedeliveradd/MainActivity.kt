package app.wedeliveradd

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.getSystemService
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {

    // List for dropdown menu of package types
    val TypesList = listOf("Envelop", "Small Package", "Large Package")

    // Variables for controls
    lateinit var deliveryType: AutoCompleteTextView
    lateinit var sendButten: Button

    // reference to the repository
    var repository: Repository = Repository()

    // Notification
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            val notificationManager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()


        //findView
        deliveryType = findViewById<AutoCompleteTextView>(R.id.addPkgTextPackageTypes)
        val adapter: ArrayAdapter<*> =
            ArrayAdapter(applicationContext, R.layout.package_type_dropdown, TypesList)
        (deliveryType as? AutoCompleteTextView)?.setAdapter(adapter)

        // cancel the error if package changed
        deliveryType.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                deliveryType.error = null
            }
        })

        sendButten = findViewById<Button>(R.id.addPkgButtonSend)

        sendButten.setOnClickListener {
            try {
                var flagIsEmpty : Boolean = false
                // find view
                var pkgName = findViewById<TextInputEditText>(R.id.addPkgTextAddresseeName).text.toString()
                var pkgAddress = findViewById<TextInputEditText>(R.id.addPkgTextAddresseeAddress).text.toString()
                var pkgType = deliveryType.text.toString()
                var pkgWeight = findViewById<TextInputEditText>(R.id.addPkgTextWeight).text.toString()
                var isFragileCB = findViewById<CheckBox>(R.id.addPkgCBisFragile).isChecked
                var lang = findViewById<TextInputEditText>(R.id.addPkgTextLongitude).text.toString()
                var lat = findViewById<TextInputEditText>(R.id.addPkgTextLatitude).text.toString()

                // Validate there is not empty fields
                if(pkgName == ""){
                    findViewById<TextInputEditText>(R.id.addPkgTextAddresseeName).
                    error = getString(R.string.enterValue)
                    flagIsEmpty = true
                }
                if(pkgAddress == ""){
                    findViewById<TextInputEditText>(R.id.addPkgTextAddresseeAddress).
                    error = getString(R.string.enterValue)
                    flagIsEmpty = true
                }
                if(pkgType == ""){
                    deliveryType.error = getString(R.string.selectType)
                    flagIsEmpty = true
                }
                if(pkgWeight == ""){
                    findViewById<TextInputEditText>(R.id.addPkgTextWeight).
                    error = getString(R.string.enterValue)
                    flagIsEmpty = true
                }
                if(lang == ""){
                    findViewById<TextInputEditText>(R.id.addPkgTextLongitude).
                    error = getString(R.string.enterValue)
                    flagIsEmpty = true
                }
                if(lat == ""){
                    findViewById<TextInputEditText>(R.id.addPkgTextLatitude).
                    error = getString(R.string.enterValue)
                    flagIsEmpty = true
                }
                if(flagIsEmpty){
                    return@setOnClickListener
                }
                // Validate correction weight
                var calcWeight:String =  calculateWeight(pkgWeight,pkgType)
                if (calcWeight != "") {
                    Toast.makeText(this, calcWeight, Toast.LENGTH_SHORT).show()
                    deliveryType.error = getString(R.string.ChangePackage)
                    return@setOnClickListener
                }

                repository.addPackage(
                    Package(
                        convertPackageTypeToEnum(pkgType),
                        isFragileCB,
                        pkgWeight.toDouble(),
                        Coordinate(lang.toDouble(), lat.toDouble()),
                        pkgName,
                        pkgAddress,
                        PackageStatusEnum.READY
                    )
                )

                val positiveButtonClick = {dialog: DialogInterface,which:Int ->
                    val intent = intent
                    finish()
                    startActivity(intent)
                }

                val alertSuccessBuilder = AlertDialog.Builder(this)
                alertSuccessBuilder.setTitle("Package added")
                    .setMessage("The package was added successfully!")
                    .setIcon(R.drawable.ic_baseline_done_25)
                    .setPositiveButton("OK", DialogInterface.OnClickListener(positiveButtonClick))
                    .setCancelable(false)
                val alertDialog = alertSuccessBuilder.create()
                alertDialog.show()

            }
            catch (E: Exception) {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("ERROR")
                    .setMessage("We apologize but an error occurred while trying to send the information\n\n$E")
                    .setIcon(R.drawable.ic_baseline_error_24)
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }

        }
    }

    fun calculateWeight(pkgWeight: String, pkgType: String):String {
        var weight: Double = pkgWeight.toDouble()
        var type: PackageTypesEnum = convertPackageTypeToEnum(pkgType)

        if (type == PackageTypesEnum.ENVELOPE && weight > 0.5)
            return "An envelope can weigh no more than 500 grams"
        else if(type == PackageTypesEnum.SMALL_PACKAGE && weight > 3.0)
            return "An small package can weigh no more than 3 kg"
        else if(weight <= 0.0)
            return "The package can not weigh less than zero"
        return ""
    }


    fun convertPackageTypeToEnum(packageType: String): PackageTypesEnum {
        return when (packageType) {
            "Envelop" -> PackageTypesEnum.ENVELOPE
            "Small Package" -> PackageTypesEnum.SMALL_PACKAGE
            "Large Package" -> PackageTypesEnum.LARG_PACKAGE
            else -> PackageTypesEnum.ENVELOPE
        }
    }
}