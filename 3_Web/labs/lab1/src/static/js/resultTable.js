const table = document.getElementById("result-table");

function saveResult(result) {

    const newRow = table.insertRow(-1);

    const rowX = newRow.insertCell(0);
    const rowY = newRow.insertCell(1);
    const rowR = newRow.insertCell(2);
    const rowTime = newRow.insertCell(3);
    const rowResult = newRow.insertCell(4);

    const prevResults = JSON.parse(localStorage.getItem("results") || "[]");
    
    const newResult = {
        X: state.x,
        Y: state.y,
        R: state.r,
        result: result,
        time: new Date().toISOString()
    };
    
    const updatedResults = [...prevResults, newResult];
    localStorage.setItem("results", JSON.stringify(updatedResults));

    rowX.innerText = newResult.X.toString();
    rowY.innerText = newResult.Y.toString();
    rowR.innerText = newResult.R.toString();

    const now = new Date();
    rowTime.innerText = now.toLocaleString('ru-RU');

    rowResult.innerText = result;
}

function loadResults() {
    const saved = localStorage.getItem("results");
    if (!saved) return;
    
    const results = JSON.parse(saved);
    const table = document.getElementById("result-table");
    
    results.forEach(item => {
        const newRow = table.insertRow(-1);
        
        newRow.insertCell(0).innerText = item.X.toString();
        newRow.insertCell(1).innerText = item.Y.toString();
        newRow.insertCell(2).innerText = item.R.toString();
        newRow.insertCell(3).innerText = new Date(item.time).toLocaleString('ru-RU');
        newRow.insertCell(4).innerText = item.result;
    });
}