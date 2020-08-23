package com.novartis.domain

interface BodyRequest {
    val startDate : String?
    val endDate : String?
}

class BodyRequestBase(
        override val startDate: String? = null,
        override val endDate: String? = null
) : BodyRequest