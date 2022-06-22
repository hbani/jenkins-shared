import groovy.text.GStringTemplateEngine

def call(name,config) {
return GStringTemplateEngine
      .newInstance()
      .createTemplate(libraryResource("templates/${name}.params"))
      .make(config)
      .toString()
}
