object ProcessStatus extends Enumeration {
  type ProcessStatus = Value
  val OK, CpuTimeLimitExceeded, WallTimeLimitExceeded, MemoryLimitExceeded, RuntimeError = Value
}
