package kz.qpexpress.qpexpress.service

import kz.qpexpress.qpexpress.dto.AddressDTO
import kz.qpexpress.qpexpress.handler.IAddressHandler
import kz.qpexpress.qpexpress.util.toUUID
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Service
class AddressService(
    private val addressHandler: IAddressHandler
) : IAddressService {
    override fun createAddress(request: ServerRequest): ServerResponse {
        val data = request.body<AddressDTO.CreateAddressDTO>()
        return addressHandler.createAddress(data)
    }

    override fun updateAddress(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        val data = request.body<AddressDTO.UpdateAddressDTO>()
        return addressHandler.updateAddress(id, data)
    }

    override fun deleteAddress(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return addressHandler.deleteAddress(id)
    }

    override fun getAddress(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toUUID() ?: return ServerResponse.badRequest().build()
        return addressHandler.getAddress(id)
    }

    override fun getAddresses(request: ServerRequest): ServerResponse {
        return addressHandler.getAddresses()
    }
}