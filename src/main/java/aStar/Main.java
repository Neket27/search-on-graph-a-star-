package aStar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        Node n0 = new Node(0,3);
//        n0.setG(0);

        Node n1 = new Node(1,2);
        Node n2 = new Node(2,2);
        Node n3 = new Node(3,2);

        n0.addBranch(1, n1);
        n0.addBranch(5, n2);
        n0.addBranch(2, n3);
        n3.addBranch(1, n2);

        Node n4 = new Node(4,1);
        Node n5 = new Node(5,1);

        Node n6 = new Node(6,0);

        n1.addBranch(3, n4);
        n2.addBranch(4, n5);
        n3.addBranch(6, n4);

        n4.addBranch(3, n6);
        n5.addBranch(1, n4);
        n5.addBranch(3, n6);

        List<Node> nodeList = List.of(n0, n1, n2, n3, n4, n5, n6);

        StartAndTarget startAndTarget = null;
        boolean runApp = true;
        while (runApp) {
            System.out.println("Выбирите действие:" + "\n" +
                    "Создать вершины - 0" + "\n" +
                    "Связать вершины - 1" + "\n" +
                    "Назначить стартовую вершину и в которую нужно попасть - 2"+"\n"+
                    "Удалить связь вершин- 3" + "\n" +
                    "Изменить эвристику вершины- 4" + "\n" +
                    "Вывести вершины- 5" + "\n" +
                    "Завершить работу - 6");
            String num;
            try {
                num = bufferedReader.readLine();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            switch (num) {
                case "0":
                    startAndTarget=null;
                    nodeList = createEdges();
                    break;
                case "1":
                    nodeList = communications(nodeList);
                    break;
                case "2":
                    try {
                      startAndTarget = createStartAndTargetEdge(nodeList);
                    } catch (RuntimeException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                    break;

                case "3":
                    removeBranch(nodeList);
                    break;

                case "4":
                    updateHeuristic(nodeList);
                    break;
                case "5":
                    printNodeList(nodeList);
                    break;
                case "6":
                    runApp=false;
                    break;
            }

            if(startAndTarget != null){
            Node res = aStar(startAndTarget.getStart(), startAndTarget.getTarget());
            printPath(startAndTarget.getStart(),res);
            }
        }
    }


    public static Node aStar(Node start, Node target){
        PriorityQueue<Node> closedList = new PriorityQueue<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();
        //сохраняем фул оценку до вершины(вес+эвр)
        start.setF(start.getG() + start.calculateHeuristic(target));
        openList.add(start);

        while(!openList.isEmpty()){
            Node n = openList.peek();
            if(n == target){
                return n;
            }
            System.out.println(n.id+"("+n.getH()+")"+" вес= "+n.getG()+" фул оценка= "+ (n.getH()+n.getG())+" -> ");
            for(Node.Edge edge : n.getNeighbors()){
                Node nodeInQuestion = edge.getNode();
                double totalWeight = n.getG() + edge.getWeight();
//                double totalGrade = n.getG() + edge.getWeight()+nodeInQuestion.getH();
                System.out.println(nodeInQuestion.id+"("+nodeInQuestion.getH()+")"+" вес= "+edge.getWeight()+", вес от стартовой вершыны= "+ (edge.getWeight()+n.getG())+"| ");


                if(!openList.contains(nodeInQuestion) && !closedList.contains(nodeInQuestion)){
                    nodeInQuestion.setParent(n);
                    nodeInQuestion.setG(totalWeight);
                    nodeInQuestion.setF(nodeInQuestion.getG() + nodeInQuestion.calculateHeuristic(target));
                    openList.add(nodeInQuestion);
                } else {
                    if(totalWeight < nodeInQuestion.getG()){
                        nodeInQuestion.setParent(n);
                        nodeInQuestion.setG(totalWeight);
                        nodeInQuestion.setF(nodeInQuestion.getG() + nodeInQuestion.calculateHeuristic(target));

                        if(closedList.contains(nodeInQuestion)){
                            closedList.remove(nodeInQuestion);
                            openList.add(nodeInQuestion);
                        }
                    }
                }
                System.out.print("openList: ");
                Arrays.stream(openList.toArray()).forEach(o->System.out.print("'вершина:"+((Node)o).getId()+" эвр:"+((Node) o).getH()+" вес:"+ ((Node) o).getG()+" f:"+((Node) o).getF()+"' "));
//                Arrays.stream(openList.toArray()).forEach(o->System.out.print("'вершина:"+((Node)o).getId()+", f:"+((Node) o).getF()+"' "));
                System.out.print("       closeList: ");
                Arrays.stream(closedList.toArray()).forEach(o->System.out.print("'вершина:"+((Node)o).getId()+" эвр:"+((Node) o).getH()+" вес:"+ ((Node) o).getG()+" f:"+((Node) o).getF()+"' "));
//                Arrays.stream(closedList.toArray()).forEach(o->System.out.print("'вершина:"+((Node)o).getId()+", f:"+((Node) o).getF()+"' "));
                System.out.println("");
            }
            System.out.println(" ");
            openList.remove(n);
            closedList.add(n);

            System.out.print("openList: ");
                            Arrays.stream(openList.toArray()).forEach(o->System.out.print("'вершина:"+((Node)o).getId()+" эвр:"+((Node) o).getH()+" вес:"+ ((Node) o).getG()+" f:"+((Node) o).getF()+"' "));
//            Arrays.stream(openList.toArray()).forEach(o->System.out.print("'вершина:"+((Node)o).getId()+", f:"+((Node) o).getF()+"' "));
            System.out.print("       closeList: ");
            Arrays.stream(closedList.toArray()).forEach(o->System.out.print("'вершина:"+((Node)o).getId()+" эвр:"+((Node) o).getH()+" вес:"+ ((Node) o).getG()+" f:"+((Node) o).getF()+"' "));
//            Arrays.stream(closedList.toArray()).forEach(o->System.out.print("'вершина:"+((Node)o).getId()+", f:"+((Node) o).getF()+"' "));
            System.out.println(" ");
            System.out.println("-----------------------------------------------------");
            System.out.println(" ");
        }
        return null;
    }

    public static void printPath(Node start,Node target){
        Node n = target;

        if(n==null)
            return;

        List<Node> nodes = new ArrayList<>();

        while(n.getParent() != null){
            nodes.add(n);
            n = n.getParent();
        }
        nodes.add(n);
        Collections.reverse(nodes);


        for(Node node : nodes){
            if(start.id<=node.id)
                System.out.print("(вершина "+node.id+" вес до вершины= "+node.g+" эвристика вершины= "+node.h + ") -> ");
        }
        System.out.println("");
    }


    static List<Node> createEdges(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Сколько ввершин будет у графа? ");
            int countEdge = Integer.parseInt(bufferedReader.readLine());
            System.out.println("Ввод данных:");

            List<Node> nodeList = new ArrayList<>();
            for (int i = 0; i < countEdge; i++) {
                System.out.println("Введите эвристическую значимость вершины - " + (i));
                nodeList.add(new Node(i,Integer.parseInt(bufferedReader.readLine())));
            }
            return nodeList;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    static List<Node> communications(List<Node>nodeList){
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        try {
        boolean inputPathEdge = true;
        while (inputPathEdge) {
            System.out.println("Введите связь вершины (номер вершины, номер вершины с которой связать, вес пути)");
            String[] dataString = bufferedReader.readLine().split(" ");

            int[] data = Arrays.stream(dataString)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            int numberN1 = data[0];
            int numberN2 = data[1];
            int weight = data[2];
            int nodeListSize = nodeList.size();


            if (!nodeList.isEmpty() && numberN1 >= 0 && numberN2 >= 0 && nodeListSize > numberN1 && nodeListSize > numberN2) {
                nodeList.get(numberN1).addBranch(weight, nodeList.get(numberN2));
            }else
                System.out.println("Проверьте, что номера введенных вершин существуют");

            if (bufferedReader.readLine().equals("end"))
                inputPathEdge = false;
        }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return nodeList;
        }

        static void removeBranch(List<Node>nodeList) throws IOException {
            System.out.println("Введите вершины (номер вершины и номер вершины с которой хотите удалить связь)");
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
            String[] dataString = bufferedReader.readLine().split(" ");

            int[] data = Arrays.stream(dataString)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            int numberN1 = data[0];
            int numberN2 = data[1];
            int nodeListSize = nodeList.size();

            if (!nodeList.isEmpty() && numberN1 >= 0 && numberN2 >= 0 && nodeListSize > numberN1 && nodeListSize > numberN2) {
                nodeList.get(numberN1).removeBranch(numberN2);
                nodeList.get(numberN2).removeBranch(numberN1);
            }else
                System.out.println("Проверьте, что номера введенных вершин существуют");
        }

        static void printNodeList(List<Node>nodeList){
        nodeList.forEach(node -> System.out.println(node.getId()+", h:"+ node.getH()+", g:"+node.getG()+", f:"+ node.getF()));
        }

        static void updateHeuristic(List<Node> nodeList) throws IOException {
            System.out.println("Введите номер вершины, затем эвристику");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String[] dataString = bufferedReader.readLine().split(" ");
            int[] data = Arrays.stream(dataString)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            int numberNode = data[0];
            int h = data[1];
            nodeList.get(numberNode).setH(h);
        }

         static StartAndTarget createStartAndTargetEdge(List<Node> nodeList){
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
            Node start;
            Node target;
            try {
                System.out.println("Введите номер стартовой вершины");
               start = nodeList.get(Integer.parseInt(bufferedReader.readLine()));
               start.setG(0);
                System.out.println("Введите номер вершины в которую нужно попасть");
               target = nodeList.get(Integer.parseInt(bufferedReader.readLine()));
            } catch (NumberFormatException e){
                throw new RuntimeException("Некорректный формат введенного числа");
            } catch (IndexOutOfBoundsException | IOException e){
                throw new RuntimeException("Введенный номер вершины вне диапазона доступных вершин");
            }
            return new StartAndTarget(start,target);
        }

}
