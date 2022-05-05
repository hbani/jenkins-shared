def call(client_name,email_notify) {
def conf = get_conf_client(client_name)
if (email_notify) {
    echo "Sending email to ${email_notify} - status is ${currentBuild.result}"

    // Interpolated header
    def emailBody = """
<h2>${env.JOB_NAME} - ${client_name} - Build # ${currentBuild.number} - ${currentBuild.result}</h2>

<h3>${client_name}</h3>
<table>
<tr><td><b>Goal:</b></td><td>${params.goal}</td></tr>
<tr><td><b>Changes:</b></td><td>${params.changes}</td></tr>
<tr><td><b>Data:</b></td><td>${params.inputS3Url}</td></tr>
<tr><td><b>MaxVarLevel:</b></td><td>${conf.config.maxVarLevel}</td></tr>
<tr><td><b>Config:</b></td><td>$conf.config.configurationS3Url}</td></tr>
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
${conf.config.additionalArgs}
</pre>

<h3>System Properties</h3>
<pre>
</pre>

<h3>Log Tail</h3>
<pre>
${BUILD_LOG, maxLines=50}
</pre>

<p>Check console output at <a href="${BUILD_URL}">${BUILD_URL}</a> to view full results.</p>
</pre>
'''

    emailext (body: emailBody, subject: "${env.JOB_NAME} - ${client_name} - Build #${currentBuild.number} - ${currentBuild.result}", to: email_notify, mimeType: "text/html")
    }
}
