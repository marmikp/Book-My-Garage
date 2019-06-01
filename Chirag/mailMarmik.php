<?php

$rand = 2164782;
$msg1 = (string) $rand;
$mail_id = "marmikp5897@gmail.com";
$msg = "python sendmail.py $msg1 $mail_id";
exec($msg);
echo $msg;

?>