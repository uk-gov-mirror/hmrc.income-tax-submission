/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import com.codahale.metrics.SharedMetricRegistries
import connectors.{IncomeTaxDividendsConnector, IncomeTaxInterestConnector}
import connectors.httpParsers.SubmittedDividendsParser.IncomeSourcesResponseModel
import models.InternalServerError
import uk.gov.hmrc.http.HeaderCarrier
import utils.TestUtils

import scala.concurrent.Future

class GetIncomeSourcesServiceSpec extends TestUtils {
  SharedMetricRegistries.clear()

  val dividendsConnector: IncomeTaxDividendsConnector = mock[IncomeTaxDividendsConnector]
  val interestConnector: IncomeTaxInterestConnector = mock[IncomeTaxInterestConnector]
  val service: GetIncomeSourcesService = new GetIncomeSourcesService(dividendsConnector, interestConnector, scala.concurrent.ExecutionContext.global)


  ".getAllIncomeSources" should {

    "return the connector response" in {

      val expectedResult: IncomeSourcesResponseModel = Left(InternalServerError)

      (dividendsConnector.getSubmittedDividends(_: String, _: Int, _: String)(_: HeaderCarrier))
        .expects("12345678", 1234, "87654321", *)
        .returning(Future.successful(expectedResult))

      val result = await(service.getAllIncomeSources("12345678", 1234, "87654321"))

      result mustBe expectedResult

    }
  }
}
