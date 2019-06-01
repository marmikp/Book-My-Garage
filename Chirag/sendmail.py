# Python code to illustrate Sending mail from 
# your Gmail account 
import smtplib 
from email.mime.text import MIMEText

import sys

OTP = sys.argv[1]
email = sys.argv[2]

# creates SMTP session 
s = smtplib.SMTP('smtp.gmail.com', 587) 

# start TLS for security 
s.starttls() 

# Authentication 
s.login("marmikp43@gmail.com", "9426495012") 

# message to be sent 
msg = MIMEText("""Your OTP is: """+str(OTP))
msg['Subject'] = "Varification Code for AlumniWebPortal."
# message = "Message_you_need_to_send"

# sending the mail 
s.sendmail("marmikp43@gmail.com", email, msg.as_string()) 

# terminating the session 
s.quit() 
