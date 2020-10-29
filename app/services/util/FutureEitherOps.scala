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

package services.util

import uk.gov.hmrc.http.HeaderCarrier
import models.ErrorResponse

import scala.concurrent.{ExecutionContext, Future}

case class FutureEitherOps[E <: ErrorResponse, R](value: Future[Either[E, R]])(implicit ec: ExecutionContext, hc: HeaderCarrier){

  def map[B](mappingFunction: R => B): FutureEitherOps[E, B] = {
    FutureEitherOps(value.map {
      case Right(value) => Right(mappingFunction(value))
      case Left(error) => Left(error)
    })
  }

}
