def call(client_name,class) {
  if ( fileExists("${WORKSPACE}/${client_name}.yaml") ) {
    def clientYaml = readYaml(file: "${WORKSPACE}/${client}.yaml")
    return clientYaml
  }
  def yamlString = libraryResource('config/${class}.yaml')
  Object conf = readYaml(text: yamlString)
  return conf.defaults + conf."$client_name"
}
