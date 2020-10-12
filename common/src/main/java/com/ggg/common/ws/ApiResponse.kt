package com.ggg.common.ws

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException

/**
 * Created by TuanNguyen on 12/12/17.
 */
class ApiResponse<T> {

    var code: Int = 500
    var body: T? = null
    var errorMessage: String? = "" // the message to dev

    constructor(e: Throwable) {
        this.code = 500
        this.body = null
        errorMessage = if (e is ConnectException) {
            "Failed to connect to server"
        } else {
            e.message
        }
    }


    fun isSuccessful(): Boolean = code in 200..299

    constructor(response: Response<T>) {

        code = response.code()
        if (response.isSuccessful) {
            body = response.body()
            errorMessage = null
        } else {
            var message: String? = null
            response.errorBody()?.let {
                val errorBody = it.string()
                try {

                    val jsonObj: JSONObject = JSONObject(errorBody)
                    when {
                        jsonObj.has("error") -> message = jsonObj.getString("error")
                        jsonObj.has("message") -> message = jsonObj.getString("message")
                        else -> message = it.string()
                    }
                } catch(e: JSONException) {
                    Timber.e(e, "error while parsing error")
                    message = "Invalid data format"
                } catch (ignored: IOException) {
                    Timber.e(ignored, "error while parsing response")
                    message = "Invalid data format"
                }

            }

            if (message == null || message!!.trim { it <= ' ' }.isEmpty()) {
                message = response.message()
            }
            errorMessage = message
            body = null
        }

    }


}