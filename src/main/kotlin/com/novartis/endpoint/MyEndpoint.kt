package com.novartis.endpoint

import com.novartis.domain.BodyRequestBase
import com.novartis.domain.MessageType
import com.novartis.service.MyService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class MyEndpoint (
    val myService : MyService
){
    @PostMapping("/events")
    fun getEventResults(@RequestBody body : BodyRequestBase) : Any {
        val message = myService.getEventResults(body)
        return if(message.type == MessageType.Error)
            message
        else
            message.value!!
    }
}