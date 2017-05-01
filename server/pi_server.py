import requests
from flask import Flask
from urllib.request import urlopen

NAME = 'TEST_DEVICE'
server_hostname = 'localhost:5000/';

def initialize():
    my_ip = urlopen('http://ip.42.pl/raw').read()
    payload = {'ip' : my_ip, 'device_name' : NAME}
    r = requests.post(server_hostname+'register', data = payload)


if __name__ == '__main__':
    initialize()
    app.run(host="0.0.0.0", port=5000, debug=True)
