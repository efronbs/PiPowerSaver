import socket

clientsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
clientsocket.connect(('192.168.0.32', 8089))
clientsocket.send(str.encode('hello'))
