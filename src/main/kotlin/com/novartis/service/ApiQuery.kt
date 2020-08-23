package com.novartis.service

import com.novartis.domain.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class ApiQuery {
    fun getResults(startDate : String, endDate : String) : Message {
        val url = UriComponentsBuilder
                .fromHttpUrl("https://api.fda.gov/drug/event.json?search=receivedate:[$startDate+TO+$endDate]&count=receivedate")
                .build()
                .toUriString()
        try {
            val result = RestTemplate()
                    .exchange(
                            url,
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            object : ParameterizedTypeReference<LinkedHashMap<String, Any>>() {}
                    ).body
            return MessageBase(
                    type = MessageType.Ok,
                    value = result?.get("results")!!.getResults()
            )
        } catch (e: HttpClientErrorException) {
            return MessageBase(
                    type = MessageType.Error,
                    value = "Sorry there is no data for this date interval !!!"
            )
        } catch (e: Exception) {
            return MessageBase(
                    type = MessageType.Error,
                    value = "OpenFDA API is unreacheable. Please check your internet connection !!!"
            )
        }
    }
    private fun Any.getResults() : List<Result> {
        val list : MutableList<Result> = mutableListOf()
        if( this is ArrayList<*>){
            this.forEach {
                if( it is LinkedHashMap<*, *>)
                    list.add(
                        ResultBase(
                            time = it["time"] as String,
                            count = it["count"] as Int
                        )
                    )
            }
        }
        return list
    }
}






