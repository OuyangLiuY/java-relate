package com.ywb.nolockvector;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest   {

    LockFreeVector<String> lockFreeVector = new LockFreeVector<String>();
    Vector<String> vector = new Vector<String>();
    List<String>  list = new ArrayList<String>();

    void writeFreeVector(){
        for (int i = 0; i < 100000; i++) {
            lockFreeVector.push_back("1->"+i);
        }

    }

    void writeVector(){
        for (int i = 0; i < 100000; i++) {
            vector.add("1->"+i);
        }
    }
    void writeList(){
        for (int i = 0; i < 100000; i++) {
            list.add("1->"+i);
        }
    }

    void readFreeVector(){
        //lockFreeVector.push_back("1");
        for (int i = 0; i < 100000; i++) {
            System.out.print(lockFreeVector.get(i) + "\t");
        }
    }

    void readVector(){
        for (int i = 0; i < 100000; i++) {
            System.out.print(vector.get(i) + "\t");
        }
    }
    void readList(){
        for (int i = 0; i < 100000; i++) {
            System.out.print(list.get(i) + "\t");
        }
    }

    public static void main(String[] args) {
        final ThreadTest test = new ThreadTest();
        Thread[] threads = new Thread[6];
        new Thread() {
            public void run(){
                test.writeFreeVector();
            }
        }.start();
       new Thread() {
            public void run(){
                test.readFreeVector();
            }
        }.start();

        /*threads[1] = new Thread() {
            public void run(){
                test.writeVector();
            }
        };
        threads[4] = new Thread() {
            public void run(){
                test.readVector();
            }
        };

        threads[2] = new Thread() {
            public void run(){
                test.writeList();
            }
        };
        threads[5] = new Thread() {
            public void run(){
                test.readList();
            }
        };*/
        /*threads[6] = new Thread() {
            public void run(){

            }
        };*/
        /*ExecutorService service = Executors.newFixedThreadPool(6);
        service.execute(new Runnable() {
            public void run() {

            }
        });*/
    }
}
