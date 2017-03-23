---
layout: docs 
title:  "Edge"
section: "datastructures"
source: "core/src/main/scala/tiki/Edge.scala"
scaladoc: "#tiki.Edge"
---
# Edges

The following case classes may used to define edges:

- `Edge` an unweighted edge from one vertex to another.
- `EdgeLabelled` an unweighted, labelled edge from one vertex to another.
- `EdgeWeighted` is a weighted edge.

The case classes _do not_ form a hierarchy. The weighted edge case class is provided
for use-cases where we require just a weighted edge and no labelling. 

Undirected edges? _Unless explicitly stated_ most algorithms would assume an undirected
 edge be represented by two directed edges. 
 
Why? Many practical use cases tend to be concerned with directed graphs, and usually directed
acyclic graphs (DAG) that are connected, i.e. _Trees_. 

_Often vertex type of an Edge may be a proxy for some underlying vertex type.
Where an instance of the underlying type may be costly to hold within a graph._
 
 
Functions:
 
- `lmap(f)` apply the function `f` on the label and return a new labelled edge.

## Usage

### Constructing Edges

Edges can be constructed directly. However, importing `implicits` 
will allow you to use the `-->` and `--> :+` operators.


#### Edge

```tut
import tiki._
import tiki.Predef._
import tiki.implicits._

val e0 = Edge[Int](1,2)

// Use the edge --> operator
val e1 = 1 --> 2
```

#### LEdge

```tut
import tiki._
import tiki.Predef._

val e0 = Edge[Int](1,2)
val le0 = LEdge(e0,"a label")

// User the :+ to apply a label to an edge.
val le1 = 1 --> 2 :+ "a label"
```

#### WEdge

The weighted edge is a case where we have only one label of type double. There is the `:#` operator
to create these edges:

```tut
import tiki._
import tiki.Predef._

val we = 1 --> 2 :# 2.345
```