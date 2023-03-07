case class InvokerReport(exitCode: Int, status: ProcessStatus.ProcessStatus, nsjailLog: String,
                         cpuTimeConsumedMs: Int, wallTimeConsumedMs: Int, memoryConsumedMb: Int)
