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

package connectors


import javax.inject.Inject
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}
import config.AppConfig
import connectors.httpParsers.SubmittedDividendsParser.{SubmittedDividendsHttpReads, IncomeSourcesResponseModel}

import scala.concurrent.{ExecutionContext, Future}

class IncomeTaxDividendsConnector @Inject() (val http: HttpClient,
                                             val config: AppConfig)(implicit ec:ExecutionContext) {

  def getSubmittedDividends(nino: String, taxYear: Int)(implicit hc: HeaderCarrier): Future[IncomeSourcesResponseModel] = {
    val submittedDividendsUrl: String = config.dividendsBaseUrl + s"/income-tax-dividends/income-tax/nino/$nino/sources?taxYear=$taxYear"
    http.GET[IncomeSourcesResponseModel](submittedDividendsUrl)
  }

}
