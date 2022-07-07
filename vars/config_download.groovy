def call(client_name,classname) {
  def awsCredentials = get_aws_cred()
  dir ("config") {
      def conf = get_conf_client(client_name,classname)
      tempconfig = [:]
      configRoot = pwd()
      tempconfig.put("configRoot",configRoot)

      downloadFileFromS3(awsCredentials, "s3://compass-simulations-config/"+conf.bootstrapProperties, configRoot)

  }
  save_conf_client(client_name,classname,tempconfig)
}
