package emr.notifier

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("emr.notifier")
                .mainClass(Application.javaClass)
                .start()
    }
}