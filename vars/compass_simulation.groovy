def call(client_name) {
def conf = get_conf_client(client_name,"SpreadComparision")
to_from = to_from()
sh  """
cd /app/fx/apps/mahifx/

sed -i 's/arbHedger1/backtest/g' \"${conf.hedgingConfiguration}\"
sed -i 's/hybridHedger1/backtest/g' \"${conf.hedgingConfiguration}\"

df -h
free -m
du -ms /tmp/*

java \
    -cp current/lib:current/lib/* \
    -Duser.timezone=UTC \
    -Xmx11G \
    -Xms11G \
    -XX:+UseParallelGC \
    -XX:+PerfDisableSharedMem \
    -XX:+HeapDumpOnOutOfMemoryError \
    -DbuilderHelperFactory=com.x.integration.MahiSimulationProcessBuilderHelperFactory \
    -DGraphPerformanceInstrumentation.PERFORMANCE_INSTRUMENTATION_MODE=Minimal \
    -Dbootstrap.properties=file:${conf.configRoot}/${conf.bootstrapProperties} \
    -Dlic.file=file:shared/conf/license.lic \
    -DFileStorageProcessBuilderHelper.DataStore=/media/ephemeral0/tickstore/ \
    -Droot.log.level=ERROR \
    -Dmahi.root.log.level=INFO \
    -Dbacktest.manage.clientTrades=true \
    -Dbackstatsprovider.suppress.dump=true \
    -Dbacktest.alternateProcessNames=${params.alternateProcessNames} \
    -Dcompass.backtest=true \
    -Dfile.encoding=UTF-8 \
    ${conf.additionalSysprops} \
    com.x.integration.analytics.CompassSimulationProcess \
    --name=backtest \
    --env=local-live,backtest \
    --loadConfig=\"${conf.configurationS3Url}\" \
    ${conf.fastMarkets}
    --forceConfig=risk.varEquivalentRisk.maxVarLevel=double:${conf.maxVarLevel} \
    --forceConfig=hedging.rules.aggressive.dynamicOrderSpeedScaling=double:${conf.dynamicOrderSpeedScaling} \
    --forceConfig=analytics.enrichedTradeOutput.persist=boolean:false \
    --forceConfig=analytics.enrichedTradeOutputSummary.persist=boolean:false \
    --forceConfig=analytics.hedgingDecisions.persist=boolean:false \
    ${conf.hybridInstrumentToProfileMappings}
    --graphBuilderHelperFactory=com.x.processbuilder.SimulationGraphBuilderHelperFactory \
    --jmsStoreUrl=\"${conf.jmsStoreUrl}\" \
    --tickStoreUrl=\"${conf.tickStoreUrl}\" \
    --outPath=\"${conf.outputPath}\" \
    --client=\"${client_name}\" \
    --jobId=\"${BUILD_TAG}\" \
    --jobDescription=\"${params.goal}\" \
    --from=\"${to_from.from}\" \
    --to=\"${to_from.to}\" \
    ${conf.additionalArgs} \
    ${conf.pos} \

"""
}
