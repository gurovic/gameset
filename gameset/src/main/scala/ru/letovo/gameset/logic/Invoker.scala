package ru.letovo.gameset.logic

import java.io.File
import java.lang.{Process, ProcessBuilder}
import java.time.{Duration, Instant}
import java.util.concurrent.TimeUnit
import scala.io.Source
import scala.sys.env
import scala.sys.process._
import scala.util.Random

sealed trait InvokerState;
case class InvokerCreated() extends InvokerState
case class InvokerRunning(proc: Process, startedAt: Instant) extends InvokerState
case class InvokerFinished(report: InvokerReport) extends InvokerState

class Invoker(path: String, argv: Seq[String]) {
  var state: InvokerState = InvokerCreated()

  var roMount: Seq[(String, String)] = Seq()
  var rwMount: Seq[(String, String)] = Seq()
  var wallTimeLimitMs: Long = 2000
  var cpuTimeLimitMs: Long = 1000
  var memoryLimitMb: Long = 256
  var stdin: Option[String] = None
  var stdout: Option[String] = None
  var stderr: Option[String] = None

  private def optArg(opt: String, x: Option[String]) = x.map(Seq(opt, _)).getOrElse(Seq())

  private val CMD = Seq("nsjail", "-x", path) ++
    optArg("-c", env.get("INV_CHROOT")) ++
    optArg("-u", env.get("INV_USER")) ++
    optArg("-g", env.get("INV_GROUP")) ++
    Seq("-H", "gameset") ++
    Seq("--disable_proc") ++
    roMount.flatMap { x => Seq("-R", s"${x._1}:${x._2}") } ++
    rwMount.flatMap { x => Seq("-B", s"${x._1}:${x._2}") } ++
    Seq("--rlimit_as", memoryLimitMb.toString)


  def run(observer: InvokerObserver, request_id: String): Unit = { // TODO CompilerReport
    val thread = new Thread {
      override def run(): Unit = {
        val logFile = "/tmp/invoker" + new Random(System.currentTimeMillis()).nextInt().toString + ".log"
        new File(logFile).createNewFile()
        val log = Source.fromFile(logFile)
        try {
          val finalSeq = CMD ++ Seq("-l", logFile, "--") ++ argv
          val proc = new ProcessBuilder(finalSeq: _*)
            .redirectInput(new File(stdin.getOrElse("/dev/null")))
            .redirectOutput(new File(stdout.getOrElse("/dev/null")))
            .redirectError(new File(stderr.getOrElse("/dev/null")))
            .start()
          val startTime = Instant.now()
          state = InvokerRunning(proc, Instant.now())
          proc.waitFor(wallTimeLimitMs, TimeUnit.MILLISECONDS)

          val status = if (proc.isAlive) {
            proc.destroyForcibly()
            ProcessStatus.WallTimeLimitExceeded // TODO check for cpu time limit
          } else {
            ProcessStatus.OK
          }

          val report = InvokerReport( // TODO use logs in report
              proc.exitValue(),
              status,
              log.mkString,
              proc.info().totalCpuDuration().orElse(Duration.ZERO).toMillis.toInt,
              (Instant.now().toEpochMilli - startTime.toEpochMilli).toInt,
              0 // TODO
            )

          state = InvokerFinished(report)

          observer.recieve(request_id)
        } finally {
          log.close()
          new File(logFile).delete()
        }
      }
    }
    thread.run();
  }
}
