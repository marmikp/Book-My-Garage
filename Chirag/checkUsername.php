<?php

$username1 = $_POST['user'];
include "conn.php";
$query = "select * from login where username = '$username1'";
$data = $con->query($query);
$num = $data->num_rows;
if ($num == 0) {
	echo "Valid";
}

?>