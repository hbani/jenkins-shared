def call() {
  def toDate = new Date()
  def fromDate = new Date(toDate.getTime() - (1000 * 60 * 60 * 24 * 7))
  def to = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(java.time.ZoneOffset.UTC).toFormat().format(toDate.toInstant())
  def from = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(java.time.ZoneOffset.UTC).toFormat().format(fromDate.toInstant())
  def map = [to: to, from: from]
  return map
}
