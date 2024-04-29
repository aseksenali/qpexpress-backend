package kz.qpexpress.qpexpress.dto

import kz.qpexpress.qpexpress.model.Address
import kz.qpexpress.qpexpress.model.Language
import java.util.*

interface AddressDTO {
    data class CreateAddressDTO(
        val country: String,
        val city: String?,
        val district: String?,
        val neighborhood: String?,
        val street: String?,
        val house: String?,
        val postcode: String?,
    ) : AddressDTO {
        fun toEntity(language: Language): Address {
            val address = Address()
            address.country = country
            address.city = city
            address.district = district
            address.neighborhood = neighborhood
            address.street = street
            address.house = house
            address.postcode = postcode
            address.language = language
            return address
        }
    }

    data class UpdateAddressDTO(
        val country: String,
        val city: String?,
        val district: String?,
        val neighborhood: String?,
        val street: String?,
        val house: String?,
        val postcode: String?,
    ) : AddressDTO {
        fun toEntity(id: UUID): Address {
            val address = Address()
            address.id = id
            address.country = country
            address.city = city
            address.district = district
            address.neighborhood = neighborhood
            address.street = street
            address.house = house
            address.postcode = postcode
            return address
        }
    }

    data class AddressResponse(
        val id: UUID,
        val country: String,
        val city: String?,
        val district: String?,
        val neighborhood: String?,
        val street: String?,
        val house: String?,
        val postcode: String?,
    ) : AddressDTO
}