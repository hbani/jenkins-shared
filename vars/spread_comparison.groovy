import groovy.text.GStringTemplateEngine

def call(client_name) {
  def conf = get_conf_client(client_name,"SpreadComparison")

  default_params = get_class_params('javaDefault',conf+['env':env,'params':params])

  SpreadComparison = get_class_params('SpreadComparison',conf+['env':env,'params':params])

  sh """
  cd /app/fx/apps/mahifx/

  df -h
  free -m
  du -ms /tmp/*

  java \
    -cp current/lib:current/lib/* \
    ${default_params} \
    ${SpreadComparison}
  """
}
