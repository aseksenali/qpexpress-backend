package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.AddressDTO
import kz.qpexpress.qpexpress.model.Language
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

interface IAddressHandler {
    fun createAddress(data: AddressDTO.CreateAddressDTO, language: Language): ServerResponse
    fun updateAddress(id: UUID, data: AddressDTO.UpdateAddressDTO): ServerResponse
    fun deleteAddress(id: UUID): ServerResponse
    fun getAddress(id: UUID): ServerResponse
    fun getAddresses(language: Language): ServerResponse
}