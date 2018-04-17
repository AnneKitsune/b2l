package net.supercraft.B2L.Scene;

import com.jme3.scene.Node;
import java.util.ArrayList;
import net.supercraft.jojoleproUtils.module.control.Module;
import net.supercraft.jojoleproUtils.module.control.ModuleManager;
import net.supercraft.jojoleproUtils.module.model.IUpdatable;

public class ModuleSceneDisable extends Module implements IUpdatable{
    
    //Nodes changed to Entity
    //This will now be used to manage physics and to batch load entities
    private ArrayList<Node> allNodes = new ArrayList<Node>();
    public ModuleSceneDisable(ModuleManager mm){
        super(mm);
    }
    public void update(float tpf){
        Node temp = new Node();
    }
    public void addNode(Node node){
        allNodes.add(node);
    }
    public void removeAllNode(Node node){
        for(int i=0;i<allNodes.size();i++){
            if(allNodes.get(i).equals(node)){
                allNodes.remove(i);
                i--;
            }
        }
    }
    public void removeOneNode(Node node){
        for(int i=0;i<allNodes.size();i++){
            if(allNodes.get(i).equals(node)){
                allNodes.remove(i);
                return;
            }
        }
    }
    public ArrayList<Node> getNodeList(){
        return allNodes;
    }
	@Override
	public void update(long arg0) {
		// TODO Auto-generated method stub
		
	}
}
