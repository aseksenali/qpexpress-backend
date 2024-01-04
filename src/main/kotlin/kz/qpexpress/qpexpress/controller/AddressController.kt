package kz.qpexpress.qpexpress.controller

import kz.qpexpress.qpexpress.handler.IAddressHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class AddressController(
    private val addressHandler: IAddressHandler
) {
    @GetMapping("/address/{id}")
    fun getAddress(@PathVariable id: UUID): String {
        return "Hello, World!"
    }
}