package org.example.demochess;

import java.io.*;

import com.google.gson.Gson;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Board;
import model.Position;
import model.Utils;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private Board board;
    private Gson gson;

    public void init() {
        this.board = Utils.createGame();
        this.gson = new Gson();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String xString = request.getParameter("x");
        String yString = request.getParameter("y");
        String playerColor = request.getParameter("playerColor");


            if((xString.equals("no") && yString.equals("no")) || (!playerColor.equals(this.board.getPlayerInTurn()))){

                response.setContentType("application/json");
                String json = gson.toJson(board);
                response.getWriter().write(json);

            }

            else {

                int x = Integer.parseInt(xString);
                int y = Integer.parseInt(yString);
                System.out.println(x);
                System.out.println(y);
                this.board.setPosition(new Position(x, y));
                response.setContentType("application/json");
                String json = gson.toJson(board);
                response.getWriter().write(json);

            }



    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
    }

    public void destroy() {
    }
}