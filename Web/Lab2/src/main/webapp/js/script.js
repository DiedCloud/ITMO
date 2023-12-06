import {isDataValid} from "./validation.js";

const form = document.getElementById("form");

form.addEventListener("submit", (event) => {
    if (!isDataValid()) {
        event.preventDefault();
        alert("Wrong data");
    }
});