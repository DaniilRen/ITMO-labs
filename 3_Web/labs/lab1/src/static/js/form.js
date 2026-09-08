document.getElementById("main-form").addEventListener("submit", function(e)  {
    e.preventDefault();
    console.log(state);

    if (!validateForm()) {
        showError("некорректные данные");
        return;
    };
    result = isPointInArea(state.x, state.y, state.r);
    saveResult(result);
})

function validateForm() {
    return validateX() && validateY() && validateR();
}

document.querySelectorAll('input[name="xInput"]').forEach(input => {
    input.addEventListener("change", function (ev) {
        console.log(parseInt(ev.target.value));
        state.x = parseInt(ev.target.value);
    });
});

document.querySelectorAll('input[name="R-input"]').forEach(input => {
    input.addEventListener("click", function (ev) {
        document.getElementById("R-value-display").textContent = ev.target.value;
        state.r = parseFloat(ev.target.value);
        console.log(parseFloat(ev.target.value), typeof(state.r));
    });
});

document.getElementById('Y-input').addEventListener("input", function() {
    state.y = this.value.replace(",", ".");
});


function validateX() {
    const x = document.querySelector("input[name='xInput']:checked");
    return x !== null;
}

function validateY() {
    const y = state.y;
    if (y === "" || y === undefined) {
        return false;
    }
    if (!isNumeric(y)) {
        return false;
    }
    const numY = parseFloat(y);
    if (numY < -5 || numY > 5) {
        return false;
    }
    return true;
}

const rValues = [1, 1.5, 2, 2.5, 3];

function validateR() {
    const r = state.r;
    return isNumeric(r) && rValues.includes(r);
}

function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}


// let currentIndex = 0;

// function switchR() {
//     currentIndex = (currentIndex + 1) % rValues.length;
//     document.getElementById('R-value-display').textContent = rValues[currentIndex];
// }

function setRandomR() {
    const randomIndex = Math.floor(Math.random() * rValues.length);
    document.getElementById('R-value-display').textContent = rValues[randomIndex];
}

function isPointInArea(x, y, R) {
    const halfR = R / 2;
    
    if (x >= 0 && x <= R && y >= -halfR && y <= 0) {
        return true;
    }
    
    if (x >= 0 && y <= 0 && x*x + y*y <= R*R) {
        return true;
    }
    
    if (x <= 0 && y >= 0 && (-x / halfR) + (y / halfR) <= 1) {
        return true;
    }
    return false;
}