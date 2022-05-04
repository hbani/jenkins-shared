def initialize_backtest_vars(client_name) {
    def conf = prep.get_conf_client(client_name)
    def additionalSysprops = ""
    def additionalArgs = ""
    def pos = ""
    if (conf.config.additionalSysprops != null && conf.config.additionalSysprops != ""){
      println conf.config.additionalSysprops
      additionalSysprops = conf.config.additionalSysprops.collect { common.shellString(it) }.join(' ')
      }

    if (conf.config.additionalArgs != null && conf.config.additionalArgs != ""){
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

def compass_simulation(client_name) {
def conf = prep.get_conf_client(client_name)
to_from = common.to_from()
sh  """
cd /app/fx/apps/mahifx/

sed -i 's/arbHedger1/backtest/g' \"${conf.config.hedgingConfiguration}\"
sed -i 's/hybridHedger1/backtest/g' \"${conf.config.hedgingConfiguration}\"

df -h
free -m
du -ms /tmp/*

echo \
    -cp current/lib:current/lib/* \
    -Duser.timezone=UTC \
    -Xmx11G \
    -Xms11G \
    -XX:+UseParallelGC \
    -XX:+PerfDisableSharedMem \
    -XX:+HeapDumpOnOutOfMemoryError \
    -DbuilderHelperFactory=com.x.integration.MahiSimulationProcessBuilderHelperFactory \
    -DGraphPerformanceInstrumentation.PERFORMANCE_INSTRUMENTATION_MODE=Minimal \
    -Dbootstrap.properties=file:${conf.config.configRoot}/${conf.config.bootstrapProperties} \
    -Dlic.file=file:shared/conf/license.lic \
    -DFileStorageProcessBuilderHelper.DataStore=/media/ephemeral0/tickstore/ \
    -Droot.log.level=ERROR \
    -Dmahi.root.log.level=INFO \
    -Dbacktest.manage.clientTrades=true \
    -Dbackstatsprovider.suppress.dump=true \
    -Dbacktest.alternateProcessNames=${params.alternateProcessNames} \
    -Dcompass.backtest=true \
    -Dfile.encoding=UTF-8 \
    ${conf.config.additionalSysprops} \
    com.x.integration.analytics.CompassSimulationProcess \
    --name=backtest \
    --env=local-live,backtest \
    --loadConfig=\"${conf.config.configurationS3Url}\" \
    ${conf.config.fastMarkets}
    --forceConfig=risk.varEquivalentRisk.maxVarLevel=double:${conf.config.maxVarLevel} \
    --forceConfig=hedging.rules.aggressive.dynamicOrderSpeedScaling=double:${conf.config.dynamicOrderSpeedScaling} \
    --forceConfig=analytics.enrichedTradeOutput.persist=boolean:false \
    --forceConfig=analytics.enrichedTradeOutputSummary.persist=boolean:false \
    --forceConfig=analytics.hedgingDecisions.persist=boolean:false \
    ${conf.config.hybridInstrumentToProfileMappings}
    --graphBuilderHelperFactory=com.x.processbuilder.SimulationGraphBuilderHelperFactory \
    --jmsStoreUrl=\"${conf.config.jmsStoreUrl}\" \
    --tickStoreUrl=\"${conf.config.tickStoreUrl}\" \
    --outPath=\"${conf.config.outputPath}\" \
    --client=\"${client_name}\" \
    --jobId=\"${BUILD_TAG}\" \
    --jobDescription=\"${params.goal}\" \
    --from=\"${to_from.from}\" \
    --to=\"${to_from.to}\" \
    ${conf.config.additionalArgs} \
    ${conf.config.pos} \

"""
}
