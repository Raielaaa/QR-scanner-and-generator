package com.example.qrscannerpractice2

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.lang.StringBuilder

object QRCode: AppCompatActivity() {
    // FOR QR CODE GENERATOR
    private lateinit var scanner: GmsBarcodeScanner
    private lateinit var bookName: StringBuilder
    private lateinit var bookAuthor: StringBuilder

    /**
     * This function generates a QR code based on a given string value and sets it as an image in an ImageView.
     * @return Unit
     */
    fun qrCodeGenerator(setQRValue: String, imageView: ImageView) {
        // Create a QRCodeWriter object for encoding the QR code.
        val writer: QRCodeWriter = QRCodeWriter()
        // Encode the QR code using the QRCodeWriter object and generate a BitMatrix object.
        // The BitMatrix is a representation of the QR code in a matrix of 0's and 1's.
        val bitMatrix: BitMatrix = writer.encode(setQRValue, BarcodeFormat.QR_CODE, 512, 512)
        // Create a BarcodeEncoder object for converting the BitMatrix into a bitmap image.
        val barcodeEncoder: BarcodeEncoder = BarcodeEncoder()
        // Create a Bitmap image from the BitMatrix using the BarcodeEncoder object.
        val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)

        // Set the generated QR code bitmap image as the image in the ImageView.
        imageView.setImageBitmap(bitmap) // ivQR is an ImageVIew
    }
}