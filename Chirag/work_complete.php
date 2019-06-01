<?php

$username1 = $_POST['username1'];
$garage_uname = $_POST['g_u'];
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
include 'conn.php';
$query = "update `request` set `ask_complete` = 1 where `work_by` = '$username1' and `username`='$garage_uname'";
$data = $con->query($query);
$js->status = 1;
print_r(json_encode($js));

?>