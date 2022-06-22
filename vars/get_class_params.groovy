import groovy.text.GStringTemplateEngine

def call(name,Config) {
return GStringTemplateEngine
      .newInstance()
      .createTemplate(libraryResource("templates/${name}.params"))
      .make(config)
      .toString()
}
