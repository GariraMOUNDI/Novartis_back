package com.novartis.domain

interface ListResult {
    val results : List<Result>
}

interface Result {
    val time : String
    val count : Int
}

class ResultBase(
        override val time: String,
        override val count: Int
) : Result

class ListResultBase(
        override val results: List<ResultBase>
) : ListResult