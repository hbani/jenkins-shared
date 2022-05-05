def call(client_name) {
  def awsCredentials = get_aws_cred()
  dir ("config") {
      def conf = get_conf_client(client_name)
      configRoot = pwd()

      downloadFileFromS3(awsCredentials, "s3://compass-simulations-config/"+conf.config.bootstrapProperties, configRoot)

      hedgingConfiguration = downloadFileFromS3(awsCredentials,  "s3://compass-simulations-config/"+conf.config.configurationS3UrlPrefix, configRoot)
  }
  save_conf_client('hfmarkets',[configRoot: configRoot, hedgingConfiguration: hedgingConfiguration])
}
