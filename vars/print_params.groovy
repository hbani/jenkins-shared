
import static groovy.json.JsonOutput.*

def call(client_name,classname) {
  def conf = get_conf_client(client_name,classname)
  if (classname == "CompassSimulation") {
  to_from = to_from()
  conf.put(from,to_from.from)
  conf.put(to,to_from.to)
  }
  print prettyPrint(toJson(params))
}
