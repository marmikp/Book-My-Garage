<?php

$username1 = $_POST['username1'];
$garage_uname = $_POST['g_u'];
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
include 'conn.php';
$query = "update `request` set `flag` = 1 where `work_by` = '$username1' and `username`='$garage_uname'";
$query1 = "update garage_location set status=0 where username = '$username1'";
$data = $con->query($query);
$con->query($query1);
$js->status = 1;
print_r(json_encode($js));

?>