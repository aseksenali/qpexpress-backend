package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface IAddressService {
    fun createAddress(request: ServerRequest): ServerResponse
    fun updateAddress(request: ServerRequest): ServerResponse
    fun deleteAddress(request: ServerRequest): ServerResponse
    fun getAddress(request: ServerRequest): ServerResponse
    fun getAddresses(request: ServerRequest): ServerResponse
}