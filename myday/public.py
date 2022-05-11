from flask import *
from database import *
public=Blueprint('public',__name__)

@public.route('/',methods=['get','post'])
def home():
	if "login" in request.form:
		u=request.form['name']
		p=request.form['password']
		q="select * from login where username='%s' and password='%s'"%(u,p)
		res=select(q)
		if res:
			if res[0]['usertype']=="admin":
				return redirect(url_for('admin.adminpanel'))
			# elif res[0]['usertype']=="user":
			# 	return redirect(url_for('user.userhome'))
	return render_template("login1.html")

# @public.route('/login',methods=['get','post'])
# def login():
# 	if "login" in request.form:
# 		u=request.form['name']
# 		p=request.form['password']
# 		q="select * from login where username='%s' and password='%s'"%(u,p)
# 		res=select(q)
# 		if res:
# 			if res[0]['usertype']=="admin":
# 				return redirect(url_for('admin.adminpanel'))
# 			elif res[0]['usertype']=="user":
# 				return redirect(url_for('user.userhome'))
# 	return render_template("login.html")