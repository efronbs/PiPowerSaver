import requests
import time
import subprocess
import socket
import RPi.GPIO as GPIO

NAME = 'my_pi'
server_hostname = 'http://137.112.89.123:8000/';

def main():

    #set up GPIO pin interface
    GPIO.setmode(GPIO.BCM)
    GPIO.setwarnings(False)
    GPIO.setup(24, GPIO.OUT)
    GPIO.output(24, GPIO.LOW) #start low

    # do registeration - send a post to the server telling it your IP
    p = subprocess.Popen(["hostname", "-I"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    my_ip, err = p.communicate()
    my_ip = my_ip.decode('utf-8')[:-2]

    payload = {'ip' : my_ip, 'device_name' : NAME}
    r = requests.post(server_hostname+'piRegister', json = payload)
    if r.json()['result'] == 'failure':
        return

    while r.json()['result'] != 'success':
        time.sleep(5)
        print ("reattempting register")
        r = requests.post(server_hostname+'piRegister', json = payload)

    print ('registration succeeded')

    # now set up server
    serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    serversocket.bind(('', 8089))
    serversocket.listen(1) # become a server socket, maximum 5 connections

    while True:
        connection, address = serversocket.accept()
        buf = connection.recv(64)
        if len(buf) > 0:
            strForm = buf.decode('utf-8')
            # print ('got string: ' + strForm)
            if (strForm == 'on'):
                print ('got on signal')
                GPIO.output(24, GPIO.HIGH)
            if (strForm == 'off'):
                print ('got off signal')
                GPIO.output(24, GPIO.LOW)


if __name__ == '__main__':
    main()
