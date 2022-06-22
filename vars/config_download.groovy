def call(client_name,classname) {
  def awsCredentials = get_aws_cred()
  dir ("config") {
      def conf = get_conf_client(client_name,classname)
      tempconfig = []
      configRoot = pwd()
      tempconfig.put("configRoot",configRoot)

      downloadFileFromS3(awsCredentials, "s3://compass-simulations-config/"+conf.bootstrapProperties, configRoot)

      if (conf.configurationS3UrlPrefix != null && conf.configurationS3UrlPrefix != "") {
      hedgingConfiguration = downloadFileFromS3(awsCredentials,  "s3://compass-simulations-config/"+conf.configurationS3UrlPrefix, configRoot)
      tempconfig.put("hedgingConfiguration",hedgingConfiguration)
      }
  }
  save_conf_client(client_name,classname,tempconfig)
}
