function handle(event) {
    let inputs = document.querySelector('form').get(0).querySelectorAll('input')
    let A = 'A'
    let selected = ''
    for (let i = 0; i < inputs.length; i++) {
        if ( inputs[i].checked) {
            selected = selected + inputs[i].name + A;
        }
    }

    let xhr = new XMLHttpRequest();
    let body = 'selected=' + encodeURIComponent(selected) +
        '&back=' + encodeURIComponent('\\home');

    xhr.open("POST", '/Memorandum/chooseTimetables', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(body);

    location.reload();
}

document.querySelector('form').addEventListener('change', handle);