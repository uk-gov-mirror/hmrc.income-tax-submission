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

import play.api.libs.json.{JsObject, Json}
import utils.TestUtils

class DividendsResponseModelSpec extends TestUtils {

  val validJson: JsObject = Json.obj(
    "ukDividends" -> 10,
    "otherUkDividends" -> 5
  )

  val validModel: DividendsResponseModel = DividendsResponseModel(
    ukDividends = Some(10),
    otherUkDividends = Some(5)
  )

  "DividendsResponseModel" should {

    "correctly parse from Json" in {
      validJson.as[DividendsResponseModel] mustBe validModel
    }

    "correctly parse to Json" in {
      Json.toJson(validModel) mustBe validJson
    }

  }

}
