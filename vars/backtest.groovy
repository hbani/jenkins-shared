def initialize_backtest_vars(client_name) {
    def conf = prep.get_conf_client(client_name)
    if (conf.config.additionalSysprops != != null && conf.config.additionalSysprops != ""){
      additionalSysprops = conf.config.additionalSysprops.split(/[\r\n]/).collect { common.shellString(it) }.join(' ')
      }

    if (conf.config.additionalArgs != null && conf.config.additionalArgs != ""){
      additionalArgs = conf.config.additionalArgs.split(/[\r\n]/).collect { common.shellString(it) }.join(' ')
    }

    if(conf.config.positionsS3Url != null && conf.config.positionsS3Url != ""){
        pos="--positionsInput="+conf.config.positionsS3Url
    }

    fastMarkets = "    --forceConfig=pricing.fastmarket.config=null \\"
    if (conf.config.disableFastMarkets != null && conf.config.disableFastMarkets == "false"){
        fastMarkets = " \\"
    }

    hybridInstrumentToProfileMappings = " \\"
    if (conf.config.hybridInstrumentToProfileMappings != null && conf.config.hybridInstrumentToProfileMappings != ""){
        hybridInstrumentToProfileMappings="--forceConfig=hedging.rules.profiles.instrumentToProfileMappings=json:"+conf.config.hybridInstrumentToProfileMappings+" \\"
    }
    save_conf_client('hfmarkets',[additionalSysprops: additionalSysprops, additionalArgs: additionalArgs,pos: pos, fastMarkets: fastMarkets, hybridInstrumentToProfileMappings: hybridInstrumentToProfileMappings])
}

def print_params(client_name) {
  def conf = prep.get_conf_client(client_name)
  to_from = common.to_from()
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
