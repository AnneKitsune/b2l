/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Log;

/**
 *
 * @author jojolepro
 */
public enum LogLevel{
    INFO("Info"),
    WARNING("Warning"),
    ERROR("Error"),
    FATAL("Fatal");
    public final String name;
    private LogLevel(String name){
        this.name = name;
    }
}
