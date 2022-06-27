def call(client_name) {

  def conf = get_conf_client(client_name,"CompassSimulation")
  Map binding = [
  "to_from": to_from(),
  "shellString": shellString,
  "env": env,
  "params": params
  ] + conf

  shellString("hussain").getClass() 
  default_params = get_class_params('javaDefault',binding)
  println default_params

  class_params = get_class_params('CompassSimulation',binding)
  println class_params
}
