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
public class NonAuthorizedException extends Exception{
    String errorMessage = "Password Required to Login";

    @Override
    public String toString() {
        return errorMessage;
    }
}
