from flask import *
from database import *
import uuid
import demjson

api=Blueprint('api',__name__)

@api.route('/login',methods=['get','post'])
def login():
	data={}
	data.update(request.args)
	username = request.args['username']
	password = request.args['password']
	q="select * from `login` where `username`='%s' and `password`='%s'"%(username,password)
	res = select(q)
	print(q)
	if(len(res) > 0):
		data['status']  = 'success'
		data['data'] = res
	else:
		data['status']	= 'failed'
	return  demjson.encode(data)


@api.route('/register/',methods=['get','post'])
def register():
    data={}
    fname=request.args['fname']
    lname=request.args['lname']
    gender=request.args['gender']
    place=request.args['place']
    phone=request.args['phone']
    email=request.args['email']
    username=request.args['username']
    password=request.args['password']
    imei=request.args['imei']

    q="select * from login where username='%s' or imei='%s'"%(username,imei)
    res=select(q)
    if res:
        data['status']='duplicate'
    else:
        q="INSERT INTO `login` VALUES(NULL,'%s','%s','%s','user')"%(username,password,imei)
        id=insert(q)
        q="INSERT INTO `user` VALUES(NULL,'%s','%s','%s','%s','%s','%s','%s')"%(id,fname,lname,place,phone,email,gender)
        insert(q)
        data['status']  = 'success'
    data['method']='send'
    return demjson.encode(data)



@api.route('/calllog/',methods=['get','post'])
def calllog():
	data={}
	id=request.args['uid']
	# imei=request.args['imei']
	phn=request.args['phone']
	date=request.args['date']
	time=request.args['time']
	duration=request.args['duration']
	calltype=request.args['type']
	q="insert into `call` values(NULL,(SELECT `user_id` FROM `user` WHERE `login_id`='%s'),'%s','%s','%s','%s','%s')"%(id,phn,duration,date,time,calltype)
	print(q)
	insert(q)
	data['status']="success"
	data['method']="calllog"


	return demjson.encode(data)

@api.route('/msglog/',methods=['get','post'])
def msglog():
	data={}
	id=request.args['id']
	imei=request.args['imei']
	phnno=request.args['phone']
	desc=request.args['message']
	messagetype=request.args['type']
	q="insert into messages values(NULL,'%s','%s','%s',curdate(),curtime(),'%s')"%(id,phnno,desc,messagetype)
	insert(q)
	data['status']="success"

	return demjson.encode(data)


@api.route('/location/',methods=['get','post'])
def location():
	data={}
	id=request.args['uid']
	lat=request.args['lat']
	longi=request.args['longi']
	imei=request.args['imei']
	place=request.args['place']
	q="select * from location where user_id=(SELECT `user_id` FROM `user` WHERE `login_id`='%s')" %(id)
	res=select(q)
	if res:
		q="update location set latitude='%s',longitude='%s', date=curdate(),time=curtime(),place='%s' where user_id=(SELECT `user_id` FROM `user` WHERE `login_id`='%s')" %(lat,longi,place,id)
		print(q)
		update(q)
	else:
		q="insert into location values(null,'%s','%s',(SELECT `user_id` FROM `user` WHERE `login_id`='%s'),curdate(),curtime(),'%s')" %(lat,longi,id,place)
		print(q)
		insert(q)
	data['status']="success"
	data['method']="location"
	return demjson.encode(data)





@api.route('/Call_history',methods=['get','post'])
def Call_history():
	data={}
	log_id=request.args['log_id']
	
	q="SELECT * FROM `call` WHERE `user_id`=(SELECT `user_id` FROM `user` WHERE `login_id`='%s')"%(log_id)
	res = select(q)
	if(len(res) > 0):
		data['status']  = 'success'
		data['data'] = res
	else:
		data['status']	= 'failed'
	data['method']='Call_history'
	return  demjson.encode(data)


@api.route('/Sms_history',methods=['get','post'])
def Sms_history():
	data={}
	log_id=request.args['log_id']
	
	q="SELECT * FROM `sms` WHERE `user_id`=(SELECT `user_id` FROM `user` WHERE `login_id`='%s')"%(log_id)
	res = select(q)
	if(len(res) > 0):
		data['status']  = 'success'
		data['data'] = res
	else:
		data['status']	= 'failed'
	data['method']='Sms_history'
	return  demjson.encode(data)


@api.route('/Location_history',methods=['get','post'])
def Location_history():
	data={}
	log_id=request.args['log_id']
	
	q="SELECT * FROM `location` WHERE `user_id`=(SELECT `user_id` FROM `user` WHERE `login_id`='%s')"%(log_id)
	res = select(q)
	if(len(res) > 0):
		data['status']  = 'success'
		data['data'] = res
	else:
		data['status']	= 'failed'
	data['method']='Location_history'
	return  demjson.encode(data)

@api.route('/sendcomplaint',methods=['get','post'])
def sendcomplaint():
	data={}
	log_id=request.args['log_id']
	
	message=request.args['complaint']
	

	q="INSERT INTO `complaint` VALUES(NULL,(SELECT `user_id` FROM `user` WHERE `log_id`='%s'),'%s','pending',CURDATE())"%(log_id,message)
	insert(q)
	data['status']  = 'success'
	data['method']='send'
	return demjson.encode(data)

