package com.novartis.service

import com.novartis.domain.BodyRequestBase
import com.novartis.domain.ListResult
import com.novartis.domain.MessageType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootApplication(scanBasePackages = ["com.novartis"])
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])

class MyServiceBaseTest{

    @Autowired
    lateinit var myServiceBase: MyServiceBase

    @Test
    fun `Valid dates`() {
        val startDate = "2002-03-08"
        val endDate = "2005-02-02"
        val body = BodyRequestBase(
                startDate = startDate,
                endDate = endDate
        )
        val results = myServiceBase.getEventResults(body)

        Assertions.assertThat(results.type).isEqualTo(MessageType.Ok)
        Assertions.assertThat(results.value).isNotNull

        val startYear = LocalDate.parse(startDate).year
        val endYear = LocalDate.parse(endDate).year
        (results.value as ListResult).results.forEach {
            val year = Integer.parseInt(
                    it.time.subSequence(IntRange(0,3)).toString()
            )
            Assertions.assertThat(year).isBetween(startYear, endYear)
        }
    }

    @Test
    fun `Invalid dates`() {
        var startDate = "Chocolat"
        var endDate = "Beurre"
        var body = BodyRequestBase(
                startDate = startDate,
                endDate = endDate
        )
        var results = myServiceBase.getEventResults(body)
        Assertions.assertThat(results.type).isEqualTo(MessageType.Error)

        startDate = "2012-12-31"
        endDate = "2008-12-31"
        body = BodyRequestBase(
                startDate = startDate,
                endDate = endDate
        )
        results = myServiceBase.getEventResults(body)
        Assertions.assertThat(results.type).isEqualTo(MessageType.Error)

        startDate = "2012-12-31"
        endDate = LocalDate.of(LocalDate.now().year + 1, 1,1).toString()
        body = BodyRequestBase(
                startDate = startDate,
                endDate = endDate
        )
        results = myServiceBase.getEventResults(body)
        Assertions.assertThat(results.type).isEqualTo(MessageType.Error)

        startDate = LocalDate.now().toString()
        endDate = LocalDate.now().toString()
        body = BodyRequestBase(
                startDate = startDate,
                endDate = endDate
        )
        results = myServiceBase.getEventResults(body)
        Assertions.assertThat(results.type).isEqualTo(MessageType.Error)

        body = BodyRequestBase(
                startDate = null,
                endDate = null
        )
        results = myServiceBase.getEventResults(body)
        Assertions.assertThat(results.type).isEqualTo(MessageType.Error)
    }
}