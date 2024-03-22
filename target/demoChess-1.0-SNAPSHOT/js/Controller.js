var hasGameStarted = false;
var checkCounter = 0;

function askPieceColor() {
    let playerColor = prompt("Enter your pieces color (White/Black)").trim().toLowerCase();

    // Convert the first letter to uppercase and the rest to lowercase
    playerColor = playerColor.charAt(0).toUpperCase() + playerColor.slice(1);

    if (playerColor === 'White' || playerColor === 'Black') {
        return playerColor; // If it's a valid value, return the piece color
    } else {
        alert("Please enter 'White' or 'Black'.");
        return askPieceColor(); // Call the function again to prompt for another value
    }
}

var playerColor = askPieceColor();


function getResponse(x, y) {

    if(!hasGameStarted){
        hasGameStarted = true;
        setRefreshButton();
    }

    let userData = {
        x:x,
        y:y,
        playerColor: playerColor
    }
    $.ajax({
        url: "/hello-servlet",
        type: "GET",
        data: userData,
        dataType: "json",
        success: function(response) {
            console.log("Success")
            paintPieces(response.positions);
            checkJaque(response.banderaJaque, response.hayGanador);
            setPlayerInTurn(response.playerInTurn);
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

function setRefreshButton(){
    let element = document.getElementById("start-game");
    element.innerHTML = "Refresh";
}

// To solve the "Check!" alert when the player being attacked sets the first piece
function checkJaque(banderaJaque, hayGanador){

    if(hayGanador){
        alert("CheckMate!!");
        return;
    }

    if (banderaJaque === false){
        checkCounter +=1;
    }

    if ((checkCounter === 1) && (banderaJaque === false)){
        alert("Check!");
    }

    if(checkCounter ===1 && banderaJaque === true){
        checkCounter = 0;
    }

    if (checkCounter === 2){
        checkCounter = 0;
    }
}

function setPlayerInTurn(playerInTurn){
    let element = document.getElementById("tog");
    element.innerHTML = playerInTurn + "'s Turn";
}

