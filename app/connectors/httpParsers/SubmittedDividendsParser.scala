/*
 * Copyright 2021 HM Revenue & Customs
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

package connectors.httpParsers

import models._
import play.api.Logging
import utils.PagerDutyHelper.PagerDutyKeys._
import utils.PagerDutyHelper.pagerDutyLog
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_FOUND, OK, SERVICE_UNAVAILABLE}
import uk.gov.hmrc.http.{HttpReads, HttpResponse}


object SubmittedDividendsParser extends APIParser with Logging {
  type IncomeSourcesResponseModel = Either[APIErrorModel, Option[SubmittedDividendsModel]]

  override val parserName: String = "SubmittedDividendsParser"
  override val service: String = "income-tax-dividends"

  implicit object SubmittedDividendsHttpReads extends HttpReads[IncomeSourcesResponseModel] {
    override def read(method: String, url: String, response: HttpResponse): IncomeSourcesResponseModel = {
      response.status match {
        case OK =>
          response.json.validate[SubmittedDividendsModel].fold[IncomeSourcesResponseModel](
          _ =>  badSuccessJsonFromAPI,
          {
            case SubmittedDividendsModel(None, None) => Right(None)
            case parsedModel => Right(Some(parsedModel))
          }
        )
        case NOT_FOUND =>
          logger.info(logMessage(response))
          Right(None)
        case BAD_REQUEST =>
          pagerDutyLog(BAD_REQUEST_FROM_API, logMessage(response))
          handleAPIError(response)
        case INTERNAL_SERVER_ERROR =>
          pagerDutyLog(INTERNAL_SERVER_ERROR_FROM_API, logMessage(response))
          handleAPIError(response)
        case SERVICE_UNAVAILABLE =>
          pagerDutyLog(SERVICE_UNAVAILABLE_FROM_API, logMessage(response))
          handleAPIError(response)
        case _ =>
          pagerDutyLog(UNEXPECTED_RESPONSE_FROM_API, logMessage(response))
          handleAPIError(response, Some(INTERNAL_SERVER_ERROR))
      }
    }
  }
}
