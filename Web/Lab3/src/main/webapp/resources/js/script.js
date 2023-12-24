const VALID_CLASS_ID = "valid";
const INVALID_CLASS_ID = "invalid";

const form = document.getElementById("form");
//const r_field = document.getElementById("r-field");
//const x_field = document.getElementById("x-field");
const y_field = document.getElementById("form:y-field");

let r_valid;
let x_valid;
let y_valid;

window.addEventListener("load", () => {
    r_valid = true;
    x_valid = true;
    y_valid = y_field.value.length === 0 || Number.isNaN(+y_field.value);
    y_field.className =  y_valid ? INVALID_CLASS_ID : VALID_CLASS_ID;
});

function checkAfterPointLength(num, error_text) {
    const regex = /\.[0-9]{15}0*[1-9]/;
    if (regex.test(num)) {
        let placeholder = document.getElementById("error-p");
        placeholder.innerHTML = error_text;
        return false;
    } else {
        let placeholder = document.getElementById("error-p");
        placeholder.innerHTML = "";
        return true;
    }
}

y_field.addEventListener("input", () => {
    const y = +y_field.value;

    y_valid = y_field.value.length !== 0
        && checkAfterPointLength(y_field.value,
            "too many significant characters after the decimal point (Y)")
        && (!Number.isNaN(y) && y > -3 && y < 3);

    y_field.className = y_valid ? VALID_CLASS_ID : INVALID_CLASS_ID;
});



function isDataValid(){
    return r_valid && x_valid && y_valid;
}
function isRValid(){
    return r_valid;
}



form.addEventListener("submit", (event) => {
    if (!isDataValid()) {
        event.preventDefault();
        alert("Wrong data");
    }
});


const plane = document.getElementById('plane');
plane.addEventListener('click', handleGraphClick);

function handleGraphClick (evt) {
    if (!isRValid()) {
        let placeholder = document.getElementById("error-p");
        placeholder.innerHTML = "Choose R for adding point with your mouse";
        return;
    }
    const r = +document.forms["form"].elements["form:r-field"].value;

    const rect = plane.getBoundingClientRect();
    const x = (evt.clientX - rect.left - 200) / 160 * r;
    const y = (- (evt.clientY - rect.top) + 200) / 160 * r;
    console.log(r);
    console.log(x);
    console.log(y);

    document.getElementById("plain-form:hiddenR").value = r;
    document.getElementById("plain-form:hiddenX").value = x;
    document.getElementById("plain-form:hiddenY").value = y;

    updateBeanValues();
    location.reload();
}