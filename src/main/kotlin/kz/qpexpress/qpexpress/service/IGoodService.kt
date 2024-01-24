package kz.qpexpress.qpexpress.service

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface IGoodService {
    fun createGood(request: ServerRequest): ServerResponse
    fun getGoods(request: ServerRequest): ServerResponse
}