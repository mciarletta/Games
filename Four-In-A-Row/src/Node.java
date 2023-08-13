public class Node {
    int[] info; //use [row, col, id, connection flag]
    Node prev;
    Node next;

    //constructor for a node to be used in doubly linked list
    public Node(int[] info){
        this.info = info;
        this.prev = null;
        this.next = null;
    }

    public void setInfo(int[] info){
        this.info = info;
    }
}
