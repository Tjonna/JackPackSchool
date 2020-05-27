import socket
import time
t=1
from sense_hat import SenseHat
sense = SenseHat()
from sense_hat import SenseHat
sense = SenseHat()
#regelt het starten van de connectie
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(("169.254.14.69", 5000))
client_socket.send("connected"+"\n")
#maakt van nummer zelfde format als de afbeeldingen
def makeimg(var):
	lengte = len(var)
	multiplier = 4-lengte
	print multiplier
	nullen = multiplier*"0"
	print (nullen)
	return(nullen+var+".png")
def checkdirection():
    #haalt data van rotatie en joystick op
	richting = ""
	acceleration = sense.get_accelerometer_raw()
	x = acceleration['x']
	x=round(x, 0)
	print (x)
	y = "0"
	enter = '0'
	if x == 1:
		print "dfdfdf"
		xsend = "1"
		t=0
	else:
		t=1
	if x == -1.0:
		xsend = "-1"
	elif t == 1:
		xsend = "0"
	t=0
	for event in sense.stick.get_events():
	# Check if the joystick was pressed
		if event.action == "pressed" or event.action == "held":
			print ("trigger activated")
		  # Check which direction
			if event.direction == "up":
				y = "1"
			if event.direction == "down":
				y = "-1"
			if event.direction == "middle":
				enter = "1"
	return (xsend+";"+y+";"+enter)
def tic():
    #ontvangt data van de game en past de sense hat aan
	while 1:
		data = client_socket.recv(1024)
		print(data)
		fuel = data.split(";")[0]
		print(fuel)
		status = data.split(";")[1]
		i = 21
		if status == "0":
			i=1
		while i <= 20:
			ree = str(i)
			img = ("anim/"+makeimg(ree))
			sense.load_image(img)
			time.sleep(0.016)
			i = i+1
		part = makeimg(fuel)
		print (part)
		img = ("fuel/"+part)
		sense.load_image(img)
		if not data:
			break
		break
def tac():
	r = checkdirection()
	print (r)
	client_socket.send(r+"\n")
while True:
    #tic =rec, tac = send
	tic()
	tac()
	

