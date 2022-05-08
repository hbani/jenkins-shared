def call(client_name) {
  if ( fileExists("${WORKSPACE}/${client_name}.yaml") ) {
    def clientYaml = readYaml(file: "${WORKSPACE}/${client_name}.yaml")
    return clientYaml
  }
  def file = new File("${WORKSPACE}/jenkins\config/analytics/jenkinsfiles/customer-simulation-client.yaml")
  assert file.exists() : "Clients config file not found"
  def conf = readYaml(file:'${WORKSPACE}/jenkins\config/analytics/jenkinsfiles/customer-simulation-client.yaml')
  return conf."$client_name"
}
