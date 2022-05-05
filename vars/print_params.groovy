def call(client_name) {
  def conf = get_conf_client(client_name)
  to_from = to_from()
  echo """
  Customer: ${client_name}
  Goal: ${params.goal}
  Changes: ${params.changes}
  MaxVarLevel: ${conf.config.maxVarLevel}
  From: ${to_from.from}
  To: ${to_from.to}
  Config: ${conf.config.configurationS3Url} -> ${conf.config.hedgingConfiguration}
  HybridInstrumentToProfileMappings: ${conf.config.hybridInstrumentToProfileMappings}
  Additionalargs: ${conf.config.additionalArgs}
  DTASpeedScaling: ${conf.config.dynamicOrderSpeedScaling}
  Sysprops: ${conf.config.additionalSysprops}
  Output: ${conf.config.outputPath}
  """
}
