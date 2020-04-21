package concur.akka;


import akka.actor.*;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import akka.pattern.CircuitBreaker;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


public class PrimaryAkka {
    //private static final Logger logger = LoggerFactory.getLogger(HelloAkka.class);
    static class StartQuick extends AbstractActor {
        private LoggingAdapter log = Logging.getLogger(this.getContext().getSystem(), this);

        public static Props createProps() {
            return Props.create(new Creator<Actor>() {
                @Override
                public Actor create() throws Exception {
                    return new StartQuick();
                }
            });
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    /**/.matchEquals("equals", s -> {
                        log.info(" equals " + s);
                        System.out.println(" equals " + s);
                    }).match(String.class, s -> {
                        log.info(" String " + s);
                        System.out.println(" String " + s);
                    }).matchAny((msg) -> {
                        System.out.println(msg);
                        log.info(" object " + msg.toString());
                    }).build();
        }

        public static void main(String[] args) {
            ActorSystem system = ActorSystem.create("sys");
            ActorRef actorRef = system.actorOf(Props.create(StartQuick.class), "StartQuick");
            actorRef.tell("obj", ActorRef.noSender());// ActorRef.noSender()实际上就是叫做deadLetters的actor

        }
    }

    static class StopActor {
        static class WorkerActor extends AbstractActor {

            @Override
            public void preStart() throws Exception {
                System.out.println("worker init...");
            }

            @Override
            public Receive createReceive() {
                return receiveBuilder().matchAny((msg) -> {
                    System.out.println("正在做事情...");
                    Thread.sleep(3000);
                    System.out.println("做完了...");
                }).build();
            }

            @Override
            public void postStop() throws Exception {
                System.out.println("Worker postStop");
            }
        }

        static class WatchActor extends AbstractActor {
            ActorRef child = null;

            @Override
            public void preStart() throws Exception {
                System.out.println("watch init...");
                child = getContext().actorOf(Props.create(WorkerActor.class), "workerActor");
                //监控child, unwatch可以解除监控
                getContext().watch(child);
            }

            @Override
            public void postStop() throws Exception {
                System.out.println("WatchActor postStop");
            }

            @Override
            public Receive createReceive() {
                return receiveBuilder().matchEquals("stopChild", s -> {
                    getContext().stop(child);
                })
                        .match(String.class, s -> {
                            child.forward(s, getContext());
                        })
                        .match(Terminated.class, (t) -> {
                            System.out.println("监控到" + t.getActor() + "停止了");
                        }).build();
            }
        }

        public static void main(String[] args) {
            ActorSystem system = ActorSystem.create("sys");
            ActorRef watchActor = system.actorOf(Props.create(WatchActor.class), "watchActor");
            watchActor.tell("work", ActorRef.noSender());

            //  system.stop(watchActor);
            //使用stop 就将正在做的事情做完，后面的就不再执行
            watchActor.tell(Kill.getInstance(), ActorRef.noSender());
            watchActor.tell(PoisonPill.getInstance(), ActorRef.noSender());
        }
    }

    static class AskActor extends AbstractActor {

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((obj) -> {
                System.out.println("发送者是：" + getSender());
                Thread.sleep(1000);
                getSender().tell("hello," + obj, getSelf());
            }).build();
        }

