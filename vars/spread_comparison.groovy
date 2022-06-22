import groovy.text.GStringTemplateEngine

def call(client_name) {
def conf = get_conf_client(client_name,"SpreadComparison")

default_params = get_class_params('javaDefault',conf)
print default_params
SpreadComparison = get_class_params('SpreadComparison',conf+[environment:env,params:params])

print SpreadComparison

}
