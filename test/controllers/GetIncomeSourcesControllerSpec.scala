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

package controllers


import models.{DividendsResponseModel, ErrorResponse, IncomeSourcesResponseModel, InternalServerError}
import org.scalamock.handlers.{CallHandler3, CallHandler4}
import play.api.http.Status._
import play.api.test.FakeRequest
import services.GetIncomeSourcesService
import uk.gov.hmrc.http.HeaderCarrier
import utils.TestUtils

import scala.concurrent.Future

class GetIncomeSourcesControllerSpec extends TestUtils {

  val getIncomeSourcesService: GetIncomeSourcesService = mock[GetIncomeSourcesService]
  val controller = new GetIncomeSourcesController(getIncomeSourcesService, mockControllerComponents,authorisedAction)
  val nino :String = "123456789"
  val mtditid :String = "1234567890"
  val taxYear: Int = 1234
  private val fakeGetRequest = FakeRequest("GET", "/").withSession("MTDITID" -> "12234567890")

  def mockGetIncomeSourcesValid(): CallHandler4[String, Int, String, HeaderCarrier, Future[Either[ErrorResponse, IncomeSourcesResponseModel]]] = {
    val incomeSources: IncomeSourcesResponseModel = IncomeSourcesResponseModel(Some(DividendsResponseModel(Some(12345.67),Some(12345.67))))
    (getIncomeSourcesService.getAllIncomeSources(_: String, _: Int, _: String)(_: HeaderCarrier))
      .expects(*, *, *, *)
      .returning(Future.successful(Right(incomeSources)))
  }

  def mockGetIncomeSourcesInvalid(): CallHandler4[String, Int, String, HeaderCarrier, Future[Either[ErrorResponse, IncomeSourcesResponseModel]]] = {
    (getIncomeSourcesService.getAllIncomeSources(_: String, _: Int, _: String)(_: HeaderCarrier))
      .expects(*, *, *, *)
      .returning(Future.successful(Left(InternalServerError)))
  }



  "calling .getIncomeSources" should {

    "with existing dividend sources" should {

      "return an OK 200 response when called as an individual" in {
        val result = {
          mockAuth()
          mockGetIncomeSourcesValid()
          controller.getIncomeSources(nino, taxYear, mtditid)(fakeGetRequest)
        }
        status(result) mustBe OK
      }

      "return an OK 200 response when called as an agent" in {
        val result = {
          mockAuthAsAgent()
          mockGetIncomeSourcesValid()
          controller.getIncomeSources(nino, taxYear, mtditid)(fakeGetRequest)
        }
        status(result) mustBe OK
      }

    }
    "without existing dividend sources" should {

      "return an InternalServerError response when called as an individual" in {
        val result = {
          mockAuth()
          mockGetIncomeSourcesInvalid()
          controller.getIncomeSources(nino, taxYear, mtditid)(fakeGetRequest)
        }
        status(result) mustBe INTERNAL_SERVER_ERROR
      }

      "return an InternalServerError response when called as an agent" in {
        val result = {
          mockAuthAsAgent()
          mockGetIncomeSourcesInvalid()
          controller.getIncomeSources(nino, taxYear, mtditid)(fakeGetRequest)
        }
        status(result) mustBe INTERNAL_SERVER_ERROR
      }

    }

  }
}
