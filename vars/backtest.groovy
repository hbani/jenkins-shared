def initialize_backtest_vars(client_name) {
    def conf = prep.get_conf_client(client_name)
    constants.additionalSysprops = params.additionalSysprops.split(/[\r\n]/).collect { shellString(it) }.join(' ')
    constants.additionalArgs = params.additionalArgs.split(/[\r\n]/).collect { shellString(it) }.join(' ')

    if(params.positionsS3Url != null && params.positionsS3Url != ""){
        constants.pos="--positionsInput="+params.positionsS3Url
    }

    constants.fastMarkets = "    --forceConfig=pricing.fastmarket.config=null \\"
    if (conf.config.disableFastMarkets != null && conf.config.disableFastMarkets == "false"){
        constants.fastMarkets = " \\"
    }

    constants.hybridInstrumentToProfileMappings = " \\"
    if (conf.config.hybridInstrumentToProfileMappings != null && conf.config.hybridInstrumentToProfileMappings != ""){
        constants.hybridInstrumentToProfileMappings="--forceConfig=hedging.rules.profiles.instrumentToProfileMappings=json:"+conf.config.hybridInstrumentToProfileMappings+" \\"
    }
}


def print_params(client_name) {
  def conf = prep.get_conf_client(client_name)
  to_from = common.to_from()
  echo """
  Customer: ${client_name}
  Goal: ${params.goal}
  Changes: ${params.changes}
  MaxVarLevel: ${conf.config.maxVarLevel}
  Config: ${conf.config.configurationS3Url} -> ${constants.hedgingConfiguration}
  From: ${to_from.from}
  To: ${to_from.to}
  HybridInstrumentToProfileMappings: ${conf.config.hybridInstrumentToProfileMappings}
  Additional args: ${constants.additionalArgs}
  DTASpeedScaling: ${conf.config.dynamicOrderSpeedScaling}
  Sysprops: ${constants.additionalSysprops}
  Output: ${constants.outputPath}
  """
}
