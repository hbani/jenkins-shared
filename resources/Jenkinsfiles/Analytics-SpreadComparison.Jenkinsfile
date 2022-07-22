@Library('jenkins-shared') _

node ('backtest_slave') {
  def classname = "SpreadComparison"
  properties([
          parameters([
              string(name: 'client_name', description: 'Which <b>client</b> does this run relate to?'),
              string(name: 'description', description: '<i>Optional</i> description of run'),
              string(name: 'pricingMarket', description: 'E.g. CLIENT_PRICE_PLATINUM / CLIENT_PRICE_TITANIUM'),
              string(name: 'benchmarkMarkets', description: 'E.g. CLIENT_PRICE_PLATINUM / CLIENT_PRICE_TITANIUM'),
              string(name: 'percentile', description: 'percentile'),
              booleanParam(name: 'useAggregateSpread',
                  description: 'whether to disable fast markets',
                  defaultValue: true
              ),

            // Common extended options
            text(name: 'additionalArgs', description: '<i>Optional</i> Backtest arguments. ONE PER LINE - quotes are automatically escaped and single quotes will automatically be added around each line!!'),
            text(name: 'additionalSysprops', description: '<i>Optional</i> JVM system properties. ONE PER LINE - quotes are automatically escaped and single quotes will automatically be added around each line!'),
            string(name: 'alternateProcessNames', description: '<i>Optional</i> Names of processes which config will be picked up from'),
            string(name: 'email_notify', description: '<i>Optional</i> Comma-separated list of email address that should receive notifications on completion (success or failure)'),
            choice(choices: ['INFO', 'DEBUG', 'TRACE'], description: 'Log level', name: 'mahiLogLevel'),
          ])
      ])

      try {
      stage('Preparation') {

          sh "rm -rf ${WORKSPACE}/${params.client_name}.yaml"

          config_download(params.client_name,classname)

          create_output_path(params.client_name,classname)
      }
      // Time out the build if the console isn't updated for 3 hours
      timeout(activity: true, time: 3, unit: 'HOURS') {
          stage('Backtest') {

              print_params(params.client_name,classname)

              run_command(params.client_name,classname)
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
              emailNotify(params.client_name,email_notify)
          }
      }
}
