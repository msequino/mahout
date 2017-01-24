/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements. See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership. The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License. You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing,
  * software distributed under the License is distributed on an
  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  * KIND, either express or implied. See the License for the
  * specific language governing permissions and limitations
  * under the License.
  */

package org.apache.mahout.math.algorithms.regression

import org.apache.mahout.math.algorithms.regression.tests._
import org.apache.mahout.math.drm._
import org.apache.mahout.math.{Vector => MahoutVector}
import org.apache.mahout.math.algorithms.Model
import org.apache.mahout.math.drm.DrmLike

import scala.reflect.ClassTag

/**
  * Abstract of Regressors
  */
trait Regressor[K] extends Model {

  var residuals: DrmLike[K] = _

  var drmY: DrmLike[K] = _

  def fit(drmFeatures: DrmLike[K],
          drmTarget: DrmLike[K],
          hyperparameters: Map[String,Any] = Map("" -> None)): Unit

  def predict(drmPredictors: DrmLike[K]): DrmLike[K]

  // Common Applicable Tests- here only for convenience.
  lazy val mse = FittnessTests.MeanSquareError(this)
  lazy val r2 = FittnessTests.CoefficientOfDetermination(this, drmY)

}