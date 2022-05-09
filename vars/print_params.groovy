def call(client_name) {
  def conf = get_conf_client(client_name)
  to_from = to_from()
  echo """
  Customer: ${client_name}
  Goal: ${params.goal}
  Changes: ${params.changes}
  MaxVarLevel: ${conf.maxVarLevel}
  From: ${to_from.from}
  To: ${to_from.to}
  Config: ${conf.configurationS3Url} -> ${conf.hedgingConfiguration}
  HybridInstrumentToProfileMappings: ${conf.hybridInstrumentToProfileMappings}
  Additionalargs: ${conf.additionalArgs}
  DTASpeedScaling: ${conf.dynamicOrderSpeedScaling}
  Sysprops: ${conf.additionalSysprops}
  Output: ${conf.outputPath}
  """
}
