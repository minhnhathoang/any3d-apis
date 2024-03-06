package com.any3d.services

import com.any3d.utils.DateUtils
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.apache.commons.codec.digest.DigestUtils
import java.security.Key
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class VuforiaApi {

    companion object {
        var BASE_URL = "https://vws.vuforia.com"

        fun generateSignature(
            requestPath: String,
            method: String,
            content: String,
            contentType: String,
            date: String,
            serverSecretKey: String
        ): String {
            val contentMD5 = DigestUtils.md5Hex(content).lowercase(Locale.getDefault());
            val stringToSign = "$method\n$contentMD5\n$contentType\n$date\n$requestPath"
            val key: Key = SecretKeySpec(serverSecretKey.toByteArray(), "HmacSHA1")
            val mac: Mac = Mac.getInstance("HmacSHA1")
            mac.init(key)
            val signatureBytes: ByteArray = mac.doFinal(stringToSign.toByteArray())
            return Base64.getEncoder().encodeToString(signatureBytes)
        }
    }

    suspend fun getAlLTargets(serverAccessKey: String, serverSecretKey: String) {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(BASE_URL + "/targets") {
            headers.clear();
            headers {
                append(HttpHeaders.Date, DateUtils.formatDate(Date()))
                append(
                    HttpHeaders.Authorization,
                    "VWS $serverAccessKey:" + generateSignature(
                        "/targets",
                        "GET",
                        "",
                        "",
                        get(HttpHeaders.Date)!!,
                        serverSecretKey
                    )
                )
            }
        }
        println(response.bodyAsText())
    }

    suspend fun getTargetById(targetId: String, serverAccessKey: String, serverSecretKey: String) {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(BASE_URL + "/targets/$targetId") {
            headers.clear();
            headers {
                append(HttpHeaders.Date, DateUtils.formatDate(Date()))
                append(
                    HttpHeaders.Authorization,
                    "VWS $serverAccessKey:" + generateSignature(
                        "/targets/$targetId",
                        "GET",
                        "",
                        "",
                        get(HttpHeaders.Date)!!,
                        serverSecretKey
                    )
                )
            }
        }
        println(response.bodyAsText())
    }

    suspend fun addTarget(
        targetName: String,
        image: ByteArray,
        serverAccessKey: String,
        serverSecretKey: String
    ) {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.post(BASE_URL + "/targets") {
            headers.clear();
            headers {
                append(HttpHeaders.Date, DateUtils.formatDate(Date()))
                append(
                    HttpHeaders.Authorization,
                    "VWS $serverAccessKey:" + generateSignature(
                        "/targets",
                        "POST",
                        image.toString(Charsets.UTF_8),
                        "application/json",
                        get(HttpHeaders.Date)!!,
                        serverSecretKey
                    )
                )
            }
        }
        println(response.bodyAsText())
    }
}