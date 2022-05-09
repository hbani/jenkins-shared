def call(client_name,config) {
  Map getconfig = get_conf_client(client_name)
  for ( e in config ) {
    getconfig.put(e.key,"${e.value}")
    }
  writeYaml(file: "${WORKSPACE}/${client_name}.yaml",data: getconfig,overwrite: true)
}
