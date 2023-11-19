const VALID_CLASS_ID = "valid";
const INVALID_CLASS_ID = "invalid";

const form = document.querySelector("form");
const r_field = document.getElementById("r-field");
const x_field = document.getElementById("x-field");
const y_field  = document.getElementById("y-field");

window.addEventListener("load", () => {
    const isInvalidR = r_field.value.length === 0 || Number.isNaN(+r_field.value);
    r_field.className = isInvalidR ? INVALID_CLASS_ID : VALID_CLASS_ID;

    const isInvalidX = x_field.value.length === 0 || Number.isNaN(+x_field.value);
    x_field.className = isInvalidX ? INVALID_CLASS_ID : VALID_CLASS_ID;

    const isInvalidY = y_field.value.length === 0 || Number.isNaN(+y_field.value);
    y_field.className = isInvalidY ? INVALID_CLASS_ID : VALID_CLASS_ID;
});

function checkAfterPointLength(num, error_text){
    const regex = /\.[0-9]{15}0*[1-9]/;
    if (regex.test(num)){
        let placeholder = document.getElementById("error-p");
        placeholder.innerHTML = error_text;
        return false;
    } else {
        let placeholder = document.getElementById("error-p");
        placeholder.innerHTML = "";
        return true;
    }
}

r_field.addEventListener("input", () => {
    const r = +r_field.value;

    const isValid = r_field.value.length !== 0
                    && checkAfterPointLength(r_field.value,
                     "too many significant characters after the decimal point (R)")
                    && (!Number.isNaN(r) && r > 1 && r < 4);
    
    r_field.className = isValid ? VALID_CLASS_ID : INVALID_CLASS_ID;
});

x_field.addEventListener("input", () => {
    const x = +x_field.value;
    
    const isValid = x_field.value.length !== 0
                    && checkAfterPointLength(x_field.value,
                     "too many significant characters after the decimal point (X)")
                    && (!Number.isNaN(x) && x > -5 && x < 5);
    
    x_field.className = isValid ? VALID_CLASS_ID : INVALID_CLASS_ID;
});

y_field.addEventListener("input", () => {
    const y = +y_field.value;

    const isValid = y_field.value.length !== 0 
                    && checkAfterPointLength(y_field.value,
                     "too many significant characters after the decimal point (Y)")
                    && (!Number.isNaN(y) && y > -5 && y < 5);

    y_field.className = isValid ? VALID_CLASS_ID : INVALID_CLASS_ID;
});

form.addEventListener("submit", (event) => {
    const r = +r_field.value;
    const x = +x_field.value;
    const y = +y_field.value;

    // no default sending data to form (it will be done using xmlhttp if js is activated)
    event.preventDefault();

    const isValidR = r_field.className === VALID_CLASS_ID;
    const isValidX = x_field.className === VALID_CLASS_ID;
    const isValidY = y_field.className === VALID_CLASS_ID;

    if (isValidR && isValidX && isValidY){
        send_query(r, x, y);
    } else {
        alert("Wrong data");
    }
});