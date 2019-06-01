<?php
include "conn.php";
$username1 = $_POST['username1'];
$location = $_POST['location'];
$query = "update `garage_temp_location` set `location` = '$location' where `username` = '$username1'";
$con->query($query);
print_r('{"status":1}');
$con->close();

?>