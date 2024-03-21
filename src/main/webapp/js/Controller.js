
function getResponse(x, y) {
    let position = {
        x:x,
        y:y
    }
    $.ajax({
        url: "/hello-servlet",
        type: "GET",
        data: position,
        dataType: "json",
        success: function(response) {
            console.log("Success")
            paintPieces(response.positions);
        },
        error: function(xhr, status, error) {
            console.log("Error: " + error);
        }
    });
}

function paintPieces(positions) {
    positions.forEach(function(row, x) {
        row.forEach(function(position, y) {
            let id = "b" + x + 0 + y;
            let element = document.getElementById(id);

            if (position !== null) {
                element.innerHTML = '<img src="images/' + position.color + '_' + position.name + '.png">';
            } else {
                element.innerHTML = '';
            }
        });
    });
}
