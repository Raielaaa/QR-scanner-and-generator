package com.example.qrscannerpractice2

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.qrscannerpractice2.databinding.ActivityMainBinding
import com.example.qrscannerpractice2.qrdb.DBViewModel
import com.example.qrscannerpractice2.qrdb.DBViewModelFactory
import com.example.qrscannerpractice2.qrdb.Database
import com.example.qrscannerpractice2.qrdb.Entity
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    // ActivityMainBinding let us interacts with the views directly using
    // the binding variable
    private lateinit var binding: ActivityMainBinding

    // For database
    private lateinit var viewModel: DBViewModel

    // For QR code
    private var imageBitmap: Bitmap? = null

    /**
     * @return Unit
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initializing the binding object
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Referencing the binding object to activity_main layout
        setContentView(binding.root)

        // Generating the QR code to be displayed on the view
        // with a QR value basing on the arguments provided
        QRCode.qrCodeGenerator("Hello3 & World3", binding.ivQRCode)
        // Creates a ViewModel instance in an Android activity.
        // It uses the ViewModelProvider to get the ViewModel, passing in the activity context and a factory to create the ViewModel.
        // The factory uses an instance of the Database class to create a DAO (data access object), which is then passed to the DBViewModel constructor,
        // then the resulting ViewModel instance is assigned to the "viewModel" variable.
        viewModel = ViewModelProvider(this@MainActivity, DBViewModelFactory(Database.getInstance(applicationContext).dao()))[DBViewModel::class.java]

        binding.apply {
            captureImage.setOnClickListener {
                // Prompting the user for camera permission. Camera permission is used so that we can use
                // the camera of the phone of the user
                cameraPermissions()
                // After getting the permission (assuming that the user clicked the "Allow" button on the prompt). A function
                // will automatically be implemented, this function is used for launching the phone camera of the user to
                // capture an image.
                takeImage()
            }
            detectScan.setOnClickListener {
                // Detects then scan the QR code taken by the user then extracts the author and book name out of it
                detectImage()
            }
        }
    }

    /**
     * This code defines a private function called "insertEntity" that takes two parameters: "bookAuthor" and "bookName".
     * It creates an "Entity" object with an id of 0, the provided book author and book name, and passes it to a "insertEntityFromVM" function inside a "viewModel" object.
     * The purpose of this code is to insert a new entity (book) into the system with the provided author and name.
     * @return Unit
     */
    private fun insertEntity(bookAuthor: String, bookName: String) {
        viewModel.insertEntityFromVM(
            Entity(0, bookAuthor, bookName)
        )
    }

    /**
     * This function requests the user's permission to access the camera and other sensitive device features like location and phone state.
     * This function also creates an array of required permissions and uses the requestPermissions method to ask for them with a specific request code.
     * The purpose of this function is to ensure that the app has the necessary permissions to use the camera and other features before attempting to use them.
     * @return Unit
     */
    private fun cameraPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.SYSTEM_ALERT_WINDOW,
            android.Manifest.permission.CAMERA
        )
        // When you request a permission from the user using "requestPermissions()" method, you can specify a request code as the second argument.
        // The purpose of the request code is to identify the permission request when the result of the request is returned
        // to the activity or fragment that initiated the request.
        // Note that the permission code can be any number, but it's a good practice to generate a unique request code for each permission request.
        val cameraPermissionRequestCode = 111
        requestPermissions(permissions, cameraPermissionRequestCode)
    }

    /**
     * This function first creates an intent using the "MediaStore.ACTION_IMAGE_CAPTURE" action, which is a pre-defined action
     * to capture an image using the device's camera. It then attempts to start the camera application by calling "startActivityForResult()" with the intent
     * and a request code of 1. If any exception occurs during this process, it catches the exception and does nothing.
     * @return Unit
     */
    private fun takeImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try { startActivityForResult(intent, 1) } catch (_: Exception) { }
        binding.textView.text = ""
    }

    /**
     * When the camera application completes, the onActivityResult() function is called.
     * This function overrides the default implementation of the onActivityResult() method and takes three parameters: requestCode, resultCode, and data.
     * @return Unit
     */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // If the requestCode is 1 and the resultCode is RESULT_OK, meaning that the image capture was successful, the function retrieves
        // the captured image data from the extras Bundle returned in the data parameter.
        // It then sets the imageBitmap variable to the retrieved Bitmap object and displays the captured image in an imageView.
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val extras: Bundle? = data?.extras
            imageBitmap = extras?.get("data") as Bitmap
            // If the image is successfully taken and then stored on the "imageBitmap" variable, it will set
            // the "imageBitmap" into the "imageView" view on the layout
            if (imageBitmap != null) binding.imageView.setImageBitmap(imageBitmap)
        }
    }

    // FOR QR CODE SCANNER
    // This code creates a configuration object for a barcode scanner with options to scan QR codes and Aztec codes,
    // enable scanning of all possible barcodes, and then build the configuration object.
    // The configuration object can be used as input for a barcode scanning function.
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
        .enableAllPotentialBarcodes()
        .build()

    /**
     * Detects barcodes from an image bitmap and extracts the book name and author name
     * @return Unit
     */
    private fun detectImage() {
        // Check if image bitmap exists
        if (imageBitmap != null) {
            // Create an InputImage object from the bitmap
            val image = InputImage.fromBitmap(imageBitmap!!, 0)
            // Create a BarcodeScanner client with options
            val scanner = BarcodeScanning.getClient(options)

            // Process the image for barcodes
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    // If no barcode is found, show a toast message and exit function
                    if (barcodes.toString() == "[]") {
                        Toast.makeText(this@MainActivity, "Nothing to scan", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }
                    // Loop through each barcode found in the image
                    for (barcode in barcodes) {
                        // If the barcode is of type text, extract the book name and author name
                        when (barcode.valueType) {
                            Barcode.TYPE_TEXT -> {
                                val rawValueStr = barcode.rawValue.toString()
                                val index = rawValueStr.indexOf("&")
                                // Take all characters before the first '&' character to get the book name
                                val bookAuthor = rawValueStr.takeWhile { it != '&' }
                                // Get the author name starting from the '&' character until the end of the string
                                val bookName = rawValueStr.substring(index + 2)
                                // Insert the book entity into the database
                                insertEntity(bookAuthor, bookName)
                                // Show the barcode raw value as a toast message
                                Toast.makeText(this@MainActivity, barcode.rawValue, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }
        // If image bitmap is null, show a toast message
        else { Toast.makeText(this@MainActivity, "Please select photo", Toast.LENGTH_SHORT).show() }
    }
}