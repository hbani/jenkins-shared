def call(client_name) {
  if ( fileExists("${WORKSPACE}/${client_name}.yaml") ) {
    def clientYaml = readYaml(file: "${WORKSPACE}/${client_name}.yaml")
    return clientYaml
  }
  def yamlString = libraryResource('clients/config/clients.yaml')
  Map conf = readYaml(text: yamlString)
  return conf."$client_name"
}
