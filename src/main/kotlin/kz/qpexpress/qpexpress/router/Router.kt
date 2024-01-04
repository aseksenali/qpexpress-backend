package kz.qpexpress.qpexpress.router

import kz.qpexpress.qpexpress.dto.CalculatorDTO
import kz.qpexpress.qpexpress.service.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router

@Configuration
class RouterConfiguration(
    private val addressService: IAddressService,
    private val deliveryService: IDeliveryService,
    private val userService: IUserService,
    private val countryService: ICountryService,
    private val cityService: ICityService,
    private val calculatorService: ICalculatorService,
    private val currencyService: ICurrencyService,
    private val fileService: IFileService,
    private val receiverService: IReceiverService
) {
    @Bean
    fun router() = router {
        "v1".nest {
            "calculator".nest {
                "delivery-price".nest {
                    POST("") {
                        val request = it.body<CalculatorDTO.CalculateDeliveryPriceRequestDTO>()
                        val response = calculatorService.calculateDeliveryPrice(request)
                        ok().body(response)
                    }
                }
            }
            "me".nest {
                GET("", userService::getMyInformation)
            }
            "my".nest {
                "orders".nest {
                    GET("", deliveryService::getMyDeliveries)
                    POST("", deliveryService::createDelivery)
                    GET("{id}", deliveryService::getMyDeliveryById)
                    PUT("{id}", deliveryService::updateDelivery)
                }
            }
            "addresses".nest {
                GET("", addressService::getAddresses)
                GET("{id}", addressService::getAddress)
                POST("", addressService::createAddress)
                PUT("{id}", addressService::updateAddress)
                DELETE("{id}", addressService::deleteAddress)
            }
            "orders".nest {
                GET("", deliveryService::getAllDeliveries)
                GET("{id}", deliveryService::getDeliveryById)
                PUT("{id}", deliveryService::updateDelivery)
                DELETE("{id}", deliveryService::deleteDelivery)
            }
            "users".nest {
                GET("", userService::getAll)
            }
            "countries".nest {
                GET("", countryService::getAllCountries)
                POST("", countryService::createCountry)
            }
            "cities".nest {
                GET("", cityService::getAllCities)
                POST("", cityService::createCity)
            }
            "currencies".nest {
                GET("", currencyService::getAllCurrencies)
                POST("", currencyService::createCurrency)
            }
            "files".nest {
                GET("{id}", fileService::getFileById)
            }
            "receivers".nest {
                POST("", receiverService::createReceiver)
            }
        }
    }
}