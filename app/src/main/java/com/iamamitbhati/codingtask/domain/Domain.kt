package com.iamamitbhati.codingtask.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AlertDialog
import com.iamamitbhati.codingtask.data.model.Pet
import com.iamamitbhati.codingtask.data.model.Setting
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

const val PETS = "pets"
const val IMAGE_URL = "image_url"
const val TITLE = "title"
const val CONTENT_URL = "content_url"
const val SETTINGS = "settings"
const val IS_CHAT_ENABLE = "isChatEnabled"
const val IS_CALL_ENABLE = "isCallEnabled"
const val WORK_HOURS = "workHours"

/**
 * Translate Pet Json to Pet ArrayList
 */
fun getPetTranslation(json: String): ArrayList<Pet> {
    val pets = JSONObject(json).getJSONArray(PETS)
    val petsList = arrayListOf<Pet>()
    for (index in 0 until pets.length()) {
        Pet().apply {
            with(pets.getJSONObject(index)) {
                imageUrl = getString(IMAGE_URL)
                title = getString(TITLE)
                contentUrl = getString(CONTENT_URL)
            }
        }.also {
            petsList.add(it)
        }

    }
    return petsList
}

/**
 * Translate Config Json to Object
 */
fun getConfigTranslation(json: String): Setting {
    val setting = JSONObject(json).getJSONObject(SETTINGS)
    return Setting().apply {
        with(setting) {
            isChatEnabled = getBoolean(IS_CHAT_ENABLE)
            isCallEnabled = getBoolean(IS_CALL_ENABLE)
            workHours = getString(WORK_HOURS)
        }
    }
}

/**
 * Function to establish connection and load image
 */

fun loadImage(string: String?): Bitmap? {
    val url: URL? = mStringToURL(string)
    val connection: HttpURLConnection?
    try {
        connection = url?.openConnection() as HttpURLConnection
        connection.connect()
        val inputStream: InputStream = connection.inputStream
        val bufferedInputStream = BufferedInputStream(inputStream)
        return BitmapFactory.decodeStream(bufferedInputStream)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

/**
 * Function to convert string to URL
 */

private fun mStringToURL(string: String?): URL? {
    try {
        return URL(string)
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    }
    return null
}

/**
 * Function to show simple dialog with message
 */
fun alertView(message: String, context: Context) {
    val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
    dialog
        .setMessage(message)
        .setPositiveButton(
            "Ok"
        ) { _, _ -> }.show()
}