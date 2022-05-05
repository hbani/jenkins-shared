def call(client_name) {
  dir ("output") {
      outputPath = pwd()

      // Create the damn folder
      writeFile file:'dummy', text:''
  }
  save_conf_client(client_name,[outputPath: outputPath])
}
