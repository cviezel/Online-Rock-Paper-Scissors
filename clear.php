<?php
$username1 = $_POST["username1"];
$username2 = $_POST["username2"];

#connecting
$dbname = "jsuhr2_rps";
$dbpassword = "aet5Aixoohai";

$dbuser = "jsuhr2";
$host = "mysql.cs.binghamton.edu";
$cid = mysqli_connect($host, $dbuser, $dbpassword, $dbname);

$sql = "delete from matches where user1=\"" . addslashes($username2) . "\" and user2=\"" . addslashes($username1) . "\"";
$result  = mysqli_query($cid, $sql);
$numRows = $result->num_rows;
