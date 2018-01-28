import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.*;



public class Visualize extends  JPanel {
    public ArrayList<Node> nList;
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        this.setBackground(Color.WHITE);

        for(Node c: nList ){



            String text = c.text;

            Font font1 = new Font("Arial", Font.PLAIN, 16);
            g.setFont(font1);
            g.drawOval(c.x, c.y, c.diameter, c.diameter);

            FontMetrics fm = g.getFontMetrics();

            Rectangle2D stringBounds = fm.getStringBounds(text, g);
            double x = c.x + (c.diameter - stringBounds.getWidth()) /2d;
            double y =  c.y + (stringBounds.getHeight()/2) + c.diameter/2;




            g.drawString(text, (int)x, (int)y);
            Node parent = c.parent;
            if(parent != null) {
                //Draw line to parent.
                g.drawLine(parent.x + parent.diameter / 2, parent.y + parent.diameter, c.x + c.diameter / 2, c.y);
            }


        }



    }

    public Visualize(int x, int y){

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        JFrame frame = new JFrame();
        frame.setTitle("Tree Visualisation (Pretty Rudimentary)");
        this.setPreferredSize(new Dimension(x + 100,y + 100));

        JScrollPane scroll = new JScrollPane(
                this,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );


        frame.add(scroll);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenSize.width, screenSize.height);
        frame.setVisible(true);


    }
    public void setNodeList(ArrayList<Node> nList){
        this.nList = nList;
    }

}
