package com.novartis.service

import com.novartis.domain.*
import org.springframework.stereotype.Service

@Service
class MyServiceBase(
        private val apiQuery: ApiQuery,
        private val validation: Validation
) : MyService {
    override fun getEventResults(body: BodyRequest): Message {
        val message = validation.dateValidation(body.startDate, body.endDate)
        return if(message.type == MessageType.Error)
            message
        else{
            val dataMessage = apiQuery.getResults(body.startDate!!, body.endDate!!)
            if(dataMessage.type == MessageType.Error)
                return dataMessage
            else
                MessageBase(
                        type = MessageType.Ok,
                        value = ListResultBase(
                                results = dataMessage.value as List<ResultBase>
                        )
                )
        }
    }
}