def call(client_name,classname,config) {
  Map getconfig = get_conf_client(client_name,classname)
  for ( e in config ) {
    getconfig.put(e.key,"${e.value}")
    }
  writeYaml(file: "${WORKSPACE}/${client_name}.yaml",data: getconfig,overwrite: true)
}
