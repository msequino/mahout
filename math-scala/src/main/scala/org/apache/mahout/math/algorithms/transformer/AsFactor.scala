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

package org.apache.mahout.math.algorithms.transformer

import org.apache.mahout.math.Vector

import collection._
import JavaConversions._
import org.apache.mahout.math._
import org.apache.mahout.math.drm._
import org.apache.mahout.math.drm.RLikeDrmOps._
import org.apache.mahout.math.scalabindings._
import org.apache.mahout.math.scalabindings.RLikeOps._
import scala.reflect.ClassTag

class AsFactor extends Transformer{

  def transform[K: ClassTag](input: DrmLike[K]): DrmLike[K] ={
    if (!isFit) {
      //throw an error
    }

    implicit val ctx = input.context

    val bcastK = drmBroadcast(fitParams.get("k").get)
    val bcastFactorMap = drmBroadcast(fitParams.get("factorMap").get)

    val res = input.mapBlock(fitParams.get("k").get.get(0).toInt) {
      case (keys, block) => {
        val k: Int = bcastK.value.get(0).toInt
        val output = new SparseMatrix(block.nrow, k)
        // This is how we take a vector of mapping to a map
        val fm = bcastFactorMap.all.toSeq.map(e => e.get -> e.index).toMap
        for (i <- 0 until output.nrow){
          output(i, ::) =  svec(fm.get(block.getQuick(i,0)).get -> 1.0 :: Nil, cardinality = k)
        }
        (keys, output)
      }
    }
    res
  }

  def fit[Int](input: DrmLike[Int]) = {
    // this should be done via allReduceBlock or something.
    val v: Vector = input.collect(::, 0)
    var a = new Array[Double](v.length)
    for (i <- 0 until v.length){
      a(i) = v.getElement(i).get
    }

    fitParams("factorMap") = dvec(a.distinct) //a.distinct.zipWithIndex.toMap
    fitParams("k") = dvec(a.distinct.length)

    isFit = true
  }

  def summary(): String = {
      s"""${fitParams.get("k").get(0).toInt} categories""".stripMargin
  }

}
