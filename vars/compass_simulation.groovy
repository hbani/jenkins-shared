def call(client_name) {

  def conf = get_conf_client(client_name,"CompassSimulation")
  binding = [
  "to_from": to_from(),
  "shellString": shellString,
  "env": env,
  "params": params
  ] + conf

println binding

  default_params = get_class_params('javaDefault',config)
  println default_params

  class_params = get_class_params('CompassSimulation',config)
  println class_params
}
