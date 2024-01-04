package kz.qpexpress.qpexpress.controller

import kz.qpexpress.qpexpress.dto.DeliveryDTO
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@Controller
class OrderController {
    @PostMapping("/order")
    fun createOrder(@MultipartForm deliveryDTO: DeliveryDTO, @RequestPart("invoice") invoice: MultipartFile) {
        TODO()
    }
}