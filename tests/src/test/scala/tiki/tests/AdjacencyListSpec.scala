/*
 * Copyright (c) 2017
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tiki
package tests


import org.scalatest.Matchers
import org.scalatest.prop.Checkers
import tiki.AdjacencyList
import tiki.tests.arbitrary.AllArbitrary
import tiki.Predef._
import tiki.Poly._


class AdjacencyListSpec extends TikiSuite with Checkers with Matchers with AllArbitrary {

  test("`successors` of adjacency list should return correct vertices")(forAll { (xs: List[Edge[Int]]) =>
    /* doesn't test leaf vertices. */
    val adjacency = buildAdjacencyList(xs)
    xs.groupBy(_.from).map { case (k,v) => (k, v.map(_.to))}.forall { case (vertex, children) =>
      adjacency.successors(vertex) == children.toSet
    } should be (true)
  })

  test("`successors` of adjacency ... correct vertices (labelled nodes") (forAll { (xs: List[Edge[Int]]) =>
    /* doesn't test leaf vertices. */
    val ys = xs.map(LEdge(_,"label"))
    val adjacency = buildAdjacencyList(ys)
    xs.groupBy(_.from).map { case (k,v) => (k, v.map(_.to))}.forall { case (vertex, children) =>
      adjacency.successors(vertex) == children.toSet
    } should be (true)
  })

  test("`successors` of leaf vertices should be the empty set.") { (xs: List[Edge[Int]]) =>
    val adjacency = buildAdjacencyList(xs)
    xs.filter(x => ! xs.exists(_.from == x.to )).forall(e=>adjacency.successors(e.to) == Set.empty[Int])
  }

  test("`successors` of vertex not in graph should return empty set") {
    AdjacencyList.empty[Int].successors(1) should be (Set.empty)
  }

  test("`predecessors` of vertex not in graph should return empty set") {
    AdjacencyList.empty[Int].predecessors(1) should be (Set.empty)
  }

  test("`predecessors` of adjacency list should return correct vertices")(forAll { (xs: List[Edge[Int]]) =>
    /* doesn't test leaf vertices. */
    val adjacency = buildAdjacencyList(xs)
    xs.groupBy(_.to).map { case (k,v) => (k, v.map(_.from))}.forall { case (vertex, parents) =>
      adjacency.predecessors(vertex) == parents.toSet
    } should be (true)
  })

  test("`predecessors` of root vertices should be the empty set.") { (xs: List[Edge[Int]]) =>
    val adjacency = buildAdjacencyList(xs)
    xs.filter(x => ! xs.exists(_.to == x.from )).forall(e=>adjacency.successors(e.to) == Set.empty[Int])
  }

}