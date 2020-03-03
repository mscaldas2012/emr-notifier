package emr.notifier

import emr.notifier.model.HL7Payload
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client(value = "https://api.redoxengine.com")
interface RedoxProxy {

    @Post("/auth/authenticate")
    fun authenticate( apiKey:String,  secret: String): RedoxToken

    @Post("/endpoint", processes = [MediaType.APPLICATION_JSON], consumes=[MediaType.APPLICATION_JSON])
    fun sendData(@Header("Authorization") token: String, @Body data: HL7Payload)

    @Post("/endpoint", processes = [MediaType.APPLICATION_XML], consumes=[MediaType.APPLICATION_XML])
    fun sendData(@Header("Authorization") token: String, @Body data: String)

}

