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
    private val recipientService: IRecipientService,
    private val orderService: IOrderService,
    private val goodService: IGoodService,
    private val kaspiService: IKaspiService
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
                PUT("", userService::updateMyInformation)
            }
            "my".nest {
                "deliveries".nest {
                    GET("", deliveryService::getMyDeliveries)
                    GET("{id}", deliveryService::getMyDeliveryById)
                    PUT("{id}", deliveryService::updateDelivery)
                }
                "orders".nest {
                    GET("", orderService::getMyOrders)
                    PUT("{id}", orderService::updateOrder)
                    DELETE("{id}", orderService::deleteOrder)
                }
                "recipients".nest {
                    GET("", recipientService::getMyRecipients)
                    POST("", recipientService::createRecipient)
                    PUT("{id}", recipientService::updateRecipient)
                }
            }
            "recipients".nest {
                GET("", recipientService::getRecipients)
                PATCH("{id}/deny", recipientService::denyRecipient)
                PATCH("{id}/accept", recipientService::acceptRecipient)
            }
            "addresses".nest {
                GET("", addressService::getAddresses)
                GET("{id}", addressService::getAddress)
                POST("", addressService::createAddress)
                PUT("{id}", addressService::updateAddress)
                DELETE("{id}", addressService::deleteAddress)
            }
            "deliveries".nest {
                GET("", deliveryService::getAllDeliveries)
                GET("{id}", deliveryService::getDeliveryById)
                POST("", deliveryService::createDelivery)
                PUT("{id}", deliveryService::updateDelivery)
                DELETE("{id}", deliveryService::deleteDelivery)
            }
            "users".nest {
                GET("", userService::getAllWithRecipient)
                GET("{id}", userService::getUserById)
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
                POST("", fileService::uploadFile)
                GET("{id}/download", fileService::downloadFileById)
            }
            "orders".nest {
                GET("", orderService::getOrders)
                POST("", orderService::createOrder)
                "{id}".nest {
                    GET("", orderService::getOrderById)
                    PUT("", orderService::updateOrder)
                    DELETE("", orderService::deleteOrder)
                    POST("goods", goodService::createGood)
                }
            }
            "goods".nest {
                GET("", goodService::getGoods)
            }
            "payment".nest {
                "kaspi".nest {
                    GET("tradepoints", kaspiService::getTradePoints)
                    "{id}".nest {
                        GET("status", kaspiService::getPaymentStatus)
                        GET("", kaspiService::getPaymentDetails)
                    }
                    POST("register", kaspiService::registerDevice)
                    POST("create", kaspiService::createPayment)
                    POST("create-link", kaspiService::createLink)
                    POST("create-qr", kaspiService::createQR)
                }
            }
        }
    }
}