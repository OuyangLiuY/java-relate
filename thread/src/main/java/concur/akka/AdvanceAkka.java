package concur.akka;

import akka.actor.*;
import akka.dispatch.OnComplete;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpResponse;
import akka.http.scaladsl.model.HttpRequest;
import akka.pattern.AskTimeoutException;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.sys.process.ProcessBuilder;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeoutException;

public class AdvanceAkka {

    /**
     * 调度
     */
    static class Dispatcher extends AbstractActor {

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((message)->{
                System.out.println(getSelf() + "--->" +Thread.currentThread().getName());
            }).build();
        }

        public static void main(String[] args) {
            ActorSystem system = ActorSystem.create("sys", ConfigFactory.load("dispatcher.conf"));
            ActorRef actorRef = system.actorOf(Props.create(Dispatcher.class), "Dispatcher");
            actorRef.tell("hello",ActorRef.noSender());
        }
    }

    static class MailBox{}

    static class FutureActor extends AbstractActor {
        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny(msg -> {
                System.out.println(" receive :" + msg);
                Thread.sleep(1000);
                getSender().tell("reply", getSelf());
                //getSender().tell("msg",getSelf());
            }).build();
        }

        public static void main(String[] args) throws TimeoutException, InterruptedException {
            ActorSystem system = ActorSystem.create("sys");
            ActorRef actorRef = system.actorOf(Props.create(FutureActor.class), "FutureActor");
            Timeout timeout = new Timeout(Duration.create(3, "seconds"));
            Future<Object> future = Patterns.ask(actorRef, "hello future", timeout);
            System.out.println(future);
            // Await同步获取响应，如果超时了则会抛出java.tank.util.concurrent.TimeoutException
            String reply = (String) Await.result(future, timeout.duration());
            System.out.println(reply);

            /*future.onSuccess(new OnSuccess<Object>() {
                @Override
                public void onSuccess(Object msg) throws Throwable {
                    System.out.println("success: " + msg);
                }
            }, system.dispatcher());*/

            future.onFailure(new OnFailure() {
                @Override
                public void onFailure(Throwable ex) throws Throwable {
                    if (ex instanceof AskTimeoutException) {
                        System.out.println("超时异常");
                    } else {
                        System.out.println("其他异常 " + ex);
                    }
                }
            }, system.dispatcher());
//
            future.onComplete(new OnComplete<Object>() {
                @Override
                public void onComplete(Throwable failure, Object success) throws Throwable {
                    if (failure != null) {
                        System.out.println("异常");
                    } else {
                        System.out.println("complete:" + success);
                    }
                }
            }, system.dispatcher());
        }
    }

    static class HttpClient {
      /*  static class Demo{
            public static void main(String[] args) {
                final ActorSystem system = ActorSystem.create();

                final CompletionStage<HttpResponse> responseFuture = Http.get(system).singleRequest(HttpRequest.create("https://akka.io"));

                final Materializer materializer = ActorMaterializer.create(system);
                responseFuture.thenAccept(response->{
                    response.tank.util.entity().getDataBytes().runWith(ProcessBuilder.Sink.foreach(content->{
                        System.out.println(content.utf8String());
                    }), materializer);
                });
            }
        }*/
    }

    /**
     * stream流
     */
    static class Stream {

    }

    /**
     * 事件总线
     */
    static class EventBus {
    }

    /**
     * 定时器
     */
    static class Scheduler {

    }

    /**
     * 扩展机制
     */
    static class Extension {
    }

    /**
     * 持久化
     */
    static class Persist {
    }



    /**
     * 路由
     */
    static class Route {
    }
    static class RemoteActor{}

}
