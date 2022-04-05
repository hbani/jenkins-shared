def get_conf_client(client_name) {
  def conf = yaml libraryResource('clients/config/clients.yaml')
  println conf
  // return conf.$client_name
}

def get_aws_cred() {
  return readJSON(text: sh(script: "curl http://169.254.169.254/latest/meta-data/iam/security-credentials/mgmt-eu-west-2-prod-ci-slave-backtest", returnStdout: true))
}

def config_download(client_name) {
  def awsCredentials = get_aws_cred()
  dir ("config") {
      def conf = get_conf_client(client_name)
      configRoot = pwd()

      downloadFileFromS3(awsCredentials, "s3://compass-simulations-config/"+conf.config.bootstrapProperties, configRoot)

      hedgingConfiguration = downloadFileFromS3(awsCredentials, conf.config.configurationS3UrlPrefix, configRoot)
  }
  return hedgingConfiguration
}

def create_output_path(client_name) {
  dir ("output") {
      outputPath = pwd()

      // Create the damn folder
      writeFile file:'dummy', text:''
  }
}
