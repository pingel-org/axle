# Visualize

The `show` function is available in the `axle._` package.
It can be applied to several types of Axle objects.

The package `axle.awt._` contains functions for creating files from the images: `png`, `jpeg`, `gif`, `bmp`.

The package `axle.web._` contains a `svg` function for creating svg files.

For example:

```scala
show(plot)

png(plot, "plot.png")

svg(plot, "plot.svg")
```

## Visualizations

* [Bar Charts](/tutorial/bar_charts/)
* [Grouped Bar Charts](/tutorial/grouped_bar_charts/)
* [Plots](/tutorial/plots/)

## Animation

`Plot`, `BarChart`, `BarChartGrouped`, and `ScatterPlot` support animation.
The visualizing frame polls for updates at a rate of approximately 24 Hz (every 42 ms).

The `play` command requires the same first argument as `show` does.
Additionally, `play` requires a `Observable[D]` function that represents the stream of data updates.
The implicit argument is a `monix.execution.Scheduler`.

An `axle.reactive.CurrentValueSubscriber` based on the `Observable[D]` can be used to create the
`dataFn` read by the visualization.

See [Grouped Bar Charts](/tutorial/grouped_bar_charts/) for a full example of animation.
