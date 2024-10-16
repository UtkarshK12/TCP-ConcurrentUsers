import org.example.GreetClientSocket;
import org.example.GreetServerSocket;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TcpTest {

    private GreetClientSocket greetClientSocket;
    private GreetClientSocket greetClientSocket1;
    private GreetServerSocket greetServerSocket;
    private ExecutorService executorService;


    @Before
    public void setup() throws IOException, InterruptedException {

        greetServerSocket = new GreetServerSocket();
        executorService= Executors.newSingleThreadExecutor();
        executorService.submit(()->{
            try {
                greetServerSocket.start(10280);
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        });

        //allowing a moment for server to start
        TimeUnit.SECONDS.sleep(10);

        greetClientSocket = new GreetClientSocket();
        greetClientSocket.startConnection("127.0.0.1", 10280);

        greetClientSocket1 = new GreetClientSocket();
        greetClientSocket1.startConnection("127.0.0.1", 10280);
    }

    @Test
    public void testResponse() throws IOException {
        Assert.assertNotEquals("Hey1",greetClientSocket.sendMessage("Yo"));
        Assert.assertEquals("Hey",greetClientSocket.sendMessage("Hello"));
        Assert.assertEquals("Cant recognize",greetClientSocket1.sendMessage("Yo"));
        Assert.assertEquals("Hey",greetClientSocket1.sendMessage("Hello"));
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        greetClientSocket.stopConnection();
        greetServerSocket.stop();
        executorService.shutdownNow();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

    }
}
