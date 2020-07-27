package com.jgainey.rabconsumerloggr.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.util.ArrayList;
import java.util.Collections;

public class MyMain {

    private final static String QUEUE_NAME = "speclogger";
    ArrayList<String> mylist;


    public void start(){
        mylist = new ArrayList<>();
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUri("amqp://2968d538-f72f-43b9-9246-9562155896ad:qpm7fVX-A7A4cf2JiF0cUrI@10.0.0.85/bb4a5c34-3a1e-4d82-9cae-065e0894dfed");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                mylist.add(message);
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void aggr(){
        System.out.println("Entering aggr with log-list size of " + mylist.size());
        ArrayList<Integer> myIntList = new ArrayList<>();
        for(int i = 0; i< mylist.size(); i++){
            String line = mylist.get(i);

            if(!line.contains("YODACOUNTER")){
                continue;
            }
            //Put in the keyword here that is sent to the tracking_app app
            String targetWord = "YODACOUNTER";

            int start = line.indexOf(targetWord);
            int lineLength = line.length()-2;

            if(start>0){
                String wordToChop = line.substring(start,lineLength);
                int countNumber = Integer.parseInt(wordToChop.substring(wordToChop.indexOf(",") + 1,wordToChop.length()-1));
                if(!myIntList.contains(countNumber)){
                    myIntList.add(countNumber);
                }else{
                    System.out.println("Found duplicate " + countNumber);
                }
            }
        }

        System.out.println("list entries with keyword: " + myIntList.size());

        //Sort the list in ascending order
        System.out.println("Sorting now...");
        Collections.sort(myIntList);

        //Find the missing values.
        System.out.println("Finding missing values...");

        int topnum = myIntList.get(myIntList.size()-1);

        System.out.println("List size " + + myIntList.size());
        System.out.println("First " + myIntList.get(0));
        System.out.println("Last " + topnum);

        for(int j = myIntList.get(0); j< topnum; j++){
            if(!myIntList.contains(j)){
                System.out.println("Missing " + j);
            }
        }
    }


}
