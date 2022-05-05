def call(client_name) {
    def conf = prep.get_conf_client(client_name)
    def additionalSysprops = ""
    def additionalArgs = ""
    def pos = ""
    if (conf.config.additionalSysprops != null && conf.config.additionalSysprops != "" && (conf.config.additionalSysprops instanceof java.util.ArrayList)){
      additionalSysprops = conf.config.additionalSysprops.collect { common.shellString(it) }.join(' ')
      }

    if (conf.config.additionalArgs != null && conf.config.additionalArgs != "" && (conf.config.additionalArgs instanceof java.util.ArrayList)){
      println conf.config.additionalArgs
      additionalArgs = conf.config.additionalArgs.collect { common.shellString(it) }.join(' ')
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
    prep.save_conf_client('hfmarkets',[additionalSysprops: additionalSysprops, additionalArgs: additionalArgs,pos: pos, fastMarkets: fastMarkets, hybridInstrumentToProfileMappings: hybridInstrumentToProfileMappings])
}
