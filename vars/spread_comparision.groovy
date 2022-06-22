import groovy.text.GStringTemplateEngine

def call(client_name) {
def conf = get_conf_client(client_name,"SpreadComparision")

default_params = get_class_params('javaDefault',conf)
SpreadComparision = get_class_params('SpreadComparision',conf)

print default_params
print SpreadComparision

}
