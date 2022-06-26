def call(client_name,classname) {
  dir ("output") {
      outputPath = pwd()
      writeFile file:'dummy', text:''
  }
  save_conf_client(client_name,classname,[outputPath: outputPath])
}
