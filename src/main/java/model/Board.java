package model;

import java.util.List;
import java.util.Optional;

public class Board {
    private Piece[][] positions;
    private Position startPosition;
    private Position destPosition;
    private String playerInTurn;
    private Moves calculatorMoves = new Moves();

    // Kings' position
    private Position blackKing;
    private Position whiteKing;
    private boolean banderaJaque = true;
    private boolean hayGanador = false;

    public Position a = new Position(-1, -1);
    public Position b = new Position(-1, -1);




    public Board() {
        this.positions = new Piece[8][8];
        this.startPosition = new Position(-1,-1);
        this.destPosition = new Position(-1,-1);
        this.playerInTurn = "White";
    }

    // Method to calculate possible moves for a piece
    public List<Position> calculatePossibleMoves(Position position) { ////////// TODO use position
        int row = position.getRow();
        int col = position.getCol();
        Piece piece = positions[row][col];

        if (piece == null) {
            System.out.println("No piece found at the source position.");
        }

        List<Position> possibleMoves = calculatorMoves.calculatePieceMoves(position,positions);

        return possibleMoves;
    }


    public boolean analisisJaque(Position myKing, String enemyColor) {
        if (!this.banderaJaque) {
            List<Position> myPieces = calculatorMoves.findMyPieces(playerInTurn, positions);
            for (Position position : myPieces) {
                calculatorMoves.calculatePieceMoves(position, positions);
                for (Position position2 : calculatorMoves.getPossibleMoves()) {
                    // Create a copy of the current game state
                    Piece[][] tempPositions = clonePositions(positions);

                    // Simulate the move
                    Piece movedPiece = tempPositions[position.row][position.col];
                    tempPositions[position2.row][position2.col] = movedPiece;
                    tempPositions[position.row][position.col] = null;

                    List<Position> ac= calculatorMoves.calculatePlayerMoves(enemyColor, tempPositions);
                    checkJaque();
                }
            }
        }
        return true; // No move found that prevents check
    }

    // Helper method to clone the game state
    private Piece[][] clonePositions(Piece[][] positions) {
        Piece[][] clone = new Piece[positions.length][];
        for (int i = 0; i < positions.length; i++) {
            clone[i] = positions[i].clone();
        }
        return clone;
    }



    public boolean movePiece() {
        int startRow = this.startPosition.getRow();
        int startCol = this.startPosition.getCol();
        int destRow = this.destPosition.getRow();
        int destCol = this.destPosition.getCol();

        String enemyColor = this.playerInTurn.equals("White") ? "Black" : "White";
        Position myKing = this.playerInTurn.equals("White") ? this.whiteKing : this.blackKing;
        Position enemyKing = this.playerInTurn.equals("White") ? this.blackKing : this.whiteKing;

        Piece piece = positions[startRow][startCol];
        if (piece == null) {
            System.out.println("No piece found at the starting position.");
            return false;
        }

        // Check if the destination position is within bounds
        if (destRow < 0 || destRow >= 8 || destCol < 0 || destCol >= 8) {
            System.out.println("Invalid destination position.");
            return false;
        }

        // Check if the destination position is occupied by own piece
        if (positions[destRow][destCol] != null && positions[destRow][destCol].color.equals(piece.color)) {
            System.out.println("Cannot move to a position occupied by your own piece.");
            return false;
        }

        // Check for valid move based on piece type (not implemented here)
        // You need to implement rules for each piece type to determine if the move is valid

        // If all checks pass, move the piece
        List<Position> ac = calculatorMoves.calculatePlayerMoves(enemyColor, this.positions);

        if (calculatorMoves.jaque(myKing, ac)) {
            banderaJaque = false;
        }

        //System.out.println("Hay ganador: " + this.hayGanador);

        if (this.hayGanador) {
            return true;
        }

        Piece deadFlag = positions[destRow][destCol];

        positions[destRow][destCol] = piece;
        positions[startRow][startCol] = null;

        if (piece.getName().equals("King")) {
            if (piece.getColor().equals("Black")) {
                this.setBlackKing(new Position(destRow, destCol));
            } else if (piece.getColor().equals("White")) {
                this.setWhiteKing(new Position(destRow, destCol));
            }
        }

        myKing = this.playerInTurn.equals("White") ? this.whiteKing : this.blackKing;
        List<Position> acf = calculatorMoves.calculatePlayerMoves(enemyColor, this.positions);

        if (calculatorMoves.jaque(myKing, acf)) {
            this.positions[destRow][destCol] = deadFlag;
            this.positions[startRow][startCol] = piece;
            banderaJaque = false;
        } else {
            banderaJaque = true;
        }

        //System.out.println("Pieza movida correctamente.");

        return banderaJaque;

    }



