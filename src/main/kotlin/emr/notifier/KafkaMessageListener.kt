package emr.notifier

import emr.notifier.model.HL7Payload
import emr.notifier.model.ValidationModel
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Value
import io.micronaut.messaging.annotation.Body
import org.slf4j.LoggerFactory


@KafkaListener(groupId = "\${app.consumer.group:unit-test}")
class KafkaMessageListener(val redoxProxy: RedoxProxy,
                           val config: RedoxConfig,
                           @Value("\${app.messagesofinterest}") val messagesOfInterest: String) {

    //CarePlan to be submitted to Redox.
    var carePlan: String

    init {
        carePlan = KafkaMessageListener::class.java.getResource("/CarePlanBundle.xml").readText()
    }

    companion object {
        val logger =  LoggerFactory.getLogger(KafkaMessageListener::class.java)
    }

    @Topic("\${app.incomingtopic}")
    fun receive(@KafkaKey recordGUID: String, @Body  message: ValidationModel) {
        logger.info("AUDIT:: Message Received $recordGUID")
        if (messagesOfInterest.equals(message.message_controller_id, true)) {
            logger.info("Message is Relevant for Redox... Sending it")
            //this Message is of Relevance for EMR - send it to Redox with Care Plan!
            val redoxPayload = HL7Payload(message.content)
            val hl7Token =  redoxProxy.authenticate(config.hl7ApiKey, config.hl7Secret)
            //Send HL7 message:
            redoxProxy.sendData("Bearer ${hl7Token.accessToken}", redoxPayload)
            //Send CarPlan:
            val fhirToken = redoxProxy.authenticate(config.fhirApiKey, config.fhirSecret)
            redoxProxy.sendData("Bearer ${fhirToken.accessToken}", carePlan)
        } else {
            logger.info("Message is not relevant... ${message.message_controller_id}")
        }
    }
}