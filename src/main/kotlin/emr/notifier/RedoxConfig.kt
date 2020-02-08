package emr.notifier

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties(value = "redox")
class RedoxConfig {
    @NotBlank
    lateinit var hl7ApiKey: String
    @NotBlank
    lateinit var hl7Secret: String

    @NotBlank
    lateinit var fhirApiKey: String
    @NotBlank
    lateinit var fhirSecret: String
}