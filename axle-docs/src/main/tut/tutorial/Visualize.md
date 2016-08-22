---
layout: page
title: Visualize
permalink: /tutorial/visualize/
---

The `show` function is available in the `axle._` package.
It can be applied to several types of Axle objects.

The package `axle.awt._` contains functions for creating files from the images: `png`, `jpeg`, `gif`, `bmp`.

The package `axle.web._` contains a `svg` function for creating svg files.

For example:

```
show(plot)

png(plot, "plot.png")

svg(plot, "plot.svg")
```

Visualizations
--------------

* [Bar Charts](/tutorial/bar_charts/)
* [Grouped Bar Charts](/tutorial/grouped_bar_charts/)
* [Plots](/tutorial/plots/)

Animation
---------

`Plot`, `BarChart`, and `BarChartGrouped` support animation.
The visualizing frame polls for updates at a rate of approximately 24 Hz (every 42 ms).

The `play` command requires the same first argument as `show` does.
Additionally, `play` requires a `refresher` function that takes the
previous data and returns the new data.
The third argument is an interval (of type `Time.Q`) that denotes the period at which values are recomputed.
The fourth argument is an `akka.actor.ActorSystem`.
