def call(client_name) {
    def conf = get_conf_client(client_name)
    def additionalSysprops = ""
    def additionalArgs = ""
    def pos = ""
    if (conf.additionalSysprops != null && conf.additionalSysprops != "" && (conf.additionalSysprops instanceof java.util.ArrayList)){
      additionalSysprops = conf.additionalSysprops.collect { shellString(it) }.join(' ')
      }

    if (conf.additionalArgs != null && conf.additionalArgs != "" && (conf.additionalArgs instanceof java.util.ArrayList)){
      additionalArgs = conf.additionalArgs.collect { shellString(it) }.join(' ')
    }

    if(conf.positionsS3Url != null && conf.positionsS3Url != ""){
        pos="--positionsInput="+conf.positionsS3Url
    }

    fastMarkets = "    --forceConfig=pricing.fastmarket.config=null \\"
    if (conf.disableFastMarkets != null && conf.disableFastMarkets == "false"){
        fastMarkets = " \\"
    }

    hybridInstrumentToProfileMappings = " \\"
    if (conf.hybridInstrumentToProfileMappings != null && conf.hybridInstrumentToProfileMappings != ""){
        hybridInstrumentToProfileMappings="--forceConfig=hedging.rules.profiles.instrumentToProfileMappings=json:"+conf.hybridInstrumentToProfileMappings+" \\"
    }
    save_conf_client(client_name,[additionalSysprops: additionalSysprops, additionalArgs: additionalArgs,pos: pos, fastMarkets: fastMarkets, hybridInstrumentToProfileMappings: hybridInstrumentToProfileMappings])
}
