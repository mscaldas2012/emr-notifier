package emr.notifier

import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test


import javax.inject.Inject

@MicronautTest
internal class RedoxControllerTest(@Inject val config:RedoxConfig, @Inject val proxy: RedoxProxy) {

    @Test
    fun authenticate() {
        val token = proxy.authenticate(config.hl7ApiKey, config.hl7Secret)
        assert(token != null)
        assert(token.accessToken != null)
    }

//    @Test
    fun send() {
    }

//    @Test
    fun sendCarePlan() {
    }
}