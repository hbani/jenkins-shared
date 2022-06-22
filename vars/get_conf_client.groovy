def call(client_name, classname) {
  if ( fileExists("${WORKSPACE}/${client_name}.yaml") ) {
    def clientYaml = readYaml(file: "${WORKSPACE}/${client}.yaml")
    return clientYaml
  }
  def yamlString = libraryResource('config/${classname}.yaml')
  Object conf = readYaml(text: yamlString)
  return conf.defaults + conf."$client_name"
}
