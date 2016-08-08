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

package org.apache.mahout.algos

//import org.apache.mahout.math._
import org.apache.mahout.math.{Vector => MahoutVector, drm}
import org.apache.mahout.math.drm.DrmLike
import org.apache.mahout.math.scalabindings._

import scala.reflect.ClassTag

abstract class Model extends Serializable {

  var fitParams = collection.mutable.Map[String, MahoutVector]()
  var isFit = false

  /**
    * A method to train the model on a given Drm
    * @param input - Drm of Festures to Train on
    */
  def fit[Int](input: DrmLike[Int])


  /**
    * Return a summary of the fit model
    */
  def summary(): String


}