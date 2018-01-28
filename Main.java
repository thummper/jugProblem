import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    //Static stuff for main algorithm
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Integer> capacity = new ArrayList<>();
    static ArrayList<Node> addedNodes = new ArrayList<>();
    static ArrayList<Node> queue = new ArrayList<>();
    static ArrayList<Node> toExplore = new ArrayList<>();
    static int depth = 1;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


    //Static ints for visualising tree
    static int maxX = 0;
    static int y = 60;
    static int maxY = 0;

    public static void main(String[] args) {


        //Get jug capacities
        System.out.println("Enter jug capacities J1,J2,..,Jn :");
        String caps = sc.nextLine();
        System.out.println("Would you like to visualize the tree after it has been made? (Enter \"Y\" or \"N\") ");
        String visInput = sc.nextLine();

        caps = caps.replaceAll("\\(", "");
        caps = caps.replaceAll("\\)", "");
        String[] split = caps.split(",");

        //Jug strings to integers
        for (String w : split) {

            capacity.add(Integer.parseInt(w));
        }
        System.out.println(capacity);

        //Now make the root node.
        int numJugs = capacity.size();
        ArrayList<Integer> jugs = new ArrayList<>();

        for (int i = 0; i < numJugs; i++) {
            jugs.add(0);
        }

        Node root = new Node(jugs);
        addedNodes.add(root);
        //Root node added to queue to expand
        queue.add(root);
        root.depth = 0;
        //Start making all children
        makeChildren();


        System.out.println("Printing Tree");
        printTree(root);
        System.out.println("There are: " + addedNodes.size() + " total nodes");




        if (visInput.equals("y") | visInput.equals("Y")) {

            //Make stuff to visualize tree.
            queue.clear();
            queue.add(root);
            //Generate all co-ordinates
            makeCoords();



            Visualize vis = new Visualize(maxX, maxY);
            //Make visualization
            vis.setNodeList(addedNodes);

        }
    }

    /*
    Ok, this is the main algorithm.

    It loops through the queue of nodes it has to expand and does the following:

    1 - Creates and tries to add all possible children, it does this by looping through the state and looking at each jug.

            If the jug is empty, a new child will be made with the jug full and it will be sent to another method to be added to the tree

            If the jug is not empty:

                - A new child will be made where the jug is empty
                - The algorithm will try and pour the jug into the other jugs - where this is possible a new child will be made and passed to the method that adds children to the tree.

                Once a node has been fully explored it is removed from the queue

    2 - After all child states have been generated and added where possible (the queue is empty), the algorithm will get the list of children it has made and successfully added to the tree and
    add them to the queue (as they have to be expanded now).

    The algorithm calls itself until all nodes have been explored and no new children have been generated.


     */
    public static void makeChildren() {
        for( int m = 0; m < queue.size(); m++){

            Node in = queue.get(m);
            Node parent = in.parent;
            depth = in.depth + 1;

            ArrayList<Integer> state = in.value;

            for (int i = 0; i < state.size(); i++) {

                if (state.get(i).equals(0)) {
                    //Jug is empty, fill it
                    ArrayList<Integer> child = (ArrayList<Integer>) state.clone();
                    child.set(i, capacity.get(i));

                    Node newChild = new Node(child);
                    newChild.depth = depth;
                    addChild(in, newChild);


                } else {

                    //Jug is not empty
                    //1 - try and empty jug?
                    ArrayList<Integer> child = (ArrayList<Integer>) state.clone();
                    child.set(i, 0);

                    Node newChild = new Node(child);
                    newChild.depth = depth;
                    addChild(in, newChild);

                    for (int j = 0; j < state.size(); j++) {
                        //Loop through all jugs, look at
                        if (i != j) {
                            //Not the jug that is not empty
                            //check jug not at capacity
                            if (state.get(j) < capacity.get(j)) {

                                //If not at capacity, pour i into j
                                int fillingFrom = state.get(i);
                                int toFill = state.get(j);
                                int leftover = fillingFrom - (capacity.get(j) - toFill);

                                //System.out.println("Pouring: " + fillingFrom + " into: " + toFill);

                                if (leftover >= 0) {

                                    //There will be leftover liquid in jug i after it fills j to capacity
                                    child = (ArrayList<Integer>) state.clone();
                                    child.set(j, capacity.get(j));
                                    child.set(i, leftover);

                                    newChild = new Node(child);
                                    newChild.depth = depth;
                                    addChild(in, newChild);

                                } else if (leftover < 0) {
                                    //There will be no liquid left in jug i after it fills j
                                    child = (ArrayList<Integer>) state.clone();
                                    child.set(j, state.get(i));
                                    child.set(i, 0);

                                    newChild = new Node(child);
                                    newChild.depth = depth;
                                    addChild(in, newChild);

                                }
                            }
                        }
                    }
                }
            }



        }
    }


    /*
    This method takes the children generated by the algorithm and decides if they should be added to the tree. It does this by maintaining an ArrayList of nodes in the tree and checks that the node
    that wants to be added is unique - we only add unique children so that the algorithm doesn't get stuck in an infinite loop.
     */
    public static void addChild(Node parent, Node child) {

        int length = addedNodes.size();
        int inList = 0;


        //For every node in addedNodes, if the new node is not the node in addedNodes increment inList,
        //this means that inList will be equal to the length of addedNodes if it is unique.
        for (Node n : addedNodes) {
            if (n.value.equals(child.value)) {

            } else {
                inList++;
            }
        }

        if (inList == length) {
            //Not added to tree so add
            //System.out.println("Adding: " + child.getValue());

            //Make sure that the node has the correct parent, and that the parent has the node as a child
            parent.addChild(child);
            child.parent = parent;

            queue.add(child);
            addedNodes.add(child);
        }
    }


    //Loops through all added nodes and prints them out.
    public static void printTree(Node node) {
        for (int i = 0; i < addedNodes.size(); i++) {

            System.out.println(addedNodes.get(i).value);


        }
    }

    //static stuff for drawing the tree.
    public static int maxChildren = 0;
    public static int maxSpace = 0;
    static ArrayList<Node> maxChildrenList = new ArrayList<Node>();


    //Works out the maximum children for a node.
    public static void getMaxChildren() {
        int tempMax = 0;
        int tempSpace = 0;
        for(Node n: maxChildrenList){
            tempMax += n.children.size();
            tempSpace+= n.space;
        }

        if(tempMax > maxChildren){
            maxChildren = tempMax;
        }
        if(tempSpace > maxSpace){
            tempSpace = maxSpace;
        }

        //Now make new array
        ArrayList<Node> newLs = new ArrayList<>();
        for(Node n: maxChildrenList){
            for(Node c: n.children){
                newLs.add(c);
            }
        }

        maxChildrenList.clear();
        maxChildrenList = (ArrayList<Node>) newLs.clone();

        if(maxChildrenList.size() > 0){
            getMaxChildren();
        }


    }

    /*
    This is a bit hacked together, the purpose of this method is to generate an x and y coordinate for each node in the tree.
    The coord will depend on its parent node and how many children the previous node has (so that all nodes line up with their parents
    and give other nodes space to fit into the tree)

    This was the best way I could come up with doing this - it's not ideal, but it works.
     */

    public static void makeCoords() {

        Node root = addedNodes.get(0);
        addedNodes.clear();
        formatList(root, addedNodes);



        for (int i = 0; i < addedNodes.size(); i++) {

            Node node = addedNodes.get(i);
            Node parent = node.parent;

            if (parent == null) {
                //Draw the root node.
                //Place root in the middle.
                node.x = screenSize.width / 2;
            } else {
                node.x = parent.x;
                maxChildren = 0;
                maxChildrenList.clear();
                maxChildrenList.add(node);
                getMaxChildren();
                //The space it takes to draw all of a node's children.
                node.space = maxChildren * node.diameter;

                int numberChild = parent.children.indexOf(node);
                if (numberChild > 0) {
                    //There is a node drawn before this node.
                    Node previousNode = parent.children.get(numberChild - 1);
                    toExplore.clear();
                    formatList(previousNode,toExplore);
                    int tempx = 0;
                    for(Node cnode: toExplore){
                        if(cnode.x > tempx){
                            tempx = cnode.x;
                        }
                    }
                    node.x = tempx + (node.diameter) + (10); //10 is padding.






                } else {
                    //No node before this node
                    if((i-1)==0){

                        node.x = 0;
                    } else {

                        node.x = node.parent.x;
                    }
                }
            }

            node.y = y * (node.depth+ 1);

            //Maintain max X,Y for drawing window.

            if (node.y > maxY) {
                maxY = node.y;
            }
            if (node.x > maxX) {

                maxX = node.x;
            }
        }
    }


    /*
    Orders nodes in correct way to draw them properly (always expand the left node)
     */
    public static void formatList(Node node, ArrayList<Node> aList){
        aList.add(node);
        for(Node n: node.children){
            formatList(n, aList);
        }
    }


}
