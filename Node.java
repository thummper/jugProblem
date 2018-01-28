import java.util.ArrayList;

public class Node {
    public ArrayList<Node> children = new ArrayList<>();
    public Node parent = null;
    public ArrayList<Integer> value;
    public Integer depth = 0;
    public int x, y, diameter, space;
    public String text = "";


    public Node(ArrayList<Integer> value) {
        this.value = value;
        //Value also to text for drawing?
        for(Integer I: this.value){
            text += Integer.toString(I);

        }
        this.diameter = 35;
    }
    //Methods for getting information about nodes.
    public void addChild(Node child) {
        children.add(child);
    }




}