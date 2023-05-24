package ru.letovo.gameset.logic

import ru.letovo.gameset.logic.Config.{compiler_args, compiler_path}

import java.nio.file.{Files, Paths}

class Compiler {
  var objPath = ""
  var observer: (CompilationReport) => Any = (c: CompilationReport) => _

  def compile(path: String, observer: (CompilationReport) => Any): Unit = {
    this.observer = observer
    objPath = path.split(".").head

    val invoker = new Invoker(compiler_path, Seq(path, "-o " + objPath) ++ compiler_args)
    invoker.rwMount = Seq((objPath, objPath))

    val invokerRequest = new InvokerRequest(1, Array(invoker), onDone, None)

    InvokerPool.addToQueue(invokerRequest)
  }

  def onDone(): Unit = {
    var is_successful = false

    if (Files.exists(Paths.get(objPath))) {
      is_successful = true
    }

    observer(
      CompilationReport(is_successful, objPath)
    )
  }

}
