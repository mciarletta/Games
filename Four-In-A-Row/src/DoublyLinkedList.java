public class DoublyLinkedList {
    Node head;
    Node tail;

    //constructor to make a blank doubly linked list
    public DoublyLinkedList(){
        this.head = null;
        this.tail = null;
    }

    //output info from head to talk. For debugging
    public void headToTail(){
        Node current = head;
        while(current !=null){
            System.out.printf("%s, %s, %s %n", current.info[0],current.info[1],current.info[2]);
            current = current.next;
        }
    }

    //output info from head to talk. For debugging
    public void goToBack(){
        Node current = tail;
        while(current !=null){
            System.out.println(current.info + " ");
            current = current.prev;
        }
    }

    //insert at the beginning - make a new head
    public void insertFront(int[] info){
        Node temp =  new Node(info);
        if (head == null) { //empty list, make this new node the head and tail
            head = temp;
            tail = temp;
        } else { //replace the current head
            temp.next = head;
            head.prev = temp;
            head = temp;
        }
    }

    //insert at the end - make a new tail
    public void insertBack(int[] info){
        Node temp =  new Node(info);
        if (tail == null) { //empty list, make this new node the head and tail
            head = temp;
            tail = temp;
        } else { //replace the current tail
            tail.next = temp;
            temp.prev = tail;
            tail = temp;
        }
    }
}
