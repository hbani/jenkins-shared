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
