# Buid and Deploy

For contributors

## Publish snapshots

Push commits to repo.

Monitor [progress](https://github.com/axlelang/axle/actions/workflows/ci-release.yml) of github action.

Confirm jars are present at the [sonatype snapshot repo](https://oss.sonatype.org/content/repositories/snapshots/org/axle-lang/)

## Release new version

For example, tag with a version:

```bash
git tag -a v0.1.6 -m "v.0.1.6"
git push origin v0.1.6
```

Monitor [progress](https://github.com/axlelang/axle/actions/workflows/ci-release.yml)

Confirm jars are present at the [sonatype repo](https://oss.sonatype.org/content/repositories/releases/org/axle-lang/)

## Update Site

Run the `site-update.sh` script

Monitor [progress](https://github.com/axlelang/axle/actions/workflows/pages/pages-build-deployment) of action.

Verify by browsing to the [site](https://www.axle-lang.org) or look at
the `gh-pages` [branch](https://github.com/axlelang/axle/tree/gh-pages)

## Verify before update

Just to do the build locally, run

```bash
sbt -J-Xmx8G 'project axle-docs' clean mdoc laikaSite
```

To preview the changes, do:

```bash
sbt 'project axle-docs' laikaPreview
```

then browse to [https://localhost:4242](https://localhost:4242)

If it looks good, push with:

```bash
sbt 'project axle-docs' ghpagesCleanSite ghpagesPushSite
```

Monitor and verify as before.

## References

* [Laika](https://planet42.github.io/Laika/index.html)
  * [http4s Laika PR](https://github.com/http4s/http4s/pull/5313)
* [sbt-site](https://www.scala-sbt.org/sbt-site/)
* [sbt-ghpages](https://github.com/sbt/sbt-ghpages)
  * Note the instructions to set up a `gh-pages` branch
* [custom domain for github pages](https://docs.github.com/en/pages/configuring-a-custom-domain-for-your-github-pages-site)
  * Note instructions for apex domains
* [sbt-sonatype](https://github.com/xerial/sbt-sonatype)
  * [sonatype](https://oss.sonatype.org/) using credentials in `~/.sbt/1.0/sonatype.sbt`
* [sbt-ci-release](https://github.com/sbt/sbt-ci-release)
