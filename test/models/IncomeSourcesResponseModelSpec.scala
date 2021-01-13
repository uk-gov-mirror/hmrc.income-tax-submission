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

package models

import com.codahale.metrics.SharedMetricRegistries
import play.api.libs.json.{JsObject, Json}
import utils.TestUtils

class IncomeSourcesResponseModelSpec extends TestUtils {
  SharedMetricRegistries.clear()

  val model: IncomeSourcesResponseModel = IncomeSourcesResponseModel(Some(DividendsResponseModel(Some(123456.78), Some(123456.78))),
    Some(Seq(SubmittedInterestModel("someName", "12345", Some(12345.67), Some(12345.67)))))

  val jsonModel: JsObject = Json.obj("dividends" ->
    Json.obj(
      "ukDividends" -> 123456.78,
      "otherUkDividends" -> 123456.78
    ),
    "interest" ->
      Seq(Json.obj(
        "accountName" -> "someName",
        "incomeSourceId" -> "12345",
        "taxedUkInterest" -> 12345.67,
        "untaxedUkInterest" -> 12345.67
      )
    ))



  "IncomeSourcesResponseModel" should {

    "parse to Json" in {
      Json.toJson(model) mustBe jsonModel
    }

    "parse from Json" in {
      jsonModel.as[IncomeSourcesResponseModel]
    }
  }

}
