package emr.notifier

import emr.notifier.model.HL7Payload
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/redox")
class RedoxController(val config:RedoxConfig, val redoxProxy: RedoxProxy) {

    val logger =  LoggerFactory.getLogger(RedoxController::class.java)
    @Get(value ="/token")
    fun authenticate(): RedoxToken {
        logger.info("AUDIT:: Authenticating with REDOX!")
        logger.debug("apiKey: ${config.hl7ApiKey}")
        logger.debug("Secrete: ${config.hl7Secret}")
        return redoxProxy.authenticate(config.hl7ApiKey, config.hl7Secret)
    }

    @Post("/sendHL7Data", consumes=[MediaType.TEXT_PLAIN])
    fun send(@Body data:String): HttpStatus {
        logger.info("AUDIT:: Sending data to REDOX...")
        val token =  redoxProxy.authenticate(config.hl7ApiKey, config.hl7Secret)

        val payload = HL7Payload(Base64.getEncoder().encodeToString(data.toByteArray()))
        redoxProxy.sendData("Bearer ${token.accessToken}", payload)
        return HttpStatus.OK
    }

    @Post("/sendCarePlan", processes = [MediaType.APPLICATION_XML])
    fun sendCarePlan(@Body data:String): HttpStatus {
        logger.info("AUDIT:: Sending data to REDOX...")
        val token =  redoxProxy.authenticate(config.fhirApiKey, config.fhirSecret)
        redoxProxy.sendData("Bearer ${token.accessToken}", data)
        return HttpStatus.OK
    }


}