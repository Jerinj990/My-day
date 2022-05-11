from flask import *
from database import *
admin=Blueprint('admin',__name__)

@admin.route('/adminpanel',methods=['get','post'])
def adminpanel():
	data={}
	q="select * from user inner join login using(login_id)"
	r=select(q)
	data['cat']=r

	# data={}
	q="select * from complaint inner join user using(login_id)"
	r=select(q)
	data['comp']=r
	print(r)
	# if "submit" in request.form:
	# 	a=request.form['reply']
	# 	q="insert into complaint values (NULL,'%s') "%(a)
	# 	res=insert(q)
	# if 'action' in request.args:
	# 	action=request.args['action']
	# 	id=request.args['id']	
	# else:
	# 	action=None	
	# if action=='reply':
	# 	q="select * from complaint where complaint_id='%s' "%(id)
	# 	res=select(q)
	# 	data['reply']=res
	# 	print(res)
	if 'reply' in request.form:
		a=request.form['reply']
		id=request.form['id']
		q="update complaint set reply='%s' where complaint_id='%s'"%(a,id)
		update(q)

		return redirect(url_for('admin.adminpanel'))

	return render_template("admin.html",data=data)

#@admin.route('/user',methods=['get','post'])
#def user():	data={}
#	q="select * from user inner join login using(login_id)"
#	r=select(q)
#	data['cat']=r
#	return render_template("users.html",data=data)

@admin.route('/complaints',methods=['get','post'])
def complaints():
	data={}
	q="select * from complaint inner join user using(login_id)"
	r=select(q)
	data['cat']=r
	print(r)
	# if "submit" in request.form:
	# 	a=request.form['reply']
	# 	q="insert into complaint values (NULL,'%s') "%(a)
	# 	res=insert(q)
	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']	
	else:
		action=None	
	if action=='reply':
		q="select * from complaint where complaint_id='%s' "%(id)
		res=select(q)
		data['reply']=res
		print(res)
	if 'reply' in request.form:
		a=request.form['reply']
		q="update complaint set reply='%s' where complaint_id='%s'"%(a,id)
		update(q)

		return redirect(url_for('admin.complaints'))
	return render_template("complaints.html",data=data)
		
