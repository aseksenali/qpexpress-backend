package kz.qpexpress.qpexpress.handler

import kz.qpexpress.qpexpress.dto.AddressDTO
import kz.qpexpress.qpexpress.repository.AddressRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

@Service
class AddressHandler(
    private val addressRepository: AddressRepository,
) : IAddressHandler {
    override fun createAddress(data: AddressDTO.CreateAddressDTO): ServerResponse {
        val savingEntity = data.toEntity()
        val result = addressRepository.save(savingEntity)
        return ServerResponse.ok().body(
            AddressDTO.AddressResponse(
                id = result.id!!,
                country = result.country,
                city = result.city,
                district = result.district,
                neighborhood = result.neighborhood,
                street = result.street,
                house = result.house,
                postcode = result.postcode,
            )
        )
    }

    override fun updateAddress(id: UUID, data: AddressDTO.UpdateAddressDTO): ServerResponse {
        addressRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        val updatingAddress = data.toEntity(id)
        val result = addressRepository.save(updatingAddress)
        return ServerResponse.ok().body(
            AddressDTO.AddressResponse(
                id = result.id!!,
                country = result.country,
                city = result.city,
                district = result.district,
                neighborhood = result.neighborhood,
                street = result.street,
                house = result.house,
                postcode = result.postcode,
            )
        )
    }

    override fun deleteAddress(id: UUID): ServerResponse {
        addressRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        addressRepository.deleteById(id)
        return ServerResponse.ok().build()
    }

    override fun getAddress(id: UUID): ServerResponse {
        val result = addressRepository.findByIdOrNull(id) ?: return ServerResponse.notFound().build()
        return ServerResponse.ok().body(
            AddressDTO.AddressResponse(
                id = result.id!!,
                country = result.country,
                city = result.city,
                district = result.district,
                neighborhood = result.neighborhood,
                street = result.street,
                house = result.house,
                postcode = result.postcode,
            )
        )
    }

    override fun getAddresses(): ServerResponse {
        val result = addressRepository.findAll()
        return ServerResponse.ok().body(result.map {
            AddressDTO.AddressResponse(
                id = it.id!!,
                country = it.country,
                city = it.city,
                district = it.district,
                neighborhood = it.neighborhood,
                street = it.street,
                house = it.house,
                postcode = it.postcode,
            )
        })
    }
}