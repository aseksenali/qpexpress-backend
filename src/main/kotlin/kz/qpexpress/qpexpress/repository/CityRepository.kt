package kz.qpexpress.qpexpress.repository

import kz.qpexpress.qpexpress.model.City
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CityRepository: JpaRepository<City, UUID> {
}