node ('backtest_slave') {
    def err
    def configRoot
    def inputData
    def outputPath
    def hedgingConfiguration

    try {
        stage('Preparation') { 
            // Until pipeline-aws is available or Jenkins fix https://github.com/jenkinsci/aws-credentials-plugin/issues/29
            def awsCredentials = readJSON(text: sh(script: "curl http://169.254.169.254/latest/meta-data/iam/security-credentials/mgmt-eu-west-2-prod-ci-slave-backtest", returnStdout: true))

            // Create config subdir in workspace and download
            dir ("config") {
                configRoot = pwd()

                downloadFileFromS3(awsCredentials, "s3://compass-simulations-config/"+params.bootstrapProperties, configRoot)

                hedgingConfiguration = downloadFileFromS3(awsCredentials, params.configurationS3Url, configRoot)
            }

            dir ("output") {
                outputPath = pwd()

                // Create the damn folder
                writeFile file:'dummy', text:''
            }
        }

        // Time out the build if the console isn't updated for 3 hours
        timeout(activity: true, time: 3, unit: 'HOURS') {
            stage('Backtest') {
                def additionalSysprops = params.additionalSysprops.split(/[\r\n]/).collect { shellString(it) }.join(' ')
                def additionalArgs = params.additionalArgs.split(/[\r\n]/).collect { shellString(it) }.join(' ')

                def pos = ""
                if(params.positionsS3Url != null && params.positionsS3Url != ""){
                    pos="--positionsInput="+params.positionsS3Url
                }

                def fastMarkets = "    --forceConfig=pricing.fastmarket.config=null \\"
                if (params.disableFastMarkets != null && params.disableFastMarkets == "false"){
                    fastMarkets = " \\"
                }

                def hybridInstrumentToProfileMappings = " \\"
                if (params.hybridInstrumentToProfileMappings != null && params.hybridInstrumentToProfileMappings != ""){
                    hybridInstrumentToProfileMappings="--forceConfig=hedging.rules.profiles.instrumentToProfileMappings=json:"+params.hybridInstrumentToProfileMappings+" \\"
                }

                echo """
Customer: ${params.client}
Goal: ${params.goal}
Changes: ${params.changes}
Input: ${params.positionsS3Url}
MaxVarLevel: ${params.maxVarLevel}
Config: ${params.configurationS3Url} -> ${hedgingConfiguration}
HybridInstrumentToProfileMappings: ${params.hybridInstrumentToProfileMappings}
Additional args: ${additionalArgs}
DTASpeedScaling: ${params.dynamicOrderSpeedScaling}
Sysprops: ${additionalSysprops}
Output: ${outputPath}
From: ${from}
To: ${to}
"""

                sh """
cd /app/fx/apps/mahifx/

sed -i 's/arbHedger1/backtest/g' \"${hedgingConfiguration}\"
sed -i 's/hybridHedger1/backtest/g' \"${hedgingConfiguration}\"

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
    -Dbootstrap.properties=file:${configRoot}/${bootstrapProperties} \
    -Dlic.file=file:shared/conf/license.lic \
    -DFileStorageProcessBuilderHelper.DataStore=/media/ephemeral0/tickstore/ \
    -Droot.log.level=ERROR \
    -Dmahi.root.log.level=INFO \
    -Dbacktest.manage.clientTrades=true \
    -Dbackstatsprovider.suppress.dump=true \
    -Dbacktest.alternateProcessNames=${params.alternateProcessNames} \
    -Dcompass.backtest=true \
    -Dfile.encoding=UTF-8 \
    ${additionalSysprops} \
    com.x.integration.analytics.CompassSimulationProcess \
    --name=backtest \
    --env=local-live,backtest \
    --loadConfig=\"${params.configurationS3Url}\" \
    ${fastMarkets}
    --forceConfig=risk.varEquivalentRisk.maxVarLevel=double:${params.maxVarLevel} \
    --forceConfig=hedging.rules.aggressive.dynamicOrderSpeedScaling=double:${params.dynamicOrderSpeedScaling} \
    --forceConfig=analytics.enrichedTradeOutput.persist=boolean:false \
    --forceConfig=analytics.enrichedTradeOutputSummary.persist=boolean:false \
    --forceConfig=analytics.hedgingDecisions.persist=boolean:false \
    ${hybridInstrumentToProfileMappings}
    --graphBuilderHelperFactory=com.x.processbuilder.SimulationGraphBuilderHelperFactory \
    --jmsStoreUrl=\"${jmsStoreUrl}\" \
    --tickStoreUrl=\"${tickStoreUrl}\" \
    --outPath=\"${outputPath}\" \
    --client=\"${params.client}\" \
    --jobId=\"${BUILD_TAG}\" \
    --jobDescription=\"${params.goal}\" \
    --from=\"${from}\" \
    --to=\"${to}\" \
    ${additionalArgs} \
    ${pos} \
    
"""
            }
        }

        if (currentBuild.result == null) {
            currentBuild.result = 'SUCCESS'
        }

    } catch (InterruptedException t) {
        echo "Aborted"

        err = t

        if (currentBuild.result == null) {
            currentBuild.result = 'ABORTED'
        }

    } catch (Throwable t) {
        echo "Failed: ${t}"
        def sw = new StringWriter()
        t.printStackTrace(new PrintWriter(sw))
        echo sw.toString()

        err = t

        if (currentBuild.result == null) {
            currentBuild.result = 'FAILURE'
        }

    } finally {
        // Email notifications regardless of status.

        stage('Notifications') {
            def email_notify = params.email_notify?.trim()

            if (email_notify) {
                echo "Sending email to ${email_notify} - status is ${currentBuild.result}"

                // Interpolated header
                def emailBody = """
<h2>${env.JOB_NAME} - ${params.client} - Build # ${currentBuild.number} - ${currentBuild.result}</h2>

<h3>${params.client}</h3>
<table>
<tr><td><b>Goal:</b></td><td>${params.goal}</td></tr>
<tr><td><b>Changes:</b></td><td>${params.changes}</td></tr>
<tr><td><b>Data:</b></td><td>${params.inputS3Url}</td></tr>
<tr><td><b>MaxVarLevel:</b></td><td>${params.maxVarLevel}</td></tr>
<tr><td><b>Config:</b></td><td>${params.configurationS3Url}</td></tr>
</table>
"""

                // Not interpolated so can use ${BUILD_LOG_REGEX}
                emailBody += '''
<h3>Summary data</h3>
<pre>
${BUILD_LOG_REGEX, regex="\\\\*\\\\*REPORT\\\\*\\\\*"}
</pre>

<hr/>

<h3>Backtest Arguments</h3>
<pre>
${additionalArgs}
</pre>

<h3>System Properties</h3>
<pre>
${additionalSysprops}
</pre>

<h3>Log Tail</h3>
<pre>
${BUILD_LOG, maxLines=50}
</pre>

<p>Check console output at <a href="${BUILD_URL}">${BUILD_URL}</a> to view full results.</p>
</pre>
'''

                emailext (body: emailBody, subject: "${env.JOB_NAME} - ${params.client} - Build #${currentBuild.number} - ${currentBuild.result}", to: email_notify, mimeType: "text/html")

            }
        }
    }
}

