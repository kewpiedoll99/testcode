import socket
import sys

"""
Simple socket server using threads

never got this to work for UDP :(
"""


HOST = ""  # Symbolic name, meaning all available interfaces
PORT = 8125       # Arbitrary non-privileged port
FAMILY = socket.AF_INET
TYPE = socket.SOCK_DGRAM
PROTOCOL = socket.SOL_UDP
s = socket.socket(FAMILY, TYPE, PROTOCOL)
print("Socket created")
try:
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s.setblocking(True)
    #Bind socket to local host and port
    s.bind((HOST, PORT))
    print("Socket bind complete")
    #Start listening on socket
    # s.listen(10)
    # print("Socket now listening")
    s.recv(1024)
    #now keep talking with the client
    #wait to accept a connection - blocking call
    conn, addr = s.accept()
    print(f"Connected with {addr[0]}: {str(addr[1])}")
    while 1:
        data = conn.recv(1024)
        if not data: break
        print(f"data: {data}")
except socket.error as msg:
    print(f"Bind failed. Error: {msg}")
    if s:
        s.close()
    sys.exit()
