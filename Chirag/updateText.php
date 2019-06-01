<?php


include "conn.php";

$username1 = $_POST['username1'];
$field = $_POST['field'];
$value = $_POST['value'];
$query = "";
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
if ($field != "location") {
	$query = "update login set `$field` = '$value' where `username` = '$username1'";
}else{
	$query = "update garage_location set `$field` = '$value' where `username` = '$username1'";
	$query1 = "update garage_temp_location set `$field` = '$value' where `username` = '$username1'";
}
$data = $con->query($query);

$js->response = 1;
print_r(json_encode($js));


$con->close();

?>