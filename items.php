<?php
//Variable for database entry

$dbhost = "cis-linux2.temple.edu";
$dbname = "Fa14_4340_Museum";
$dbuser = "";
$dbpass ="";

$conn = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
if ($conn->connect_error){
die ("Connection failed: " . $conn->connect_error);
}

$query = "SELECT * FROM Items";
$result = $conn->query($query);
if($result->num_rows > 0){
while($row  = $result->fetch_assoc())
{
//echo "Museum ID: " . $row['Museum_id']. " Start Hours: " . $row['Museum_Start_hours']. "<br>;

$out[]=$row;
}
print(json_encode($out));
echo "<br>";
}


$conn->close();

?>






