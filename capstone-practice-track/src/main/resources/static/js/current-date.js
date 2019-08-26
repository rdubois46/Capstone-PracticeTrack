var today = new Date();
var dd = today.getDate();
//January is 0!
var mm = today.getMonth()+1;
var yyyy = today.getFullYear();

if(dd < 10) {
    dd = '0'+dd
}

if(mm < 10) {
    mm = '0'+mm
}

today = yyyy + '-' + mm + '-' + dd;

var dateControl = document.querySelector('input[type="date"]');
dateControl.value = today;