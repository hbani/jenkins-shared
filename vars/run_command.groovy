def call(client_name,classname) {

  def conf = get_conf_client(client_name,classname)

  default_params = get_class_params('javaDefault',conf+['env':env,'params':params])

  classparams = get_class_params(classname,conf+['env':env,'params':params])

  sh """
  cd /app/fx/apps/mahifx/

  df -h
  free -m
  du -ms /tmp/*

  echo "\
    -cp current/lib:current/lib/* \
    ${default_params} \
    ${classparams}
    "
  """
}
