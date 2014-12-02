
<?php
//Variable for database entry

$dbhost = "cis-linux2.temple.edu";
$dbname = "Fa14_4340_Museum";
$dbuser = "";
$dbpass ="";
$itemid = $_POST["Item_id"];


$conn = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
if ($conn->connect_error){
die ("Connection failed: " . $conn->connect_error);
}

$items = "SELECT * FROM Items where Item_id = '" . $itemid ."'" ;
$res = $conn->query($items);

if ($res->num_rows > 0){
while($row = $res->fetch_assoc())
{
//echo $row['Items_id'];
$output[]=$row;
}
print(json_encode($output));
}


$conn->close();

?>









^G Get Help                 ^O WriteOut                 ^R Read File                ^Y Prev Page                ^K Cut Text                 ^C Cur Pos
^X Exit                     ^J Justify                  ^W Where Is                 ^V Next Page                ^U UnCut Text               ^T To Spell
