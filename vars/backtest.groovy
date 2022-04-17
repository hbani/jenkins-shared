def to_from() {
  def toDate = new Date()
  def fromDate = new Date(toDate.getTime() - (1000 * 60 * 60 * 24 * 7))
  def to = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(java.time.ZoneOffset.UTC).toFormat().format(toDate.toInstant())
  def from = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(java.time.ZoneOffset.UTC).toFormat().format(fromDate.toInstant())
  def map = [to: to, from: from]
  return map
}

def print_params(client_name, hedgingconfig) {
  def conf = prep.get_conf_client(client_name)
  to_from = to_from()
  echo """
  Customer: ${client_name}
  Goal: ${params.goal}
  Changes: ${params.changes}
  MaxVarLevel: ${conf.config.maxVarLevel}
  Config: ${conf.config.configurationS3Url} -> ${hedgingconfig}
  From: ${to_from.from}
  To: ${to_from.to}
  """
}