@api.route('/viewcomplaint',methods=['get','post'])
def viewcomplaint():

	data={}
	log_id=request.args['log_id']
	
	q="SELECT * FROM `complaint`  WHERE `user_id`=(SELECT `user_id` FROM `user` WHERE `log_id`='%s')"%(log_id)
	res = select(q)
	if(len(res) > 0):
		data['status']  = 'success'
		data['method']='viewcomplaint'
		data['data'] = res
	else:
		data['status']	= 'failed'
		data['method']='viewcomplaint'
	return  demjson.encode(data)

	
# @api.route('/payment/',methods=['get','post'])
# def payment():
# 	data={}
# 	proposal_id=request.args['proposal_id']
	
# 	booking_id=request.args['booking_id']
# 	amount=request.args['amount']

	

# 	q="INSERT INTO `payment` VALUES(NULL,'%s','%s','payment done successfully',CURDATE())"%(proposal_id,amount)
# 	insert(q)
# 	q="UPDATE `booking` SET booking_status='paid' WHERE `booking_id`='%s'"%(booking_id)
# 	update(q)
# 	q="UPDATE `proposal` SET `proposal_status`='paid' WHERE`proposal_id`='%s'"%(proposal_id)
# 	update(q)
# 	data['status']  = 'success'
# 	data['method']='send'
# 	return demjson.encode(data)

# @api.route('/addreview/',methods=['get','post'])
# def addreview():
# 	data={}
# 	provider_id=request.args['provider_id']
	
# 	rate=request.args['rate']
# 	desca=request.args['desca']
# 	log_id=request.args['log_id']

# 	q="INSERT INTO `review` VALUES(NULL,(select `user_id` from `users` where `log_id`='%s'),'%s','%s','%s',curdate())"%(log_id,provider_id,desca,rate)
# 	insert(q)
# 	data['status']  = 'success'
# 	data['method']='send'
# 	return demjson.encode(data)
# @api.route('/viewreview/',methods=['get','post'])
# def viewreview():
# 	data={}
# 	provider_id=request.args['provider_id']
# 	q="select * from `review` where `provider_id`='%s'"%(provider_id)
# 	res = select(q)
# 	if(len(res) > 0):
# 		data['status']  = 'success'
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']='view'
# 	return demjson.encode(data)
# @api.route('/area/',methods=['get','post'])
# def area():
# 	data={}
	
# 	q="SELECT * FROM `areas`"  
# 	res = select(q)
# 	if(len(res) > 0):
# 		data['status']  = 'success'
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']='view'
# 	return demjson.encode(data)

# @api.route('/upload_image/',methods=['get','post'])
# def upload_image():
# 	data={}
# 	image=request.files['image']

# 	path='static/images/'+str(uuid.uuid4())+image.filename
# 	image.save(path)
# 	print(path)

# 	image1=request.files['image1']
# 	path1='static/images/'+str(uuid.uuid4())+image1.filename
# 	image1.save(path1)
# 	print(path1)
# 	description=request.form['description']
# 	log_id=request.form['log_id']
# 	type_id=request.form['type_id']
# 	print(description)
# 	q="INSERT INTO `booking` VALUES(NULL,(SELECT `user_id` FROM `users` WHERE `log_id`='%s'),'%s','0',CURDATE(),'%s','%s','%s','pending')"%(log_id,type_id,description,path,path1)
# 	insert(q)
# 	print(q)
# 	data['status']="success"
# 	data['method']='upload_image'
# 	return demjson.encode(data)
# @api.route('/service/',methods=['get','post'])
# def service():
# 	data={}
	
# 	q="SELECT * FROM `service_type`"  
# 	res = select(q)
# 	if(len(res) > 0):
# 		data['status']  = 'success'
# 		data['data'] = res
# 	else:
# 		data['status']	= 'failed'
# 	data['method']='service'
# 	return demjson.encode(data)
# @api.route('/prpo_accept/',methods=['get','post'])
# def prpo_accept():
# 	data={}
# 	provider_id=request.args['provider']
	
# 	proposal_id=request.args['proposal_id']
# 	bookid=request.args['bookid']
	

# 	q="UPDATE `proposal` SET `proposal_status`='accept' WHERE`proposal_id`='%s'"%(proposal_id)
# 	update(q)
# 	q2="UPDATE `booking` SET `booking_status`='accept' ,`provider_id`=(SELECT `provider_id` FROM `proposal` WHERE `proposal_id`='%s') WHERE `booking_id`='%s'"%(provider_id,bookid)
# 	update(q2)
# 	data['status']  = 'success'
# 	data['method']='prpo_accept'
# 	return demjson.encode(data)
# @api.route('/prpo_reject/',methods=['get','post'])
# def prpo_reject():
# 	data={}
# 	provider_id=request.args['provider']
	
# 	proposal_id=request.args['proposal_id']
# 	bookid=request.args['bookid']
	

# 	q="UPDATE `proposal` SET `proposal_status`='reject' WHERE`proposal_id`='%s'"%(proposal_id)
# 	update(q)
# 	q2="UPDATE `booking` SET `booking_status`='reject' ,`provider_id`=(SELECT `provider_id` FROM `proposal` WHERE `proposal_id`='%s') WHERE `booking_id`='%s'"%(provider_id,bookid)
# 	update(q2)
# 	data['status']  = 'success'
# 	data['method']='prpo_reject'
# 	return demjson.encode(data)
