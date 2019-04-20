<?php
$username = $_POST["username"];
$password = $_POST["password"];

#connecting
$dbname = "jsuhr2_prs";
$dbpassword = "aet5Aixoohai";

$dbuser = "jsuhr2";
$host = "mysql.cd.binghamton.edu";
$cid = mysqli_connect($host, $dbuser, $dbpassword, $dbname);
