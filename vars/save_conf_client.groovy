def call(client_name,config) {
  Map getconfig = get_conf_client(client_name)
  for ( e in config ) {
    getconfig.config.put(e.key,"${e.value}")
    }
  sh  """
  rm -rf "${WORKSPACE}/${client_name}.yaml"
  """
  writeYaml(file: "${WORKSPACE}/${client_name}.yaml",data: getconfig,returnText: true)
  def clientYaml = readYaml(file: "${WORKSPACE}/${client_name}.yaml")
  println(clientYaml)
}
