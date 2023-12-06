const VALID_CLASS_ID = "valid";
const INVALID_CLASS_ID = "invalid";

//const form = document.querySelector("form");
//const r_field = document.getElementById("r-field");
//const x_field = document.getElementById("x-field");
const y_field = document.getElementById("y-field");

let r_valid;
let x_valid;
let y_valid;

window.addEventListener("load", () => {
    r_valid = false;
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

const checkboxAll = document.querySelectorAll('input[type=checkbox]');
checkboxAll.forEach((checkbox) => checkbox.addEventListener('change', function (){
    const checkboxAll = document.querySelectorAll('input[type=checkbox]');
    let count = 0;
    for (let i = 0; i < checkboxAll.length; ++i) {
        if (checkboxAll[i].type === 'checkbox' && checkboxAll[i].checked) {
            count++;
        }
    }
    if (count === 1){
        r_valid = VALID_CLASS_ID;
        let placeholder = document.getElementById("error-p");
        placeholder.innerHTML = "";
    }
    else {
        r_valid = INVALID_CLASS_ID;
        let placeholder = document.getElementById("error-p");
        placeholder.innerHTML = "Choose only one value";
    }
}));

const radioButtons = document.querySelectorAll('input[name="X"]');

// Add event listener for each radio button
radioButtons.forEach((radioButton) => {
    radioButton.addEventListener('change', function () {
        // Check if this radio button is selected
        if (this.checked) {
            const selectedValue = this.value;
            console.log(`Selected value: ${selectedValue}`);
        }
    });
});

y_field.addEventListener("input", () => {
    const y = +y_field.value;

    y_valid = y_field.value.length !== 0
        && checkAfterPointLength(y_field.value,
            "too many significant characters after the decimal point (Y)")
        && (!Number.isNaN(y) && y > -3 && y < 3);

    y_field.className = y_valid ? VALID_CLASS_ID : INVALID_CLASS_ID;
});


export function isDataValid(){
    return r_valid && x_valid && y_valid;
}
export function isRValid(){
    return r_valid;
}