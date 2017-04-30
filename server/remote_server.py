from flask import Flask
from urllib.request import urlopen

app = Flask(__name__)

@app.route('/')
def hello_world():
    my_ip = urlopen('http://ip.42.pl/raw').read()
    return my_ip

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)
