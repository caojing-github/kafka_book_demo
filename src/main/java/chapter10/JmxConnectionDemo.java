package chapter10;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

/**
 * 代码清单10-1 使用 JMX 来获取监控指标
 * Created by 朱小厮 on 2018/10/20.
 */
public class JmxConnectionDemo {
    private MBeanServerConnection conn;
    private String jmxURL;
    private String ipAndPort;

    public JmxConnectionDemo(String ipAndPort) {
        this.ipAndPort = ipAndPort;
    }

    /**
     * 初始化JMX连接
     */
    public boolean init() {
        jmxURL = "service:jmx:rmi:///jndi/rmi://" + ipAndPort + "/jmxrmi";
        try {
            JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);
            JMXConnector connector = JMXConnectorFactory.connect(serviceURL, null);
            conn = connector.getMBeanServerConnection();
            if (conn == null) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 1分钟内流入的平均速度
     */
    public double getMsgInPerSec() {
        String objectName = "kafka.server:type=BrokerTopicMetrics," + "name=MessagesInPerSec";
        Object val = getAttribute(objectName, "OneMinuteRate");
        if (val != null) {
            return (double) (Double) val;
        }
        return 0.0;
    }

    /**
     * 根据Mbean名称和属性来获取具体的值
     */
    private Object getAttribute(String objName, String objAttr) {
        ObjectName objectName;
        try {
            objectName = new ObjectName(objName);
            return conn.getAttribute(objectName, objAttr);
        } catch (MalformedObjectNameException | IOException | ReflectionException | InstanceNotFoundException | AttributeNotFoundException | MBeanException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        JmxConnectionDemo jmxConnectionDemo = new JmxConnectionDemo("localhost:9999");
        jmxConnectionDemo.init();
        System.out.println(jmxConnectionDemo.getMsgInPerSec());
    }
}