    // Getters and setters
    public Piece[][] getPositions() {
        return positions;
    }

    public void placePiece( Piece piece, int row, int col){
        this.positions[row][col] = piece;
    }

    // Basic Functions

    public void changePlayerInTurn(){
        if(this.playerInTurn.equals("White")){
            this.playerInTurn = "Black";
        }
        else{
            this.playerInTurn = "White";
        }
    }

    public void setPosition(Position position){
        if(isStartPositionSet() && !isDestPositionSet()){

            this.destPosition = position;
            if(calculatorMoves.containsPosition(position))
            {
                if(movePiece()){
                    a=this.startPosition;
                    b=this.destPosition;
                    if(this.hayGanador)
                        System.exit(0);

                    checkJaque();
                    resetPositions();
                    changePlayerInTurn();
                }
                else{
                    resetPositions();
                }
            }
            else
                resetPositions();

        }

        else if(!isStartPositionSet() && !isEmpty(position) && playerInTurn.equals(getPositionColor(position))){

            startPosition = position;
            List<Position> possibleMoves = calculatorMoves.calculatePieceMoves(position, this.positions);

            //System.out.println(possibleMoves);

        }
    }

    public void checkJaque(){
        String mycolor = this.playerInTurn;
        Position enemyKing = this.playerInTurn.equals("White") ? this.blackKing : this.whiteKing;
        System.out.println(enemyKing);
        //System.out.println("Yo soy" + mycolor);
        List<Position> myPossibleMoves = calculatorMoves.calculatePlayerMoves(mycolor, this.positions);
        if(calculatorMoves.jaque(enemyKing, myPossibleMoves)){
            this.banderaJaque = false;
        }


    }

    public String getPositionColor(Position position){
        int row = position.getRow();
        int col = position.getCol();
        return positions[row][col].color;
    }

    public boolean isEmpty(Position position){
        int row = position.getRow();
        int col = position.getCol();
        Optional<Piece> optionalPiece = Optional.ofNullable(this.positions[row][col]);
        return optionalPiece.isEmpty();

    }

    public boolean isStartPositionSet(){
        Position unsetPosition = new Position(-1,-1);
        return !this.startPosition.equals(unsetPosition);
    }

    public boolean isDestPositionSet(){
        Position unsetPosition = new Position(-1,-1);
        return !this.destPosition.equals(unsetPosition);
    }

    public Position unsetPosition(){
        return new Position(-1,-1);
    }

    public void resetPositions(){
        Position position = new Position(-1,-1);
        startPosition = position;
        destPosition = position;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getDestPosition() {
        return destPosition;
    }

    public void setDestPosition(Position destPosition)
    {
        this.destPosition = destPosition;
        movePiece();
    }
    public void setStartPosition(Position startPosition)
    {
        this.startPosition = startPosition;
    }

    public Position getBlackKing() {
        return blackKing;
    }

    public void setBlackKing(Position blackKing) {
        this.blackKing = blackKing;
    }

    public Position getWhiteKing() {
        return whiteKing;
    }

    public void setWhiteKing(Position whiteKing) {
        this.whiteKing = whiteKing;
    }

    public boolean isBanderaJaque() {
        return banderaJaque;
    }

    public String getPlayerInTurn() {
        return playerInTurn;
    }
}
