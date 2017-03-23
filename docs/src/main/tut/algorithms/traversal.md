---
layout: docs 
title:  "Traversal"
section: "algorithms"
source: "core/src/main/scala/tiki/Traversal.scala"
scaladoc: "#tiki.Traversal"
---
# Traversal functions
 
Traversal is done by performing an `unfold` on the graph representation, the 
 function will return a stream of vertices.

```scala
private def unfold[T,R](z:T)(f: T => Option[(R,T)]): Trampoline[Stream[R]] = f(z) match {
    case None => Trampoline.done(Stream.empty[R])
    case Some((r,v)) =>
        Trampoline.suspend(unfold(v)(f)).flatMap(stream => Trampoline.done(r #:: stream))
  }
}
```

The traversal can be depth or breadth first (_note_ the `distinct` on the stream does preserve order,
a vertex may be visited more than once in a traversal, most of the time we want the first instance).

Currently, cycles are ignored.

```scala
private def traverse[A](g: DirectedGraphRep[A], v: A, dfs: Boolean): Stream[A]
= unfold( (List(v),Set.empty[A]) ) {
      case (current,visited) => current match {
        case w :: Nil =>
          Some((w, (g.successors(w).toList.filterNot(visited.contains), visited + w)))
        case w :: vs =>
          val next = if (dfs) g.successors(w).toList ::: vs
          else vs ::: g.successors(w).toList
          Some((w, (next.filterNot(visited.contains), visited + w)))
        case _ =>
          None
      }
    }.run
```

 Two primary functions are available:
 
 - `dfs(g,start)` will perform a depth-first traversal of the graph _g_, starting at _start_ vertex.
 - `bfs(g,start)` as above, except a breadth first search will be done.
 
 Both are implemented in terms of the `visitOrder` function.
 
 ```scala
private def visitOrder[A](g: DirectedGraphRep[A], start: A, dfs: Boolean): Stream[A]
  = if (g.contains(start)) traverse(g, start, dfs).distinct else Stream.empty

def dfs[A](g: DirectedGraphRep[A], start: A): Stream[A]
  = visitOrder(g,start,dfs=true)

def bfs[A](g: DirectedGraphRep[A], start: A): Stream[A]
  = visitOrder(g,start,dfs=false)
```

```tut
import tiki.Predef._
import tiki.Poly._
import tiki.Traversal._
import tiki.implicits._
import scala.util.Random


val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D'))
val adj = buildAdjacencyList(edges)
val search = dfs(adj, 'A')
search.mkString

```