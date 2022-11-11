function handle(event) {
    let inputs = document.getElementById("selectTimetables").querySelectorAll('input')
    let A = 'A'
    let selected = ''
    for (let i = 0; i < inputs.length; i++) {
        if ( inputs[i].checked) {
            selected = selected + inputs[i].name + A;
        }
    }
    console.log(selected);

    let xhr = new XMLHttpRequest();
    let body = 'selected=' + encodeURIComponent(selected) +
        '&back=' + encodeURIComponent('\\home');

    xhr.open("POST", '/Memorandum/chooseTimetables', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(body);

    location.reload();
}
document.getElementById("selectTimetables").addEventListener('change', handle);
document.querySelector('form').addEventListener('change', handle);