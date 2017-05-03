import requests
from flask import Flask
from urllib.request import urlopen

app = Flask(__name__)

NAME = 'my_pi'
server_hostname = 'http://198.199.72.246:8000/';

def initialize():
    my_ip = urlopen('http://ip.42.pl/raw').read()
    payload = {'ip' : my_ip, 'device_name' : NAME}
    r = requests.post(server_hostname+'piRegister', data = payload)
    print (my_ip)

if __name__ == '__main__':
    initialize()
    app.run(host='0.0.0.0', port=8000, debug=True)
