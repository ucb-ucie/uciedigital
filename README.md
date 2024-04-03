# UCIe Digital IP

An open-source digital implementation of the UCIe 1.1 specification.

You can request a copy of the UCIe 1.1 specification [here](https://www.uciexpress.org/1-1-spec-download).

## Getting Started

Install [sbt](https://www.scala-sbt.org/).
If you run into issues building the Scala code,
you may need to switch to Java 17.

To run the tests, run `sbt test` from the root of this repository.
To compile the code, run `sbt compile`.
To format the code using scalafmt, run `sbt scalafmt`.

If you are running `sbt` commands frequently, you may find it useful
to leave the sbt shell (launched by running `sbt` with no arguments) open.
You can then compile/test the code from the sbt shell.

`sbt` can not [compile the Scala compiler bridge under JDK21](https://github.com/sbt/sbt/issues/7235) (without bumping the Scala minor version - for which the chisel compiler plugin is unavailable).
To work around this, install an older JDK and pass its path to sbt (e.g. `sbt --java-home /usr/lib/jvm/java-17-openjdk/`).

## Documentation

See the [API documentation](https://ucb-ucie.github.io/uciedigital/edu/berkeley/cs/ucie/digital/index.html).
The API docs are updated automatically in continuous integration.

## Contributing

If you'd like to contribute, please let us know. You can:

- Open an issue.
- Email vikramj@berkeley.edu and rahulkumar@berkeley.edu.

Documentation updates, tests, and bugfixes are always welcome.
For larger feature additions, please discuss your ideas with us before implementing them.

Contributions can be submitted by opening a pull request against the main branch of this repository.

Unless you explicitly state otherwise, any contribution intentionally submitted for inclusion in the work by you shall be licensed under the BSD 3-Clause license, without any additional terms or conditions.
