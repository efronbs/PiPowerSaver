import socket

clientsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
clientsocket.connect(('137.112.221.209', 8089))
clientsocket.send(str.encode('on'))
