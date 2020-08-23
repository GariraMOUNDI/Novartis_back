package com.novartis.service

import com.novartis.domain.BodyRequest
import com.novartis.domain.Message

interface MyService {
    fun getEventResults(body : BodyRequest) : Message
}