def call(client_name) {

  def conf = get_conf_client(client_name,"CompassSimulation")

  default_params = get_class_params('javaDefault',conf+['env':env,'params':params])

  CompassSimulation = get_class_params('CompassSimulation',conf+['env':env,'params':params])

  println default_params
  println CompassSimulation
}
