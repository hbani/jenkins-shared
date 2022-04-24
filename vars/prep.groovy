def get_conf_client(client_name) {
  if ( fileExists("${WORKSPACE}/${client_name}.yaml") ) {
    def clientYaml = readYaml(file: "${WORKSPACE}/${client_name}.yaml")
    return clientYaml
  }
  def yamlString = libraryResource('clients/config/clients.yaml')
  Object conf = readYaml(text: yamlString)
  return conf."$client_name"
}

def save_conf_client(client_name,config) {
  Object getconfig = get_conf_client(client_name)
  for ( e in config ) {
    getconfig.config.put(e.key,e.value)
    }
  println(getconfig)
  if (getconfig instanceof Map) {
  for (Object entry : ((Map)getconfig).entrySet()) {
                if (!isValidObjectType(((Map.Entry)entry).getKey()) || !isValidObjectType(((Map.Entry)entry).getValue())) {
                    println false;
                }
            }
    println true;
  }
  println(getconfig.getClass())
  writeYaml(file: "${WORKSPACE}/${client_name}.yaml",data: getconfig,overwrite: true)
  def clientYaml = readYaml(file: "${WORKSPACE}/${client_name}.yaml")
  return clientYaml
}

def get_aws_cred() {
  return readJSON(text: sh(script: "curl http://169.254.169.254/latest/meta-data/iam/security-credentials/mgmt-eu-west-2-prod-ci-slave-backtest", returnStdout: true))
}

def config_download(client_name) {
  def awsCredentials = get_aws_cred()
  dir ("config") {
      def conf = get_conf_client(client_name)
      configRoot = pwd()

      common.downloadFileFromS3(awsCredentials, "s3://compass-simulations-config/"+conf.config.bootstrapProperties, configRoot)

      hedgingConfiguration = common.downloadFileFromS3(awsCredentials,  "s3://compass-simulations-config/"+conf.config.configurationS3UrlPrefix, configRoot)
  }
  save_conf_client('hfmarkets',[configRoot: configRoot, hedgingConfiguration: hedgingConfiguration])
}

def create_output_path() {
  dir ("output") {
      outputPath = pwd()

      // Create the damn folder
      writeFile file:'dummy', text:''
  }
}
