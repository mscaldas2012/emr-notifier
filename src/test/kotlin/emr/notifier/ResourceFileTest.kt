package emr.notifier

import org.junit.jupiter.api.Test

class ResourceFileTest {

    @Test
    fun tstReadResourceFile() {
        val carePlan = KafkaMessageListener::class.java.getResource("/CarePlanBundle.xml").readText()
        println("carePlan = ${carePlan}")
    }
}