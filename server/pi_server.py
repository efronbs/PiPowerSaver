import requests
import time
import subprocess
from flask import Flask
from urllib.request import urlopen

app = Flask(__name__)

NAME = 'my_pi'
server_hostname = 'http://198.199.72.246:8000/';

def initialize():

    p = subprocess.Popen(["hostname", "-I"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    my_ip, err = p.communicate()
    my_ip = my_ip.decode('utf-8')[:-2]

    payload = {'ip' : my_ip, 'device_name' : NAME}
    r = requests.post(server_hostname+'piRegister', json = payload)
    # while r.json()['result'] != 'success':
    #     time.sleep(5)
    #     print ("reattempting register")
    #     r = requests.post(server_hostname+'piRegister', json = payload)
    print ('registration succeeded')


@app.route('/')
def test():
    return jsonify({'fuck' : 'you'})

@app.route('/piToggle', methods = ['POST'])
def toggle():
    if request.method != 'POST':
        return jsonify({'data' : 'incorrect request type', 'result' : 'failure'})

    print ("toggle message was recieved")


if __name__ == '__main__':
    initialize()
    app.run(host='0.0.0.0', port=8000, debug=True)
