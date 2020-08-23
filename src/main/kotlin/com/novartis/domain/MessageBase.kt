package com.novartis.domain

interface Message {
        val type : MessageType
        val value: Any?
}
class MessageBase(
        override val type: MessageType,
        override val value: Any? = null
) : Message

enum class MessageType {
        Error, Ok
}