
import static groovy.json.JsonOutput.*

def call(client_name,classname) {
  def conf = get_conf_client(client_name,classname)
  print prettyPrint(toJson(conf+params))
}
