
import laika.helium.config._
import laika.ast._
import laika.ast.Path.Root
import laika.helium.Helium
import laika.theme.ThemeProvider

object AxleLaika {

    def theme(version: String): ThemeProvider =
        Helium
        .defaults
        .site.tableOfContent(title = "Contents", depth = 4)
        .site.landingPage(
          // logo = Some(Image(Root / "images" / "axle.png")),
          title = Some("Axle"),
          subtitle = Some("Lawful Scientific Computing for Scala"),
          latestReleases = Seq(
            ReleaseInfo("Latest Stable Release", version)
          ),
          license = Some("Apache 2.0"),
          documentationLinks = Seq(
            TextLink.internal(Root / "Introduction.md", "Introduction"),
            TextLink.internal(Root / "Foundation.md", "Foundation"),
            TextLink.internal(Root / "Units.md", "Units of Measurement"),
            TextLink.internal(Root / "Math.md", "Math"),
            TextLink.internal(Root / "Visualization.md", "Visualization"),
            TextLink.internal(Root / "RandomnessUncertainty.md", "Randomness and Uncertainty"),
            TextLink.internal(Root / "GameTheory.md", "Game Theory"),
            TextLink.internal(Root / "ChaosTheory.md", "Chaos Theory"),
            TextLink.internal(Root / "MachineLearning.md", "Machine Learning"),
            TextLink.internal(Root / "Biology.md", "Biology"),
            TextLink.internal(Root / "Text.md", "Text"),
            TextLink.internal(Root / "QuantumCircuits.md", "Quantum Circuits"),
            TextLink.internal(Root / "Appendix.md", "Appendix")
          ),
          projectLinks = Seq(
            // IconLink.internal(Root / "doc-2.md", HeliumIcon.demo),
            // ButtonLink.external("https://github.com/axlelang/axle", "Source on GitHub")
          ),
          teasers = Seq(
            // Teaser("Teaser 1", "Description 1"),
          ))
        .site.downloadPage(
          title = "Documentation Downloads",
          description = Some("PDFs"),
          downloadPath = laika.ast.Path.Root / "downloads",
          includeEPUB = false,
          includePDF = true)
        .pdf.tableOfContent(title = "Contents", depth = 4)
        .build
}