        public static void main(String[] args) {
            ActorSystem system = ActorSystem.create("sys");
            ActorRef actorRef = system.actorOf(Props.create(AskActor.class), "AskActor");
            // actorRef.tell("obj", ActorRef.noSender());// ActorRef.noSender()实际上就是叫做deadLetters的actor
            Timeout timeout = new Timeout(FiniteDuration.create(2, TimeUnit.SECONDS));
            Future<Object> f = Patterns.ask(actorRef, "Ask ask", timeout); //超时获取数据报错
            f.onSuccess(new OnSuccess<Object>() {
                @Override
                public void onSuccess(Object result) throws Throwable {
                    System.out.println("收到消息：" + result);
                }
            }, system.dispatcher());
            // f.onComplete();


        }
    }

    static class ForwardActors {

        static class TargetActor extends AbstractActor {
            @Override
            public Receive createReceive() {
                System.out.println("target : " + getSender());
                return receiveBuilder().matchAny(System.out::println).build();
            }
        }

        static class ForwardActor extends AbstractActor {
            private ActorRef target = getContext().actorOf(Props.create(TargetActor.class), "targetActor");

            @Override
            public Receive createReceive() {
                return receiveBuilder().matchAny((message) -> {
                    target.forward(message, getContext());
                }).build();
            }
        }

        public static void main(String[] args) {
            ActorSystem system = ActorSystem.create("sys");
            ActorRef actorRef = system.actorOf(Props.create(ForwardActor.class), "forwardDemo");
            actorRef.tell("123", ActorRef.noSender());  // 转发之后sender不会发生变化
        }
    }

    static class BecomeActor extends AbstractActor {
        int aa = 0;
        Receive receiveC = receiveBuilder()
                .matchAny(s -> {
                    if (s.equals("unbecome")) {
                        getContext().unbecome();
                    } else {
                        System.out.println("优惠100");
                    }
                })
                .build();

        Receive receiveB = receiveBuilder()
                .matchAny(s -> {
                    if (s.equals("unbecome")) {
                        getContext().unbecome();
                    }
                    if (s.equals("become")) {
                        getContext().become(receiveC, false);//false默认不删除中间节点 true表示 A发送到B，B发送到C，C再转发回来之后就发送到A,中间B会删除
                    } else {
                        System.out.println("优惠500");
                    }
                })
                .build();

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> {
                aa++;
                System.out.println("优惠1000");
                if (aa == 3) {
                    getContext().become(receiveB);
                }

            }).build();

        }

        public static void main(String args[]) {
            ActorSystem system = ActorSystem.create("sys");
            ActorRef actorRef = system.actorOf(Props.create(BecomeActor.class), "BecomeActor");
            actorRef.tell("1", ActorRef.noSender());
            actorRef.tell("2", ActorRef.noSender());
            actorRef.tell("3", ActorRef.noSender());
            actorRef.tell("4", ActorRef.noSender());
            actorRef.tell("5", ActorRef.noSender());
//        actorRef.tell("unbecome", ActorRef.noSender());
            actorRef.tell("become", ActorRef.noSender());
            actorRef.tell("6", ActorRef.noSender());
            actorRef.tell("unbecome", ActorRef.noSender());
            actorRef.tell("7", ActorRef.noSender());
        }
    }

    static class SelectionActor {
        static class TargetActor extends AbstractActor {
            @Override
            public Receive createReceive() {
                System.out.println("target  Object : " + getSender());
                return receiveBuilder().matchAny(message -> System.out.println("target  receive :" + message)).build();
            }
        }

        public static void main(String[] args) {
            ActorSystem system = ActorSystem.create("sys");
            ActorRef targetActor2 = system.actorOf(Props.create(TargetActor.class), "targetActor2");
            ActorRef targetActor1 = system.actorOf(Props.create(TargetActor.class), "targetActor1");
            ActorSelection actorSelection = system.actorSelection("user/targetActor*");
            actorSelection.tell("hello", ActorRef.noSender());

            Future<ActorRef> future = actorSelection.resolveOne(new Timeout(FiniteDuration.create(3, TimeUnit.SECONDS)));
            future.onSuccess(new OnSuccess<ActorRef>() {
                @Override
                public void onSuccess(ActorRef result) throws Throwable {
                    System.out.println(result);
                }
            }, system.getDispatcher());
            //Future<ActorRef> future = actorSelection.resolveOne(new Timeout(Duration.create(3, TimeUnit.SECONDS)));
        }
    }


    static class SupervisorActors {

        static class SupervisorActor extends AbstractActor {

            SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.ofSeconds(1), t -> {
                if (t instanceof IOException) {
                    System.out.println("========自动恢复=======");
                    return SupervisorStrategy.resume(); // 恢复
                } else if (t instanceof IndexOutOfBoundsException) {
                    System.out.println("=========重启==========");
                    return SupervisorStrategy.restart(); // 重启
                } else if (t instanceof SQLException) {
                    System.out.println("==========停止=========");
                    return SupervisorStrategy.stop();  // 停止
                } else {
                    System.out.println("==========上报=========");
                    return SupervisorStrategy.escalate(); // 上报
                }
            });

            @Override
            public SupervisorStrategy supervisorStrategy() {
                return strategy;
            }

            @Override
            public void preStart() throws Exception {
                System.out.println("SupervisorActor:"+getSender());
                ActorRef workerActor = getContext().actorOf(Props.create(WorkerActor.class), "workerActor");
                getContext().watch(workerActor);
            }

            @Override
            public void postStop() throws Exception {
                super.postStop();
                System.out.println("SupervisorActor stopped ");
            }

            @Override
            public Receive createReceive() {
                return receiveBuilder().matchAny(msg -> {
                    if (msg instanceof Terminated) {
                        Terminated ter = (Terminated) msg;
                        System.out.println(ter.getActor() + "已经终止");
                    } else {
                        System.out.println("stateCount=" + msg);
                    }
                }).build();
            }
        }

        static class WorkerActor extends AbstractActor {
            //状态数据
            private int stateCount = 0;

            @Override
            public void preStart() throws Exception {
                System.out.println("启动前preStart");
                System.out.println("WorkerActor:"+getSender());
                super.preStart();
            }

            @Override
            public void postStop() throws Exception {
                System.out.println("停止后postStop");
                super.postStop();
            }

            @Override
            public void preRestart(Throwable reason, Option<Object> message)
                    throws Exception {

                System.out.println("重启前preRestart");
                super.preRestart(reason, message);
            }

            @Override
            public void postRestart(Throwable reason) throws Exception {
                super.postRestart(reason);
                System.out.println("重启后postRestart");
            }

            @Override
            public Receive createReceive() {
                return receiveBuilder().matchAny(msg -> {
                    //模拟计算任务
                    if (msg.equals("add")) {
                        stateCount++;
                        System.out.println(stateCount);
                    } else if (msg.equals("get")) {
                        System.out.println(stateCount);
                    } else if (msg instanceof Exception) {
                        throw (Exception) msg;
                    } else {
                        unhandled(msg);
                    }
                }).build();
            }
        }

        public static void main(String[] args) {
            ActorSystem system = ActorSystem.create("sys");
            ActorRef supervisorActor = system.actorOf(Props.create(SupervisorActor.class), "supervisorActor");
            ActorSelection workerActor = system.actorSelection("akka://sys/user/supervisorActor/workerActor");

            System.out.println("发送消息");

            workerActor.tell("add", ActorRef.noSender());
            workerActor.tell("add", ActorRef.noSender());
         //   workerActor.tell(new IndexOutOfBoundsException(), ActorRef.noSender());
           // supervisorActor.tell(new IndexOutOfBoundsException(), ActorRef.noSender());
           // workerActor.tell(new IndexOutOfBoundsException(), ActorRef.noSender());
    //        workerActor.tell(new IndexOutOfBoundsException(), ActorRef.noSender());
//        workerActor.tell(new SQLException("SQL异常"), ActorRef.noSender());
         //   workerActor.tell(new IOException(), ActorRef.noSender());
      //      workerActor.tell(new IOException(), ActorRef.noSender());


            workerActor.tell("get", ActorRef.noSender());
        }
    }

    static class CircuitBreakerActors {

        static class CircuitBreakerActor extends AbstractActor {
            private ActorRef workerChild;
            private static SupervisorStrategy strategy = new OneForOneStrategy(20, Duration.ofMinutes(1), param -> SupervisorStrategy.resume());

            @Override
            public void preStart() throws Exception {
                super.preStart();
                workerChild = getContext().actorOf(Props.create(WorkerActor.class), "workerActor");
            }

            @Override
            public SupervisorStrategy supervisorStrategy() {
                return strategy;
            }

            @Override
            public Receive createReceive() {
                return receiveBuilder().matchAny(message -> workerChild.tell(message, getSender())).build();
            }
        }

        static class WorkerActor extends AbstractActor {
            private CircuitBreaker breaker;

            @Override
            public void preStart() throws Exception {
                super.preStart();
                // 调用报错或超时（超过1秒）失败次数加1，超过3次后进入开启状态，30秒后进入半开启状态，
                // 如果在半开启状态中处理第一个请求成功，则关闭熔断器，如果失败则重回开启状态。
                this.breaker = new CircuitBreaker(getContext().dispatcher(), getContext().system().scheduler(),
                        3, Duration.ofSeconds(1), Duration.ofSeconds(15))
                        .onOpen(() -> {
                            System.out.println("---> 熔断器开启");
                            return null;
                        }).onHalfOpen(() -> {
                            System.out.println("---> 熔断器半开启");
                            return null;
                        }).onClose(() -> {
                            System.out.println("---> 熔断器关闭");
                            return null;
                        });
            }

            @Override
            public Receive createReceive() {
                return receiveBuilder().matchAny(msg -> {
                    breaker.callWithSyncCircuitBreaker(new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            if (msg.equals("error")) {
                                System.out.println("msg:" + msg);
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("msg:" + msg);
                            }
                            return "success";
                        }
                    });
                }).build();
            }
        }

        public static void main(String[] args) {
            ActorSystem system = ActorSystem.create("sys");
            ActorRef actorRef = system.actorOf(Props.create(CircuitBreakerActors.CircuitBreakerActor.class), "CircuitBreakerActor");
            for (int i = 0; i < 40; i++) {
                if (i > 4) {
                    actorRef.tell("normal", ActorRef.noSender());
                } else {
                    actorRef.tell("error", ActorRef.noSender());
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
