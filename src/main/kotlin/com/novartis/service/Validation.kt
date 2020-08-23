package com.novartis.service

import com.novartis.domain.Message
import com.novartis.domain.MessageBase
import com.novartis.domain.MessageType
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Component
class Validation {

    fun dateValidation(startDate: String?, endDate: String?) : Message {
        val startMessage = checkExistence(startDate)
        if(startMessage.type == MessageType.Error)
            return startMessage
        val endMessage = checkExistence(endDate)
        if(endMessage.type == MessageType.Error)
            return endMessage

        val startFormat = checkDateFormat(startMessage.value.toString())
        val endFormat = checkDateFormat(endMessage.value.toString())

        if(startFormat.type == MessageType.Ok && endFormat.type == MessageType.Ok){
            return compareDate(startFormat.value as LocalDate, endFormat.value as LocalDate)
        }else{
            if(startFormat.type == MessageType.Error)
                return startFormat
            if(endFormat.type == MessageType.Error)
                return endFormat
        }
        return MessageBase(
                type = MessageType.Error
        )
    }

    private fun checkExistence(date : String?) : Message {
        val existDate = date ?: return MessageBase(
                type = MessageType.Error,
                value = "Please set the start and end date !!!"
        )
        return MessageBase(
                type = MessageType.Ok,
                value = existDate
        )
    }

    private fun checkDateFormat(date: String): Message {
        return try{
            MessageBase(
                    type = MessageType.Ok,
                    value = LocalDate.parse(date)
            )
        }catch (e: DateTimeParseException){
            val message = "Error, unable to parse this date $date. Please make sure your dates have the correct format (YYYY-MM-DD)"
            MessageBase(
                    type = MessageType.Error,
                    value = message
            )
        }
    }

    private fun compareDate(startDate : LocalDate, endDate : LocalDate) : Message {
        return when {
            endDate > LocalDate.now() -> MessageBase(
                    type = MessageType.Error,
                    value = "Error, the end date must not be later than today !!!"
            )
            startDate > endDate -> MessageBase(
                    type = MessageType.Error,
                    value = "Error, the start date must not be later than the end date "
            )
            else -> MessageBase(
                    type = MessageType.Ok
            )
        }

    }
}