@Library('Analytics') _

node ('backtest_slave') {
properties([
        parameters([
            string(name: 'client_name', description: 'Which <b>client</b> does this run relate to?'),
            string(name: 'classname', description: 'Which <b>Job</b> does this run relate to?'),
            string(name: 'description', description: 'Optional description of run'),
            string(name: 'email_notify', description: 'Comma-separated list of email address that should receive notifications on completion (success or failure)'),
            string(name: 'pricingMarket', description: 'CLIENT_PRICE_PLATINUM CLIENT_PRICE_TITANIUM'),
            string(name: 'benchmarkMarkets', description: 'CLIENT_PRICE_PLATINUM CLIENT_PRICE_TITANIUM'),
            choice(choices: ['INFO', 'DEBUG', 'TRACE'], description: 'Log level', name: 'mahiLogLevel'),
            string(name: 'percentile', description: 'percentile'),
            text(name: 'additionalArgs', description: 'Backtest arguments. ONE PER LINE - quotes are automatically escaped and single quotes will automatically be added around each line!!'),
            booleanParam(name: 'useAggregateSpread',
                description: 'whether to disable fast markets',
                defaultValue: true
            ),
        ])
    ])

    try {
    stage('Preparation') {

        sh "rm -rf ${WORKSPACE}/${params.client_name}.yaml"

        config_download(params.client_name,params.classname)

        create_output_path(params.client_name,params.classname)
    }
    // Time out the build if the console isn't updated for 3 hours
    timeout(activity: true, time: 3, unit: 'HOURS') {
        stage('Backtest') {

            print_params(params.client_name,params.classname)

            spread_comparison(params.client_name)
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
