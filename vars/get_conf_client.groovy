def call(client_name, classname) {
  Object conf = [:]
  if ( fileExists("${WORKSPACE}/${client_name}.yaml") ) {
    def clientYaml = readYaml(file: "${WORKSPACE}/${client_name}.yaml")
    return clientYaml
  }
  if ( libraryResource("config/${classname}.yaml" ) {
    def yamlStringclass = libraryResource("config/${classname}.yaml")
    Object confClass = readYaml(text: yamlStringclass)
    if (conf.containsKey(client_name)) {
      conf << conf."client_name"
    }
  }

  def yamlStringclients = libraryResource("config/clients.yaml")
  Object confClients = readYaml(text: yamlStringclients)
  conf << confClass.defaults + conf."$client_name"
  return conf
}
