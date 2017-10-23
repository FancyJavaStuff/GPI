/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.rs.logiceditor.model.master;

import ch.rs.logiceditor.model.util.Tools;
import com.google.gson.annotations.Expose;
import java.util.HashMap;
import java.util.LinkedList;
import javax.management.modelmbean.InvalidTargetObjectTypeException;

/**
 *
 * @author Ricardo
 */
public class LogicPanel implements Runnable{

    @Expose
    private String name, creator, lastEdit, lastEditor;
    @Expose
    private boolean isEditable = true;
    @Expose
    private LinkedList<LogicBlock> blocks = new LinkedList<>();
    private HashMap blockMap = new HashMap();
    @Expose
    private LinkedList<Connections> connections = new LinkedList<>();
    @Expose
    private int listSpot;
    private boolean isInitialized = false;

    
    public int getListSpot(){
        return listSpot;
    }
    
    public void setListSpot(int listSpot){
        this.listSpot = listSpot;
    }
    
    public String getName () {
        return name;
    }

    public void setName (String name) {
        if (isEditable) {
            this.name = name;
        } else {

        }
    }

    public boolean isEditable () {
        return isEditable;
    }

    public void setCreator (String creator) {
        if (creator == null) {
            this.creator = creator;
        } else {

        }
    }

    public void setEditable (boolean isEditable) {
        this.isEditable = isEditable;
    }
    
    /**
     * call this whenever you save the logicPanel.
     */
    public void setLastEdit(){
        lastEdit = Tools.getCurrentTime();
    }
    
    /**
     * set this whenever someone other than the original creator
     * edits this pane.
     * @param name name of the editor.
     */
    public void setLastEditor(String name){
        lastEditor = name;
    }
    
    public LinkedList<LogicBlock> getBlocks(){
        return blocks;
    }
    
    public LinkedList<Connections> getConnections(){
        return connections;
    }
    
    public void addBlock(LogicBlock block){
        blocks.add(block);
        blockMap.put(block.getUniqueID(), block);
    }

    @Override
    public void run () {
        if(!isInitialized){
            createConnections();
            startBlockFunctions();
        }
    }
    
    private void startBlockFunctions(){
        for(LogicBlock block : blocks){
            block.startBlockFunctions();
        }
    }
    
    private void createConnections(){
        for(Connections connec : connections){
            addConnection(connec);
        }
    }
    
    public void addConnection(LogicBlock source, int sourceOutput, 
            LogicBlock destination, int destinationInput){
        try{
            source.getOutputs().get(sourceOutput).addObserver(destination.getInputs().get(destinationInput));
            System.out.println("connected " + source.getName() + " TO " + destination.getName());
        } catch (Exception e) {
            System.out.println("error @ " + e.getMessage());
            System.out.println("Youre trying to connect " + source.getName() + " TO " + destination.getName());
                    
        }
        connections.add(new Connections(source.getUniqueID() + "." + sourceOutput, destination.getUniqueID() + "." + destinationInput));
    }
    
    public void addConnection(Connections connection){
        try{
        getBlockOutput(connection.getStartID()).addObserver(getBlockInput(connection.getEndID()));
        } catch (InvalidTargetObjectTypeException e){
            System.out.println(e.getMessage());
        }
    }
    
    
    
    private String getUniqueId(String string){
        return string.split(".")[0];
    }
    
    private int getConnectionId(String string){
        return Integer.parseInt(string.split(".")[1]);
    }
    
    private LogicBlock getBlock(String uniqueID){
        return (LogicBlock) blockMap.get(uniqueID);
    }
    
    private ConnectionPoint getBlockOutput(String uniqueID){
        return getBlock(getUniqueId(uniqueID)).getOutputs().get(getConnectionId(uniqueID));
    }
    
    private ConnectionPoint getBlockInput(String uniqueID){
        return getBlock(getUniqueId(uniqueID)).getInputs().get(getConnectionId(uniqueID));
    }

}