def downloadFileFromS3(awsCredentials, s3Url, localDir) {
// TODO: Need Jenkins 2.60.3 for https://plugins.jenkins.io/pipeline-aws :(
//         withAws(credentials:'tickdata-s3-readonly') {
//            s3Download(file:'/', bucket:'compass-simulations-config', path:configRoot, force:true)
//         }

    s3Url = s3Url.replaceAll(/^https?:\/\/s3-[^\.\/]+\.amazonaws\.com\/([^\/]+)\//, 's3://$1/')

//   withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'JenkinsBacktestSlave', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) { 
    sh "s3cmd --verbose --preserve --access_key=${awsCredentials.AccessKeyId} --secret_key=\"${awsCredentials.SecretAccessKey}\" --access_token=\"${awsCredentials.Token}\" --force get ${s3Url} ${localDir}"
//   }

   return new File(localDir, s3Url.substring(s3Url.lastIndexOf('/') + 1));
}

// See: https://issues.jenkins-ci.org/browse/JENKINS-44231?focusedCommentId=323802&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-323802
//
// Given arbitrary string returns a strongly escaped shell string literal.
// I.e. it will be in single quotes which turns off interpolation of $(...), etc.
// E.g.: 1'2\3\'4 5"6 (groovy string) -> '1'\''2\3\'\''4 5"6' (groovy string which can be safely pasted into shell command).
def shellString(s) {
    if (s.trim() == '') {
        return '';
    }

    // Replace ' with '\'' (https://unix.stackexchange.com/a/187654/260156). Then enclose with '...'.
    // 1) Why not replace \ with \\? Because '...' does not treat backslashes in a special way.
    // 2) And why not use ANSI-C quoting? I.e. we could replace ' with \'
    // and enclose using $'...' (https://stackoverflow.com/a/8254156/4839573).
    // Because ANSI-C quoting is not yet supported by Dash (default shell in Ubuntu & Debian) (https://unix.stackexchange.com/a/371873).
    '\'' + s.replace('\'', '\'\\\'\'') + '\''
}
