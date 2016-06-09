/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Dulanjaya
 */
public class InvalidCredentialsException extends Exception{
    String errorMessage = "Username or Password is incorrect!";

    @Override
    public String toString() {
        return errorMessage;
    }

}
