/*var today = new Date();
var dd = today.getDate();
//January is 0!
var mm = today.getMonth()+1;
var yyyy = today.getFullYear();*/

/*var today = new Date();
var dd = String(today.getDate()).padStart(2, '0');
var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
var yyyy = today.getFullYear();

if(dd < 10) {
    dd = '0'+dd
}

if(mm < 10) {
    mm = '0'+mm
}

today = yyyy + '-' + mm + '-' + dd;

var dateControl = document.querySelector('input[type="date"]');
dateControl.value = today;*/

document.getElementById('datePicker').valueAsDate = new Date();