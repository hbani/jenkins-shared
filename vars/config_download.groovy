def call(client_name,classname) {
  def awsCredentials = get_aws_cred()
  dir ("config") {
      def conf = get_conf_client(client_name,classname)
      configRoot = pwd()

      downloadFileFromS3(awsCredentials, "s3://compass-simulations-config/"+conf.bootstrapProperties, configRoot)

      hedgingConfiguration = downloadFileFromS3(awsCredentials,  "s3://compass-simulations-config/"+conf.configurationS3UrlPrefix, configRoot)
  }
  save_conf_client(client_name,classname,[configRoot: configRoot, hedgingConfiguration: hedgingConfiguration])
}
