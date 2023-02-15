package compiler

import scala.concurrent.{ExecutionContext, Future}
import scala.sys.process._

class Compiler {
    def compile(path: String)(implicit ec: ExecutionContext): Future[Option[String]] = {
        val compiledPath = path + ".out"
        val cmd = Process("gcc '" + path + "' -g -O3 -o " + compiledPath)

        Future {
            if (cmd.run.exitValue() == 0) Some(compiledPath) else None
        }
    }
}
