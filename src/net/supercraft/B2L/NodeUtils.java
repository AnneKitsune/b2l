/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L;

import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author jojolepro
 */
public class NodeUtils {
    /**
     * Returns true if the childNode is somewhere the child of parentNode <br/>
     * this will return true, even if not directly attached (parent a, child c:  a->b->c returns true, because c is child of a at some point)<br/>
     * @param parentNode
     * @param childNode
     * @return 
     */
    public static boolean isNodeChildOf(Node parentNode, Node childNode){
        //In case we use a getParent on a geometry that is not attached to anything
        //So we avoid the nullpointerexception
        if(childNode==null){
            return false;
        }
        
        Node currentComparator = childNode.getParent();
        while(currentComparator!=null){
            if(currentComparator.equals(parentNode)){
                return true;
            }else{
                currentComparator = currentComparator.getParent();
            }
        }
        return false;
    }
    /**
     * Starts from start going up to parent and then going back from depth childs
     * a->b->c->d->c  for start=a parent=d depth=1
     * 
     * top parent depth is 0
     * @param start
     * @param parent
     * @param depth
     * @return 
     */
    public static Node getDeepChildOfParent(Node start, Node parent, int depth) {
        ArrayList<Node> trace = new ArrayList<Node>();
        Node current = start;
        //Fill the trace by going from start up to parent
        while(current!=null && !current.equals(parent)){
            trace.add(current);
            current = current.getParent();
        }
        //We add the parent at the end, since the loop will stop right before the parent node
        trace.add(parent);
        
        //Then we go back down
        if(depth<=trace.size()){
            return trace.get(trace.size()-depth-1);
        }
        return null;
    }
    //TODO
    public static ArrayList<Node> childToParentStack(){
        return null;
    }
}
