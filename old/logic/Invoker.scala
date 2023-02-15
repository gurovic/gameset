import scala.sys.process;

class Invoker(executable: String, redirect_stdin: String, redirect_stdout: String, argv: Array[String]) {
  def run(wait: Boolean): Int = {
    var proc = process.Process(Seq(executable) ++ argv);
    proc = proc #< new java.io.File(redirect_stdin);
    proc = proc #> new java.io.File(redirect_stdout);
    proc.run();
    if (wait) {
      return proc.!;
    }
    return 0;
  }
}
