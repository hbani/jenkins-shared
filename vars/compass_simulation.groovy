def call(client_name) {

  def conf = get_conf_client(client_name,"CompassSimulation")

  default_params = get_class_params('javaDefault',conf+['env':env,'params':params])

  CompassSimulation = get_class_params('CompassSimulation',conf+['env':env,'params':params])

  sh """
  cd /app/fx/apps/mahifx/

  df -h
  free -m
  du -ms /tmp/*

  java \
    -cp current/lib:current/lib/* \
    ${default_params} \
    ${CompassSimulation}

  """
}
