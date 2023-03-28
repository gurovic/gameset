package compiler

import java.io.File
import scala.concurrent.{ExecutionContext, Future}
import scala.sys.process._

object Compiler {
    def compile(path: String, args: Seq[String])(implicit ec: ExecutionContext): Future[Option[String]] = {
        val compiledPath = path + ".out"
        if (new File(compiledPath).exists()) {
           Future(Some(compiledPath))
        } else {
            val cmds = "clang++ --std=gnu++20 '" + path + "' -g -O3 -o " + compiledPath ++ " " ++ args.reduce((a, b) => a ++ " " ++ b)
            println(cmds)
            val cmd = Process(cmds)
            Future {
                if (cmd.run().exitValue() == 0) Some(compiledPath) else None
            }
        }
    }
}
